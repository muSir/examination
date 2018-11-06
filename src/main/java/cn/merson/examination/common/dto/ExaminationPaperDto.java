package cn.merson.examination.common.dto;

import cn.merson.examination.entity.ExaminationPaper;
import cn.merson.examination.entity.Question;

import java.util.Date;
import java.util.List;

/**
 * 客户端使用的试卷实体  包含具体的题目
 */
public class ExaminationPaperDto{

    private Long examinationPaperNo;
    private String paperName;
    private int questionsNum;
    private int singleNum;
    private int multiNum;
    private int shortAnswerQuestionNum;
    private Date validDeadline;
    private String questionsId;
    private Date createTime;

    //将ExaminationPaper中的questionsId转化为对应的实体
    private List<QuestionDto> questions;

    //单选： 多选：问答：总题目数：
    private String detail;

    //创建时间的字符串
    private String createTimeStr;

    //有效截止日期的字符串
    private String decodeTimeStr;

    public Long getExaminationPaperNo() {
        return examinationPaperNo;
    }

    public void setExaminationPaperNo(Long examinationPaperNo) {
        this.examinationPaperNo = examinationPaperNo;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public int getQuestionsNum() {
        return questionsNum;
    }

    public void setQuestionsNum(int questionsNum) {
        this.questionsNum = questionsNum;
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

    public String getQuestionsId() {
        return questionsId;
    }

    public void setQuestionsId(String questionsId) {
        this.questionsId = questionsId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getDecodeTimeStr() {
        return decodeTimeStr;
    }

    public void setDecodeTimeStr(String decodeTimeStr) {
        this.decodeTimeStr = decodeTimeStr;
    }
}
