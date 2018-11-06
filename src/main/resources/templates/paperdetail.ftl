<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>试卷：<#if paperName?exists>${paperName}</#if></title>
</head>
<body>
    <div align="center" style="border: 1px;border-color: #000000;border-style: solid;width: 100%;height: auto">
        <div align="center">
            <p><h3><#if paperName??>${paperName}</#if></h3></p>
        </div>
        <table class="tab2">
            <tbody>
                <#if questions?exists>
                    <#list questions as question>
                        <tr>
                            <#--题目标号+题干-->
                            <td>${question_index?if_exists+1}.${question.title}</td>
                            <#--如果是选择题，则有选项-->
                        </tr>
                        <#if question.dtoOptions??>
                            <tr>
                                <#list question.dtoOptions as option>
                                    <td><#if option??>${option}</#if></td>
                                </#list>
                            </tr>
                        </#if>
                        <tr>
                            <td>答案 : ${question.answer}</td>
                        </tr>
                        <#--空行-->
                        <tr></tr>
                    </#list>
                </#if>
            </tbody>
        </table>
    </div>
</body>
</html>