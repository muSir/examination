package cn.merson.examination.controller;

import cn.merson.examination.common.util.FileUtil;
import cn.merson.examination.common.util.StringUtil;
import cn.merson.examination.service.IQuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.Date;

/**
 * @Description: 文件上传
 * @Author: created by Merson
 * Created on 2017/12/11 0011 20:21
 */
@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    private IQuestionService questionService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/jump",method = RequestMethod.GET)
    public String jump(){
        return "test";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String fileUpload(HttpServletRequest request){
        request.setAttribute("msg","123");
        return "file";
    }

    /**
     * 模板文件下载
     * @param request
     * @return
     */
    public String fileDownload(HttpServletRequest request, HttpServletResponse response){
        String excelFileName="模板1.xls";
        String xmlFileName="模板2.xml";
        String filePath = "F:/test" ;
        File file = new File(filePath + "/" + excelFileName);

        if(file.exists()){ //判断文件父目录是否存在
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + excelFileName);

            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("----------file download" + excelFileName);
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

}
