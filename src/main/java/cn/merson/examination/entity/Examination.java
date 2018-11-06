package cn.merson.examination.entity;

import cn.merson.examination.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * 考试记录
 */
@Entity
public class Examination {

    //@Autowired
    //private DateUtil dateUtil;

    //记录ID
    @Id
    @GeneratedValue
    private Long recordId;

    //考生ID
    @Column(nullable = false)
    private Long userId;

    //考试开始时间
    @Column(nullable = false)
    private Date startTime;

    //考试结束时间
    @Column(nullable = false)
    private Date endTime;

    //考试总用时  单位：分钟
    @Column(nullable = false)
    private int useTime;

    //系统试卷ID
    @Column(nullable = false)
    private Long examinationPaperId;

    //试卷名称
    @Column(nullable = false)
    private String paperName;

    //总得分
    @Min(value = 0,message = "本套试题最低是0分！")
    @Max(value = 100,message = "本套试题最低是0分！")
    @Column(nullable = false)
    private Integer score;

    //记录试卷答案详情 => TODO 做成单独的一张表
    //private


    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getUseTime() {
        return useTime;
    }

    public void setUseTime() {
        try {
            DateUtil dateUtil = (DateUtil) DateUtil.getBean("dateUtil");
            this.useTime = dateUtil.timeDifference(startTime,endTime);
        } catch (Exception e) {
            this.useTime = 0;
        }
    }

    public Long getExaminationPaperId() {
        return examinationPaperId;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public void setExaminationPaperId(Long examinationPaperId) {
        this.examinationPaperId = examinationPaperId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
