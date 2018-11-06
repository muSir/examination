package cn.merson.examination.service;

import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.entity.Question;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 试题管理逻辑接口
 */
public interface IQuestionService {

    /**
     * 查询指定ID的试题
     * @param id
     * @return
     */
    ResultModel getQuestionById(String id,HttpServletRequest request);

    /**
     * 编辑试题 提交
     * @param questionId
     * @param title
     * @param answer
     * @param value
     * @param type
     * @param request
     * @return
     */
    ResultModel questionSave(String questionId,String title,String answer,String value,String type,
                             HttpServletRequest request);

    /**
     * 新增试题
     * @param request
     * @return
     */
    ResultModel questionNew(HttpServletRequest request);

    ResultModel questionsDelete(List<String> idsList,HttpServletRequest request);

    /**
     * 其他方法调用从缓存或者数据库获取全部题目
     * @return
     */
     List<Question> getQuestions();

    /**
     * 获取题库全部题目  不分页
     * @return
     */
    ResultModel getAllQuestions(HttpServletRequest request);

    /**
     * 分页查询  默认查询第一页，每页10条数据
     * @param pageNo 第N页
     * @param request
     * @return
     */
    ResultModel getAllQuestionsByPage(int pageNo,HttpServletRequest request);

    /**
     * 筛选分值相同的 题目
     * @param value
     * @param request
     * @return
     */
    ResultModel getQuestionsByValue(String value,HttpServletRequest request);

    /**
     * 管理员用一键筛选题库
     * @param questionName
     * @param questionAnswer
     * @param questionValue
     * @param questionType
     * @param questionRefence
     * @param request
     * @return
     */
    ResultModel queryByConditions(String questionName,String questionAnswer,String questionValue,
                      String questionType,String questionRefence,HttpServletRequest request);

    /**
     * 条件分页查询
     * @param condition
     * @param request
     * @return
     */
    ResultModel queryByConditions(String condition,int pageNo,HttpServletRequest request);

    /**
     * 批量导入题库（管理员上传excel或xml文件）
     * @param multipartFile
     * @param request
     * @return
     */
    ResultModel questionsSaveFromFile(MultipartFile multipartFile,HttpServletRequest request);

}
