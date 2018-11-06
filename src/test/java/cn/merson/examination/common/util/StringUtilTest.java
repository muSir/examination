package cn.merson.examination.common.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 字符串工具类单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StringUtilTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StringUtil stringUtil;

    @Test
    public void isIDCard() throws Exception {
        boolean result = stringUtil.isIDCard("51138119930812875");
        //assertEquals(false,result);
        assertEquals(true,result);
    }

    @Test
    public void isEmail() throws Exception {
        String email = "mersonDt163.com";
        //boolean result = StringUtil.isEmail("446463188@qq.com");
        boolean result = stringUtil.isEmail(email);
        //assertEquals(false,result);
        assertEquals(true,result);
    }

    @Test
    public void testPackageIds(){
        List<Long> ids = new ArrayList<>();
        for (long i = 100;i<150;i++){
            ids.add(i);
        }
        logger.info("组装之后的字符串为：" + stringUtil.packageIds(ids));
    }

    @Test
    public void testParserIds(){
        String str = "100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,";
        List<Long> ids = stringUtil.parseIds(str);
        for (Long id : ids){
            logger.info("" + id);
        }
    }

    @Test
    public void isMobile() throws Exception {

    }

    @Test
    public void isPhone() throws Exception {
    }

}