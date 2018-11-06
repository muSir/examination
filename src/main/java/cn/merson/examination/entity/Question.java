package cn.merson.examination.entity;

import cn.merson.examination.common.exceptions.ExaException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体 - 题目 ：对应数据库question表 由jpa生成
 */
@Entity
public class Question implements Serializable{
    //主键 自增
    @Id
    @GeneratedValue
    private Long questionId;

    //标题 不为空  长度限制为1000
    @Column(nullable = false,length = 1000)
    private String title;

    //试题类型 ：单选、多选、简答题
    @Column(nullable = false,length = 2000)
    private String type;

    //分值 最小值为1 最大值为100  大概判断
    @Min(value = 1,message = "试题分值不能低于1分")
    @Max(value = 100,message = "试题分值不能超过100分")
    @Column(nullable = false)
    private Integer value;

    //参考答案 默认长度为2000
    @Column(length = 2000)
    private String answer;

    //单选题或者多选题的选项（至少有两个选项） 多个选项以字符串",,,"隔开
    @Column(nullable = true,length = 2000)
    private String options;

    //关键词(多个关键词以字符串",,,"隔开) 用于问答题的评阅（问答题得分 = 问答题分值*(答对关键词个数/关键词总个数)）
    @Column(nullable = true,length = 2000)
    private String keywords;

    //参考答案解析，可以为空
    @Column(length = 2000,nullable = true)
    private String reference;

    //题目附图路径
    @Column(nullable = true,length = 2000)
    private String imagePath;

    //创建时间
    @Column(nullable = false)
    private Date createTime;

    //最近更新时间
    @Column(nullable = false)
    private Date lastUpdateTime;

    public Question(){}

    /**
     *  试题编辑时的构造方法
     * @param title
     * @param type
     * @param value
     * @param answer
     */
    public Question(Long id,String title, String type, Integer value, String answer) {
        this.questionId = id;
        this.title = title;
        this.type = type;
        this.value = value;
        this.answer = answer;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        if (value<=0 || value >100){
            throw new ExaException("question value is incorrect.");
        }
        this.value = value;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
