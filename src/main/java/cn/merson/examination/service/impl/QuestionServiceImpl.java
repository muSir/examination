package cn.merson.examination.service.impl;

import cn.merson.examination.common.dto.Page;
import cn.merson.examination.common.dto.QuestionDto;
import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.common.enums.IRedisKey;
import cn.merson.examination.common.enums.QuestionType;
import cn.merson.examination.common.util.*;
import cn.merson.examination.entity.Question;
import cn.merson.examination.entity.User;
import cn.merson.examination.repository.IQuestionRepository;
import cn.merson.examination.service.IQuestionService;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 题库模块业务逻辑实现类
 */
@Service
public class QuestionServiceImpl implements IQuestionService{

    @Autowired
    private IQuestionRepository questionRepository;

    @Autowired
    private ResultUtil resultUtil;
    @Autowired
    private QuestionUtil questionUtil;
    @Autowired
    private LoginUserUtil loginUserUtil;
    @Autowired
    private StringUtil stringUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private FileUtil fileUtil;

    @Override
    public ResultModel getQuestionById(String id,HttpServletRequest request) {
        Long questionId = null;
        try {
            questionId = Long.parseLong(id);
        }catch (NumberFormatException ex){
            return resultUtil.errorResult(1,"试题主键错误");
        }
        Question question = questionRepository.findOne(questionId);
        List<Question> questions = new ArrayList<>();
        questions.add(question);
        List<QuestionDto> dtos = questionUtil.convert(questions);
        QuestionDto dto = null;
        if (dtos != null){
            dto = dtos.get(0);
        }
        return resultUtil.successResult(dto,"");
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Transactional
    @Override
    public ResultModel questionSave(String questionId, String title, String answer, String value, String type,
                                    HttpServletRequest request) {
        if (!loginUserUtil.isAdmin(request)){
            return resultUtil.errorResult(1,"","login");
        }
        if (stringUtil.isNullOrEmpty(questionId) || stringUtil.isNullOrEmpty(title)
                || stringUtil.isNullOrEmpty(answer) || stringUtil.isNullOrEmpty(value) || stringUtil.isNullOrEmpty(type)){
            return resultUtil.errorResult(1,"请将所有信息补充完整！","questions");
        }
        Long id = Long.parseLong(questionId);
        Question question = new Question(id,title,type,0,answer);
        Integer value1 = null;
        try {
            value1 = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return resultUtil.errorResult(1,"分值必须是大于0并且小于100的整数",
                    "questiondetail",question);
        }

        ResultModel model = getQuestionById(questionId,request);
        Question old = (Question) model.getData();
        if (model.getCode() == 1 || old == null){
            return resultUtil.errorResult(1,"找不到要修改的试题！","questiondetail",old);
        }
        old.setTitle(title);
        old.setType(type);
        old.setValue(value1);
        old.setAnswer(answer);
        old.setLastUpdateTime(new Date());

        Question back = questionRepository.save(old);
        if (back == null){
            return resultUtil.errorResult(1,"修改试题失败！","questiondetail",old);
        }
        //更新到缓存中
        redisUtil.updateQuestionsToList(IRedisKey.QUESTIONS,back);
        return resultUtil.successResult(back,"修改试题成功！","redirect:/questions/list");
    }

    /**
     * 新增试题
     * @param request
     * @return
     */
    @Transactional
    @Override
    public ResultModel questionNew(HttpServletRequest request) {
        if (!loginUserUtil.isAdmin(request)){
            return resultUtil.errorResult(1,"没有权限添加题库试题","redirect:/questions/question/new");
        }
        String questionType = request.getParameter("questionType");
        String type = "";
        if ("single".equals(questionType)){
            type = QuestionType.SingleSelect.toString();
        }else if ("multi".equals(questionType)){
            type = QuestionType.MultiSelect.toString();
        }else if ("shortAnswer".equals(questionType)){
            type = QuestionType.ShortAnswer.toString();
        }
        String questionTitle = request.getParameter("questionTitle");
        String questionOptions = request.getParameter("questionOptions");
        String questionAnswer = request.getParameter("questionAnswer");
        String questionKeywords = request.getParameter("questionKeywords");
        String questionReference = request.getParameter("questionReference");
        String questionValue = request.getParameter("questionValue");
        int value = 0;
       try {
           value = Integer.parseInt(questionValue);
       }catch (NumberFormatException e){
           logger.error("QuestionService,questionNew:题目分值不能为0-100之间的整数之外的其他类型值");
           return resultUtil.errorResult(1,"题目分值不能为0-100之间的整数之外的其他类型值","redirect:/questions/question/new");
       }

       Question question = questionUtil.newQuestionInstance(type,questionTitle,questionOptions,questionAnswer,value,questionKeywords,questionReference);
        if (question == null){
            logger.error("QuestionService,questionNew:题目输入错误");
            return resultUtil.errorResult(1,"题目输入错误","redirect:/questions/question/new");
        }
        //保存到数据库
        Question back = questionRepository.save(question);
        if (back == null){
            logger.error("QuestionService,questionNew:保存失败");
            return resultUtil.errorResult(1,"题目保存失败，请重新提交","redirect:/questions/question/new");
        }
        //返回到题目列表最后一页
        Page page = new Page();
        page.setPageSize(10);
        int count = questionRepository.getTotalCount();
        page.setCount(count);
        page.setPageNo(page.getTotalPage());
        return resultUtil.successResult(page,"保存题目成功","redirect:/questions/list/"+ page.getPageNo() +"/pages");
    }

    /**
     * 删除指定的试题
     * @param id
     * @param request
     * @return
     */
    @Transactional
    public ResultModel questionDelete(String id,HttpServletRequest request) {
        if (!loginUserUtil.isAdmin(request)){
            return resultUtil.errorResult(1,"您没有权限删除题库中的题目！",
                    "redirect:/users/admin");
        }
        Long questionId = Long.parseLong(id);
        //先从数据库删除
        try {
            questionRepository.delete(questionId);
        } catch (EmptyResultDataAccessException e) {
            return resultUtil.errorResult(1,"删除失败，找不到对应的试题！","redirect:/questions/list");
        }catch (Exception ex){
            return resultUtil.errorResult(1,"删除失败","redirect:/questions/list");
        }
        //删除缓存中的数据
        redisUtil.deleteQuestionsFromList(IRedisKey.QUESTIONS,questionId);
        return resultUtil.successResult(questionId,"删除成功！","redirect:/questions/list");
    }

    /**
     * 删除
     * @param idsList
     * @param request
     * @return
     */
    @Transactional
    @Override
    public ResultModel questionsDelete(List<String> idsList, HttpServletRequest request) {
        logger.info("QuestionService questionsDelete start.");
        if (!loginUserUtil.isAdmin(request)){
            return resultUtil.errorResult(1,"删除题库权限不足");
        }
        if (idsList == null || idsList.size() == 0){
            return resultUtil.errorResult(1,"请选择需要删除的题目");
        }
        for (String idStr : idsList){
            try {
                Long id = Long.parseLong(idStr);
                questionRepository.delete(id);
            } catch (NumberFormatException e) {
                logger.error("QuestionService questionsDelete : NumberFormatException.question的ID不能被转换成Long类型错误," + idStr);
                return resultUtil.errorResult(1,"数据错误");
            }
        }
        logger.info("QuestionService questionsDelete end.");
        return resultUtil.successResult(null,"删除成功");
    }


    /**
     * 从缓存或者数据库中获取题库全部题目 并按value排序
     * @return
     */
    private List<Question> getAllQuestions(){
        //TODO 考虑题库可能没有试题的情况
        //先从redis缓存中获取题库
        List<Question> questions = redisUtil.getList(IRedisKey.QUESTIONS);
        //如果没有则从数据库中获取并加载到缓存中
        if (questions == null){
            questions = (List<Question>) questionRepository.findAll();
            if (questions == null){
                return null;
            }
            if (questions != null && questions.size() > 0){
                redisUtil.cacheList(IRedisKey.QUESTIONS,questions);
            }
        }
        if (questions != null && questions.size() > 0){
            int size = questions.size();
            //排序
            questions.sort(new Comparator<Question>() {
                @Override
                public int compare(Question q1, Question q2) {
                    return q1.getValue().compareTo(q2.getValue());
                }
            });
        }
        return questions;
    }

     /**
     * 供外部调用
     * @return
     */
     @Override
    public List<Question> getQuestions(){
        return getAllQuestions();
    }

    /**
     * 获取题库全部题目，不分页
     * @return
     */
    @Override
    public ResultModel getAllQuestions(HttpServletRequest request) {
        if (!loginUserUtil.isAdmin(request)){
            return resultUtil.errorResult(1,"您没有权限访问题库！","redirect:/users/admin");
        }
        int totalCount = questionRepository.getTotalCount();
        int totalPages = totalCount / 10 +1;
        //先从缓存中读数据
        //List<Question> questions = getAllQuestions();
        List<Question> questions = questionRepository.getAllQuestionsByPage(1,10);

        List<QuestionDto> dtos = questionUtil.convert(questions);
        return resultUtil.successResult(dtos,totalPages+"","admin/questions");
    }

    /**
     * 分页查询  默认查询第一页，每页10条数据
     * @param pageNo 第N页
     * @param request
     * @return
     */
    @Override
    public ResultModel getAllQuestionsByPage(int pageNo, HttpServletRequest request) {
        User loginUser = loginUserUtil.getCurrentLoginUser(request);
        //需要登录才能获取数据
        if (loginUser == null){
            return resultUtil.errorResult(1,"登录后才能获取题库数据","ftl/login");
        }
        int count = questionRepository.getTotalCount();
        if (count == 0){
            return resultUtil.successResult(null,"数据库暂无题目数据","admin/questions");
        }
        //用request生成page对象  page.getTotalCount() >totalCount
        Page page = new Page(request);
        if (pageNo <= 0 || page.getPageSize() <= 0){
            return resultUtil.errorResult(1,"分页查询页数错误","ftl/login");
        }
        page.setPageNo(pageNo);
        //数据库总条数

        page.setCount(count);

        //TODO  如果数据库的记录小于请求的数据  则需要重新计算  Page中的setPage就不对  暂时不做
        //暂时不从缓存中拿数据
        List<Question> questions = getPageQuestions(page,0);
        if (questions != null){
            page.setResultCount(questions.size());
        }else {
            page.setResultCount(0);
        }
        return resultUtil.successResult(questionUtil.convert(questions),page,"分页查询题库成功","admin/questions");
    }

    /**
     * 查询分值相同的题目
     * @param value 分值数
     * @param request
     * @return
     */
    @Override
    public ResultModel getQuestionsByValue(String value, HttpServletRequest request) {
        if (!loginUserUtil.isAdmin(request)){
            return resultUtil.errorResult(1,"您没有权限访问题库！","login");
        }
        Integer score = null;
        try {
            score = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return resultUtil.errorResult(1,"题目分值必须为大于0小于100的整数！","questions");
        }
        List<Question> questions = redisUtil.getList(IRedisKey.QUESTIONS);
        if (questions == null){
            //不用添加到缓存
            questions = questionRepository.getQuestionsByValue(score);
        }
        if (questions == null){
            return resultUtil.errorResult(1,"题库中没有分值为"+ value + "的试题！","questions");
        }
        return resultUtil.successResult(questions,"","questions");
    }

    /**
     * 一键查询
     * @param questionName
     * @param questionAnswer
     * @param questionValue
     * @param questionType
     * @param questionRefence
     * @param request
     * @return
     */
    @Override
    public ResultModel queryByConditions(String questionName, String questionAnswer, String questionValue, String questionType,
                                         String questionRefence, HttpServletRequest request) {
        if (!loginUserUtil.isAdmin(request)){
            return resultUtil.errorResult(1,"您没有权限访问题库！","redirect:/users/admin");
        }
        if (stringUtil.isNullOrEmpty(questionValue)){
            questionValue = "-1";
        }
        Integer value = null;
        try {
            value = Integer.parseInt(questionValue);
        }catch (NumberFormatException ex){
            return resultUtil.errorResult(1,"题目分值必须为大于0小于100的整数！",
                    "questions");
        }
        List<QuestionDto> dtos = null;
        if (stringUtil.isNullOrEmpty(questionName)){
            questionName = "";
        }
        List<Question> questions = getQuestionsLikeName(questionName);

        if (questions == null){
            return resultUtil.errorResult(1,"题目分值必须为大于0小于100的整数！",
                    "questions");
        }
        //筛选答案条件
        if (stringUtil.isNullOrEmpty(questionAnswer)){
            questionAnswer = "";
        }
        List<Question> questions1 = getQuestionsLikeAnswer(questionAnswer);
        if (questions1 == null){
            return resultUtil.errorResult(1,"题目分值必须为大于0小于100的整数！",
                    "questions");
        }
        List<Question> questions2 = new ArrayList<>();
        for (Question q : questions){
            if (questions1.contains(q)){
                questions2.add(q);
            }
        }
        if (questions2.size() == 0){
            return resultUtil.errorResult(1,"题目分值必须为大于0小于100的整数！",
                    "questions");
        }

        if ((value <=0 && value != -1) || value > 100){
            return resultUtil.errorResult(1,"题目分值必须为大于0小于100的整数！",
                    "questions");
        }

        questions.clear();
        questions1.clear();
        if (value == -1){
            //查询全部
            questions = questions2;
        }else {
            //筛选
            for (Question q : questions2) {
                if (q.getValue() == value) {
                    questions.add(q);
                }
            }
        }

        if (questions.size() == 0){
            return resultUtil.errorResult(1,"题目分值必须为大于0小于100的整数！",
                    "questions");
        }
        //筛选reference
        if (stringUtil.isNullOrEmpty(questionRefence)){
            questions1 = getAllQuestions();
        }
        else {
            questions1 = getQuestionsLikeReference(questionRefence);
        }

        if (questions1 == null || questions1.size() == 0){
            return resultUtil.errorResult(1,"",
                    "questions");
        }
        List<Question> questions3 = new ArrayList<>();
        for (Question q1 :  questions1){
            if (questions.contains(q1)){
                questions3.add(q1);
            }
        }
        if (questions3.size() == 0){
            return resultUtil.errorResult(1,"",
                    "questions");
        }
        questions.clear();
        questions1.clear();
        questions2.clear();
        //筛选类型
        for (Question question : questions3){
            if (question.getType().equals(questionType)){
                questions.add(question);
            }
        }
         if (questions.size() == 0){
             return resultUtil.errorResult(1,"题目分值必须为大于0小于100的整数！",
                     "questions");
         }
        dtos = questionUtil.convert(questions);

        return resultUtil.successResult(dtos,"","questions");
    }

    /**
     * 查询
     * @param condition
     * @param  pageNo 当前页码
     * @param request
     * @return
     */
    @Override
    public ResultModel queryByConditions(String condition,int pageNo, HttpServletRequest request) {
        logger.info("QuestionService queryByConditions start:condition => " + condition);
        if (stringUtil.isNullOrEmpty(condition)){
            logger.info("QuestionService queryByConditions start:condition is empty.");
            return resultUtil.errorResult(1,"请输入查询条件");
        }
        Page page = new Page(request);
        if (pageNo <= 0 || page.getPageSize() <= 0){
            return resultUtil.errorResult(1,"分页查询页数错误","ftl/login");
        }
        page.setPageNo(pageNo);


        List<Question> questions =null;
        //按类型分页查询全部的题目
        boolean isType = questionUtil.SINGLE_SELECT.equals(condition) || questionUtil.MULTI_SELECT.equals(condition)
                || questionUtil.SHORT_ANSWER.equals(condition);
        if (isType){
            questions = questionRepository.getQuestionsByTypeDividedPage(condition,page.getStartIndex(),page.getTotalCount());
            if (questions != null){
                /*page.setCount(questions.size());*/
                page.setResultCount(questions.size());
            }
            else {
                page.setResultCount(0);
            }
            return resultUtil.successResult(questionUtil.convert(questions),page,"查询成功","admin/questions");
        }

        return null;
    }

    /**
     * 批量导入题库
     * @param multipartFile 从浏览器上传的文件
     * @param request
     * @return
     */
    @Override
    public ResultModel questionsSaveFromFile(MultipartFile multipartFile, HttpServletRequest request) {
        logger.info("QuestionService questionsSaveFromFile：题库文件上传成功，开始处理...");
        try {
            //把Excel的内容转换为Questions
            List<Question> questions = fileUtil.handle(multipartFile);
            Iterable<Question> savaBack = questionRepository.save(questions);
            if (savaBack == null){
                logger.error("QuestionService questionsSaveFromFile：保存到数据库失败 => saveBack == null.");
                return resultUtil.errorResult(1,"保存题目失败","redirect:/questions/question/new?fileUploadResult=保存题目失败");
            }
            //跳转到题库列表最后一页
            Page page = new Page();
            page.setPageSize(10);
            int count = questionRepository.getTotalCount();
            page.setCount(count);
            page.setPageNo(page.getTotalPage());
            return resultUtil.successResult(page,"保存题目成功","redirect:/questions/list/"+ page.getPageNo() +"/pages");
            //return ResultUtil.successResult(savaBack,"保存题目成功","redirect:/questions/question/new");
        } catch (Exception e) {
            String errorInfo = "文件内容错误：" + e.getMessage();
            logger.error("QuestionService questionsSaveFromFile => " + errorInfo);
            return resultUtil.errorResult(1,errorInfo,"redirect:/questions/question/new?fileUploadResult="+errorInfo);
        }
    }


    /**
     * 试题名称模糊查询
     * @param questionName
     * @return
     */
    private List<Question> getQuestionsLikeName(String questionName){
        return questionRepository.getQuestionsLikeName(questionName);
    }

    /**
     * 试题答案模糊查询
     * @param questionAnswer
     * @return
     */
    private List<Question> getQuestionsLikeAnswer(String questionAnswer){
        return questionRepository.getQuestionsLikeAnswer(questionAnswer);
    }

    /**
     *
     * @return
     */
    private List<Question> getQuestionsLikeReference(String reference){
        return questionRepository.getQuestionsLikeReference(reference);
    }


    /**
     * 查询题库指定页码的数据
     * @return
     */
    private List<Question> getPageQuestions(Page page,int totalCount){
        if (totalCount == -1){
            //查询单页数据
            return questionRepository.getAllQuestionsByPage(page.getStartIndex(),page.getPageSize());
        }
        //查询指定数量的数据
        return questionRepository.getAllQuestionsByPage(page.getStartIndex(),page.getTotalCount());
    }

    /**
     * 查询题库最后一页的数据
     * @return
     */
    private List<Question> getQuestionsInLastPage(Page page){
        return null;
    }

}
