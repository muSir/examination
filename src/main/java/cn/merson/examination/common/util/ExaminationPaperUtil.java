package cn.merson.examination.common.util;

import cn.merson.examination.common.dto.ExaminationPaperDto;
import cn.merson.examination.common.dto.QuestionDto;
import cn.merson.examination.common.enums.IRedisKey;
import cn.merson.examination.common.enums.QuestionType;
import cn.merson.examination.configuration.BaseExaminationApplicationContext;
import cn.merson.examination.entity.ExaminationPaper;
import cn.merson.examination.entity.Question;
import cn.merson.examination.repository.IExaminationPaperRepository;
import cn.merson.examination.repository.IQuestionRepository;
import cn.merson.examination.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 试卷工具类:把题目集合按分值从小到大的顺序进行排序，这样最大机会的能正确生成试卷 但是往往试卷上的题目是素有满足条件里最多的
 */
@Component
public class ExaminationPaperUtil extends BaseExaminationApplicationContext{

    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private StringUtil stringUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private QuestionUtil questionUtil;

    /**
     * 根据题库中的题目随机生成满足条件的试卷
     * @param singleNum 单选题数目
     * @param multiNum 多选题数目
     * @param shortAnswerNum 问答题数目
     * @param questionService
     * @return
     */
    public ExaminationPaper createExaminationPaper(int singleNum,int multiNum,int shortAnswerNum,
                                                          IQuestionService questionService){
        List<Question> questions = questionService.getQuestions();
        if (questions == null || questions.size() == 0){
            return null;
        }
        if (singleNum == 0 && multiNum == 0 && shortAnswerNum == 0){
            return createExaminationPaper(questionService);
        }

        int single = singleNum;
        int multi = multiNum;
        int shortAnswer = shortAnswerNum;

        ExaminationPaper paper = new ExaminationPaper();

        List<Long> ids = new ArrayList<>();

        //累加分数
        int sumValue = 0;
        //单选题数量被指定大于0 循环直到生成指定数量的单选题
        if (singleNum > 0){
            while (single > 0){
                //挑选一道合适的单选题目
                Question question = findQuestion(questions,QuestionType.SingleSelect.toString(),100-sumValue);
                if (question == null){
                    //已经找不到满足条件的单选题
                    return null;
                }
                sumValue += question.getValue();
                ids.add(question.getQuestionId());
                questions.remove(question);
                single--;
            }
        }
        if (multiNum > 0){
            //多选题数量被指定
            while (multi > 0){
                Question question = findQuestion(questions,QuestionType.SingleSelect.toString(),100-sumValue);
                if (question == null){
                    return null;
                }
                sumValue += question.getValue();
                ids.add(question.getQuestionId());
                questions.remove(question);
                multi--;
            }
        }
        if (shortAnswerNum > 0){
            //问答题数量被指定
            while (shortAnswer > 0){
                Question question = findQuestion(questions,QuestionType.SingleSelect.toString(),100-sumValue);
                if (question == null){
                    return null;
                }
                sumValue += question.getValue();
                ids.add(question.getQuestionId());
                questions.remove(question);
                shortAnswer--;
            }
        }

        //剩余分数的题目填充
        //两种情况：1.指定题目不能在生成 2.如果都被指定固定数量则试卷生成失败
        if (sumValue < 100){
            if (singleNum == 0){
                //单选题数目没有被指定
                while (sumValue <100){
                    Question question = findQuestion(questions,QuestionType.SingleSelect.toString(),100-sumValue);
                    if (question == null){
                        //已经找不到满足条件的单选题目,继续寻找其他类型的题目
                        break;
                    }
                    sumValue += question.getValue();
                    ids.add(question.getQuestionId());
                    singleNum++;
                    questions.remove(question);
                }
            }
            if (multiNum == 0){
                while (sumValue <100){
                    Question question = findQuestion(questions,QuestionType.MultiSelect.toString(),100-sumValue);
                    if (question == null){
                        break;
                    }
                    sumValue += question.getValue();
                    ids.add(question.getQuestionId());
                    multiNum++;
                    questions.remove(question);
                }
            }
            if (shortAnswerNum == 0){
                while (sumValue <100){
                    Question question = findQuestion(questions,QuestionType.ShortAnswer.toString(),100-sumValue);
                    if (question == null){
                        //不再有满足条件的题目，试卷生成失败
                        return null;
                    }
                    sumValue += question.getValue();
                    ids.add(question.getQuestionId());
                    shortAnswerNum++;
                    questions.remove(question);
                }
            }
        }
        if (sumValue != 100){
            return null;
        }
        paper.setSingleNum(singleNum);
        paper.setMultiNum(multiNum);
        paper.setShortAnswerQuestionNum(shortAnswerNum);
        paper.setQuestionsId(stringUtil.packageIds(ids));
        return paper;
    }

    /**
     * 随机生成总分100分的试卷（全部类型题目数不固定）
     * TODO 算法还可以优化 还有就是怎么判断题目完全一样的两张试卷是否一样
     * @param questionService
     * @return 题目编号，中间用英文,隔开
     */
    public ExaminationPaper createExaminationPaper(IQuestionService questionService){
        List<Question> questions = questionService.getQuestions();
        if (questions != null){
            //题库总题目数量
            int size = questions.size();
            //三种不同类型题目的初始数量
            int singleNum = 0;
            int multiNum = 0;
            int shortAnswerNum = 0;
            //当前累加总分 不能产生相同的题目（编号相同）
            //如果成功筛选一道题，则将该题从集合中移出，防止产生相同的题目
            // 并且防止一套完整的试卷生成（总分始终不能累加到100）
            int value = 0;
            //id集合 => 已经加入到试卷中的题目
            List<Long> ids = new ArrayList<>();
            //产生随机的试卷
            while (value < 100){
                //重新计算集合题目数量
                size = questions.size();
                //题库的题目不能生成一套完整的试卷
                if (size == 0){
                    return null;
                }
                //生成一个从0到size-1的随机整数数
                int index = stringUtil.randomBound(size);
                Question question = questions.get(index);
                //当前题目的分值
                int addValue = question.getValue();
                //最后一次叠加时 超过100则放弃这道题
                if (value + addValue >100){
                    //移出不满足条件的题目
                    questions.remove(question);
                    continue;
                }
                //对应类型的数量+1
                if (QuestionType.SingleSelect.toString().equals(question.getType())){
                    singleNum++;
                }else if (QuestionType.MultiSelect.toString().equals(question.getType())){
                    multiNum++;
                }else if (QuestionType.ShortAnswer.toString().equals(question.getType())){
                    shortAnswerNum++;
                }else {
                    //题目类型错误 一般不存在这种错误
                    questions.remove(question);
                    continue;
                }
                //新增一道题，分数累加
                value += addValue;
                ids.add(question.getQuestionId());
                //移出已经添加到试卷中的题目
                questions.remove(question);
            }
            return new ExaminationPaper(singleNum,multiNum,shortAnswerNum, stringUtil.packageIds(ids));
        }
        return null;
    }

    /**
     * 从可选集合中挑选出value<=compareValue且type为questionType的题目
     * @param questions 可选题目集合
     * @param questionType 题目类型
     * @param compareValue 比较分值 在剩余分值范围内的即可 value<=compareValue
     * @return
     */
    private Question findQuestion(List<Question> questions,String questionType,int compareValue){
        //已经没有满足的题目或者不再需要题目
        if (questions == null || compareValue <= 0){
            return null;
        }
        Question question = null;
        if (stringUtil.isNullOrEmpty(questionType)) {
            //当类型为null或者""时，则去三种类型的题目中查找
            //找到第一个满足条件的数据
            for (Question q : questions){
                if (q.getValue() <= compareValue){
                    question = q;
                }
            }
        } else {
            if (questionType.equals(QuestionType.SingleSelect.toString())
                    || questionType.equals(QuestionType.MultiSelect.toString())
                    || questionType.equals(QuestionType.ShortAnswer.toString())){
                //题目类型错误
                for (Question q : questions){
                    if (q.getType().toString().equals(questionType) && q.getValue() <= compareValue){
                        question = q;
                    }
                }
            }
        }
        return question;
    }

    /**
     * 获取指定试卷的全部题目
     * @param examinationPaper
     * @param questionService
     * @return
     */
    public List<QuestionDto> getExaminationPaperQuestions(ExaminationPaper examinationPaper,
                                                                 IQuestionService questionService){
        if (examinationPaper == null){
            return null;
        }
        String noStr = examinationPaper.getExaminationPaperNo().toString();
        //从缓存中获取
        List<QuestionDto> questionsDto = redisUtil.getList(stringUtil.buildKey(noStr));

        if (questionsDto == null || questionsDto.size() == 0){
            //重新计算所有题目
            questionsDto = questionUtil.convert(getQuestionsByIds(examinationPaper.getQuestionsId(),questionService));
            if (questionsDto != null){
                redisUtil.cacheList(stringUtil.buildKey(noStr),questionsDto);
            }
        }
        return questionsDto;
    }

    /**
     *
     * @param examinationPapers
     * @param questionService
     * @return
     */
    public List<ExaminationPaperDto> convert(List<ExaminationPaper> examinationPapers,
                                                    IQuestionService questionService){
        if (examinationPapers == null || examinationPapers.size() ==0){
            return null;
        }
        List<ExaminationPaperDto> convertTos = new ArrayList<>();
        ExaminationPaperDto dto = null;
        for (ExaminationPaper entity : examinationPapers){
            dto = new ExaminationPaperDto();
            dto.setExaminationPaperNo(entity.getExaminationPaperNo());
            dto.setPaperName(entity.getPaperName());
            dto.setSingleNum(entity.getSingleNum());
            dto.setMultiNum(entity.getMultiNum());
            dto.setShortAnswerQuestionNum(entity.getShortAnswerQuestionNum());
            dto.setValidDeadline(entity.getValidDeadline());
            dto.setCreateTime(entity.getCreateTime());
            dto.setQuestionsId(entity.getQuestionsId());
            dto.setDecodeTimeStr(dateUtil.dateFormat(entity.getValidDeadline()));
            dto.setCreateTimeStr(dateUtil.dateFormat(entity.getCreateTime()));
            dto.setDetail("本套试卷有" + entity.getSingleNum() + "道单选题，" + entity.getMultiNum() +
            "道多选题，" + entity.getShortAnswerQuestionNum() + "道问答题，共" + entity.getQuestionsNum() + "道。");

            //添加到缓存中
            List<QuestionDto> questions = getExaminationPaperQuestions(entity,questionService);
            //RedisUtil.cacheList(StringUtil.buildKey(dto.getExaminationPaperNo().toString()),questions);
            dto.setQuestions(questions);
            convertTos.add(dto);
        }
        return convertTos;
    }

    /**
     * 将ids转成相应的Question
     * @param ids
     * @return
     */
    public List<Question> getQuestionsByIds(String ids,IQuestionService questionService){
        List<Question> questions = questionService.getQuestions();
        if (stringUtil.isNullOrEmpty(ids) || questions == null || questions.size() == 0){
            return null;
        }
        //本试卷中的题目集合
        List<Question> paperQuestions = new ArrayList<>();
        List<Long> qIds =  stringUtil.parseIds(ids);
        for (Long id : qIds){
            for (Question q : questions){
                if (q.getQuestionId() == id){
                    paperQuestions.add(q);
                    continue;
                }
            }
        }
        return paperQuestions;
    }

}
