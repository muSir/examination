package cn.merson.examination.common.util;

import cn.merson.examination.common.dto.QuestionDto;
import cn.merson.examination.common.enums.IRedisKey;
import cn.merson.examination.common.enums.QuestionType;
import cn.merson.examination.entity.Question;
import cn.merson.examination.repository.IQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 题目工具类
 */
@Component
public class QuestionUtil {

    @Autowired
    private StringUtil stringUtil;

    public final String SINGLE_SELECT = "SingleSelect";
    public final String MULTI_SELECT = "MultiSelect";
    public final String SHORT_ANSWER = "ShortAnswer";
    /*public static final String SINGLE_SELECT = "SingleSelect";
    public static final String SINGLE_SELECT = "SingleSelect";
    public static final String SINGLE_SELECT = "SingleSelect";*/

    /**
     * 以当前时间创建question实例并检测正确性
     * @param type
     * @param title
     * @param options
     * @param answer
     * @param value
     * @param keywords
     * @param reference
     * @return
     */
    public Question newQuestionInstance(String type,String title,String options,String answer,int value,String keywords,String reference){
        Question question = new Question();
        question.setType(type);
        question.setTitle(title);
        question.setOptions(options);
        question.setAnswer(answer);
        question.setValue(value);
        question.setKeywords(keywords);
        question.setReference(reference);
        question.setCreateTime(new Date());
        question.setLastUpdateTime(new Date());
        if (!isQuestion(question)){
            return null;
        }
        try {
            if (isSelectQuestion(question)){
                question.setOptions(optionsTransfer(options));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return question;
    }

    /**
     * 是否是选择题
     * @param question
     * @return
     */
    public boolean isSelectQuestion(Question question){
        return QuestionType.SingleSelect.toString().equals(question.getType()) || QuestionType.MultiSelect.toString().equals(question.getType());
    }

    /**
     * 判断题目是否正确:选择题必须有选项，问答题必须有关键词
     * @param question
     * @return
     */
    public boolean isQuestion(Question question){
        if (question == null){
            return false;
        }
        boolean isType = QuestionType.SingleSelect.toString().equals(question.getType()) || QuestionType.MultiSelect.toString().equals(question.getType())
                || QuestionType.ShortAnswer.toString().equals(question.getType());
        if (!isType){
            return false;
        }
        if (stringUtil.isNullOrEmpty(question.getTitle())){
            return false;
        }
        if (isSelectQuestion(question)){
            if (stringUtil.isNullOrEmpty(question.getOptions())){
                return false;
            }
        }
        if (!isSelectQuestion(question)){
            if (stringUtil.isNullOrEmpty(question.getKeywords())){
                return false;
            }
        }
        if (stringUtil.isNullOrEmpty(question.getAnswer())){
            return false;
        }
        if (question.getValue() <= 0 || question.getValue() > 100){
            return false;
        }
        return true;
    }

    /**
     * 将Question转为QuestionDto
     * @param questions
     * @return
     */
    public List<QuestionDto> convert(List<Question> questions){
        if (questions == null || questions.size() == 0){
            return  null;
        }
        List<QuestionDto> questionDtos = new ArrayList<>();
        QuestionDto dto = null;
        Question q = null;
        int size = questions.size();
        for (int i = 0;i < size;i++){
            q = questions.get(i);
            dto = new QuestionDto();
            dto.setQuestionId(q.getQuestionId());
            dto.setTitle(q.getTitle());
            dto.setType(q.getType());
            dto.setValue(q.getValue());
            dto.setAnswer(q.getAnswer());
            dto.setOptions(q.getOptions());
            dto.setKeywords(q.getKeywords());
            dto.setReference(q.getReference());
            dto.setCreateTime(q.getCreateTime());
            dto.setLastUpdateTime(dto.getLastUpdateTime());
            dto.setDtoType();
            dto.setSelect();
            dto.setDtoOptions();
            dto.setDtoKeywords();
            dto.setIndex(i+1);
            //是否为此集合中的最后一个元素
            if (i == size -1){
                dto.setIsLast("isLast");
            }
            else {
                dto.setIsLast("");
            }
            questionDtos.add(dto);
        }
        return questionDtos;
    }


    /**
     * 把用户输入的选项组装成数据库存储的格式
     * @param option
     * @return
     */
    public String optionsTransfer(String option){
        if (stringUtil.isNullOrEmpty(option)){
            throw new NullPointerException("options is empty");
        }
        String[] options = option.split(";  ");
        if (options.length ==  1){
            return options[0];
        }
        StringBuilder sb = new StringBuilder("");
        for (String str : options){
            sb.append(str + ",,,");
        }
        return sb.toString();
    }


}
