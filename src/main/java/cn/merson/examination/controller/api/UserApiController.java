package cn.merson.examination.controller.api;

import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: created by Merson
 * Created on 2018/1/4 0004 12:30
 */
@RestController
public class UserApiController {

    @Autowired
    private IUserService userService;

    /**
     * 登录api
     * @return
     */
    @RequestMapping(value = "/api/v1/login",method = RequestMethod.POST)
    @ResponseBody
    public ResultModel loginApi(@RequestParam(value = "username",required = false) String username,
                                @RequestParam(value = "password",required = false)String password, HttpServletRequest request, Model model){
        return userService.login(username,password,request);
    }

}
