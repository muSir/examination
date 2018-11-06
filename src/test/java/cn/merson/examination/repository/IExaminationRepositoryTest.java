package cn.merson.examination.repository;

import cn.merson.examination.entity.Examination;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IExaminationRepositoryTest {

    @Autowired
    private IExaminationRepository examinationRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void getAllByUserId() throws Exception {
        List<Examination> examinations = examinationRepository.getAllByUserId(1L);
        if (examinations != null){
            logger.info(examinations.size()+ "");
        }
    }

}