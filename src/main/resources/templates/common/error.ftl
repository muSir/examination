<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title><#if errorInfo??>梦想网-${errorInfo}<#else >出错啦</#if></title>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body style="text-align: center">
    <#include "../common/top.ftl">
    <div style="margin-top: 40px">
        <div>
            <div><#if errorInfo??>${errorInfo}<#else >访问出错啦</#if></div>
        </div>
        <div style="margin-top: 20px">
            <img src="/images/v1/error-v1.png" height="400" width="422" />
        </div>
    </div>
</body>
</html>