/**
 * 将label的值置为空
 */
function tooltipReset(i) {
    switch (i){
        case 1:
            $("#phoneNumLabel").html("");
            break;
        case 2:
            $("#emailLabel").html("");
            break;
        case 3:
            $("#passwordLabel").html("");
            break;
        case 4:
            $("#passwordRepeatLabel").html("");
            break;
        case 5:
            $("#idCardLabel").html("");
            break;
        case 6:
            $("#realNameLabel").html("");
            break;
        default:
            //全部清空
            $("#phoneNumLabel").html("");
            $("#emailLabel").html("");
            $("#passwordLabel").html("");
            $("#passwordRepeatLabel").html("");
            $("#idCardLabel").html("");
            $("#realNameLabel").html("");
            $("#submitLabel").html("");
    }
}
/**
 * 手机号输入框失去焦点时前后端验证
 */
function phoneValidate() {
    var phoneNum = $("#phoneNumIpt").val().trim();
    if(!phoneNum){
        $("#phoneNumLabel").html("手机号码不能为空");
        return false;
    }else {
        tooltipReset(1);
        //交到后端验证
        $.ajax({
            url: "/verifications/phone?phoneNum=" + phoneNum,
            type: "get",
            success: function(data){
                //解析成字符串
                var resultModel = JSON.stringify(data);
                //包装成一个js的json对象
                var result = eval('(' + resultModel + ')');
                //将验证结果显示到tooltip
                $("#phoneNumLabel").html(result.message);
            }
        });
        return true;
    }
}

/**
 * 邮箱输入框失去焦点
 */
function emailValidate() {
    var email = $("#emailIpt").val().trim();
    if(!email){
        $("#emailLabel").html("邮箱不能为空");
        return false;
    } else {
        tooltipReset(2);
        $.ajax({
            url: "/verifications/email?email=" + email,
            type: "get",
            success: function(data){
                var resultModel = JSON.stringify(data);
                var result = eval('(' + resultModel + ')');
                $("#emailLabel").html(result.message);
            }
        });
        return true;
    }
}

/**
 * 密码输入框失去焦点 一般是检查输入完成
 */
function passwordValidate() {
    var password = $("#passwordIpt").val().trim();
    if(!password){
        $("#passwordLabel").html("密码不能为空");
        return false;
    }else if (password.length < 8){//当密码的长度小于8时
        $("#passwordLabel").html("密码最少为8位数字或字符");
        return false;
    } else {
        tooltipReset(3);
        /*$("#passwordLabel").html("");*/
        return true;
    }
}

/**
 * 密码重复输入框失去焦点 一般是检查输入完成
 */
function passwordRepValidate() {
    var password = $("#passwordIpt").val().trim();
    var passwordRep = $("#passwordIptRepeatIpt").val().trim();
    if(!password){
        $("#passwordRepeatLabel").html("请先输入密码");
        return false;
    }else if(!passwordRep){
        $("#passwordRepeatLabel").html("请重复密码");
        return false;
    }else if (passwordRep != password){
        $("#passwordRepeatLabel").html("两次输入密码不一致");
        return false;
    } else {
        tooltipReset(4);
        /*$("#passwordRepeatLabel").html("");*/
        return true;
    }
}

/**
 * 身份证输入框失去焦点 一般是检查输入完成
 */
function iDCardValidate() {
    var idCard = $("#idCardIpt").val().trim();
    if(!idCard){
        $("#idCardLabel").html("身份证号不能为空");
        return false;
    }else {
        tooltipReset(5);
        $.ajax({
            url: "/verifications/idCard?idCard=" + idCard,
            type: "get",
            success: function(data){
                var resultModel = JSON.stringify(data);
                var result = eval('(' + resultModel + ')');
                $("#idCardLabel").html(result.message);
            }
        });
        return true;
    }
}

/**
 * 真实姓名输入框失去焦点 一般是检查输入完成
 */
function realNameValidate() {
    var realName = $("#realNameIpt").val().trim();
    if(!realName){
        $("#realNameLabel").html("真实姓名不能为空");
        return false;
    }else {
        tooltipReset(6);
       /* $("#realNameLabel").html("");*/
        return true;
    }
}

/**
 * 注册表点击提交按钮时验证
 */
function validate_register() {
    tooltipReset(0);
    var phoneNum = $("#phoneNumIpt").val().trim();
    var email = $("#emailIpt").val().trim();
    var password = $("#passwordIpt").val().trim();
    var passwordRep = $("#passwordIptRepeatIpt").val().trim();
    var idCard = $("#idCardIpt").val().trim();
    var realName = $("#realNameIpt").val().trim();

    if (!phoneNum || !email || !password || !passwordRep || !idCard || !realName){
        $("#submitLabel").html("请将信息补充完整");
        return false;
    }else {
        tooltipReset(0);
        return true;
        /*var nickName = $("#nickNameIpt").val().trim();
        //直接传到后端进行验证
        $.ajax({
            url: "/users/register?phoneNum=" + phoneNum + "&email=" + email + "&password=" + password + "&idCard="
                + idCard + "&realName=" + realName + "&nickName=" + nickName,
            type: "post",
            success: function(data){
                var resultModel = JSON.stringify(data);
                var result = eval('(' + resultModel + ')');

            }
        });*/
    }

}