package cn.merson.examination.controller;

import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.common.enums.UserType;
import cn.merson.examination.common.util.DateUtil;
import cn.merson.examination.common.util.LoginUserUtil;
import cn.merson.examination.common.util.StringUtil;
import cn.merson.examination.entity.Examination;
import cn.merson.examination.entity.User;
import cn.merson.examination.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *  管理员和考生角色 操作管理
 */
@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private LoginUserUtil loginUserUtil;
    @Autowired
    private StringUtil stringUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *   用户登录（管理员+普通用户）
     * @param username
     * @param password
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestParam(value = "username",required = false) String username,
                        @RequestParam(value = "password",required = false)String password, HttpServletRequest request, Model model){
        ResultModel resultModel = userService.login(username,password,request);
        if (resultModel.getCode() == 1){
            //返回具体错误原因
            model.addAttribute("errorInfo",resultModel.getMessage());
        }
        return resultModel.getUrl();
    }

    /**
     * 注销 - 退出登录
     * @param model
     * @return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logout(HttpServletRequest request,Model model){
        loginUserUtil.cancellation(request);
        model.addAttribute("isLogin",false);
        //返回首页
        return "redirect:/login";
    }

    /**
     * 跳转到注册界面
     * @param request
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String regist(HttpServletRequest request){
        //判断当前是否有用户登录
        User currentUser = loginUserUtil.getCurrentLoginUser(request);
        if (currentUser != null){
            //提示先注销登录
        }
        return "ftl/register";
    }

    /**
     * 用户注册
     * @param user 前端填写的用户注册信息
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String regist(User user, HttpServletRequest request,Model model){
        ResultModel resultModel = userService.register(user,request);
        if (resultModel == null){
            //注册成功，但是登录失败
            //跳转到登录界面重新登录
            return "redirect:/login";
        }else if (resultModel.getCode() == -1){
            //注册信息错误
            if (user != null){
                model.addAttribute("phoneNum",user.getPhoneNum());
                model.addAttribute("email",user.getEmail());
                model.addAttribute("idCard",user.getIdCard());
                model.addAttribute("password",user.getPassword());
                model.addAttribute("nickName",user.getNickName());
                model.addAttribute("realName",user.getRealName());
            }
            model.addAttribute("errorInfo",resultModel.getMessage());
            return "ftl/register";
        }else if (resultModel.getCode() == 1){
            //信息正确但是注册失败
            model.addAttribute("errorInfo","注册失败");
        }
        //注册且登录都成功则跳转到对应页面
        return resultModel.getUrl();
    }

    /**
     * 返回管理员首页
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/admin/{userId}",method = RequestMethod.GET)
    public String adminFirstPage(@PathVariable(value = "userId")String userId, HttpServletRequest request,Model model){
        User loginUser = loginUserUtil.getCurrentLoginUser(request);
        //没有登录或是非管理员账号登录，则返回登录界面
        if (loginUser == null || !loginUserUtil.isAdmin(request)){
            return "redirect:/login";
        }
        //已经登录但是传过来的userId不为空并且和登录账号的ID不同，则返回用户不存在错误页面
        if (!loginUser.getUserId().toString().equals(userId)){
            model.addAttribute("errorInfo","用户不存在");
            return "common/error";
        }
        //已经登录并且传过来的ID一致
        model.addAttribute("isLogin",true);
        model.addAttribute("loginUser",loginUser);
        return "admin/first";
    }

    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public String adminFirstPage(HttpServletRequest request,Model model){
        User loginUser = loginUserUtil.getCurrentLoginUser(request);
        //没有登录或是非管理员账号登录，则返回登录界面
        if (loginUser == null || !loginUserUtil.isAdmin(request)){
            return "redirect:/login";
        }
        //已经登录并且传过来的ID一致
        model.addAttribute("isLogin",true);
        model.addAttribute("loginUser",loginUser);
        return "admin/first";
    }

    /**
     * 返回首页
     * @return
     */
    @RequestMapping(value = "/dream/{userId}",method = RequestMethod.GET)
    public String firstPage(@PathVariable(value = "userId")String userId,  HttpServletRequest request,Model model){
        //判断当前登录用户的ID是否一致
        User loginUser = loginUserUtil.getCurrentLoginUser(request);
        if (loginUser == null){
            return "ftl/first";
        }
        if (!loginUser.getUserId().toString().equals(userId)){
            model.addAttribute("errorInfo","用户不存在");
            return "common/error";
        }
        //用户已经登录
        model.addAttribute("isLogin",true);
        model.addAttribute("loginUser",loginUser);
        return "ftl/first";
    }
    @RequestMapping(value = {"","/","/dream","/first"},method = RequestMethod.GET)
    public String firstPage(HttpServletRequest request,Model model){
        User loginUser = loginUserUtil.getCurrentLoginUser(request);
        if (loginUser != null){
            model.addAttribute("isLogin",true);
            model.addAttribute("loginUser",loginUser);
        }
        return "ftl/first";
    }
    /**
     * 返回登录页面
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String goToLogin(){
        return "ftl/login";
    }

    /**
     * 获取系统当前时间
     * @return
     */
    @RequestMapping(value = "time",method = RequestMethod.GET)
    @ResponseBody
    public String systemCurrentTime(){
        return dateUtil.getCurrentTime();
    }

}
