<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>管理员首页</title>
    <link rel="stylesheet" type="text/css" href="/css/common/layout.css">
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <style>
        .carousel{
            height: 650px;
            background-color: cornflowerblue;
        }
        .carousel .item{
            height: 650px;
            background-color: cornflowerblue;
        }
        .carousel img{
            width: 100%;
        }
    </style>

</head>

<body>
    <#include "../common/top.ftl">
    <#include "./common/navigation.ftl">

    <div style="margin-left: 240px">
        <h1>
            <label style="color: red">欢迎您，管理员</label>
        </h1>
        <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
            <!--轮播组件下方的控制器-->
            <ol class="carousel-indicators">
                <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                <li data-target="#carousel-example-generic" data-slide-to="1"></li>
                <li data-target="#carousel-example-generic" data-slide-to="2"></li>
            </ol>
            <!-- 轮播组件的内容 -->
            <div class="carousel-inner" role="listbox">
                <div class="item active">
                    <img src="/images/v1/challenger.jpg" alt="1 slide">
                    <div class="carousel-caption">
                        <p>道奇挑战者</p>
                    </div>
                </div>
                <div class="item">
                    <img src="/images/v1/falali.jpg" alt="2 slide">
                    <div class="carousel-caption">
                        <p>法拉利恩佐</p>
                    </div>
                </div>
                <div class="item">
                    <img src="/images/v1/pagani.jpg" alt="3 slide">
                    <div class="carousel-caption">
                        <p>帕加尼风神</p>
                    </div>
                </div>
            </div>
            <!-- 轮播组件左右切换按钮 -->
            <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
                <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                <span class="sr-only">Previous</span>
            </a>
            <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
                <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                <span class="sr-only">Next</span>
            </a>
        </div>
    </div>


</body>
</html>