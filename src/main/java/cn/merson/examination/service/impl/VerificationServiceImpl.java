package cn.merson.examination.service.impl;

import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.common.util.ResultUtil;
import cn.merson.examination.common.util.StringUtil;
import cn.merson.examination.service.IUserService;
import cn.merson.examination.service.IVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 验证功能具体逻辑
 * @Author: created by Merson
 * Created on 2017/12/19 0019 20:37
 */
@Service
public class VerificationServiceImpl implements IVerificationService {

    @Autowired
    private IUserService userService;
    @Autowired
    private StringUtil stringUtil;
    @Autowired
    private ResultUtil resultUtil;

    /**
     * 用户注册时电话号码的验证
     * @param phoneNum 电话号码
     * @return
     */
    @Override
    public ResultModel validateCellPhone(String phoneNum) {
        if (!stringUtil.isMobile(phoneNum)) {
            //11位电话号码格式不正确
            return resultUtil.verificationErrorResult("手机号格式不正确");
        }
        //电话号码唯一，如果数据库里已经存在对应电话号的用户，则失败
        if (userService.getUserByPhone(phoneNum) != null) {
            //电话号码已经被注册
            return resultUtil.verificationErrorResult("电话号码已经被注册");
        }
        return resultUtil.verificationSuccessResult();
    }

    /**
     * 用户注册时邮箱格式的验证
     * @param email 邮箱
     * @return
     */
    @Override
    public ResultModel validateEmail(String email) {
        if (!stringUtil.isEmail(email)){
            return resultUtil.verificationErrorResult("邮箱格式不正确");
        }
        return resultUtil.verificationSuccessResult();
    }

    /**
     * 用户注册时身份证号码的验证
     * @param idCard 身份证号码
     * @param realName 与身份证对应的真是姓名
     * @return
     */
    @Override
    public ResultModel validateIdCard(String idCard, String realName) {
        if (!stringUtil.isIDCard(idCard)) {
            return resultUtil.verificationErrorResult("身份证号格式不正确");
        }
        //身份证号唯一，如果数据库里已经存在对应身份证号的用户，则失败
        if (userService.getUserByIdCard(idCard) != null) {
            return resultUtil.verificationErrorResult("身份证号已经被注册");
        }
        return resultUtil.verificationSuccessResult();
    }
}
