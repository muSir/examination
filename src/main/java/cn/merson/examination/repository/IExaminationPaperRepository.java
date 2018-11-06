package cn.merson.examination.repository;

import cn.merson.examination.entity.Examination;
import cn.merson.examination.entity.ExaminationPaper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * 试卷模块持久层
 */
public interface IExaminationPaperRepository extends CrudRepository<ExaminationPaper,Long>{

    /**
     * 分页查询所有的试卷
     * @param start
     * @param count
     * @return
     */
    @Query(value = "SELECT * FROM question limit ?1,?2",nativeQuery = true)
    List<ExaminationPaper> getAllExaminationPapersByPage(int start,int count);

    /**
     * 查询数据库中试卷的总数
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM question",nativeQuery = true)
    int getTotalCount();

    /**
     * 查询有效日期范围内的试卷
     * @param dateTime
     * @return
     */
    @Query(value = "SELECT * from examination_paper WHERE valid_deadline>=?1",nativeQuery = true)
    List<ExaminationPaper> getExaminationPapersBeforeDeadline(Date dateTime);

    /**
     * 查询所有单选题数量为指定值的试卷
     * @param singleNum
     * @return
     */
    List<ExaminationPaper> getExaminationPapersBySingleNum(Integer singleNum);

    /**
     * 多选题查询
     * @param multiNum
     * @return
     */
    List<ExaminationPaper> getExaminationPapersByMultiNum(Integer multiNum);

    /**
     * 问答题查询
     * @param shortAnswerNum
     * @return
     */
    List<ExaminationPaper> getExaminationPapersByShortAnswerQuestionNum(Integer shortAnswerNum);

    /**
     * 试卷名称模糊查询
     * @param paperName
     * @return
     */
    @Query(value = "SELECT * FROM examination_paper WHERE paper_name LIKE %?1%",nativeQuery = true)
    List<ExaminationPaper> getExaminationPapersLikePaperName(String paperName);

}
