package cn.merson.examination.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description: MD5加密
 * @Author: created by Merson
 * Created on 2017/12/19 0019 22:26
 */
@Component
public class MD5Util {

    @Autowired
    private StringUtil stringUtil;

    // 全局数组
    private final String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    // 返回形式为数字跟字符
    private String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 转换字节数组16进制字串
    private String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * 将给定的字符串转换为加密字符串
     * @param strObj
     * @return
     */
    public String getMD5Code(String strObj) {
        if (stringUtil.isNullOrEmpty(strObj)){
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回为存放哈希结果的byte数组
            return byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return strObj;
        }
    }

}
