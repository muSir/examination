package cn.merson.examination.service;

import cn.merson.examination.common.dto.ExaminationPaperDto;
import cn.merson.examination.common.dto.QuestionDto;
import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.entity.ExaminationPaper;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 试卷模块逻辑接口
 */
public interface IExaminationPaperService {


    /**
     * 管理员创建指定条件的试卷
     * @param paperName
     * @param single
     * @param multi
     * @param shortAnswer
     * @param validDecodeValue
     * @param request
     * @return
     */
    ResultModel createExaminationPaper(String paperName,String single,String multi,String shortAnswer,
                                       String validDecodeValue,HttpServletRequest request);

    /**
     * 外部调用，没有权限判断
     * @return
     */
    List<ExaminationPaperDto> getAllPapers();

    /**
     * 获取有效期内的试卷 和当前系统时间作比较
     * @return
     */
    List<ExaminationPaperDto> getValidPapers();

    /**
     * 获取所有试卷信息
     * @param request
     * @return
     */
    ResultModel getAllExaminationPapers(HttpServletRequest request);

    /**
     * 获取指定试卷的所有题目
     * @param paperNo
     * @return
     */
    ResultModel getExaminationPaperQuestions(String paperNo);

    List<QuestionDto> getExaminationPaperQuestions(Long paperNo);

    /**
     * 删除指定No的试卷
     * @param paperNo
     * @param request
     * @return
     */
    ResultModel deleteExaminationPaper(String paperNo,HttpServletRequest request);

    /**
     * 按编号查询试卷
     * @param no
     * @return
     */
    ExaminationPaper getExaminationPaperByNo(Long no);

    /**
     *  一键查询满足所有条件的试卷
     * @param paperName
     * @param singleNum
     * @param multiNum
     * @param shortNum
     * @param deadline
     * @param create
     * @param request
     * @return
     */
    ResultModel queryExaminationPapers(String paperName,String singleNum,String multiNum,String shortNum,
                                       String deadline,String create,HttpServletRequest request);

    /**
     * 试卷名称模糊查询
     * @param paperName
     * @return
     */
    ResultModel getExaminationPaperLikeName(String paperName,HttpServletRequest request);

    /**
     * 查询有效截止日期内能使用的试卷
     * @param deadline
     * @return
     */
    ResultModel getExaminationPapersBeforeDeadline(Date deadline);

    /**
     * 按创建时间查询试卷
     * @param createTime
     * @return
     */
    ResultModel getExaminationPapersByCreateTime(Date createTime);

    /**
     * 总题数小于等于指定值
     * @param questionsNum
     * @return
     */
    ResultModel getExaminationPapersLessQuestionsNum(int questionsNum);

    /**
     * 总题数大于等于指定值
     * @param questionsNum
     * @return
     */
    ResultModel getExaminationPapersMoreQuestionsNum(int questionsNum);

    /**
     * 单选题数小于等于指定值
     * @param singleNum
     * @return
     */
    ResultModel getExaminationPapersLessSingleNum(int singleNum);

    /**
     * 单选题数大于等于指定值
     * @param singleNum
     * @return
     */
    ResultModel getExaminationPapersMoreSingleNumNum(int singleNum);

    /**
     * 多选题数目小于等于指定值
     * @param multiNum
     * @return
     */
    ResultModel getExaminationPapersLessMultiNum(int multiNum);

    /**
     * 多选题数目大于等于指定值
     * @param multiNum
     * @return
     */
    ResultModel getExaminationPapersMoreMultiNum(int multiNum);

    /**
     * 问答题数小于等于指定值
     * @param shortAnswerQuestionNum
     * @return
     */
    ResultModel getExaminationPapersLessShortAnswerQuestionNum(int shortAnswerQuestionNum);

    /**
     * 问答题数大于等于指定值
     * @param shortAnswerQuestionNum
     * @return
     */
    ResultModel getExaminationPapersMoreShortAnswerQuestionNum(int shortAnswerQuestionNum);

}
