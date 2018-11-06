<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="/css/common/layout.css"/>
    <link rel="stylesheet" type="text/css" href="/css/navigation.css"/>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<style type="text/css">
    #line-chart {
        height:300px;
        width:800px;
        margin: 0px auto;
        margin-top: 1em;
    }
    .navbar-default .navbar-brand, .navbar-default .navbar-brand:hover {
        color: #fff;
    }
</style>

    <#--<#include "../../common/top.ftl">-->
    <div class="sidebar-nav">
        <ul>
            <li><a href="#" data-target=".dashboard-menu" class="nav-header" data-toggle="collapse">
                <i class="fa fa-fw fa-dashboard"></i> 用户管理<i class="fa fa-collapse"></i></a></li>
            <li><ul class="dashboard-menu nav nav-list collapse in">
                <li><a href="/users/admin"><span class="fa fa-caret-right"></span> 管理员首页</a></li>
                <li><a href="index.html"><span class="fa fa-caret-right"></span> 添加管理员</a></li>
                <li ><a href="users.html"><span class="fa fa-caret-right"></span>用户列表</a></li>
                <li ><a href="user.html"><span class="fa fa-caret-right"></span>权限管理</a></li>
                <li ><a href="calendar.html"><span class="fa fa-caret-right"></span>日   历</a></li>
            </ul></li>
            <li data-popover="true" data-content="Items in this group require a <strong>
            <a href='http://portnine.com/bootstrap-themes/aircraft' target='blank'>premium license</a><strong>." rel="popover" data-placement="right">
                <a href="#" data-target=".premium-menu" class="nav-header collapsed" data-toggle="collapse">
                    <i class="fa fa-fw fa-fighter-jet"></i>题库管理<i class="fa fa-collapse"></i></a></li>
            <li>
                <ul class="premium-menu nav nav-list collapse">
                    <#--题目列表第一页，每页最多10条数据-->
                    <li><a href="/questions/list/1/pages">题目列表</a></span></li>
                </ul>
            </li>

            <li><a href="#" data-target=".accounts-menu" class="nav-header collapsed" data-toggle="collapse"><i class="fa fa-fw fa-briefcase"></i> 试卷管理 <span class="label label-info"></span></a></li>
            <li><ul class="accounts-menu nav nav-list collapse">
                <li ><a href="sign-in.html"><span class="fa fa-caret-right">试卷列表</span></a></li>
                <li ><a href="sign-up.html"><span class="fa fa-caret-right">创建试卷</span></a></li>
                <#--<li ><a href="reset-password.html"><span class="fa fa-caret-right"></span></a></li>-->
            </ul></li>

            <li><a href="#" data-target=".legal-menu" class="nav-header collapsed" data-toggle="collapse"><i class="fa fa-fw fa-legal"></i> Legal<i class="fa fa-collapse"></i></a></li>
            <li><ul class="legal-menu nav nav-list collapse">
                <li ><a href="privacy-policy.html"><span class="fa fa-caret-right"></span> Privacy Policy</a></li>
                <li ><a href="terms-and-conditions.html"><span class="fa fa-caret-right"></span> Terms and Conditions</a></li>
            </ul></li>

            <li><a href="help.html" class="nav-header"><i class="fa fa-fw fa-question-circle"></i>帮助</a></li>
            <li><a href="http://portnine.com/bootstrap-themes/aircraft" class="nav-header" target="blank"><i class="fa fa-fw fa-heart"></i></a></li>
        </ul>
    </div>

    <#--<div class="content">
        <div class="header">
            <div class="stats">
                <p class="stat"><span class="label label-info">5</span> Tickets</p>
                <p class="stat"><span class="label label-success">27</span> Tasks</p>
                <p class="stat"><span class="label label-danger">15</span> Overdue</p>
            </div>

            <h1 class="page-title">Dashboard</h1>
            <ul class="breadcrumb">
                <li><a href="index.html">Home</a> </li>
                <li class="active">Dashboard</li>
            </ul>

        </div>
        <div class="main-content">
            <div class="panel panel-default">
                <a href="#page-stats" class="panel-heading" data-toggle="collapse">Latest Stats</a>
                <div id="page-stats" class="panel-collapse panel-body collapse in">

                    <div class="row">
                        <div class="col-md-3 col-sm-6">
                            <div class="knob-container">
                                <input class="knob" data-width="200" data-min="0" data-max="3000" data-displayPrevious="true" value="2500" data-fgColor="#92A3C2" data-readOnly=true;>
                                <h3 class="text-muted text-center">Accounts</h3>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-6">
                            <div class="knob-container">
                                <input class="knob" data-width="200" data-min="0" data-max="4500" data-displayPrevious="true" value="3299" data-fgColor="#92A3C2" data-readOnly=true;>
                                <h3 class="text-muted text-center">Subscribers</h3>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-6">
                            <div class="knob-container">
                                <input class="knob" data-width="200" data-min="0" data-max="2700" data-displayPrevious="true" value="1840" data-fgColor="#92A3C2" data-readOnly=true;>
                                <h3 class="text-muted text-center">Pending</h3>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-6">
                            <div class="knob-container">
                                <input class="knob" data-width="200" data-min="0" data-max="15000" data-displayPrevious="true" value="10067" data-fgColor="#92A3C2" data-readOnly=true;>
                                <h3 class="text-muted text-center">Completed</h3>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-6 col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading no-collapse">Not Collapsible<span class="label label-warning">+10</span></div>
                        <table class="table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Username</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>Mark</td>
                                <td>Tompson</td>
                                <td>the_mark7</td>
                            </tr>
                            <tr>
                                <td>Ashley</td>
                                <td>Jacobs</td>
                                <td>ash11927</td>
                            </tr>
                            <tr>
                                <td>Audrey</td>
                                <td>Ann</td>
                                <td>audann84</td>
                            </tr>
                            <tr>
                                <td>John</td>
                                <td>Robinson</td>
                                <td>jr5527</td>
                            </tr>
                            <tr>
                                <td>Aaron</td>
                                <td>Butler</td>
                                <td>aaron_butler</td>
                            </tr>
                            <tr>
                                <td>Chris</td>
                                <td>Albert</td>
                                <td>cab79</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="col-sm-6 col-md-6">
                    <div class="panel panel-default">
                        <a href="#widget1container" class="panel-heading" data-toggle="collapse">Collapsible </a>
                        <div id="widget1container" class="panel-body collapse in">
                            <h2>Here's a Tip</h2>
                            <p>This template was developed with <a href="http://middlemanapp.com/" target="_blank">Middleman</a> and includes .erb layouts and views.</p>
                            <p>All of the views you see here (sign in, sign up, users, etc) are already split up so you don't have to waste your time doing it yourself!</p>
                            <p>The layout.erb file includes the header, footer, and side navigation and all of the views are broken out into their own files.</p>
                            <p>If you aren't using Ruby, there is also a set of plain HTML files for each page, just like you would expect.</p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-6 col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading no-collapse">
                <span class="panel-icon pull-right">
                    <a href="#" class="demo-cancel-click" rel="tooltip" title="Click to refresh"><i class="fa fa-refresh"></i></a>
                </span>

                            Needed to Close
                        </div>
                        <table class="table list">
                            <tbody>
                            <tr>
                                <td>
                                    <a href="#"><p class="title">Care Hospital</p></a>
                                    <p class="info">Sales Rating: 86%</p>
                                </td>
                                <td>
                                    <p>Date: 7/19/2012</p>
                                    <a href="#">View Transaction</a>
                                </td>
                                <td>
                                    <p class="text-danger h3 pull-right" style="margin-top: 12px;">$20,500</p>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <a href="#"><p class="title">Custom Eyesight</p></a>
                                    <p class="info">Sales Rating: 58%</p>
                                </td>
                                <td>
                                    <p>Date: 7/19/2012</p>
                                    <a href="#">View Transaction</a>
                                </td>
                                <td>
                                    <p class="text-danger h3 pull-right" style="margin-top: 12px;">$12,600</p>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <a href="#"><p class="title">Clear Dental</p></a>
                                    <p class="info">Sales Rating: 76%</p>
                                </td>
                                <td>
                                    <p>Date: 7/19/2012</p>
                                    <a href="#">View Transaction</a>
                                </td>
                                <td>
                                    <p class="text-danger h3 pull-right" style="margin-top: 12px;">$2,500</p>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <a href="#"><p class="title">Safe Insurance</p></a>
                                    <p class="info">Sales Rating: 82%</p>
                                </td>
                                <td>
                                    <p>Date: 7/19/2012</p>
                                    <a href="#">View Transaction</a>
                                </td>
                                <td>
                                    <p class="text-danger h3 pull-right" style="margin-top: 12px;">$22,400</p>
                                </td>
                            </tr>

                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="col-sm-6 col-md-6">
                    <div class="panel panel-default">
                        <a href="#widget2container" class="panel-heading" data-toggle="collapse">Collapsible </a>
                        <div id="widget2container" class="panel-body collapse in">
                            <h2>Built with Less</h2>
                            <p>The CSS is built with Less. There is a compiled version included if you prefer plain CSS.</p>
                            <p>Fava bean jícama seakale beetroot courgette shallot amaranth pea garbanzo carrot radicchio peanut leek pea sprouts arugula brussels sprout green bean. Spring onion broccoli chicory shallot winter purslane pumpkin gumbo cabbage squash beet greens lettuce celery. Gram zucchini swiss chard mustard burdock radish brussels sprout groundnut. Asparagus horseradish beet greens broccoli brussels.</p>
                            <p><a class="btn btn-primary">Learn more »</a></p>
                        </div>
                    </div>
                </div>
            </div>

            <footer>
                <hr>
                <!-- Purchase a site license to remove this link from the footer: http://www.portnine.com/bootstrap-themes &ndash;&gt;
                <p class="pull-right">A <a href="http://www.portnine.com/bootstrap-themes" target="_blank">Free Bootstrap Theme</a> by <a href="http://www.portnine.com" target="_blank">Portnine</a></p>
                <p>© 2014 <a href="http://www.portnine.com" target="_blank">Portnine</a></p>
            </footer>
        </div>
    </div>-->
</body>
</html>

<#--<div class="navbar navbar-default" role="navigation">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="" href="index.html"><span class="navbar-brand"><span class="fa fa-paper-plane"></span> 管理员首页</span></a></div>

        <div class="navbar-collapse collapse" style="height: 1px;">
            <ul id="main-menu" class="nav navbar-nav navbar-right">
                <li class="dropdown hidden-xs">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <span class="glyphicon glyphicon-user padding-right-small" style="position:relative;top: 3px;"></span> Jack Smith
                        <i class="fa fa-caret-down"></i>
                    </a>

                    <ul class="dropdown-menu">
                        <li><a href="./">My Account</a></li>
                        <li class="divider"></li>
                        <li class="dropdown-header">Admin Panel</li>
                        <li><a href="./">Users</a></li>
                        <li><a href="./">Security</a></li>
                        <li><a tabindex="-1" href="./">Payments</a></li>
                        <li class="divider"></li>
                        <li><a tabindex="-1" href="sign-in.html">Logout</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>-->

<#--<body>
<div class="row">
    <#include "../common/top.ftl">
    <div class="col-md-2">
        <div class="panel-group table-responsive" role="tablist">
            <div class="panel panel-primary leftMenu">
                <!-- 利用data-target指定要折叠的分组列表 &ndash;&gt;
                <div class="panel-heading" id="collapseListGroupHeading1" data-toggle="collapse" data-target="#collapseListGroup1" role="tab" >
                    <h4 class="panel-title">
                        分组1
                        <span class="glyphicon glyphicon-chevron-up right"></span>
                    </h4>
                </div>
                <!-- .panel-collapse和.collapse标明折叠元素 .in表示要显示出来 &ndash;&gt;
                <div id="collapseListGroup1" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="collapseListGroupHeading1">
                    <ul class="list-group">
                        <li class="list-group-item">
                            <!-- 利用data-target指定URL &ndash;&gt;
                            <button class="menu-item-left" data-target="test2.html">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项1-1
                            </button>
                        </li>
                        <li class="list-group-item">
                            <button class="menu-item-left">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项1-2
                            </button>
                        </li>
                        <li class="list-group-item">
                            <button class="menu-item-left">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项1-3
                            </button>
                        </li>
                        <li class="list-group-item">
                            <button class="menu-item-left">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项1-4
                            </button>
                        </li>
                        <li class="list-group-item">
                            <button class="menu-item-left">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项1-5
                            </button>
                        </li>
                        <li class="list-group-item">
                            <button class="menu-item-left">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项1-6
                            </button>
                        </li>
                        <li class="list-group-item">
                            <button class="menu-item-left">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项1-7
                            </button>
                        </li>
                        <li class="list-group-item">
                            <button class="menu-item-left">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项1-8
                            </button>
                        </li>
                        <li class="list-group-item">
                            <button class="menu-item-left">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项1-9
                            </button>
                        </li>
                        <li class="list-group-item">
                            <button class="menu-item-left">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项1-10
                            </button>
                        </li>
                        <li class="list-group-item">
                            <button class="menu-item-left">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项1-11
                            </button>
                        </li>
                    </ul>
                </div>
            </div><!--panel end&ndash;&gt;
            <div class="panel panel-primary leftMenu">
                <div class="panel-heading" id="collapseListGroupHeading2" data-toggle="collapse" data-target="#collapseListGroup2" role="tab" >
                    <h4 class="panel-title">
                        分组2
                        <span class="glyphicon glyphicon-chevron-down right"></span>
                    </h4>
                </div>
                <div id="collapseListGroup2" class="panel-collapse collapse" role="tabpanel" aria-labelledby="collapseListGroupHeading2">
                    <ul class="list-group">
                        <li class="list-group-item">
                            <button class="menu-item-left">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项2-1
                            </button>
                        </li>
                        <li class="list-group-item">
                            <button class="menu-item-left">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项2-2
                            </button>
                        </li>
                        <li class="list-group-item">
                            <button class="menu-item-left">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项2-3
                            </button>
                        </li>
                        <li class="list-group-item">
                            <button class="menu-item-left">
                                <span class="glyphicon glyphicon-triangle-right"></span>分组项2-4
                            </button>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-10">
        内容
    </div>
</div>
<!-- jQuery1.11.3 (necessary for Bo otstrap's JavaScript plugins) &ndash;&gt;
<script src="js/jquery-1.11.3.min.js "></script>
<!-- Include all compiled plugins (below), or include individual files as needed &ndash;&gt;
<script src="js/bootstrap.min.js "></script>
<script>
    $(function(){
        $(".panel-heading").click(function(e){
            /*切换折叠指示图标*/
            $(this).find("span").toggleClass("glyphicon-chevron-down");
            $(this).find("span").toggleClass("glyphicon-chevron-up");
        });
    });
</script>
</body>-->