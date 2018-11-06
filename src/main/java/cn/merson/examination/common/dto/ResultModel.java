package cn.merson.examination.common.dto;

/**
 * Class: ResultModel
 * @Description:  所有接口返回的对象模型
 * @Author: Merson
 */
public class ResultModel<T> {
    private int code;
    //请求成功返回的数据主体
    private T data;
    //错误信息描述
    private String message;
    //分页
    private Page page;
    //需要redirect或这forward的url地址
    private String url;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        //TODO
        this.url = url;
    }
}
