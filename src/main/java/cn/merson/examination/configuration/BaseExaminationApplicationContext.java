package cn.merson.examination.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: created by Merson
 * Created on 2018/1/4 0004 13:35
 */
@Component
public class BaseExaminationApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (BaseExaminationApplicationContext.applicationContext == null){
            BaseExaminationApplicationContext.applicationContext = applicationContext;
        }
    }

    //getter applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取bean
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    //通过class获取bean
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    //通过name和class获取指定的bean
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name,clazz);
    }

}
