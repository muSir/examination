package cn.merson.examination.common.util;

import cn.merson.examination.common.dto.QuestionDto;
import cn.merson.examination.configuration.BaseExaminationApplicationContext;
import cn.merson.examination.entity.ExaminationPaper;
import cn.merson.examination.entity.Question;
import cn.merson.examination.service.IQuestionService;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 计算试题的得分
 * @Author: created by Merson
 * Created on 2017/12/8 0008 02:19
 */
@Component
public class ScoreUtil extends BaseExaminationApplicationContext {

    @Autowired
    private ExaminationPaperUtil examinationPaperUtil;

    @Autowired
    private StringUtil stringUtil;

    /**
     *
     * @param uid
     * @param pid
     * @param currentScore
     * @param request
     */
    public void saveScore(String uid,String pid,int currentScore,HttpServletRequest request){
        request.getSession().setAttribute(stringUtil.buildKey(uid+pid),currentScore);
    }

    //获取当前试卷得分
    public int getScore(String uid,String pid,HttpServletRequest request){
        int score = 0;
        try {
            score = (int) request.getSession().getAttribute(stringUtil.buildKey(uid+pid));
        } catch (Exception e) {
            score = 0;
        }
        return score;
    }

    /**
     * 计算得分
     * @param questionId
     * @param single
     * @param multi
     * @param shortAnswer
     * @param paper
     * @param questionService
     * @return
     */
    public int score(Long questionId,String single,String multi,String shortAnswer,ExaminationPaper paper,IQuestionService questionService){
        return 0;
    }

    /**
     * 计算单选题得分 单选题答案只有一个，必须是标准答案
     * @param  paper 试卷
     * @param questionId 试题ID
     * @param result 考生答案
     * @param  questionService
     * @return
     */
    public int singleScore(ExaminationPaper paper, Long questionId, String result, IQuestionService questionService){
        //没有作答，得0分
        if (stringUtil.isNullOrEmpty(result)){
            return 0;
        }
        int score = 0;
        try {
            Question question = getQuestionById(paper,questionId,questionService);
            if (question != null){
                if (result.equals(question.getValue())){
                    //如果选项正确得全分
                    score = question.getValue();
                }
            }
        } catch (Exception e) {

        }

        return score;
    }

    /**
     *  多选题得分计算方式：如果有一个选项错误，则得0分；如果选项全部正确，得满分；如果部分正确，则得一半
     * @param paper
     * @param questionId
     * @param result
     * @param questionService
     * @return
     */
    public int multiScore(ExaminationPaper paper, Long questionId, String result, IQuestionService questionService){
        //没有作答，得0分
        if (stringUtil.isNullOrEmpty(result)){
            return 0;
        }
        int score = 0;
        try {
            Question question = getQuestionById(paper,questionId,questionService);
            if (question != null){
                String answer = question.getAnswer();
                int value = question.getValue();

                if (result.length() == answer.length()){
                    if (answer.equals(result)){
                        //得满分
                        score = value;
                    }
                }else if (result.length() < answer.length()){
                    //包含
                    if (answer.contains(result)){
                        //得一半分
                        score = value / 2;
                    }
                }
            }
        } catch (Exception e) {
        }
        return score;
    }


    /**
     * 问答题得分计算方式： 关键词个数  总长度>=80%酌情给分
     * @param paper
     * @param questionId
     * @param result
     * @param questionService
     * @return
     */
    public int shortQuestionScore(ExaminationPaper paper, Long questionId, String result, IQuestionService questionService){
        //没有作答，得0分
        if (stringUtil.isNullOrEmpty(result)){
            return 0;
        }
        int score = 0;
        try {
            QuestionDto question = getQuestionById(paper,questionId,questionService);
            if (question != null){
                String answer = question.getAnswer();
                List<String> keywords = question.getDtoKeywords();
                int value = question.getValue();

                //关键词个数
                int count = 0;
                if (keywords != null){
                    for (String keyword : keywords){
                        if (result.contains(keyword)){
                            count++;
                        }
                    }
                }
                //关键词全部答对,并且长度超过0.8得满分
                if (count == keywords.size() && result.length() >= answer.length()*0.8){
                    score = value;
                }else if (count < keywords.size()){
                    //关键词数量少，百分比得分
                    score = count * value / keywords.size();
                }
            }
        } catch (Exception e) {
        }
        return score;
    }


    /**
     *   获取指定问题的答案
     * @param paper
     * @param questionId
     * @param questionService
     * @return question 包含标准答案和分值
     */
    private QuestionDto getQuestionById(ExaminationPaper paper, Long questionId, IQuestionService questionService){
        QuestionDto question = null;
        List<QuestionDto> questionDtos = examinationPaperUtil.getExaminationPaperQuestions(paper,questionService);
        if (questionDtos == null){
            throw new  RuntimeException("cache timeout exception.");
        }
        for (QuestionDto dto : questionDtos){
            if (dto.getQuestionId().equals(questionId)){
                question = dto;
            }
        }
        return question;
    }

}
