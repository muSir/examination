package cn.merson.examination.common.dto;

import cn.merson.examination.common.enums.QuestionType;
import cn.merson.examination.common.util.StringUtil;
import cn.merson.examination.entity.Question;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 试题DTO
 */
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class QuestionDto extends Question{

    private StringUtil stringUtil = (StringUtil) StringUtil.getBean("stringUtil");

    //类型 ： 单选题/多选题/问答题
    private String dtoType;

    //是否是试卷中的最后一题
    private String isLast;

    //在试卷中的编号
    private int index;

    //是否是选择题
    private boolean isSelect;

    //选项集合
    private List<String> dtoOptions;

    //关键词集合
    private List<String> dtoKeywords;

    public String getDtoType() {
        return dtoType;
    }

    public void setDtoType(){
        if (QuestionType.SingleSelect.toString().equals(this.getType())){
            this.dtoType = "单选题";
        }else if (QuestionType.MultiSelect.toString().equals(this.getType())){
            this.dtoType = "多选题";
        }else if (QuestionType.ShortAnswer.toString().equals(this.getType())){
            this.dtoType = "问答题";
        }else {
            this.dtoType = "未知题型";
        }
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect() {
        this.isSelect = QuestionType.SingleSelect.toString().equals(this.getType())
                || QuestionType.MultiSelect.toString().equals(this.getType());
    }

    public List<String> getDtoOptions() {
        return dtoOptions;
    }
    public void setDtoOptions() {
        this.dtoOptions = isSelect ? stringUtil.parseStr(this.getOptions()) : null;
    }

    public String getIsLast() {
        return isLast;
    }

    public void setIsLast(String isLast) {
        this.isLast = isLast;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<String> getDtoKeywords() {
        return dtoKeywords;
    }

    public void setDtoKeywords() {
        this.dtoKeywords = !isSelect ? stringUtil.parseStr(this.getKeywords()) : null;
    }
}
