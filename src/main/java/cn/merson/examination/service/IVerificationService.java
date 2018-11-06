package cn.merson.examination.service;

import cn.merson.examination.common.dto.ResultModel;

/**
 * @Description: 验证功能逻辑接口
 * @Author: created by Merson
 * Created on 2017/12/19 0019 20:33
 */
public interface IVerificationService {

    /**
     * 电话号码验证
     * @param phoneNum
     * @return
     */
    ResultModel validateCellPhone(String phoneNum);

    /**
     * 邮箱验证
     * @param email
     * @return
     */
    ResultModel validateEmail(String email);

    /**
     * 身份证验证
     * @param idCard
     * @param realName
     * @return
     */
    ResultModel validateIdCard(String idCard,String realName);
}
