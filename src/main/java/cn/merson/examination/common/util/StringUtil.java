package cn.merson.examination.common.util;

import cn.merson.examination.configuration.BaseExaminationApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
@Component
public class StringUtil extends BaseExaminationApplicationContext{

    @Autowired
    private LoginUserUtil loginUserUtil;

    /**
     * 判断给定的字符串是否为空
     * @param content
     * @return 如果为null或者""则返回true，否则返回false
     */
    public boolean isNullOrEmpty(String content){
        return content == null || "".equals(content.trim());
    }

    /**
     * 随机产生一个0-bound之间的整数
     * @param bound
     * @return boound非法返回-1，否则是0-size的一个随机整数
     */
    public int randomBound(int bound){
        if (bound <= 0){
            return -1;
        }
        Random random = new Random();
        return random.nextInt(bound);
    }

    /**
     *   将列表中的所有字符串合成一个字符串，每两个中间以英文逗号隔开
     * @param ids
     * @return 给定集合为null或者没有值返回null，否则返回对应的字符串
     */
    public String packageIds(List<Long> ids){
        if (ids == null || ids.size() == 0){
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        //末尾也加上逗号
        for (Long id : ids){
            sb.append(id+ ",");
        }
        return sb.toString();
    }

    /**
     * 将指定字符串解析为Long类型的id集合  空格也暂时不处理
     * @param content
     * @return
     */
    public List<Long> parseIds(String content){
        if (isNullOrEmpty(content)){
            return null;
        }
        List<Long> ids = new ArrayList<>();
        String[] idsArr = content.split(",");
        for (String idStr : idsArr){
            //不用抛出异常
            Long id = Long.parseLong(idStr);
            ids.add(id);
        }
        return ids;
    }

    /**
     * 将指定字符串解析为字符串数组 分隔符号为,,,
     * @param content
     * @return
     */
    public List<String> parseStr(String content){
        if (isNullOrEmpty(content)){
            return null;
        }
        //TODO 如果没有ABCD的选项标示则需要加上 暂时不做
        String[] arr = content.split(",,,");
        List<String> list = new ArrayList<>();
        for (String str : arr){
            list.add(str);
        }
        return list;
    }

    /**
     * 生成试卷所有试题的key
     * @param ori 试卷No
     * @return
     */
    public String buildKey(String ori){
        if (isNullOrEmpty(ori)){
            throw new NullPointerException("generator key is null or empty.");
        }
        StringBuilder sb = new StringBuilder("PAPERQUESTION_");
        sb.append(ori);
        return sb.toString();
    }

    /**
     * 18位身份证号码的简单验证
     * @param idCard 给定的字符串
     * @return 如果为空或者格式不正确则返回false，否则返回true
     */
    public boolean isIDCard(String idCard){
        if (isNullOrEmpty(idCard)){
            return false;
        }
        Pattern pattern = Pattern.compile("^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$");
        Matcher matcher = pattern.matcher(idCard);
        if(matcher.matches()){
            return true;
        }
        return false;
    }

    /**
     * 邮箱正则验证
     * @param email
     * @return
     */
    public boolean isEmail(String email) {
        if (isNullOrEmpty(email)){
            return false;
        }
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return Pattern.matches(regex, email);
    }

    /**
     * 手机号验证
     * @param  cellphone
     * @return 验证通过返回true
     */
    public boolean isMobile(String cellphone) {
        if (isNullOrEmpty(cellphone)){
            return false;
        }
        String regex = "^[1][3,4,5,7,8][0-9]{9}$";
        return Pattern.matches(regex,cellphone);
    }
    /**
     * 电话号码验证 暂时不使用
     * @param  phoneNum
     * @return 验证通过返回true
     */
    public boolean isPhone(final String phoneNum) {
        if (isNullOrEmpty(phoneNum)){
            return false;
        }
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (phoneNum.length() > 9) {
            m = p1.matcher(phoneNum);
            b = m.matches();
        } else {
            m = p2.matcher(phoneNum);
            b = m.matches();
        }
        return b;
    }
}
