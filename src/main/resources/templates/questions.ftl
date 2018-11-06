<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>管理员-题库管理</title>
    <link rel="stylesheet" type="text/css" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
    <script src="/js/questions.js"></script>
</head>
<body>
    <div align="center">
        <p>题库试题列表<br/><br/><#--<a href="#">批量删除</a></p>-->
        <form class="tab1" action="/questions/search" method="get">
            题目内容&nbsp;&nbsp;<input name="questionName" type="text" class="allInput" id="questionName"
                                   value="<#if questionName?exists>${questionName}</#if>"/>
            题目答案&nbsp;&nbsp;<input name="questionAnswer" type="text" class="allInput"  id="questionAnswer"
                                   value="<#if questionAnswer?exists>${questionAnswer}</#if>"/>
            题目分值&nbsp;&nbsp;<input name="questionValue" type="text" class="allInput" id="questionValue"
                                   value="<#if questionValue?exists>${questionValue}</#if>"/>
            题目类型&nbsp;&nbsp;
                <select name="questionType">
                    <option value="SingleSelect">单选题</option>
                    <option value="MultiSelect">多选题</option>
                    <option value="ShortAnswerQuestion">问答题</option>
                </select>
            答案解析&nbsp;&nbsp;<input type="text" name="questionRefence" id="questionRefence"
                                   value="<#if questionRefence?exists>${questionRefence}</#if>"/>
            <input type="submit" value="查询"/>&nbsp;&nbsp;
            <input type="reset" value="清空"/>
        </form>

        <div>
            <table width="100%" class="tab2">
                <tbody>
                <tr>
                    <th width="20"><input type="checkbox" id="all"  onclick="checkAll();"/></th>
                    <th width="40">编号</th>
                    <th width="20%">题干</th>
                    <th width="10%">选项</th>
                    <th width="20%">答案</th>
                    <th width="10%">关键词</th>
                    <th width="50">分值</th>
                    <th width="40">类型</th>
                    <th width="20%">答案解析</th>
                    <th width="80">操作</th>
                </tr>
                <#if questions?exists >
                    <#list questions as question>
                        <tr style="height: 50px">
                            <td><input type="checkbox" name="id" value="${question.questionId}"/></td>
                            <td>${question_index?if_exists+1}</td>
                            <td align="left">${question.title}</td>
                            <td >
                                <#if question.dtoOptions??>
                                    <#list question.dtoOptions as option>
                                        <p>${option}</p>
                                    </#list>
                                </#if>
                            </td>
                            <td align="left">${question.answer}</td>
                            <td>
                                <#if question.dtoKeywords??>
                                    <#list question.dtoKeywords as keyword>
                                        <p>${keyword}</p>
                                    </#list>
                                </#if>
                            </td>
                            <td>${question.value}</td>
                            <td>${question.dtoType}</td>
                            <td align="left"><#if question.reference??>${question.reference}</#if></td>
                            <td align="center">
                                <a href="/questions/${question.questionId}">编辑</a>&nbsp;
                                <a href="/questions/${question.questionId}/delete">删除</a>
                            </td>
                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
            <div class='page fix'>
                共 <b><#if pageSize??>${pageSize}<#else >0</#if></b> 条
                <a href='###' class='first'>首页</a>
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