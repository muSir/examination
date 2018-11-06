<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>在线题库-用户登录</title>
    <link rel="stylesheet" type="text/css" href="/css/login.css"/>
    <script type="text/javascript" src="/js/common/jquery-1.8.0.min.js"></script>
    <script src="/js/login.js"></script>
</head>
<body>

        <div class="main">
            <form action="/login" method="post">
                                <fieldset>
                                    <#--<legend>用户登录</legend>-->
                                    <p >
                                        <label for="emailIpt" class="control-label">邮箱/手机&nbsp;&nbsp;</label>
                                        <input type="text" id="emailIpt" name="username" placeholder="请输入邮箱/手机号"/>
                                        <a href="/register" style="margin-left: 10px">去注册</a>
                                    </p>
                                    <p >
                                        <label for="passwordIpt" class="control-label">密码&nbsp;&nbsp;</label>
                                        <input type="password" id="passwordIpt" name="password" placeholder="请输入密码"/>
                                        <#--验证登录信息是否正确提示-->
                                        <#if login?? && login=true><label style="color: red">账号或密码错误</label></#if>
                                    </p>
                                    <#if errorInfo??><p><label class="error-label">${errorInfo}</label></p></#if>
                                    <p class="operate-pwd-container">
                                        <input type="checkbox"  id="hidden-input" class="old-check" checked=checked><label for="hidden-input">记住密码</label>
                                        <a href="#" class="forget-pwd" >忘记密码？</a>
                                    </p>
                                    <p>
                                        <#--<a class="btn btn-primary btn-block" href="javascript:void(0);" id="loginBtn"></a>-->
                                            <input type="submit" class="btnSubmit" value="立即登录" onclick="validate_login()"/>
                                    </p>
                                </fieldset>
                            </form>
        </div>
</body>
</html>