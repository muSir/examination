package cn.merson.examination.common.util;

import cn.merson.examination.entity.ExaminationPaper;
import cn.merson.examination.repository.IQuestionRepository;
import cn.merson.examination.service.IQuestionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExaminationPaperUtilTest {

    @Autowired
    private IQuestionRepository questionRepository;

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private ExaminationPaperUtil examinationPaperUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void createExaminationPaper() throws Exception {

    }

    /**
     * 随机生成试卷单元测试
     * @throws Exception
     */
    @Test
    public void createExaminationPaper1() throws Exception {
        ExaminationPaper paper = examinationPaperUtil.createExaminationPaper(questionService);
        if (paper != null){
            logger.info("随机生成试卷的总题数：" + paper.getQuestionsNum());
            logger.info("随机生成试卷的题目编号：" + paper.getQuestionsId());
        }
    }

}