<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>题目修改页面</title>
    <script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
    <script src="/js/questiondetail.js"></script>
</head>
<body>
    <div align="center" style="width: 50%;border: 1px;border-color: #000000;border-style: solid;">
        <form action="/questions/${question.questionId}" method="post">
            <input type="hidden" name="questionId" value="${question.questionId}"/>
            题&nbsp;&nbsp;干：<input type="text" name="title" value="${question.title}" width="200px" height="400px"/><br /><br />
            <#if question.dtoOptions??>
                <#list question.dtoOptions as option>
                    选项${option_index?if_exists+1}：<input type="text" value="${option}" name="option"/><br/>
                </#list>
            </#if><br /><br />
            答&nbsp;&nbsp;案：<input type="text" name="answer" value="${question.answer}" width="200px" height="200px"/><br /><br />
            分&nbsp;&nbsp;值：<input type="text" name="value" value="${question.value}" width="200px" height="40px"/><br /><br />
            类&nbsp;&nbsp;型：
            <select name="type">
                <option value="SingleSelect">单选题</option>
                <option value="MultiSelect">多选题</option>
                <option value="ShortAnswerQuestion">问答题</option>
            </select>
            <br /><br />
            <input type="submit" value="保存" width="60px" height="40px"/>
        </form>

    </div>
</body>
</html>