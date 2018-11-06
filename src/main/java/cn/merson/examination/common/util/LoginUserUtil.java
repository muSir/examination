package cn.merson.examination.common.util;

import cn.merson.examination.common.enums.UserType;
import cn.merson.examination.configuration.BaseExaminationApplicationContext;
import cn.merson.examination.entity.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 当前用户登录处理
 */
@Component
public class LoginUserUtil extends BaseExaminationApplicationContext{
    /**
     * 获取当前登录的用户
     * @param request
     * @return
     */
    public User getCurrentLoginUser(HttpServletRequest request){
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        return loginUser;
    }

    /**
     * 登录成功后将当前用户信息保存到session中
     * @param request
     * @param user
     */
    public void saveCurrentLoginUser(HttpServletRequest request,User user){
        request.getSession().setAttribute("loginUser",user);
    }

    /**
     * 注销当前登录用户
     * @param request
     */
    public void cancellation(HttpServletRequest request){
        //清除所有属性
        request.getSession().invalidate();
        //request.getSession().setAttribute("loginUser",null);
    }

    /**
     * 判断当前登录的用户是否为管理员用户
     * @param request
     * @return 如果为管理员用户则返回true，否则返回false
     */
    public boolean isAdmin(HttpServletRequest request){
        User user = getCurrentLoginUser(request);
        if (user == null){
            return false;
        }
        String type = user.getUserType();
        return UserType.Administrator.toString().equals(type);
    }
}
