package cn.merson.examination.common.util;

import cn.merson.examination.common.enums.QuestionType;
import cn.merson.examination.entity.Question;
import cn.merson.examination.repository.IQuestionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;

/**
 * Redis缓存单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisUtilTest {

    @Autowired
    private IQuestionRepository questionRepository;
    @Autowired
    private RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //测试缓存List集合
    @Test
    public void testCacheList(){
        List<Question> questions = null;//questionRepository.getAllQuestionsByPage(0,20);
        if (questions == null || questions.size() == 0){
            questions = new ArrayList<>();
            for(int i = 0;i<10;i++){
                Question question = new Question();
                question.setTitle("题目" + i);
                question.setAnswer("答案" + i);
                question.setCreateTime(new Date());
                question.setLastUpdateTime(new Date());
                question.setType(QuestionType.ShortAnswer.toString());
                question.setValue(10);
                questions.add(question);
            }
            redisUtil.cacheList("questions",questions);
            List<Question> redisQuestions = redisUtil.getList("questions");
            if (redisQuestions != null){
                for (Question q : redisQuestions){
                    logger.info(q.getTitle());
                }
            }
            else {
                logger.info("error");
            }
        }
    }

    @Test
    public void testCacheObject() throws Exception {
        Question question = new Question();
        question.setTitle("题目");
        question.setAnswer("答案");
        question.setCreateTime(new Date());
        question.setLastUpdateTime(new Date());
        question.setType(QuestionType.ShortAnswer.toString());
        question.setValue(10);
        redisUtil.cacheObject("question",question);
        Question q = (Question) redisUtil.getObject("question");
        if (q != null){
            logger.info(q.getTitle());
        }
        else {
            logger.info("error");
        }
    }

    @Test
    public void testGetList() throws Exception {
    }

    @Test
    public void testGetObject() throws Exception {
    }

}