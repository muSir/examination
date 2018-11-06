package cn.merson.examination.service.impl;

import cn.merson.examination.common.dto.ExaminationPaperDto;
import cn.merson.examination.common.dto.Page;
import cn.merson.examination.common.dto.QuestionDto;
import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.common.enums.IRedisKey;
import cn.merson.examination.common.util.*;
import cn.merson.examination.entity.ExaminationPaper;
import cn.merson.examination.entity.Question;
import cn.merson.examination.entity.User;
import cn.merson.examination.repository.IExaminationPaperRepository;
import cn.merson.examination.repository.IQuestionRepository;
import cn.merson.examination.service.IExaminationPaperService;
import cn.merson.examination.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 试卷模块业务逻辑具体实现类
 */
@Service
public class ExaminationPaperServiceImpl implements IExaminationPaperService {

    @Autowired
    private IExaminationPaperRepository examinationPaperRepository;

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private ExaminationPaperUtil examinationPaperUtil;
    @Autowired
    private StringUtil stringUtil;
    @Autowired
    private ResultUtil resultUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LoginUserUtil loginUserUtil;

    /**
     * 管理员角色创建随机或者指定试卷
     */
    @Transactional
    @Override
    public ResultModel createExaminationPaper(String paperName,String single,String multi,String shortAnswer,
                                              String validDecodeValue,HttpServletRequest request) {
        if (!loginUserUtil.isAdmin(request)){
            return resultUtil.errorResult(1,"您没有权限创建试卷！","newpaper");
        }
        /**
         * 试卷基础信息判断
         */
        ExaminationPaper paper = new ExaminationPaper();
        if (stringUtil.isNullOrEmpty(paperName)){
            return resultUtil.errorResult(1,"试卷名字不能为空！","newpaper");
        }
        paper.setPaperName(paperName);
        if (stringUtil.isNullOrEmpty(single)){
            single = "0";
        }
        if (stringUtil.isNullOrEmpty(multi)){
            multi= "0";
        }
        if (stringUtil.isNullOrEmpty(shortAnswer)){
            shortAnswer = "0";
        }
        int singleNum = 0;
        int multiNum = 0;
        int shortAnswerNum = 0;
        //题目数量类型转换
        try {
            singleNum = Integer.parseInt(single)<0 ? 0: Integer.parseInt(single);
            multiNum = Integer.parseInt(multi)<0 ? 0: Integer.parseInt(multi);
            shortAnswerNum = Integer.parseInt(shortAnswer)<0 ? 0: Integer.parseInt(shortAnswer);
        }catch (NumberFormatException ex){
            return resultUtil.errorResult(1,"题目数目格式错误！","newpaper");
        }
        Date validDecode = dateUtil.dateParse(validDecodeValue);
        if (validDecode == null){
            return resultUtil.errorResult(1,"时间转换错误！","newpaper");
        }
        //有效日期判断 有效日期不能小于当前系统时间
        if (!dateUtil.compareDate(validDecode)) {
            //有效日期错误
            return resultUtil.errorResult(1,"试卷的有效日期不能少于当前时间！","newpaper");
        }
        paper.setValidDeadline(validDecode);
        //生成试卷逻辑
        ExaminationPaper examinationPaper =
                examinationPaperUtil.createExaminationPaper(singleNum,multiNum,shortAnswerNum,questionService);
        if (examinationPaper == null){
            return resultUtil.errorResult(1,"题库的题目不能生成满足条件的试卷！","newpaper");
        }
        paper.setQuestionsId(examinationPaper.getQuestionsId());
        paper.setSingleNum(examinationPaper.getSingleNum());
        paper.setMultiNum(examinationPaper.getMultiNum());
        paper.setShortAnswerQuestionNum(examinationPaper.getShortAnswerQuestionNum());
        paper.setCreateTime(new Date());
        ExaminationPaper saveBack = examinationPaperRepository.save(paper);
        if (saveBack == null){
            return resultUtil.errorResult(1,"保存试卷失败","newpaper");
        }
        //写到缓存
        redisUtil.appendObjectToList(IRedisKey.PAPER,saveBack);

        List<ExaminationPaper> redisExaminationPapers = redisUtil.getList(IRedisKey.PAPER);
        if (redisExaminationPapers != null){
            redisExaminationPapers.add(saveBack);
        }

        //刷新列表
        return resultUtil.successResult(saveBack,"创建试卷成功！","redirect:/papers");
    }

    @Override
    public List<ExaminationPaperDto> getAllPapers() {
        //判断redis中是否存在，如果不存在则从库里查询得到并缓存到redis中
        //int totalCount = examinationPaperRepository.getTotalCount();
        List<ExaminationPaper> examinationPapers = (List<ExaminationPaper>) examinationPaperRepository.findAll();
        /*Page page = new Page(request);
        int startIndex = page.getStartIndex();
        List<ExaminationPaper> examinationPapers =
                examinationPaperRepository.getAllExaminationPapersByPage(startIndex,page.getTotalCount());*/
        //将Entity转化为dto
        return examinationPaperUtil.convert(examinationPapers,questionService);
    }

    /**
     *
     * @return
     */
    @Override
    public List<ExaminationPaperDto> getValidPapers() {
        List<ExaminationPaper> papers =
                examinationPaperRepository.getExaminationPapersBeforeDeadline(new Date());
        return examinationPaperUtil.convert(papers,questionService);
    }

    /**
     * 获取试卷列表+权限判断
     * @param request
     * @return
     */
    @Override
    public ResultModel getAllExaminationPapers(HttpServletRequest request) {
        //权限判断
        if (!loginUserUtil.isAdmin(request)){
            return resultUtil.errorResult(1,"非管理员没有权限查看所有试卷！","redirect:/users/admin");
        }
        List<ExaminationPaperDto> paperDtos = getAllPapers();
        return resultUtil.successResult(paperDtos,"","paper");
    }

    /**
     * 查看指定试卷所有题目
     * @param paperNo
     * @return
     */
    @Transactional
    @Override
    public ResultModel getExaminationPaperQuestions(String paperNo) {
        Long examinationPaperNo = Long.parseLong(paperNo);
        //TODO 应该先从缓存中去找
        ExaminationPaper examinationPaper = examinationPaperRepository.findOne(examinationPaperNo);
        List<QuestionDto> dtos = getExaminationPaperQuestions(examinationPaperNo);
        return resultUtil.successResult(dtos,examinationPaper.getPaperName(),"paperdetail");
    }

    /**
     *  外部可以调用
     * @return
     */
    @Override
    public List<QuestionDto> getExaminationPaperQuestions(Long paperNo){
        ExaminationPaper examinationPaper = getExaminationPaperByNo(paperNo);
        return examinationPaperUtil.getExaminationPaperQuestions(examinationPaper,questionService);
    }

    /**
     * 删除指定编号的试卷
     * @param paperNo
     * @param request
     * @return
     */
    @Override
    public ResultModel deleteExaminationPaper(String paperNo, HttpServletRequest request) {
        if (!loginUserUtil.isAdmin(request)){
            return resultUtil.errorResult(1,"非管理员没有权限删除试卷！",
                    "redirect:/users/admin");
        }
        if (stringUtil.isNullOrEmpty(paperNo)){
            return resultUtil.errorResult(1,"试卷编号错误！","redirect:/papers");
        }
        Long examinationPaperNo = Long.parseLong(paperNo);
        try {
            examinationPaperRepository.delete(examinationPaperNo);
        } catch (Exception e) {
            return resultUtil.errorResult(1,"从数据库中删除失败！","redirect:/papers");
        }
        return resultUtil.successResult(examinationPaperNo,"删除试卷成功。","redirect:/papers");
    }

    @Override
    public ExaminationPaper getExaminationPaperByNo(Long no) {
        ExaminationPaper examinationPaper = examinationPaperRepository.findOne(no);
        return examinationPaper;
    }

    /**
     * 一键查询满足指定条件的试卷
     * @param paperName
     * @param singleNum
     * @param multiNum
     * @param shortNum
     * @param deadline
     * @param create
     * @param request
     * @return
     */
    @Override
    public ResultModel queryExaminationPapers(String paperName, String singleNum, String multiNum, String shortNum,
                                              String deadline, String create, HttpServletRequest request) {
        if (!loginUserUtil.isAdmin(request)){
            return resultUtil.errorResult(1,"您没有权限查阅试卷！",
                    "redirect:/users/admin");
        }
        if (stringUtil.isNullOrEmpty(paperName)){
            paperName = "";
        }
        //试卷名模糊查询,如果为空字符串，则返回所有数据
        List<ExaminationPaper> papers = getExaminationPaperLikeName(paperName);
        if (papers == null || papers.size() == 0){
            return resultUtil.errorResult(1,"没有试卷！",
                    "redirect:/papers");
        }
        if (stringUtil.isNullOrEmpty(singleNum)){
            singleNum = "-1";
        }
        if (stringUtil.isNullOrEmpty(multiNum)){
            multiNum = "-1";
        }
        if (stringUtil.isNullOrEmpty(shortNum)){
            shortNum = "-1";
        }
        int single = 0;
        int multi = 0;
        int shortAnswer = 0;
        try {
            single = Integer.parseInt(singleNum);
            multi = Integer.parseInt(multiNum);
            shortAnswer = Integer.parseInt(shortNum);
        }catch (NumberFormatException e){
            return resultUtil.errorResult(1,"题目数据格式错误！",
                    "redirect:/papers");
        }
        List<ExaminationPaper> papers1 = new ArrayList<>();
        if (single >= 0){
            for (ExaminationPaper paper : papers){
                if (paper.getSingleNum() == single){
                    papers1.add(paper);
                }
            }
            papers = papers1;
        }

        if (multi >= 0){
            papers1 = new ArrayList<>();
            for (ExaminationPaper paper : papers){
                if (paper.getMultiNum() == multi) {
                    papers1.add(paper);
                }
            }
            papers = papers1;
        }

        if (shortAnswer >= 0){
            papers1 = new ArrayList<>();
            for (ExaminationPaper paper : papers){
                if (paper.getShortAnswerQuestionNum() == shortAnswer) {
                    papers1.add(paper);
                }
            }
            papers = papers1;
        }
        //有效日期判断 => 给定时间 after 有效时间
        Date validDeadline = dateUtil.dateParse(deadline);
        if (validDeadline != null){
            papers1 = new ArrayList<>();
            for (ExaminationPaper paper : papers){
                //TODO  并且当前时间有效  && DateUtil.compareDate(paper.getValidDeadline())
                if (validDeadline.after(paper.getValidDeadline())){
                    papers1.add(paper);
                }
                papers = papers1;
            }
        }
        //创建时间
        Date createTime = dateUtil.dateParse(create);
        if (createTime != null){
            papers1 = new ArrayList<>();
            for (ExaminationPaper paper : papers){
                if (createTime.equals(paper.getCreateTime())){
                    papers1.add(paper);
                }
                papers = papers1;
            }
        }

        //筛选完毕
        List<ExaminationPaperDto> paperDtos = examinationPaperUtil.convert(papers,questionService);
        return resultUtil.successResult(paperDtos,"查询完成","paper");
    }

    /**
     * 试卷名称模糊查询
     * @param paperName
     * @param request
     * @return
     */
    @Override
    public ResultModel getExaminationPaperLikeName(String paperName,HttpServletRequest request) {
        if (!loginUserUtil.isAdmin(request)){
            return resultUtil.errorResult(1,"您没有权限查阅试卷！",
                    "redirect:/users/admin");
        }
        //TODO  缓存
        List<ExaminationPaper> papers= getExaminationPaperLikeName(paperName);
        return resultUtil.successResult(papers,"","");
    }

    //
    private List<ExaminationPaper> getExaminationPaperLikeName(String paperName){
        return examinationPaperRepository.getExaminationPapersLikePaperName(paperName);
    }

    @Override
    public ResultModel getExaminationPapersBeforeDeadline(Date deadline) {
        return null;
    }

    @Override
    public ResultModel getExaminationPapersByCreateTime(Date createTime) {
        return null;
    }

    @Override
    public ResultModel getExaminationPapersLessQuestionsNum(int questionsNum) {
        return null;
    }

    @Override
    public ResultModel getExaminationPapersMoreQuestionsNum(int questionsNum) {
        return null;
    }

    @Override
    public ResultModel getExaminationPapersLessSingleNum(int singleNum) {
        return null;
    }

    @Override
    public ResultModel getExaminationPapersMoreSingleNumNum(int singleNum) {
        return null;
    }

    @Override
    public ResultModel getExaminationPapersLessMultiNum(int multiNum) {
        return null;
    }

    @Override
    public ResultModel getExaminationPapersMoreMultiNum(int multiNum) {
        return null;
    }

    @Override
    public ResultModel getExaminationPapersLessShortAnswerQuestionNum(int shortAnswerQuestionNum) {
        return null;
    }

    @Override
    public ResultModel getExaminationPapersMoreShortAnswerQuestionNum(int shortAnswerQuestionNum) {
        return null;
    }
}
