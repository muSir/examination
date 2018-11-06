package cn.merson.examination.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * 试卷
 */
@Entity
public class ExaminationPaper {

    //试卷编号
    @Id
    @GeneratedValue
    private Long examinationPaperNo;

    //试卷名称 每套试卷都对应唯一的名称 eg:xxx试卷
    @Column(nullable = false,unique = true)
    private String paperName;

    //总题数
    @Min(value = 0,message = "最少一道题")
    @Max(value = 100,message = "最多一百道题")
    @Column(nullable = false)
    private int questionsNum;

    //单选题数量
    @Max(value = 100,message = "最多一百道题")
    @Column(nullable = false)
    private int singleNum;

    //多选题数量
    @Max(value = 100,message = "最多一百道题")
    @Column(nullable = false)
    private int multiNum;

    //问答题数量
    @Max(value = 100,message = "最多一百道题")
    @Column(nullable = false)
    private int shortAnswerQuestionNum;

    //有效截止日期
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validDeadline;

    //所有试题的编号，中间用英文逗号","隔开
    @Column(nullable = false,length = 2500)
    private String questionsId;

    //试卷创建时间
    @Column(nullable = false)
    private Date createTime;

    //默认构造方法
    public ExaminationPaper(){}

    /**
     * 创建试卷时使用的构造方法
     * @param singleNum
     * @param multiNum
     * @param shortAnswerQuestionNum
     * @param questionsId
     */
    public ExaminationPaper(int singleNum, int multiNum, int shortAnswerQuestionNum, String questionsId) {
        this.singleNum = singleNum;
        this.multiNum = multiNum;
        this.shortAnswerQuestionNum = shortAnswerQuestionNum;
        this.questionsId = questionsId;
    }

    public Long getExaminationPaperNo() {
        return examinationPaperNo;
    }

    public void setExaminationPaperNo(Long examinationPaperNo) {
        this.examinationPaperNo = examinationPaperNo;
    }

    /**
     * 试题总数= 三种类型题目之和
     * @return
     */
    public int getQuestionsNum() {
        return this.singleNum + multiNum + shortAnswerQuestionNum;
    }

    public int getSingleNum() {
        return singleNum;
    }

    public void setSingleNum(int singleNum) {
        this.singleNum = singleNum;
    }

    public int getMultiNum() {
        return multiNum;
    }

    public void setMultiNum(int multiNum) {
        this.multiNum = multiNum;
    }

    public int getShortAnswerQuestionNum() {
        return shortAnswerQuestionNum;
    }

    public void setShortAnswerQuestionNum(int shortAnswerQuestionNum) {
        this.shortAnswerQuestionNum = shortAnswerQuestionNum;
    }

    public Date getValidDeadline() {
        return validDeadline;
    }

    public void setValidDeadline(Date validDeadline) {
        this.validDeadline = validDeadline;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getQuestionsId() {
        return questionsId;
    }

    public void setQuestionsId(String questionsId) {
        this.questionsId = questionsId;
    }
}
