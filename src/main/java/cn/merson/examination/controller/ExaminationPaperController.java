package cn.merson.examination.controller;

import cn.merson.examination.common.dto.ExaminationPaperDto;
import cn.merson.examination.common.dto.QuestionDto;
import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.entity.Question;
import cn.merson.examination.service.IExaminationPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 试卷模块web
 */
@Controller
@RequestMapping("/papers")
public class ExaminationPaperController {

    @Autowired
    private IExaminationPaperService examinationPaperService;

    /**
     * 查询全部试卷
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String getAllExaminationPaper(HttpServletRequest request, Model model){
        ResultModel resultModel = examinationPaperService.getAllExaminationPapers(request);
        List<ExaminationPaperDto> dtos = (List<ExaminationPaperDto>)resultModel.getData();
        if (dtos != null){
            model.addAttribute("totalCount",dtos.size());
            model.addAttribute("papers",dtos);
        }
        return resultModel.getUrl();
    }

    /**
     * 跳转到创建试卷页面
     * @return
     */
    @RequestMapping(value = "/newpaper",method = RequestMethod.GET)
    public String createPage(){
        return "newpaper";
    }

    /**
     * 根据填写的信息创建试卷
     * @param request
     * @return
     */
    @RequestMapping(value = "/newpaper",method = RequestMethod.POST)
    public String createPaperSuccess(@RequestParam("paperName") String paperName, @RequestParam("singleNum") String singleNum,
                                     @RequestParam("multiNum") String multiNum,@RequestParam("shortAnswerQuestionNum") String shortAnswerQuestionNum,
                                     @RequestParam("validDeadline") String validDeadline,HttpServletRequest request,Model model){
        //时间字符串转化为Date类型
        ResultModel resultModel =
                examinationPaperService.createExaminationPaper(paperName,singleNum,multiNum,shortAnswerQuestionNum,validDeadline,request);
        if (resultModel.getCode() == 1){
            model.addAttribute("errorInfo",resultModel.getMessage());
        }
        return resultModel.getUrl();
    }

    /**
     *  在试卷列表中删除试卷
     * @param paperNo
     * @param request
     * @return
     */
    @RequestMapping(value = "/{no}/delete",method = RequestMethod.GET)
    public String paperDelete(@PathVariable("no") String paperNo, HttpServletRequest request){
        ResultModel resultModel = examinationPaperService.deleteExaminationPaper(paperNo,request);
        return resultModel.getUrl();
    }

    /**
     * 查看试卷详情 => 显示所有题目
     * @param paperNo
     * @param request
     * @return
     */
    @RequestMapping(value = "/{no}/detail",method = RequestMethod.GET)
    public String paperDetail(@PathVariable("no") String paperNo, HttpServletRequest request,Model model){
        ResultModel resultModel = examinationPaperService.getExaminationPaperQuestions(paperNo);
        if(resultModel.getCode() == 1){
            model.addAttribute("errorInfo",resultModel.getMessage());
        }
        if (resultModel.getCode() == 100){
            model.addAttribute("paperName",resultModel.getMessage());
            model.addAttribute("questions",(List<QuestionDto>)resultModel.getData());
        }
        return resultModel.getUrl();
    }

    /**
     * 一键查询
     * @param paperName
     * @param singleNum
     * @param multiNum
     * @param shortNum
     * @param deadline
     * @param createTime
     * @param request
     * @return
     */
    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public String paperQuery(@RequestParam(value = "paperName",required = false)String paperName,
                             @RequestParam(value = "singleNum",required = false)String singleNum,
                             @RequestParam(value = "multiNum",required = false)String multiNum,
                             @RequestParam(value = "shortNum",required = false)String shortNum,
                             @RequestParam(value = "deadline",required = false)String deadline,
                             @RequestParam(value = "createTime",required = false)String createTime,
                             HttpServletRequest request,Model model){
        ResultModel resultModel = examinationPaperService.queryExaminationPapers(paperName,singleNum,multiNum,shortNum,
                deadline,createTime,request);
        //条件返回
        model.addAttribute("queryName",paperName);
        model.addAttribute("querySingle",singleNum);
        model.addAttribute("queryMulti",multiNum);
        model.addAttribute("queryShort",shortNum);
        model.addAttribute("queryDeadline",deadline);
        model.addAttribute("queryCreateTime",deadline);
        List<ExaminationPaperDto> dtos = (List<ExaminationPaperDto>)resultModel.getData();
        model.addAttribute("papers",dtos);
        if (dtos != null){
            model.addAttribute("totalCount",dtos.size());
        }
        return resultModel.getUrl();
    }

}
