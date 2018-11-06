/*登录验证*/
function validate_login() {
    /*先做简单验证*/
    var username = $("#emailIpt").val();
    if (!username){
        alert("请输入正确的邮箱或手机号！");
        return false;
    }
    var password = $("#passwordIpt").val();
    if (!password){
        alert("请输入登录密码！");
        return false;
    }
}