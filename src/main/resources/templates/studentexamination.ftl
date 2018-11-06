<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>考生试卷管理</title>
    <link rel="stylesheet" type="text/css" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
    <script src="/js/papers.js"></script>
</head>
<body>
<div align="center">
    <p>有效试卷<br/><br/></p>
    <#--查询table-->
    <form class="tab1" action="/papers/search" method="get">
        试卷名称&nbsp;&nbsp;<input name="paperName" type="text" class="allInput" id="paperName"
                               value="<#if queryName?exists>${queryName}</#if>"/>
        有效日期&nbsp;&nbsp;<input type="datetime-local" name="deadline" id="deadline"/>
        <input type="submit" value="查询"/>
    </form>
    <p></p>
    <div>
        <table width="100%" class="tab2">
            <tbody>
            <tr>
                <th width="20"><input type="checkbox" id="all" /></th>
                <th width="40">编号</th>
                <th width="20%">试卷名称</th>
                <th width="40%">试卷题目</th>
                <th width="20%">有效日期</th>
                <th width="80">操作</th>
            </tr>
            <#if validPapers?exists >
                <#list validPapers as paper>
                <tr>
                    <td><input type="checkbox" name="id" value="${paper.examinationPaperNo}"/></td>
                    <td>${paper_index?if_exists+1}</td>
                    <td>${paper.paperName}</td>
                    <td>${paper.detail}</td>
                    <td>${paper.decodeTimeStr}</td>
                    <td align="center">
                        <a href="/examinations/${userId}/${paper.examinationPaperNo}/start?paperName=${paper.paperName}">开始答题</a>&nbsp;
                    </td>
                </tr>
                </#list>
            </#if>
            </tbody>
        </table>
        <div class='page fix'>
            共 <b><#if totalCount?exists>${totalCount}</#if></b> 条
            <a href='/users/welcome' class='first'>首页</a>
            <a href='###' class='pre'>上一页</a>
            当前第<span>1/1</span>页
            <a href='###' class='next'>下一页</a>
            <a href='###' class='last'>末页</a>
            跳至&nbsp;<input type='text' value='1'/>&nbsp;页&nbsp;
            <a href='###' class='go'>GO</a>
        </div>
    </div>
</div>
</body>
</html>