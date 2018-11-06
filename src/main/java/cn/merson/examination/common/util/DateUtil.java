package cn.merson.examination.common.util;

import cn.merson.examination.configuration.BaseExaminationApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间格式化工具
 */
@Component
public class DateUtil extends BaseExaminationApplicationContext{

    //系统时间格式字符串
    private final String pattern = "yyyy-MM-dd HH:mm";
    //抛出异常说明
    private final String exceptionMsg = "start time or end time cannot be null.";
    @Autowired
    private StringUtil stringUtil;

    /**
     * 将指定的日期时间标准格式化
     * @param date
     * @return
     */
    public String dateFormat(Date date){
        if (date == null){
            return  null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     *将ISO 8601标准的日期转化为Date
     * dateTime:  "2017-12-31T00:00" 最小单位为分钟
     * @param dateTime
     * @return
     */
    public Date dateParse(String dateTime){
        if (stringUtil.isNullOrEmpty(dateTime)){
            return null;
        }
        //去掉ISO 8601标准中的T
        Date date = null;
        try {
            String formatDateTime = dateTime;
            if (dateTime.contains("T")){//字符串包含T  ISO 8601标准
                formatDateTime = dateTime.replace('T',' ');
            }
            date = new SimpleDateFormat(pattern).parse(formatDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }catch (Exception ex){}
        return date;
    }

    /**
     * 获取系统的当前时间并以字符串的形式返回
     * @return 系统当前时间的字符串
     */
    public String getCurrentTime(){
        Date now = new Date();
        return dateFormat(now);
    }

    /**
     *   判断给定时间和当前系统时间的大小
     * @param date
     * @return 当前时间<date 返回true 否则返回false
     */
    public boolean compareDate(Date date){
        if (date == null){
            return false;
        }
        Date now = new Date();

        if (now.after(date)){
            return false;
        }
        return true;
    }

    /**
     *  after - before
     * @param before
     * @param after
     * @return
     */
    public int timeDifference(Date before,Date after){
        if (before == null || after == null){
            throw new NullPointerException(exceptionMsg);
        }
        long time1 = before.getTime();
        long time2 = after.getTime();
        //1s = 1000ms;   TODO 绝对值
        long interval = time2 - time1;
        //换算成分钟的单位
        int conversion = 1000 * 60;
        return (int) interval / conversion;
    }

}
