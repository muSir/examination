<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>创建试卷</title>
</head>
<script language="JavaScript" type="text/javascript">
    <!-- 简单的表单验证 -->
    /*function validate() {
        var username = (document.getElementById("username")).value;
        if (username == null || username == undefined || username.trim().length == 0){
            alert("请填写你的真实姓名！");
            return false;
        }
        var idCard = (document.getElementById("idCard")).value;
        if (idCard == null || idCard == undefined || idCard.trim().length == 0){
            alert("请填写你的真实身份证号！");
            return false;
        }
        var cellphone = (document.getElementById("cellphone")).value;
        if (cellphone == null || cellphone == undefined || cellphone.trim().length == 0){
            alert("请填写你的手机号码！");
            return false;
        }
        var email = (document.getElementById("email")).value;
        if (email == null || email == undefined || email.trim().length == 0){
            alert("请填写常用邮箱！");
            return false;
        }
    }*/
</script>
<body>
<div align="center" style="border: 1px;border-color: #000000;border-style: solid;width: 60%">
    <h3>试卷信息</h3>
    <form action="/papers/newpaper" method="post">
        试卷名称：&nbsp;<input type="text" name="paperName" id="paperName"/><br /><br />
        单选题数：&nbsp;<input type="text" name="singleNum" id="singleNum" value="0"/><br /><br />
        多选题数：&nbsp;<input type="text" name="multiNum" id="multiNum" value="0"/><br /><br />
        问答题数：&nbsp;<input type="text" name="shortAnswerQuestionNum"  id="shortAnswerQuestionNum" value="0"/><br /><br />
        有效日期：&nbsp;<input type="datetime-local" name="validDeadline" value="" id="validDeadline"/> <br /><br />
        <#--onclick="validate()"-->
        <input type="submit" value="提交" />&nbsp;&nbsp;
        <input type="reset" value="清空" />
    </form>
    <br />
    <h3><#if errorInfo??>${errorInfo}</#if></h3><br />
    <a href="/users/welcome">返回首页</a>
</div>
</body>
</html>