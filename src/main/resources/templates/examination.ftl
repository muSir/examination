<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>答题页：<#if question.title??>${question.title}</#if></title>
    <link rel="stylesheet" type="text/css" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
    <script src="/js/examination.js"></script>
</head>
<body>
<div align="center">
    <div align="center" style="border: 1px;border-color: #000000;border-style: solid;height:100%;width: 60%">
        <div align="center">
            <#-- 试卷名称 -->
            <p style="width: 100%"><h3><#if paperName?exists>[试卷] ${paperName}</#if></h3></p >
        </div>
        <div align="center">
            <#-- 题目类型 -->
            <p><h4>
                <#if question.type=="SingleSelect">[单选题]
                <#elseif question.type=="MultiSelect">[多选题]
                <#elseif question.type=="ShortAnswerQuestion">[问答题]
                </#if>
            </h4></p>
        </div>
        <div align="center">
            <#-- 题干-->
            <p style="width: 100%"><#if question.title??>题目<#if question.index??> ${question.index}：${question.title}</#if></#if>
                &nbsp;&nbsp;&nbsp;<#if question.value??>（${question.value}分）</#if></p>
        </div>
        <form action="/examinations/${userId}/${paperId}/next" method="get">
        <input type="hidden" name="qid" value="<#if question.questionId??>${question.questionId}</#if>"/>
            <input type="hidden" name="paperName" value="<#if paperName??>${paperName}</#if>"/>
            <input type="hidden" name="nid" value="<#if nid??>${nid}</#if>"/>
        <#if question.type=="SingleSelect">
            <div align="center">
                <#if question.dtoOptions??>
                    <#list question.dtoOptions as option>
                    <#--0-A,1-B-->
                        <p><input type="radio" name="single" value="${option_index?if_exists}"/>${option}</p>
                    </#list >
                <#else >此题没有选项，请联系管理人员修改题目！
                </#if>
            </div>
        <#elseif question.type=="MultiSelect">
            <div align="center">
                <#if question.dtoOptions??>
                    <#list question.dtoOptions as option>
                    <#--0-A,1-B-->
                        <p><input type="checkbox" name="multi" value="${option_index?if_exists}"/>${option}</p>
                    </#list >
                <#else >此题没有选项，请联系管理人员修改题目！
                </#if>
            </div>
        <#elseif question.type=="ShortAnswerQuestion">
            <div align="center">
                <textarea name="shortQuestionAnswer" style="width:400px;height:200px;font-family: 'Microsoft New Tai Lue';font-size: 14pt"></textarea>
            </div>
        <#else>
        </#if>
            <div>
                <p>
                <#if question.isLast=="isLast">
                    <#--<a href="/examinations/${userId}/${paperId}/end">交卷</a>-->
                    <input type="submit" value="交卷">
                <#else >
                <#--答案?-->
                    <input type="submit" value="下一题">
                    <#--<a href="/examinations/${userId}/${paperId}/next?paperName=${paperName}&answer=&nid=<#if nid??>${nid}</#if>">下一题</a>&nbsp;&nbsp;-->
                    <#--<a href="/examinations/${userId}/${paperId}/end">提前交卷</a>-->
                </#if>
                </p>
            </div>
        </form>
    </div>
</div>
</body>
</html>