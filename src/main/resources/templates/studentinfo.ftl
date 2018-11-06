<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>考生登录</title>
</head>
<script language="JavaScript" type="text/javascript">
    <!-- 简单的表单验证 -->
    function validate() {
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
    }
</script>
<body>
    <div align="center">
        <div  style="border: 1px;border-color: #000000;border-style: solid;width: 500px;height: 200px">
            <h3>考生信息</h3>
            <form action="/users/Examinee" method="post">
                真实姓名：<input type="text" name="username" id="username"/><br />
                身份证号：<input type="text" name="idCard" id="idCard"/><br />
                手机号码：<input type="text" name="cellphone" id="cellphone"/><br />
                邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱：
                <input type="text" name="email"  id="email"/><br /><br />
                <input type="submit" value="提交" onclick="validate()"/>&nbsp;&nbsp;
                <input type="reset" value="清空" />
            </form>
            <br />
            <h3><#if errorInfo??>${errorInfo}</#if></h3>
            <a href="/users/welcome">返回首页</a>
        </div>
    </div>
</body>
</html>