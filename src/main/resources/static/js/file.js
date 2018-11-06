
function click(name) {
    alert(name);
    $.ajax({
        url :"/users" ,
        type : "GET",
        timeout : 1000,
        success:function (data) {
            alert("跳转成功");
        },
        error:function (data) {
            alert("跳转失败");
        },
    });
}

function save() {
    var file = $("#file").val();
    if (!file){
        alert("请选择模板上传");
    }
    $.ajax({
        url: "/verifications/idCard?idCard=" + idCard,
        type: "post",
        data:$( '#fileUploadForm').serialize(),
        success: function(data){
            alert("124");
            var resultModel = JSON.stringify(data);
            var result = eval('(' + resultModel + ')');
            alert(result.message);
        }
    });
}