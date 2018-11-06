<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>文件上传下载</title>
    <script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
    <script src="/js/file.js"></script>
</head>
<body>
    <h1 th:inlines="text">文件上传</h1>
    <form id="fileUploadForm" action="/files/upload" method="post" enctype="multipart/form-data">
        <p>选择文件<input type="file" name="file" id="file"/></p>
        <p><input type="submit" value="上传" /></p>
        <#--<p> <input type="search"/></p>-->
    </form>
    <h1>${Request["msg"]}</h1>
    <h2>${Request.msg}</h2>
    <#--<input type="submit" onclick="click(${Request.msg})" value="save"/>-->
    <form action="/users/top" method="get">
        <input type="submit"  value="jump"/>
    </form>
</body>
</html>