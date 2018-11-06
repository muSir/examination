package cn.merson.examination.controller;

import cn.merson.examination.common.dto.ExaminationPaperDto;
import cn.merson.examination.common.dto.QuestionDto;
import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.common.util.DateUtil;
import cn.merson.examination.common.util.LoginUserUtil;
import cn.merson.examination.common.util.StringUtil;
import cn.merson.examination.entity.Examination;
import cn.merson.examination.entity.User;
import cn.merson.examination.service.IExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 考试模块web
 */
@Controller
@RequestMapping("/examinations")
public class ExaminationController {

    @Autowired
    private IExaminationService examinationService;

    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private StringUtil stringUtil;
    @Autowired
    private LoginUserUtil loginUserUtil;

    /**
     * 点击开始答题 => 把有效期范围内并且还未考过的试卷显示出来，由用户自定义选择
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/{studentId}/papers",method = RequestMethod.GET)
    public String examinations(@PathVariable("studentId")String studentId,HttpServletRequest request,Model model){
        ResultModel resultModel = examinationService.beforeExamination(studentId);
        //该考生可用试卷
        if (resultModel.getCode() == 100){
            model.addAttribute("userId",studentId);
            model.addAttribute("validPapers",(List<ExaminationPaperDto>)resultModel.getData());
        }
        return resultModel.getUrl();
    }

    /**
     * 开始考试  返回第一个题
     * @param studentId
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/{studentId}/{paperId}/start",method = RequestMethod.GET)
    public String startExamination(@PathVariable("studentId")String studentId,@PathVariable("paperId")String paperId,
                                  HttpServletRequest request, Model model){
        String paperName = request.getParameter("paperName");
        ResultModel resultModel = examinationService.startExamination(studentId,paperId,request);
        //记录开始时间
        if (resultModel.getCode() == 100){
            model.addAttribute("paperName",paperName);
            model.addAttribute("startTime", new Date());
            model.addAttribute("userId",studentId);
            model.addAttribute("paperId",paperId);
            List<QuestionDto> dtos = (List<QuestionDto>)resultModel.getData();
            if (dtos != null){
                model.addAttribute("question",dtos.get(0));
                if (!"isLast".equals(dtos.get(0).getIsLast())){
                    model.addAttribute("nid",dtos.get(1).getQuestionId());
                }
            }
        }
        return resultModel.getUrl();
    }

    /**
     * 考试 下一道题 计算当前题目的得分，并保存在Redis中
     * @param studentId
     * @param paperId
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/{studentId}/{paperId}/next",method = RequestMethod.GET)
    public String nextQuestion(@PathVariable("studentId")String studentId, @PathVariable("paperId")String paperId,
                                HttpServletRequest request, Model model){
        String paperName = request.getParameter("paperName");
        ResultModel resultModel = examinationService.nextQuestion(studentId,paperId,request);
        model.addAttribute("paperName",paperName);
        model.addAttribute("userId",studentId);
        model.addAttribute("paperId",paperId);
        if (resultModel.getCode() == 100){
            if ("examination".equals(resultModel.getUrl())){
                model.addAttribute("question",(QuestionDto)resultModel.getData());
                if (!stringUtil.isNullOrEmpty(resultModel.getMessage())){
                    model.addAttribute("nid",resultModel.getMessage());
                }
            } else if ("student".equals(resultModel.getUrl())){
                User loginUser = loginUserUtil.getCurrentLoginUser(request);
                model.addAttribute("idCard",loginUser.getIdCard());
                model.addAttribute("user",loginUser);
                model.addAttribute("currentTime", dateUtil.getCurrentTime());
                model.addAttribute("record",(List<Examination>)resultModel.getData());
            }
        }
        return resultModel.getUrl();
    }

    //交卷/提前交卷
    @RequestMapping(value = "/{studentId}/{paperId}/end",method = RequestMethod.GET)
    public String handExamination(@PathVariable("studentId")String studentId, @PathVariable("paperId")String paperId,
                                  HttpServletRequest request){
        ResultModel resultModel = examinationService.finishExamination(studentId,paperId,request);

        return resultModel.getUrl();
    }

}
