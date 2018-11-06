package cn.merson.examination.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 用户实体
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long userId;

    //身份证号码  限制为18位 不是主键 但是是唯一标识 因为18位身份证最后以为可能是X或者x，所以用字符串表示
    @Column(unique = true,nullable = false)
    private String idCard;

    //用户姓名：管理员账号Admin  考生则为手机号中
    @Column(nullable = true)
    private String username;

    //昵称，如果没有昵称则默认账号为昵称
    @Column(nullable = true)
    private String nickName;

    //身份证上的真实姓名
    @Column(nullable = false)
    private String realName;

    //登录密码
    @Column(nullable = false)
    private String password;

    //电话号码 必须为11位的手机号
    @Column(unique = true,nullable = false)
    private String phoneNum;

    //用户邮箱
    @Column(nullable = false)
    private String email;

    //用户类型  数据库默认只保存考生类型
    @Column(nullable = false)
    private String userType;

    //上次登录时间
    @Column(nullable = false)
    private Date lastLoginTime;

    //注册时间
    @Column(nullable = false)
    private Date createTime;

    //用户等级
    private int level;

    //会员等级
    private int vipLevel;

    public Long getUserId() {
        return userId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
