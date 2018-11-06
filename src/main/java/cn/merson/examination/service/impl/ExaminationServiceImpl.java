package cn.merson.examination.service.impl;

import cn.merson.examination.common.dto.ExaminationPaperDto;
import cn.merson.examination.common.dto.QuestionDto;
import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.common.util.*;
import cn.merson.examination.entity.Examination;
import cn.merson.examination.entity.ExaminationPaper;
import cn.merson.examination.entity.Question;
import cn.merson.examination.entity.User;
import cn.merson.examination.repository.IExaminationRepository;
import cn.merson.examination.service.IExaminationPaperService;
import cn.merson.examination.service.IExaminationService;
import cn.merson.examination.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 考试模块具体实现
 */
@Service
public class ExaminationServiceImpl implements IExaminationService{

    @Autowired
    private IExaminationRepository examinationRepository;

    @Autowired
    private IExaminationPaperService examinationPaperService;

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private ScoreUtil scoreUtil;
    @Autowired
    private StringUtil stringUtil;
    @Autowired
    private ResultUtil resultUtil;

    /**
     * 获取满足考生考试的试卷
     * @param studentId
     * @return
     */
    @Override
    public ResultModel beforeExamination(String studentId) {
        if (stringUtil.isNullOrEmpty(studentId)){
            //跳转到考生登录界面
            return resultUtil.errorResult(1,"请填写信息后再参加考试！","redirect:/users/student");
        }
        //获取有效期内的试卷
        List<ExaminationPaperDto> papers = examinationPaperService.getValidPapers();
        List<ExaminationPaperDto> papers1 = new ArrayList<>();
        boolean hasPaper = papers != null &&  papers.size() > 0;
        //第二次筛选
        if (hasPaper){
            Long userId = Long.parseLong(studentId);
            //获取该学生的考试记录
            List<Examination> examinations = getAllRecordByUserId(userId);
            if (examinations != null){
                List<Long> ids = new ArrayList<>();
                for (Examination e : examinations){
                    ids.add(e.getExaminationPaperId());
                }
                for(ExaminationPaperDto dto : papers){
                    if (!ids.contains(dto.getExaminationPaperNo())){
                        papers1.add(dto);
                    }
                }
            }
        }
        //
        if (!hasPaper){
            return resultUtil.errorResult(1,
                    "当前没有您可用的试卷，请联系管理员创建试卷！","student");
        }
        return resultUtil.successResult(papers1,"","studentexamination");
    }


    @Override
    public ResultModel queryRecord(String userId) {
        List<Examination> examinations = getAllRecordByUserId(Long.parseLong(userId));
        return resultUtil.successResult(examinations,"","student");
    }

    private List<Examination> getAllRecordByUserId(Long userId){
        return  examinationRepository.getAllByUserId(userId);
    }

    @Override
    public List<Examination> queryExaminationRecordByUserId(Long userId) {
        return examinationRepository.getAllByUserId(userId);
    }

    /**
     *  开始考试
     * @param userId
     * @param paperNo
     * @return
     */
    @Override
    public ResultModel startExamination(String userId, String paperNo,HttpServletRequest request) {
        String errorUrl =  "forward:/examinations/"+ paperNo +"/papers";
        Long no = Long.parseLong(paperNo);
        //获取该试卷的全部题目Dto
        List<QuestionDto> questions = examinationPaperService.getExaminationPaperQuestions(no);
        if (questions == null){
            return resultUtil.errorResult(1,"没有找到题目！",errorUrl);
        }
        //开始时间写入缓存 session
        //RedisUtil.cacheObject(StringUtil.buildKey(userId+paperNo),new Date());
        request.getSession().setAttribute("startTime:" + userId + paperNo,new Date());
        return resultUtil.successResult(questions,"","examination");
    }

    /**
     * 特定试卷中的下一题
     * @param uid 用户ID
     * @param pid 试卷编号
     * @param request
     * @return
     */
    @Override
    public ResultModel nextQuestion(String uid, String pid, HttpServletRequest request) {
       // User user = LoginUserUtil.getCurrentLoginUser(request);
        /*if (user == null || !user.getUserId().toString().equals(uid)){
            //考试用户和登录用户不匹配或者已经断开连接  放弃考试 返回登录界面
            return ResultUtil.errorResult(1,"","redirect:/users/student");
        }*/
        String qid = request.getParameter("qid");
        String nid = request.getParameter("nid");

        ExaminationPaper paper = examinationPaperService.getExaminationPaperByNo(Long.parseLong(pid));
        String single = request.getParameter("single");
        String multi = request.getParameter("multi");
        String shortQuestionAnswer = request.getParameter("shortQuestionAnswer");
        int beforeScore = scoreUtil.getScore(uid,pid,request);
        //计算当前题目得分
        int score = 0;
        if (!stringUtil.isNullOrEmpty(single)){
            //单选题得分
            score = scoreUtil.singleScore(paper,Long.parseLong(qid),single,questionService);
        }else if (!stringUtil.isNullOrEmpty(multi)){
            score = scoreUtil.multiScore(paper,Long.parseLong(qid),multi,questionService);
        }else if (!stringUtil.isNullOrEmpty(shortQuestionAnswer)){
            score = scoreUtil.multiScore(paper,Long.parseLong(qid),shortQuestionAnswer,questionService);
        }
        //将当前放在session中
        scoreUtil.saveScore(uid,pid,score+beforeScore,request);
        //下一题的ID
        if (stringUtil.isNullOrEmpty(nid)){
            //最后一题  交卷
            return finishExamination(uid,pid,request);
        }
        if (stringUtil.isNullOrEmpty(nid)){
            return resultUtil.errorResult(1,"已经是最后一题啦！交卷！","");
        }
        //从缓存中获取已经缓存好的试卷题目
        List<QuestionDto> questionDtos = examinationPaperService.getExaminationPaperQuestions(Long.parseLong(pid));
        QuestionDto next = null;
        Long nextId = null;
        //是否包含此题目
        boolean flag = false;
        boolean hasNext = false;
        QuestionDto dto = null;
        for (int i = 0;i < questionDtos.size();i++){
            dto = questionDtos.get(i);
            if (dto.getQuestionId() ==Long.parseLong(nid)){
                flag = true;
                next = dto;
                if (!"isLast".equals(dto.getIsLast())){
                    hasNext = true;
                    nextId = questionDtos.get(i+1).getQuestionId();
                }
                break;
            }
        }

        if (!flag){
            return resultUtil.errorResult(1,"试卷中没有此题目！","examination");
        }

        if (hasNext){
            //还有下一题
            return resultUtil.successResult(next,nextId.toString(),"examination");
        }

        return resultUtil.successResult(next,"","examination");
    }


    /**
     * 结束考试
     * @param userId
     * @param paperNo
     * @param request
     * @return
     */
    @Override
    public ResultModel finishExamination(String userId, String paperNo,HttpServletRequest request) {
        //开始时间
        //Date startTime = (Date) RedisUtil.getObject(StringUtil.buildKey(userId+paperNo));
        Date startTime = (Date) request.getSession().getAttribute("startTime:" + userId + paperNo);
        //结束时间
        Date end = new Date();
        Long endTimes = System.currentTimeMillis();
        //试卷名称
        String paperName = request.getParameter("paperName");

        //最后一题的得分

        Examination record = new Examination();
        record.setStartTime(startTime);
        record.setEndTime(end);
        record.setUseTime();
        record.setExaminationPaperId(Long.parseLong(paperNo));
        record.setUserId(Long.parseLong(userId));
        record.setPaperName(paperName);
        record.setScore(scoreUtil.getScore(userId,paperNo,request));

        Examination back = examinationRepository.save(record);
        if (back == null){
            return resultUtil.errorResult(1,"系统异常","");
        }
        List<Examination> examinations = getAllRecordByUserId(Long.parseLong(userId));
        //评阅完成，跳转到考试记录页面
        return resultUtil.successResult(examinations,"","student");
    }
}
