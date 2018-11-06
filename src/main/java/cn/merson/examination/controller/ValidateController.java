package cn.merson.examination.controller;

import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.service.IVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 用于验证的web
 * @Author: created by Merson
 * Created on 2017/12/19 0019 20:30
 */
@RestController
@RequestMapping(value = "/verifications")
public class ValidateController {

    @Autowired
    private IVerificationService verificationService;

    /**
     * 电话号码验证
     * @param request
     * @return
     */
    @RequestMapping(value = "/phone",method = RequestMethod.GET)
    public ResultModel phoneNumValidate(HttpServletRequest request){
        return verificationService.validateCellPhone(request.getParameter("phoneNum"));
    }

    /**
     * 邮箱验证
     * @param request
     * @return
     */
    @RequestMapping(value = "/email",method = RequestMethod.GET)
    public ResultModel emailValidate(HttpServletRequest request){
        return verificationService.validateEmail(request.getParameter("email"));
    }

    /**
     * 身份证验证
     * @param request
     * @return
     */
    @RequestMapping(value = "/idCard",method = RequestMethod.GET)
    public ResultModel idCardValidate(HttpServletRequest request){
        return verificationService.validateIdCard(request.getParameter("idCard"),"");
    }
}
