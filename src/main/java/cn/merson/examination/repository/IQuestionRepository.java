package cn.merson.examination.repository;

import cn.merson.examination.entity.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**\
 * 题库模块持久层
 */
public interface IQuestionRepository extends CrudRepository<Question,Long>{

    /**
     * 分页查询所有的试题
     * @param start 第一条数据的位置 数据库是从0开始计算
     * @param count 一共需要查询多少条数据
     * @return
     */
    @Query(value = "SELECT * FROM question limit ?1,?2",nativeQuery = true)
    List<Question> getAllQuestionsByPage(int start,int count);

    /**
     * 按类型分页查询
     * @param type
     * @param start
     * @param count
     * @return
     */
    @Query(value = "SELECT * FROM question WHERE type=?1 limit ?2,?3",nativeQuery = true)
    List<Question> getQuestionsByTypeDividedPage(String type,int start,int count);

    /**
     * 获取试题的总数目
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM question",nativeQuery = true)
    int getTotalCount();

    /**
     *  查询所有分值相同（为value）的题目
     * @param value 试题分值
     * @return
     */
    List<Question> getQuestionsByValue(Integer value);

    /**
     * 题目名称模糊查询
     * @param questionName
     * @return
     */
    @Query(value = "SELECT * FROM question WHERE title LIKE %?1%",nativeQuery = true)
    List<Question> getQuestionsLikeName(String questionName);

    @Query(value = "SELECT * FROM question WHERE answer LIKE %?1%",nativeQuery = true)
    List<Question> getQuestionsLikeAnswer(String questionAnswer);

    @Query(value = "SELECT * FROM question WHERE reference LIKE %?1%",nativeQuery = true)
    List<Question> getQuestionsLikeReference(String reference);
}
