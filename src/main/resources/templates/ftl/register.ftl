<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>在线题库-用户注册</title>
    <link rel="stylesheet" type="text/css" href="/css/register.css"/>
    <script type="text/javascript" src="/js/common/jquery-1.8.0.min.js"></script>
    <script src="/js/register.js"></script>
</head>
<body>

        <div class="main">
            <form action="/register" method="post" onsubmit="return validate_register()">
                <fieldset>
                    <#--<legend>用户登录</legend>-->
                        <p >
                            <#--发送验证码暂时不做-->
                            <input type="text" id="phoneNumIpt" name="phoneNum" placeholder="手机号" value="<#if phoneNum??>${phoneNum}</#if>"
                                   onblur="phoneValidate()" onfocus="tooltipReset(1)"/>
                            <label class="tooltips-label" id="phoneNumLabel"></label>
                        </p>
                        <p >
                            <input type="text" id="emailIpt" name="email" placeholder="邮箱" value="<#if email??>${email}</#if>"
                                   onfocus="tooltipReset(2)" onblur="emailValidate()"/>
                            <label class="tooltips-label" id="emailLabel"></label>
                        </p>
                        <p >
                            <input type="password" id="passwordIpt" name="password" placeholder="密码" value="<#if password??>${password}</#if>"
                                   onfocus="tooltipReset(3)" onblur="passwordValidate()"/>
                            <label class="tooltips-label" id="passwordLabel"></label>
                        </p>
                        <p >
                            <input type="password" id="passwordIptRepeatIpt" name="passwordIptRepeat" placeholder="重复密码"
                                    onfocus="tooltipReset(4)" onblur="passwordRepValidate()"/>
                            <label class="tooltips-label" id="passwordRepeatLabel"></label>
                        </p>
                        <p >
                            <input type="text" id="idCardIpt" name="idCard" placeholder="身份证号码" value="<#if idCard??>${idCard}</#if>"
                                    onfocus="tooltipReset(5)" onblur="iDCardValidate()"/>
                            <label class="tooltips-label" id="idCardLabel"></label>
                        </p>
                        <p >
                            <input type="text" id="realNameIpt" name="realName" placeholder="真实姓名" value="<#if realName??>${realName}</#if>"
                                    onblur="realNameValidate()" onfocus="tooltipReset(6)"/>
                            <label class="tooltips-label" id="realNameLabel"></label>
                        </p>
                        <p ><#--昵称可以不填，默认以手机号码显示  后面可以修改-->
                            <input type="text" id="nickNameIpt" name="nickName" placeholder="昵  称" value="<#if nickName??>${nickName}</#if>"/>
                            <label class="tooltips-label" id="nickNameLabel"></label>
                        </p>
                        <#if errorInfo??><p><label class="error-label">${errorInfo}</label></p></#if>
                        <p>
                            <input type="submit" class="btnSubmit" value="注 册"/>
                            <input type="reset" class="btnReset" value="清 空" onclick="tooltipReset(0)"/>
                        </p>
                        <p >
                            <label class="tooltips-label" id="submitLabel"></label>
                        </p>
                </fieldset>
            </form>
        </div>
</body>
</html>