<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>试题</title>
    <link rel="stylesheet" type="text/css" href="/css/common/layout.css">
    <link rel="stylesheet" type="text/css" href="/css/question.css">
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <#--文件上传插件css-->
    <link href="https://cdn.bootcss.com/bootstrap-fileinput/4.4.6/css/fileinput.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-fileinput/4.4.6/css/fileinput-rtl.min.css" rel="stylesheet">
    <script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <script src="https://cdn.bootcss.com/bootstrap-fileinput/4.4.6/js/fileinput.min.js"></script>
    <#--对应中文  必须放在fileinput.js后面，否则不生效-->
    <script src="https://cdn.bootcss.com/bootstrap-fileinput/4.4.6/js/locales/zh.min.js"></script>
    <#--https://cdn.bootcss.com/bootstrap-fileinput/4.3.5/js/locales/zh.min.js-->
    <script src="/js/question.js"></script>
</head>
<body>
    <!--遮罩层-->
    <div class="bgPop"></div>
    <!--上传文件弹出框-->
    <div class="pop" id="upload">
        <div class="pop-top">
            <span class="window-title">选择文件</span>
            <button class="btn btn-default" id="closeBtn" onclick="closeUploadDialog()">
                <span class="glyphicon glyphicon-remove"></span>
            </button>
        </div>
        <form role="form" action="/questions/question/new/templates" method="post" enctype="multipart/form-data" onsubmit="return checkFile()">
            <div class="pop-content">
                <div class="container-fluid">
                    <#--class="file"会导致中文不生效-->
                    <input id="uploadfile" name="uploadfile" type="file" class="file-loading">
                    <#-- fileinput初始化 -->
                        <script type="text/javascript">
                            initFileInput();
                            /*$("#uploadfile").on("fileuploaded",fileUploadSuccess());*/
                        </script>
                </div>
            </div>
            <div class="pop-foot">
                <input type="submit" class="btn btn-primary" style="margin-right: 15px;" value="提  交">
            </div>
        </form>
    </div>
    <!-- 下载文件弹出框 -->
    <div class="pop" id="download" style="height: 300px">
        <div class="pop-top">
            <span class="window-title">下载模板</span>
            <button class="btn btn-default" id="closeBtn" onclick="closeDownloadDialog()">
                <span class="glyphicon glyphicon-remove"></span>
            </button>
        </div>
        <form role="form" action="/questions/question/new/templates" method="get" onsubmit="return downloadCheck()">
            <div class="dialog-download-container">
                    <div class="checkbox">
                        <label class="download-file-label"><input type="checkbox" value="xml" name="template" id="xml">xml模板</label>
                    </div>
                    <div class="checkbox">
                        <label class="download-file-label"><input type="checkbox" value="excel" name="template" id="excel">excel模板</label>
                    </div>
                </div>
            <div class="pop-foot">
                <input type="submit" class="btn btn-primary" style="margin-right: 15px;" value="下 载">
            </div>
        </form>
    </div>

    <div>
        <#include "../../common/top.ftl">
        <#include "./navigation.ftl">
        <div class="content">
            <div class="header">
                <h1 class="page-title">新增题目</h1>
                <ul class="breadcrumb" style="margin-top: 10px">
                    <li><a href="/users/admin/first">首页</a> </li>
                    <li><a href="/questions/list/1/pages">题目列表</a> </li>
                    <li class="active">新增</li>
                </ul>
            </div>
            <div class="row" style="text-align: center;width: 800px;margin-left: 240px">
                <button class="btn btn-default" onclick="openDownloadDialog()">
                    <span class="glyphicon glyphicon-download"></span>
                    <span style="margin-left: 3px"><i class="fa fa-plus"></i>下载模板</span>
                </button>
                <button class="btn btn-default" style="margin-left: 15px" onclick="openUploadDialog()">
                    <span class="glyphicon glyphicon-upload"></span>
                    <span style="margin-left: 3px"><i class="fa fa-plus"></i>上传文件</span>
                </button>
            </div>
            <#if fileUploadResult??>
                <div style="text-align: center;width: 800px;margin-left: 240px;margin-top: 10px;" id="tipDiv">
                    <button class="btn btn-default" style="border: none">
                        <span class="glyphicon glyphicon-exclamation-sign"></span>
                        <span style="color: red"><i class="fa fa-plus"></i>${fileUploadResult}</span>
                    </button>
                </div>
            </#if>
            <div class="panel panel-default" style="height: 700px;width: 850px;padding: 0;margin-left: 240px;margin-top: 20px;border-bottom-style: none">
                <div class="panel-heading" style="width: 100%;padding: 0;height: 40px;text-align: center;margin: 0 auto">
                    <h2 class="panel-title" style="text-align: center;height: 40px;"><span style="line-height: 40px;text-align: center">题目内容</span></h2>
                </div>
                <div class="panel-body" style="height: auto">
                    <form role="form" action="/questions/question/new" method="post" onsubmit="return submitCheck()">
                            <div class="form-group" style="margin-top: 15px">
                                <label for="type" class="col-sm-2 control-label" style="text-align: center">类  型</label>
                                <div style="margin-left: 10px">
                                    <label class="radio-inline">
                                        <input type="radio" name="questionType" id="single" value="single" checked>单选题
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="questionType" id="multi"  value="multi">多选题
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="questionType" id="shortAnswer" value="shortAnswer">问答题
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="title" class="col-sm-2 control-label" id="title-label">题  干</label>
                                <div class="col-sm-10" style="margin-top: 10px">
                                    <textarea class="form-control" rows="3" placeholder="请输入题目内容" id="title" name="questionTitle"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="options" class="col-sm-2 control-label" id="title-label">选  项</label>
                                <div class="col-sm-10" style="margin-top: 10px">
                                    <textarea class="form-control" rows="5" placeholder="请输入选择题选项(选项以26个小写的英文字母加英文逗号.开始，以英文分号;加两个空格结束，如：A.选项1;  B.选项2)"
                                              id="options" name="questionOptions"></textarea>
                                </div>
                                <#--<div class="col-sm-10" style="margin-top: 10px">
                                    A <input type="text" class="form-control" id="questionOption" name="option" placeholder="选项内容(多个选项请点击添加按钮)">
                                    <div id="options">

                                    </div>
                                    <input type="button" class="btn btn-primary" style="margin-top: 5px" value="添  加" onclick="addOption()">-->
                                <#--<input type="button" class="btn btn-primary" value="移  除">-->
                            </div>
                            <div class="form-group">
                                <label for="answer" class="col-sm-2 control-label" id="title-label">答  案</label>
                                <div class="col-sm-10" style="margin-top: 10px">
                                    <textarea class="form-control" rows="3" name="questionAnswer" placeholder="请输入题目答案(选择题请直接输入正确选项即可,忽略大小写)" id="answer"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="keywords" class="col-sm-2 control-label" id="title-label">答案关键词</label>
                                <div class="col-sm-10" style="margin-top: 10px">
                                    <input type="text" class="form-control" id="keywords" name="questionKeywords" placeholder="请输入题目答案关键词(关键词之间以空格隔开)">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="ref" class="col-sm-2 control-label" id="title-label">答案解析</label>
                                <div class="col-sm-10" style="margin-top: 10px">
                                    <textarea class="form-control" rows="3" name="questionReference" placeholder="请输入题目答案解析" id="ref"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="value" class="col-sm-2 control-label" id="title-label">分  值</label>
                                <div class="col-sm-10" style="margin-top: 10px">
                                    <input type="text" class="form-control" id="value" name="questionValue" placeholder="请输入题目分值(大于0小于100的整数)">
                                </div>
                            </div>
                        <#--<form action="/questions/question/new/templates" method="post" enctype="multipart/form-data">-->
                            <div class="form-group">
                                <label for="image" class="col-sm-2 control-label" id="title-label">附  图</label>
                                <div class="col-sm-10" style="margin-top: 10px">
                                    <input id="questionImage" name="questionImage" type="file" class="fileloading">
                                    <#--<script type="text/javascript">

                                    </script>-->
                                </div>
                            </div>
                        <#--</form>-->

                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-10" style="text-align: right;margin-right: 20px;margin-top: 15px">
                                    <button type="submit" class="btn btn-primary">保 存</button>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-10" style="text-align: center;margin-top: 15px;">
                                    <label id="toolTip" style="font-family: 微软雅黑;font-size: 14pt;color: red"></label>
                                </div>
                            </div>
                        </form>
                </div>
            </div>
        </div>
    </div>
</body>
<script type="text/javascript">
    initImageInput();
</script>
</html>