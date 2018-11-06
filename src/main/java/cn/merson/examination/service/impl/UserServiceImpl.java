package cn.merson.examination.service.impl;

import cn.merson.examination.common.dto.ResultModel;
import cn.merson.examination.common.enums.UserType;
import cn.merson.examination.common.util.LoginUserUtil;
import cn.merson.examination.common.util.MD5Util;
import cn.merson.examination.common.util.ResultUtil;
import cn.merson.examination.common.util.StringUtil;
import cn.merson.examination.entity.Examination;
import cn.merson.examination.entity.Question;
import cn.merson.examination.entity.User;
import cn.merson.examination.repository.IQuestionRepository;
import cn.merson.examination.repository.IUserRepository;
import cn.merson.examination.service.IExaminationService;
import cn.merson.examination.service.IUserService;
import cn.merson.examination.service.IVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IQuestionRepository questionRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IExaminationService examinationService;

    //验证逻辑
    @Autowired
    private IVerificationService verificationService;

    @Autowired
    private StringUtil stringUtil;
    @Autowired
    private ResultUtil resultUtil;
    @Autowired
    private MD5Util md5Util;
    @Autowired
    private LoginUserUtil loginUserUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 用户登录
     * @param username
     * @param password
     * @param request
     * @return
     */
    @Override
    public ResultModel login(String username, String password, HttpServletRequest request) {
        if (stringUtil.isNullOrEmpty(username) || stringUtil.isNullOrEmpty(password)){
            return resultUtil.errorResult(1,"账号或密码不能为空","ftl/login");
        }
        //密码格式转换
        String md5Pass =  md5Util.getMD5Code(password);
        if (username.contains("admin") || username.contains("Admin")){
            //管理员用户
            return adminLogin(username,password,request);
        }
        //普通用户登录
        return examineeLogin(username,md5Pass,request);
    }

    /**
     * 用户注册
     * @param user
     * @param request
     * @return
     */
    @Override
    public ResultModel register(User user, HttpServletRequest request) {
        //数据格式及正确性校验
        ResultModel resultModel = null;
        //1.电话号码验证
        resultModel = verificationService.validateCellPhone(user.getPhoneNum());
        if (resultModel.getCode() == -1){
            //验证未通过
            return resultModel;
        }
        resultModel = verificationService.validateEmail(user.getEmail());
        //2.邮箱验证
        if (resultModel.getCode() == -1){
            //验证未通过
            return resultModel;
        }
        //3.身份验证
        if (stringUtil.isNullOrEmpty(user.getRealName())){
            return resultUtil.verificationErrorResult("真实姓名不能为空");
        }
        resultModel = verificationService.validateIdCard(user.getIdCard(),user.getRealName());
        if (resultModel.getCode() == -1){
            //验证未通过
            return resultModel;
        }
        //4.其他验证
        if (stringUtil.isNullOrEmpty(user.getPassword()) || user.getPassword().length() <8){
            return resultUtil.verificationErrorResult("密码至少包含9个数字或字符");
        }
        //5.其他信息补充
        //密码转换
        user.setPassword(md5Util.getMD5Code(user.getPassword()));
        //用户类型
        user.setUserType(UserType.Normal.toString());
        //注册时间
        user.setCreateTime(new Date());
        //上次登录时间
        user.setLastLoginTime(new Date());
        //昵称:注册时如果没有填写昵称，则默认设置为电话号码
        if (stringUtil.isNullOrEmpty(user.getNickName())){
            user.setNickName(user.getPhoneNum());
        }
        //用户名（可空）
        user.setUsername(user.getPhoneNum());
        //注册时等级为1
        user.setLevel(1);
        //注册时vip等级为0 代表普通用户
        user.setVipLevel(0);

        //保存
        User back = userRepository.save(user);
        if (back == null){
            //注册失败
            return resultUtil.errorResult(1,"","ftl/register");
        }
        //注册成功，自动登录
        return afterLoginSuccess(back,request);
    }

    /**
     * 管理员用户登录
     * @param username
     * @param password
     * @param request
     * @return
     */
    @Transactional
    public ResultModel adminLogin(String username,String password,HttpServletRequest request) {
        //MD5码
        User user = userRepository.adminLogin(username,password,UserType.Administrator.toString());
        //登录失败
        if (user == null){
            //还没有注册
            return resultUtil.errorResult(1,"错误的管理员账号","ftl/login");
        }
        return afterLoginSuccess(user,request);
    }

    /**
     * 普通用户登录
     * @param identification
     * @param password
     * @param request
     * @return
     */
    @Transactional
    public ResultModel examineeLogin(String identification,String password,HttpServletRequest request){
        if (!stringUtil.isMobile(identification) && !stringUtil.isEmail(identification)){
            return resultUtil.errorResult(1,"登录账号格式不正确","ftl/login");
        }
        User user = userRepository.studentLogin(identification,password);
        //登录失败
        if (user == null){
            //还没有注册
            return resultUtil.errorResult(1,"账号或密码错误","ftl/login");
        }
        return afterLoginSuccess(user,request);
        /*return ResultUtil.errorResult(1,"用户登录发生未知错误","ftl/login");*/
    }

    /**
     * 用户登录成功后的一些公共操作，更新用户最近登录时间
     * @param user
     */
    private ResultModel afterLoginSuccess(User user,HttpServletRequest request){
        if (user == null){
            return null;
        }
        //设置最新登录时间
        user.setLastLoginTime(new Date());
        User back = userRepository.save(user);
        if (back == null){
            return null;
        }
        //添加当前登录用户
        loginUserUtil.saveCurrentLoginUser(request,user);

        if (UserType.Administrator.toString().equals(back.getUserType())){
            //管理员界面 删除/更新试题 => 试题列表  分页  试卷维护
            //分页获取试题并存于redis中
            return resultUtil.successResult(back,"管理员登录成功","redirect:/admin/" + back.getUserId());
        }
        //非管理员用户登录  获取考试记录，跳转到首页
        //List<Examination> examinations = examinationService.queryExaminationRecordByUserId(user.getUserId());
        return resultUtil.successResult(null,"用户登录成功","redirect:/dream/" + back.getUserId());
    }

    /**
     * 检查身份证号是否已经被注册
     * @param idCard
     * @return 如果不存在则返回null
     */
    @Override
    public User getUserByIdCard(String idCard) {
        return userRepository.getUserByIdCard(idCard);
    }

    /**
     * 检查电话号是否已经被注册
     * @param phoneNum
     * @return 如果不存在则返回null
     */
    @Override
    public User getUserByPhone(String phoneNum) {
        return userRepository.getUserByPhoneNum(phoneNum);
    }

}
