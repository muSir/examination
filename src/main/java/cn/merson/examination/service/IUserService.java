package cn.merson.examination.service;

import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理逻辑
 */
public interface IUserService {

    /**
     * 用户登录
     * @param username
     * @param password
     * @param request
     * @return
     */
    ResultModel login(String username,String password,HttpServletRequest request);

    /**
     * 非管理员用户注册
     * @param user
     * @param request
     * @return
     */
    ResultModel register(User user,HttpServletRequest request);

    /**
     * 根据身份证号码检查用户是否已经被注册
     * @param idCard
     * @return
     */
    User getUserByIdCard(String idCard);

    /**
     * 根据电话号码检查用户是否已经被注册
     * @param phoneNum
     * @return
     */
    User getUserByPhone(String phoneNum);
}
