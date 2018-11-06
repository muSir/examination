package cn.merson.examination.common.util;

import cn.merson.examination.common.enums.QuestionType;
import cn.merson.examination.common.exceptions.ExaException;
import cn.merson.examination.configuration.BaseExaminationApplicationContext;
import cn.merson.examination.entity.Question;
import org.apache.poi.ss.usermodel.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @Description: 文件操作工具类   Excel模板要将所有的列都设置为文本格式
 * @Author: created by Merson
 * Created on 2017/12/11 0011 23:12
 */
@Component
public class FileUtil extends BaseExaminationApplicationContext{

    @Autowired
    private QuestionUtil questionUtil;

    @Autowired
    private StringUtil stringUtil;

    /**
     * 可以处理的格式列表
     */
    public final List<String> SUFFIXS = Arrays.asList(".xls",".xlsx",".xml");

    //路径应该写在配置文件中
    private final String PATH = "D:/work/idea_projects/examination/src/main/resources/download/template";
    private final String EXCEL = "/QuestionsExcelTemplate.xls";
    private final String XML = "/QuestionsXmlTemplate.xml";
    //压缩文件  一起下载
    private final String ZIP = "/template.rar";

    public List<Question> handle(MultipartFile file) throws Exception{
        if (file == null || file.isEmpty()){
            throw new ExaException("没有内容，请重新上传");
        }
        //文件后缀名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (!SUFFIXS.contains(suffix)){
            throw new ExaException("格式不正确");
        }

        if (SUFFIXS.get(0).equals(suffix) || SUFFIXS.get(1).equals(suffix)){
            return readExcel(file);
        }else {
            return readXml(file);
        }
    }

    /**
     *   读取上传的Excel（不管是哪种后缀名格式）,并返回题库集合
     * @param file
     * @throws Exception
     */
    private List<Question> readExcel(MultipartFile file) throws Exception{
        if (file == null || file.isEmpty()){
            throw new ExaException("没有内容，请重新上传");
        }
        //文件后缀名
        /*String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));*/
        //临时保存文件
        String path = "C:/Users/Administrator/AppData/Local/Temp/questions";
        File targetFile = new File(path,file.getOriginalFilename());
        file.transferTo(targetFile);
        //开始读取Excel
        Workbook wb = WorkbookFactory.create(targetFile);
        return readExcelCell(wb);
    }

    /**
     * 读取Excel表格表头的内容
     * @return String 表头内容的数组
     */
    private String[] readExcelTitle(Workbook wb) throws Exception{
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        System.out.println("colNum:" + colNum);
        if (colNum > 0){
            String[] title = new String[colNum];
            for (int i = 0; i < colNum; i++) {
                title[i] = row.getCell(i).getCellFormula();
            }
            return title;
        }
        return null;
    }

    /**
     * 读取Excel数据单元内容
     * @return Map 包含单元格数据内容的Map对象
     */
    private List<Question> readExcelCell(Workbook wb) throws Exception{
        if(wb==null){
            throw new ExaException("Workbook对象为空！");
        }
        List<Question> questions = null;
        try {
            //Excel页数（sheet）
            int sheetNum = wb.getNumberOfSheets();
            if (sheetNum <= 0){
                throw new ExaException("没有内容，请重新上传");
            }
            questions = new ArrayList<>();
            //循环页
            for (int i = 0;i < sheetNum;i++){
                Sheet sheet = wb.getSheetAt(i);
                //如果当前页没有数据，则继续循环下一页
                if (sheet == null){
                    continue;
                }
                //循环行
                for (int j = 0;j<=sheet.getLastRowNum();j++){
                    Row row = sheet.getRow(j);
                    if (row != null){
                        Cell startCell = row.getCell(0);
                        String startStr = startCell.getStringCellValue();
                        if (!"选择题".equals(startStr) && !"多选题".equals(startStr) && !"问答题".equals(startStr)){
                            continue;//当前行不是正确数据，继续检查下一行数据
                        }
                        Cell cell = null;
                        //第一列：题目类型
                        cell = row.getCell(0);
                        String type = questionType(cell);
                        //第二列：题干
                        cell = row.getCell(1);
                        String title = cell.getStringCellValue();
                        //第三列：选项（选择题）
                        String options = "";
                        if (!QuestionType.ShortAnswer.toString().equals(type)){
                            cell = row.getCell(2);
                            options = cell.getStringCellValue();
                        }
                        //第四列：答案
                        cell = row.getCell(3);
                        String answer = cell.getStringCellValue();
                        //第五列：答案关键词（问答题）
                        String keywords = "";
                        if (QuestionType.ShortAnswer.toString().equals(type)) {
                            cell = row.getCell(4);
                            keywords = cell.getStringCellValue();
                        }
                        //第六列：答案解析
                        cell = row.getCell(5);
                        String reference = cell.getStringCellValue();
                        //第七列：分值
                        cell = row.getCell(6);
                        int value = 0;
                        try {
                            String questionValue = cell.getStringCellValue();
                             value = Integer.parseInt(questionValue);
                        }catch (NumberFormatException | ExaException e){
                            //分值输入错误
                            throw e;
                        }
                        //加入集合
                        Question question = questionUtil.newQuestionInstance(type,title,options,answer,value,keywords,reference);
                        if (question == null){
                            throw new Exception("format is incorrect");
                        }
                        questions.add(question);
                    }
                }
            }
        } catch (Exception ex){
            throw ex;
        }finally {
            if (wb != null){
                wb.close();
            }
        }
        return questions;
    }

    /**
     * 读取xml文件
     * @param file
     */
    public List<Question> readXml(MultipartFile file){
        List<Question> questions = null;
        return questions;
    }

    //添加题库模板下载
    public void downloadQuestionsTemplate(String[] templates,HttpServletRequest request, HttpServletResponse response){
        if (templates == null || templates.length <1){
            return;
        }
        String fullPath = "";
        if (templates.length == 2){
            fullPath = PATH + ZIP;
        }else if (templates.length == 1){
            if (templates[0].equals("xml")){
                fullPath = PATH + XML;
            }else {
                fullPath = PATH + EXCEL;
            }
        }else {
            return;
        }

        File downloadFile = new File(fullPath);
        ServletContext context = request.getServletContext();
        //get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (stringUtil.isNullOrEmpty(mimeType)){
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }

        // set content attributes for the response
        response.setContentType(mimeType);
        //文件大小
        int fileSize = (int) downloadFile.length();
        response.setContentLength(fileSize);

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;fileName=" + downloadFile.getName();/*String.format("attachment;filename=\"%s\"",downloadFile.getName());*/
        response.setHeader(headerKey,headerValue);

        // Copy the stream to the response's output stream
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fullPath);
            IOUtils.copy(inputStream,response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                    response.flushBuffer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputStream = null;
            }
        }
    }

    /**
     * 将Excel中的题目类型转换为数据库中存储的字段值
     * @param cell
     * @return
     * @throws Exception
     */
    private String questionType(Cell cell) throws Exception{
        String cellStr = cell.getStringCellValue();
        String questionType = "";
        if ("单选题".equals(cellStr)){
            questionType = QuestionType.SingleSelect.toString();
        }else if ("多选题".equals(cellStr)){
            questionType = QuestionType.MultiSelect.toString();
        }else if ("问答题".equals(cellStr)){
            questionType = QuestionType.ShortAnswer.toString();
        }else {
            throw new Exception("题目类型只能是单选题、多选题及问答题中的一种");
        }
        return questionType;
    }

}
