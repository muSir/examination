package cn.merson.examination.repository;

import cn.merson.examination.entity.ExaminationPaper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IExaminationPaperRepositoryTest {

    @Autowired
    private IExaminationPaperRepository examinationPaperRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testGetExaminationPapersBeforeDeadline(){
        List<ExaminationPaper> papers = examinationPaperRepository.getExaminationPapersBeforeDeadline(new Date());
        if (papers != null){
            logger.info("一共有数据：" + papers.size());
        }
    }

    @Test
    public void getExaminationPapersLikePaperName(){
        List<ExaminationPaper> papers = examinationPaperRepository.getExaminationPapersLikePaperName("秋季");
        if (papers != null){
            logger.info("共找到" + papers.size() + "条满足条件的数据。");
            logger.info(papers.get(0).getPaperName());
        }
    }

}