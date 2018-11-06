package cn.merson.examination.common.dto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Class: Page
 * @Description: 分页
 * @Author: Merson
 */
public class Page<T> {
    //当前页数 默认为第一页
    private int pageNo = 1;
    //每页数据的条数 默认为10条
    private int pageSize = 10;
    //总页数 => 当前查询数据总条数/每页条数 + 1
    private int totalPage;
    //需要查询的数据总条数
    private int totalCount;
    //数据
    private List<T> data;
    //是否是第一页
    private boolean isFirstPage = true;
    //是否是最后一页
    private boolean isLastPage = false;
    //当前请求
    private HttpServletRequest request;
    //数据库记录总条数
    private int count;
    //实际返回结果的条数
    private int resultCount;

    //默认构造器
    public Page(){}

    /**
     * 通过HttpServletRequest构造Page:单页查询时使用
     * 前端携带的分页数据
     * @param request
     */
    public Page(HttpServletRequest request){
        this.request = request;
        //页数标签:第几页
        try {
            int no = Integer.parseInt(request.getParameter("pageNo"));
            this.setPageNo(no);
        } catch (NumberFormatException e) {
            this.setPageNo(1);
        }
        //每页显示数据条数
        try {
            int size = Integer.parseInt(request.getParameter("pageSize"));
            this.setPageSize(size);
        } catch (NumberFormatException e){
            this.setPageSize(10);
        }

        try {
            int totalCount = Integer.parseInt(request.getParameter("totalCount"));
            //初始化时  count默认为taotalCount
            this.setTotalCount(totalCount);
        } catch (Exception e) {
            //如果没有找到对应属性  则默认单页查询
            this.setTotalCount(this.pageSize);
        }
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        //适当的处理
        if (pageNo < 1){
            pageNo = 1;
        }
        if(pageNo == 1){
            this.isFirstPage = true;
        }
        else {
            this.isFirstPage = false;
        }
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize <= 0){
            pageSize = 10;
        }
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage() {
        if (this.count <= 0){
            //没有数据时
            this.totalPage = 0;
            throw new NullPointerException("database has no data exception.");
        }
        //计算设置
        this.totalPage = this.count/this.pageSize + 1;//todo pagesize可能为0,也就是必须要先设置pageSize，才能设置count
        if(this.pageNo == this.totalPage){
            //最后一页
            this.isLastPage = true;
        }
        else {
            this.isLastPage = false;
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isFirstPage() {
        return this.pageNo == 1;
    }

    public boolean isLastPage() {
        return this.pageNo == this.totalPage;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        this.setTotalPage();
    }

    public int getResultCount() {
        if (this.data != null){
            return this.data.size();
        }
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    /**
     * 获取开始行数  总是以整数10位开始 0,10,20...
     * @return
     */
    public int getStartIndex(){
        return (this.pageNo - 1) * this.pageSize;
    }
}
