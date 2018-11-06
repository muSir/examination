package cn.merson.examination.repository;

import cn.merson.examination.common.enums.QuestionType;
import cn.merson.examination.entity.Question;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IQuestionRepositoryTest {

    @Autowired
    private IQuestionRepository questionRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    @Test
    public void add(){
        Question q = new Question();
        q.setTitle("问题1");
        q.setType(QuestionType.MultiSelect.toString());
        q.setAnswer("C");
        q.setValue(15);
        SimpleDateFormat sdf = new SimpleDateFormat();
        Date now = new Date();
        q.setCreateTime(now);
        q.setLastUpdateTime(now);
        Question back = questionRepository.save(q);
        if (back != null){
            logger.info("id:" + back.getQuestionId());
        }
        else {
            logger.info("insert failed");
        }
    }

    @Test
    public void get(){
        List<Question> questions = (List<Question>) questionRepository.findAll();
        if (questions != null){
            logger.info(questions.size() + "");
        }
    }

    @Test
    public void delete(){
        Long id = 1L;
        questionRepository.delete(id);
    }

    @Test
    public void testConditionSearch(){
        List<Question> questions = questionRepository.getQuestionsByTypeDividedPage("MultiSelect",0,10);
        if (questions != null){
            for (Question q : questions){
                System.out.println(q.getTitle());
            }
        }
    }

}