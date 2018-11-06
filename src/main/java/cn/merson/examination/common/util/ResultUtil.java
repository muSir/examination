package cn.merson.examination.common.util;

import cn.merson.examination.common.dto.Page;
import cn.merson.examination.common.dto.ResultModel;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * Class: ResultUtil
 * @Description: 封装返回数据模型
 * @Author: Merson
 */
@Component
public class ResultUtil {
    /**
     * 请求发生错误时的数据封装
     * @param code
     * @param message 错误描述
     * @return
     */
    public ResultModel errorResult(Integer code, String message){
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(code);
        resultModel.setMessage(message);
        return resultModel;
    }

    public ResultModel errorResult(Integer code, String message,String url,Object object){
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(code);
        resultModel.setMessage(message);
        resultModel.setUrl(url);
        resultModel.setData(object);
        return resultModel;
    }

    public ResultModel errorResult(Integer code, String message,String url){
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(code);
        resultModel.setMessage(message);
        resultModel.setUrl(url);
        return resultModel;
    }


    /**
     * 请求成功并且有数据返回时调用
     * @param object
     * @param message
     * @return
     */
    public ResultModel successResult(Object object,String message){
        //请求成功时，代码为100
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(100);
        resultModel.setData(object);
        resultModel.setMessage(message);//TODO 可以使用枚举  后面在改
        return resultModel;
    }


    /**
     * 请求成功且不带说明时调用
     * @param object
     * @return
     */
    public ResultModel successResult(Object object){
        //请求成功时，代码为100
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(100);
        resultModel.setData(object);
        resultModel.setMessage("请求成功");
        return resultModel;
    }

    /**
     * 请求成功并且有数据返回时调用
     * @param object
     * @param message
     * @return
     */
    public ResultModel successResult(Object object,String message,String url){
        //请求成功时，代码为100
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(100);
        resultModel.setData(object);
        resultModel.setMessage(message);
        resultModel.setUrl(url);
        return resultModel;
    }

    public ResultModel successResult(Object object, Page page, String message, String url){
        //请求成功时，代码为100
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(100);
        resultModel.setData(object);
        resultModel.setMessage(message);
        resultModel.setPage(page);
        resultModel.setUrl(url);
        return resultModel;
    }


    /**
     *  请求成功但是无数据返回时调用
     * @return
     */
    public ResultModel successResult(){
        //请求成功时，代码为100
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(100);
        resultModel.setMessage("请求成功");
        return resultModel;
    }

    /**
     * ajax验证成功时返回模型
     * @return
     */
    public ResultModel verificationSuccessResult(){
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(1000);
        resultModel.setMessage("验证成功");
        return resultModel;
    }

    /**
     * ajax验证失败时返回模型
     * @param message
     * @return
     */
    public ResultModel verificationErrorResult(String message){
        ResultModel resultModel = new ResultModel();
        resultModel.setCode(-1);
        resultModel.setMessage(message);
        return resultModel;
    }

}
