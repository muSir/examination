package cn.merson.examination.service;

import cn.merson.examination.common.dto.ExaminationPaperDto;
import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.common.util.ResultUtil;
import cn.merson.examination.entity.Examination;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 考试模块业务逻辑接口
 */
public interface IExaminationService {

    /**
     * 考试前业务逻辑处理
     * @param studentId
     * @return
     */
    ResultModel beforeExamination(String studentId);

    /**
     * 查询全部考试记录
     * @param userId
     * @return
     */
    ResultModel queryRecord(String userId);


    List<Examination> queryExaminationRecordByUserId(Long userId);

    /**
     *  开始考试
     * @param userId
     * @param paperNo
     * @return
     */
    ResultModel startExamination(String userId,String paperNo,HttpServletRequest request);

    /**
     *
     * @param uid 用户ID
     * @param pid 试卷编号
     * @param request
     * @return
     */
    ResultModel nextQuestion(String uid, String pid, HttpServletRequest request);

    /**
     *  考生提交试卷  考试结束   评阅
     * @param userId
     * @param paperNo
     * @param request
     * @return
     */
    ResultModel finishExamination(String userId, String paperNo, HttpServletRequest request);

}
