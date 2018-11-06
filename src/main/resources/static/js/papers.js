/**
 * 一键查询试卷
 */
function query() {
    var paperName = $("#paperName").val();
    var singleNum = $("#singleNum").val();
    var multiNum = $("#multiNum").val();
    var shortNum = $("#shortNum").val();
    var deadline = $("#deadline").val();
    var createTime = $("#createTime").val();
    $.ajax({
        url : "/papers/search?paperName=" + paperName + "&singleNum=" + singleNum + "&multiNum" + multiNum
            + "&shortNum=" + shortNum + "&deadline=" + deadline + "&createTime=" + createTime,
        type : "GET",
        dataType : "json",
        timeout : 10000,
        success:function (data) {
            //将json类型字符串转换为json对象
            //var json = eval("("+data+")");
            var paper = data.data;//onject对象，怎么转换成要使用的值
            $.each(list,function (i,paper) {
                
            })
            //数据
            return json.data;
        },
        error:function (data) {
            var json = eval("("+data+")");
            alert(json.message);
        },
    });
}