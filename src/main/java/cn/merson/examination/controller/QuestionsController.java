package cn.merson.examination.controller;

import cn.merson.examination.common.dto.Page;
import cn.merson.examination.common.dto.QuestionDto;
import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.common.util.FileUtil;
import cn.merson.examination.common.util.LoginUserUtil;
import cn.merson.examination.common.util.ResultUtil;
import cn.merson.examination.common.util.StringUtil;
import cn.merson.examination.entity.Question;
import cn.merson.examination.entity.User;
import cn.merson.examination.service.IQuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Level;

/**
 * 题库模块
 */
@Controller
@RequestMapping("/questions")
public class QuestionsController {

    @Autowired
    private IQuestionService questionService;
    @Autowired
    private LoginUserUtil loginUserUtil;
    @Autowired
    private StringUtil stringUtil;
    @Autowired
    private FileUtil fileUtil;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取题库全部试题，不分页
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String getAllQuestions(HttpServletRequest request, Model model){
        ResultModel resultModel = questionService.getAllQuestions(request);
        List<QuestionDto> dtos = (List<QuestionDto>)resultModel.getData();
        model.addAttribute("questions",dtos);
        model.addAttribute("pageSize",dtos==null?0:dtos.size());
        model.addAttribute("totalPages",resultModel.getMessage());
        return resultModel.getUrl();
    }

    /**
     * 分页获取题库试题：pageNo:默认第一页（从1开始），pageSize:每页10条数据，totalCount:总数为数据库记录总数
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/list/{pageNo}/pages",method = RequestMethod.GET)
    public String getAllQuestionsByPage(@PathVariable(value = "pageNo")int pageNo, HttpServletRequest request, Model model){
        User loginUser = loginUserUtil.getCurrentLoginUser(request);
        if (loginUser != null){
            model.addAttribute("isLogin",true);
            model.addAttribute("loginUser",loginUser);
        }
        ResultModel resultModel = questionService.getAllQuestionsByPage(pageNo,request);
        if (resultModel.getCode() == 100){
            List<QuestionDto> dtos = (List<QuestionDto>)resultModel.getData();
            model.addAttribute("questions",dtos);
            model.addAttribute("page",resultModel.getPage());
        }
        return resultModel.getUrl();
    }


    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public String conditionQuestions(@RequestParam(value = "questionName",required = false)String questionName,
                                                        @RequestParam(value = "questionAnswer",required = false)String questionAnswer,
                                                         @RequestParam(value = "questionValue",required = false)String questionValue,
                                                         @RequestParam(value = "questionType",required = false)String questionType,
                                                         @RequestParam(value = "questionRefence",required = false)String questionRefence,
                                                         HttpServletRequest request, Model model){

        ResultModel resultModel = questionService.queryByConditions(questionName,questionAnswer,questionValue,questionType,questionRefence,request);
        //条件返回
        model.addAttribute("questionName",questionName);
        model.addAttribute("questionAnswer",questionAnswer);
        model.addAttribute("questionValue",questionValue);
        model.addAttribute("questionType",questionType);
        model.addAttribute("questionRefence",questionRefence);
        if (resultModel.getCode() == 100){
            List<QuestionDto> dtos = (List<QuestionDto>)resultModel.getData();
            model.addAttribute("questions",dtos);
            model.addAttribute("pageSize",dtos.size());
        }

        return resultModel.getUrl();
    }

    /**
     * 批量删除
     * @param idStr
     * @param request
     * @return
     */
    @RequestMapping(value = "/question",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultModel bulkDelete(@RequestParam("ids")String idStr, HttpServletRequest request){
        //String idStr = request.getParameter("ids");
        logger.info("DELETE /questions/question Start,idStr=" + idStr);
        String[] ids = idStr.split(",");
        return questionService.questionsDelete(Arrays.asList(ids),request);
    }

    /**
     * 单条删除
     * @param questionId
     * @param request
     * @return
     */
    @RequestMapping(value = "/question/{questionId}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultModel singleDelete(@PathVariable("questionId") String questionId,HttpServletRequest request){
        logger.info("DELETE /questions/question/" + questionId + ",Start.");
        List<String> ids = new ArrayList<>();
        ids.add(questionId);
        return questionService.questionsDelete(ids,request);
    }

    /**
     * 新增试题  跳转到新增页面
     * @return
     */
    @RequestMapping(value = "/question/new",method = RequestMethod.GET)
    public String add(@RequestParam(value = "fileUploadResult",required = false)String fileUploadResult,Model model){
        if (!stringUtil.isNullOrEmpty(fileUploadResult)){
            model.addAttribute("fileUploadResult",fileUploadResult);
        }
        return "admin/common/question";
    }

    /**
     *  填写信息新增
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/question/new",method =RequestMethod.POST)
    public String addQuestion(HttpServletRequest request,Model model){
        logger.info("POST /questions/question/new : start.");
        ResultModel resultModel = questionService.questionNew(request);
        logger.info("POST /questions/question/new : end.");
        return resultModel.getUrl();
    }

    /**
     * 下拉框查询
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/list/{condition}/{pageNo}/pages",method = RequestMethod.GET)
    public String searchBySelect(@PathVariable("condition") String condition,@PathVariable("pageNo") Integer pageNo,
                                 HttpServletRequest request,Model model){
        User loginUser = loginUserUtil.getCurrentLoginUser(request);
        if (loginUser != null){
            model.addAttribute("isLogin",true);
            model.addAttribute("loginUser",loginUser);
        }
        ResultModel resultModel = questionService.queryByConditions(condition,pageNo,request);
        if (resultModel.getCode() == 100){
            List<QuestionDto> dtos = (List<QuestionDto>)resultModel.getData();
            model.addAttribute("questions",dtos);
            model.addAttribute("page",resultModel.getPage());
            String dropdown = "单选题";
            if ("MultiSelect".equals(condition)){
                dropdown = "多选题";
            }else if ("ShortAnswer".equals(condition)){
                dropdown = "问答题";
            }
            model.addAttribute("dropdown",dropdown);
        }
        return resultModel.getUrl();
    }

    /**
     * 跳转到题目详情页面
     * @param questionId
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/{questionId}",method = RequestMethod.GET)
    public String editPage(@PathVariable("questionId") String questionId,
                               HttpServletRequest request,Model model){
        ResultModel resultModel = questionService.getQuestionById(questionId,request);
        model.addAttribute("question",(QuestionDto)resultModel.getData());
        return "questiondetail";
    }

    /**
     * 试题编辑完成并提交
     * @param questionId
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/{questionId}",method = RequestMethod.POST)
    public String editSuccess(@PathVariable("questionId") String questionId, @RequestParam("title")String title,@RequestParam("answer")String answer,
                              @RequestParam("value")String value,@RequestParam("type")String type, HttpServletRequest request,Model model){
        ResultModel resultModel = questionService.questionSave(questionId,title,answer,value,type,request);
        if (resultModel.getCode() == 1 && resultModel.getData() != null){
            model.addAttribute("question",(QuestionDto)resultModel.getData());
        }
        return resultModel.getUrl();
    }


    /**
     *   上传文件 保存到题库
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/question/new/templates",method = RequestMethod.POST)
    public ModelAndView fileUpload(@RequestParam(value = "uploadfile")MultipartFile file, HttpServletRequest request, Model model){
        ResultModel resultModel = questionService.questionsSaveFromFile(file,request);
        logger.info(resultModel.getUrl() + "   " + resultModel.getMessage());

        if (resultModel.getCode() == 1){
            Map<String,String> map = new HashMap<>();
            map.put("fileUploadResult",resultModel.getMessage());
            return new ModelAndView(new RedirectView("/questions/question/new"),map);
        }
        String url = "/questions/list/" + ((Page) resultModel.getData()).getPageNo() + "/pages";
        return new ModelAndView(new RedirectView(url));
    }

    //下载模板
    @RequestMapping(value = "/question/new/templates",method = RequestMethod.GET)
    /*@ResponseBody*/
    public String templateDownload(@RequestParam("template")String[] templates, HttpServletRequest request, HttpServletResponse response){
        fileUtil.downloadQuestionsTemplate(templates,request,response);
        return null;
    }

}
