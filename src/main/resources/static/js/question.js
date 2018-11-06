//点击上传文件按钮，弹出上传文件对话框
function openUploadDialog() {
    $('.bgPop,#upload').show();
    tipDivHide();
}

function openDownloadDialog() {
    $('.bgPop,#download').show();
    tipDivHide();
}

/**
 * 隐藏上传文件弹出框以及遮罩层
 */
function closeUploadDialog() {
    $('.bgPop,#upload').hide();
    tipDivHide();
}

/**
 * 关闭下载文件对话框及遮罩层
 */
function closeDownloadDialog() {
    $('.bgPop,#download').hide();
    tipDivHide();
}

//fileinput参数初始化
function initFileInput() {
    $("#uploadfile").fileinput({
        language: 'zh', //设置语言
        uploadUrl: "/questions/question/new/template", //上传的地址(访问接口地址)
        allowedFileExtensions: ['xml', 'xls', 'xlsx'],//接收的文件后缀
        uploadAsync: true, //默认异步上传
        showUpload: false, //是否显示上传按钮
        showCancel:false,//是否显示取消按钮
        showRemove : true, //显示移除按钮
        showPreview : true, //是否显示预览
        showCaption: false,//是否显示标题
        browseClass: "btn btn-primary", //按钮样式
        dropZoneEnabled: true,//是否显示拖拽区域
        //minImageWidth: 50, //图片的最小宽度
        //minImageHeight: 50,//图片的最小高度
        //maxImageWidth: 1000,//图片的最大宽度
        //maxImageHeight: 1000,//图片的最大高度
        //maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
        enctype: 'multipart/form-data',
        validateInitialCount:true,
        previewFileIcon: "<i class='glyphicon glyphicon-file'></i>",
        /*msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",*/
    })
}

function initImageInput() {
    $("#questionImage").fileinput({
        language: 'zh', //设置语言
        //uploadUrl: "/questions/question/new/template", //上传的地址(访问接口地址)
        allowedFileExtensions: ['jpg', 'gif', 'png','bmp'],//接收的文件后缀
        uploadAsync: false, //默认异步上传
        showUpload: false, //是否显示上传按钮
        showCancel:false,//是否显示取消按钮
        showRemove : true, //显示移除按钮
        showPreview : true, //是否显示预览
        showCaption: false,//是否显示标题
        browseClass: "btn btn-primary", //按钮样式
        dropZoneEnabled: true,//是否显示拖拽区域
        //minImageWidth: 50, //图片的最小宽度
        //minImageHeight: 50,//图片的最小高度
        //maxImageWidth: 1000,//图片的最大宽度
        //maxImageHeight: 1000,//图片的最大高度
        maxFileSize: 6120,//单位为kb，如果为0表示不限制文件大小
        maxFileCount:10,//允许同时上传文件的最大个数
        enctype: 'multipart/form-data',
        validateInitialCount:true,
        /*previewFileIcon: "<i class='glyphicon glyphicon-file'></i>",*/
        msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
    })
}

//文件上传成功之后的回调
function fileUploadSuccess(event,data,previewedId,index) {
    alert("文件上传成功，服务器处理完成...");
}

/**
 * 点击提交上传文件前的校验
 */
function checkFile() {
    var file = $("#uploadfile").val();
    if (!file){
        alert("请选择要上传的文件");
        return false;
    }
}

/**
 * 文件下载前检测是否勾选需要下载的模板
 */
function downloadCheck() {
    var xml = $("#xml");
    var excel = $("#excel");
    if (!xml.is(':checked') && !excel.is(':checked')){
        alert("请选择至少一个模板下载");
        return false;
    }
}

//设置三秒自动消失
function tipDivShow() {
    setTimeout(tipDivHide(),3000);
}

function tipDivHide() {
    /*$("#tipDiv").style.display="none";*/
    $("#tipDiv").hide();
}

/**
 * 添加选项，添加选项栏
 */
function addOption(index) {
    var div = "<div></div>";
    var htmlInput = "<input type='text'  id='option' class='option-input' name='questionOption' placeholder='新增选项'>";
    var htmlRemoveBtn = "<input type='button' class='btn btn-default' value='移  除' style='margin-top: 5px;margin-left: 10px' >";
    $("#options").append(htmlInput+htmlRemoveBtn);
}

/**
 * 移除选项栏
 */
function removeOption() {

}

function submitCheck() {
    var toolTip = $("#toolTip");
    var type=$('input:radio[name="questionType"]:checked').val();
    var title = $("#title").val();
    var options = $("#options").value;
    var answer = $("#answer").val();
    var keywords = $("#keywords").val();
    /*var ref = $("#ref").val();*/
    var value = $("#value").val();
    if (!title){
        toolTip.html("题目内容不能为空！");
        return false;
    }
    if (!answer){
        toolTip.html("题目答案不能为空！");
        return false;
    }
    if (!value){
        toolTip.html("题目分值不能为空！");
        return false;
    }
    var integerValue = value.parseInt();
    if (!(/(^[0-9]*[1-9][0-9]*$)/.test(value)) || integerValue <= 0 || integerValue > 100){
        alert("题目分值必须是0-100之间的整数");
        return false;
    }
    if ((type == "single" || type == "multi") && !options){
        toolTip.html("选择题选项不能为空！");
        return false;
    }
    if (type == "shortAnswer" && !keywords){
        toolTip.html("问答题关键词不能为空！");
        return false;
    }

    return true;
}