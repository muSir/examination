/**
 * 新增题目按钮
 */
function add() {
    alert("123");
    var href = "";
}

/**
 * 单个删除
 * @param questionId
 */
function singleDelete(questionId) {
    if (!questionId)
    {
        alert("错误的数据!");
    }
    $.ajax({
        url:"/questions/" + questionId,
        type:"Post",
        dataType:"text/json",//返回的数据格式
        timeout:10000,
        success:function(data){
            alert("删除成功");
            //从界面上删除

        },
        error:function(result){
            alert("删除失败");
        }
    });
}

/**
 * Go：跳转到第N页
 */
function goPages() {
    var currentPage = $("#currentPage").text();
    //var totalPage =
    var goPage = $("#goPage").val().trim();
    if (!goPage || goPage <= 0){
        alert("请输入需要跳转到的正确页码");
        return false;
    }
    $.ajax({
        url: "/questions/list/" + goPage + "/pages",
        type: "get",
        success: function(data){
            var resultModel = JSON.stringify(data);
            var result = eval('(' + resultModel + ')');
            alert(result);
            if (result.code == 100){
                alert(result.message);
                var page = result.data;
                var questions = page.data;
                var tbody = $("#tbody-questions");
                tbody.html("");
                if (questions == null || questions.size() == 0){
                    return false;
                }

                //遍历集合写入数据
                for (var i = 0; i < questions.size(); i++){
                    var tr = "<tr style='height: 50px'></tr>>";
                    //编号列
                    var question = questions.get(i)
                    var tdIndex = "<td style='text-align: center'>" +
                        "<input type='checkbox' name='id' value='" +(questions.questionId)+ "'/>" +
                        "<span style='margin-left: 5px'>" + (i+1) + "</span></td>";
                    //题干列
                    var tdTitle = "<td>" + question.title + "</td>";
                    // 选项列
                    var options = question.dtoOptions;
                    var tdOptions = "<td></td>";
                    if (options != null){
                        //选择题才有选项
                        for (var j = 0;j<options.size();j++){
                            tdOptions.append("<p>"+ options.get(j) +"</p>");
                        }
                    }
                    //答案列
                    var tdAnswer = "<td>" + question.answer + "</td>";
                    //问答题关键词列
                    var keywords = question.dtoKeywords;
                    var tdKeywords = "<td></td>";
                    if (keywords != null){
                        //选择题才有选项
                        for (var j = 0;j<keywords.size();j++){
                            tdKeywords.append("<p>"+ keywords.get(j) +"</p>");
                        }
                    }
                    //题目分值列
                    var tdValue = "<td style='text-align: center'>" + question.value + "</td>";
                    //题目类型列
                    var tdType = "<td style='text-align: center'>" + question.dtoType + "</td>";
                    //题目解析列  <td align="left"><#if question.reference??>${question.reference}</#if></td>
                    var tdRef = "<td></td>";
                    if (question.reference != null){
                        tdRef.append(question.reference);
                    }
                    //操作列
                    var tdOperate = "<td style='text-align: center'></td>";
                    var hrefEdit = "/questions/" + question.questionId;
                    var hrefDelete = "/questions/${question.questionId}/delete"
                    var editOperate = "<a href='#'>编 辑</a>&nbsp;";
                    var deleteOperate = "<a href='#'>删 除</a>";
                    tdOperate.append(editOperate);
                    tdOperate.append(deleteOperate);

                    tr.append(tdIndex);
                    tr.append(tdTitle);
                    tr.append(tdOptions);
                    tr.append(tdAnswer);
                    tr.append(tdKeywords);
                    tr.append(tdValue);
                    tr.append(tdType);
                    tr.append(tdRef);
                    tr.append(tdOperate);

                    tbody.append(tr);
                }
                //$("#tbody-questions").replaceWith(tbody);
                $("#currentPage").html(goPage+"");
            }
        }
    });
}

/**
 * 跳转到试题列表首页
 */
function goFirst(pageNoStr) {
    if (pageNoStr == "1"){
        alert("当前已经是第一页啦");
    }else {
        var href = "/questions/list/1/pages";
        window.location.href=href;
    }
}

/**
 * 跳转到指定的页数
 * @returns {boolean}
 */
function goPage(totalPageStr) {
    var totalPage = parseInt(totalPageStr);
    var goPage = $("#goPage").val().trim();
    if (!goPage || goPage < 1){
        alert("页码不能为空或者小于1");
    }else if (goPage > totalPage){
        alert("页码不能超过最大页码");
    }else {
        var href = "/questions/list/"+goPage+"/pages";
        window.location.href=href;
    }
}

/*
  *跳转到上一页
 */
function goPre(pageNoStr) {
    var pageNo = parseInt(pageNoStr);
    if (pageNo == 1){
        alert("当前已经是第一页啦");
    }else {
        var href = "/questions/list/"+(pageNo-1)+"/pages";
        window.location.href=href;
    }
}

/*
  *跳转到下一页
 */
function goNext(pageNoStr,totalPageStr) {
    var pageNo = parseInt(pageNoStr);
    var totalPage = parseInt(totalPageStr);
    if (pageNo == totalPage){
        alert("当前已经是最后一页啦");
    }else {
        var href = "/questions/list/"+(pageNo+1)+"/pages";
        window.location.href=href;
    }
}

/**
 * 跳转到末页
 */
function goLast(pageNoStr,totalPageStr) {
    var pageNo = parseInt(pageNoStr);
    var totalPage = parseInt(totalPageStr);
    if (pageNo == totalPage){
        alert("当前已经是最后一页啦");
    }else {
        var href = "/questions/list/"+totalPage+"/pages";
        window.location.href=href;
    }
}

/**
 * 批量删除按钮点击事件
 */
function bulkDelete() {
    //被选中条目
    var str = "";
    $("td input[name = 'id']:checked").each(function () {
        str += $(this).val()+",";
    });
    if (str == ""){
        alert("请选择需要删除的题目");
        return false;
    }
    var ids = str.split(",");
    if(confirm("确定要删除选中的数据吗？")) {
        $.ajax({
            url: "/questions/question?ids=" + str,
            type: "delete",
            success: function(data){
                var resultModel = JSON.stringify(data);
                var json = JSON.parse(resultModel);
                if (json.code == 100){
                    //服务端删除成功
                    for (var index in ids){
                        var tr = $("#"+ids[index]);
                        tr.remove();
                    }
                }else if (json.code == 1){
                    alert(json.message);
                }else {
                    alert("未知错误");
                }
            }
        });
    }
}

/**
 * 单条删除
 * @param questionId  主键
 */
function singleDelete(questionId) {
    if (!questionId){
        return false;
    }
    if (confirm("确定删除此题目吗？")){
        $.ajax({
            url: "/questions/question/"+ questionId,
            type: "delete",
            success: function(data){
                var resultModel = JSON.stringify(data);
                var json = JSON.parse(resultModel);
                if (json.code == 100){
                    var tr = $("#"+questionId);
                    tr.remove();
                }else if (json.code == 1){
                    alert(json.message);
                }else {
                    alert("未知错误");
                }
            }
        });
    }
}

/**
 * 复选框的全选/取消全选
 */
function checkAll() {
    var allChecked = $("#allCheck");
    var singleChecked = $("td input:checkbox");
    if(allChecked.is(':checked')){
        //singleChecked.attr("checked","checked");
        singleChecked.prop("checked",true);
    }else {
        singleChecked.prop("checked",false);
    }
}