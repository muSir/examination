package cn.merson.examination.repository;

import cn.merson.examination.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @Description: 用户类 数据库只保存考生角色信息
 * @Author: created by Merson
 * Created on 2017/12/4  22:30
 */
public interface IUserRepository extends CrudRepository<User,Long> {

    /**
     *  查找指定身份证号的用户
     * @param idCard
     * @return
     */
    User getUserByIdCard(String idCard);

    /**
     * 查找指定电话号的用户
     * @param phoneNum
     * @return
     */
    User getUserByPhoneNum(String phoneNum);

    /**
     * 根据ID查询用户
     * @param userId
     * @return
     */
    User getUserByUserId(Long userId);

    /**
     * 用户登录(账号+密码)
     * @param username
     * @param password
     * @return
     */
    @Query(value = "SELECT *  FROM USER WHERE (email=?1 OR phone_num=?1) AND password=?2",nativeQuery = true)
    User studentLogin(String username,String password);

    @Query(value = "SELECT *  FROM USER WHERE username=?1 AND password=?2 AND user_type=?3",nativeQuery = true)
    User adminLogin(String username,String password,String userType);
}
