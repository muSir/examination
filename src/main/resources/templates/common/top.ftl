<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="/css/top.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/layout.css"/>
    <title>导航栏</title>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <#--<script src="/js/top.js"></script>-->
    <style>
        /*如果导航条样式是navbar-fixed-top(固定在顶部)  则需要内补 body{padding-top: 70px}*/
        #logo{
            font-size: 18pt;
            font-family: 微软雅黑;
            color: white;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-default navbar-inverse navbar-static-top" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <span class="navbar-brand" id="logo">梦想在哪里？</span>
            </div>

            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="margin-left: 50px">
                <ul class="nav navbar-nav">
                    <!--class="active":被选中时-->
                    <li>
                        <a href="/dream<#if loginUser??>/${loginUser.userId}</#if>">
                            <span class="glyphicon glyphicon-home"></span>
                            <span style="font-family: 微软雅黑;font-size: 14pt;margin-left: 5px">首   页</span>
                        </a>
                    </li>
                    <li>
                        <#--data-target 弹出框的ID #id-->
                        <a data-toggle="modal" data-target="#reservation">
                            <span style="font-family: 微软雅黑;font-size: 14pt;margin-left: 15px">预   留</span>
                        </a>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                            <span style="font-family: 微软雅黑;font-size: 14pt;margin-left: 15px">题   库</span><span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li class="dropdown-li"><a href="#">Java</a></li>
                            <li class="dropdown-li"><a href="#">C#</a></li>
                            <li class="dropdown-li"><a href="#">C/C++</a></li>
                            <li role="separator" class="divider" class="dropdown-li"></li>
                            <li class="dropdown-li"><a href="#">语文真题</a></li>
                            <li role="separator" class="divider" class="dropdown-li"></li>
                            <li class="dropdown-li"><a href="#" >数学真题</a></li>
                        </ul>
                    </li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <form class="navbar-form" action="#" method="get">
                            <div class="form-group">
                                <input type="text" class="form-control" placeholder="搜索">
                            </div>
                            <button type="submit" class="btn btn-default">
                                <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                            </button>
                        </form>
                    </li>
                    <li><a href="#" data-toggle="modal" data-target="#message">
                        <span class="glyphicon glyphicon-envelope" style="margin-left: 2px"></span>
                        <span style="font-family: 微软雅黑;font-size: 12pt;">消息(0条)</span>
                    </a></li>
                    <#if isLogin?? && isLogin=true>
                        <li class="dropdown" id="login">
                            <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                                <span class="glyphicon glyphicon-user"></span>
                                <span style="font-family: 微软雅黑;font-size: 12pt;margin-left: 5px"><#if loginUser??>${loginUser.username}</#if></span>
                                <span class="caret"></span>
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="login">
                                <li class="dropdown-li"><a href="#">个人主页</a></li>
                                <li class="dropdown-li"><a href="#">账号设置</a></li>
                                <li class="dropdown-li"><a href="/logout">退出登录</a></li>
                            </ul>
                        </li>
                    <#else >
                        <li><a href="/login">
                            <span class="glyphicon glyphicon-log-in"></span>
                            <span  style="font-family: 微软雅黑;font-size: 12pt;margin-left: 5px">登    录<span>
                        </li>
                    </#if>
                </ul>
            </div>
        </div>
    </nav>
    <!--预留弹出框-->
    <div class="modal fade" id="reservation" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">预留标题</h4>
                </div>
                <div class="modal-body">
                    预留内容
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary">确定</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>

    <!--  消息弹出框 -->
    <div class="modal fade" id="message" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">消息</h4>
                </div>
                <div class="modal-body">
                    推送消息的内容
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary">确定</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>

</body>
</html>