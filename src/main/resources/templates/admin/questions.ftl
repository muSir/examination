<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>题库</title>
    <link rel="stylesheet" type="text/css" href="/css/common/layout.css">
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="/js/questions.js"></script>
</head>

<body>
    <#include "../common/top.ftl">
    <#include "./common/navigation.ftl">

    <div class="content">
        <div class="header">
            <h1 class="page-title">题目列表</h1>
            <ul class="breadcrumb" style="margin-top: 10px">
                <li><a href="/users/admin/first">首页</a> </li>
                <li class="active">题目列表</li>
            </ul>
        </div>

        <div>
            <div class="btn-toolbar list-toolbar">
                    <a class="btn btn-default" href="/questions/question/new" data-toggle="tooltip" data-placement="top" title="新增题目">
                        <span class="glyphicon glyphicon-plus"></span>
                        <span style="margin-left: 3px"><i class="fa fa-plus"></i>新  增</span>
                    </a>
                    <button class="btn btn-default" onclick="bulkDelete()" data-toggle="tooltip" data-placement="top" title="批量删除">
                        <span class="glyphicon glyphicon-minus"></span>
                        <span style="margin-left: 3px"><i class="fa fa-plus"></i>删  除</span>
                    </button>
                    <a class="btn btn-default" style="margin-left: 50px" href="/questions/list/${page.pageNo}/pages">
                        <span class="glyphicon glyphicon-refresh"></span>
                        <span style="margin-left: 3px"><i class="fa fa-plus"></i>刷  新</span>
                    </a>
                    <#--<button class="btn btn-default">导  出</button>-->
                    <div class="btn-group">
                    </div>
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <form class="navbar-form" action="#" method="get">
                                <div class="row">
                                    <div class="btn-group">
                                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <#if dropdown??>${dropdown}<#else >单选题</#if><span class="caret"></span>
                                        </button>
                                        <ul class="dropdown-menu">
                                            <li><a href="/questions/list/SingleSelect/1/pages">单选题</a></li>
                                            <li><a href="/questions/list/MultiSelect/1/pages">多选题</a></li>
                                            <li><a href="/questions/list/ShortAnswer/1/pages">问答题</a></li>
                                            <li role="separator" class="divider"></li>
                                            <li><a href="#">题目分值</a></li>
                                            <li role="separator" class="divider"></li>
                                            <li><a href="#">Java</a></li>
                                            <li role="separator" class="divider"></li>
                                            <li><a href="/questions/list/1/pages">所有题型</a></li><#--查询全部类型-->
                                        </ul>
                                    </div>
                                    <div class="col-lg-6">
                                        <div class="input-group" >
                                            <input type="text" class="form-control" placeholder="关键词查询" style="width: 100px">
                                            <span class="input-group-btn">
                                                    <button class="btn btn-default" type="button">查  询</button>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </li>
                    </ul>
            </div>

            <div style="margin-top: 15px" align="center">
                <table width="100%" class="tab2" cellspacing="0" border="1" style="border-collapse:collapse;">
                    <thead >
                        <tr style="height: 40px">
                            <th width="60" style="text-align: center">
                                <input type="checkbox" id="allCheck" onclick="checkAll()"/>
                                <span style="margin-left: 5px">编  号</span>
                            </th>
                            <th width="20%" style="text-align: center">题  干</th>
                            <th width="10%" style="text-align: center">选  项</th>
                            <th width="20%" style="text-align: center">答  案</th>
                            <th width="10%" style="text-align: center">关键词</th>
                            <th width="50" style="text-align: center">分  值</th>
                            <th width="40" style="text-align: center">类  型</th>
                            <th width="20%" style="text-align: center">答案解析</th>
                            <th width="80" style="text-align: center">操  作</th>
                        </tr>
                    </thead>
                    <tbody id="tbody-questions">
                        <#if questions?exists >
                            <#list questions as question>
                                <tr style="height: 50px" id="${question.questionId}">
                                    <td style="text-align: center">
                                        <input id="check" type="checkbox" name="id" value="${question.questionId}"/>
                                        <span style="margin-left: 5px">${question_index?if_exists+1}</span>
                                    </td>
                                    <td align="left">${question.title}</td>
                                    <td >
                                        <#if question.dtoOptions??>
                                    <#list question.dtoOptions as option>
                                        <p>${option}</p>
                                    </#list>
                                </#if>
                                    </td>
                                    <td align="left">${question.answer}</td>
                                    <td>
                                        <#if question.dtoKeywords??>
                                            <#list question.dtoKeywords as keyword>
                                                <p>${keyword}</p>
                                            </#list>
                                        </#if>
                                    </td>
                                    <td style="text-align: center">${question.value}</td>
                                    <td style="text-align: center">${question.dtoType}</td>
                                    <td><#if question.reference??>${question.reference}</#if></td>
                                    <td style="text-align: center">
                                        <a href="/questions/${question.questionId}">编辑</a>&nbsp;
                                        <a href="javascript:void(0)" onclick="singleDelete('${question.questionId}')">删除</a>
                                    </td>
                                </tr>
                            </#list>
                        </#if>
                    </tbody>
                </table>
                <#--<ul class="pagination" style="text-align: center;width: 300px">
                    <li><a href="#">&laquo;</a></li>
                    <li><a href="#">1</a></li>
                    <li><a href="#">2</a></li>
                    <li><a href="#">3</a></li>
                    <li><a href="#">4</a></li>
                    <li><a href="#">5</a></li>
                    <li><a href="#">&raquo;</a></li>
                </ul>-->
                <#if page??>
                    <div class='page fix' style="margin-bottom: 20px;margin-top: 15px">
                        <form action="/questions/list" method="get">
                            共 <b>${page.resultCount}</b> 条
                            <a href='javascript:void(0);' class='first' onclick="goFirst('${page.pageNo?js_string}')">首 页</a>
                            <a href='javascript:void(0);' class='pre' onclick="goPre('${page.pageNo?js_string}')">上一页</a>
                            第<span><span id="currentPage"><#if page??>${page.pageNo}<#else >1</#if></span>/${page.totalPage}</span>页
                            <a href='javascript:void(0);' class='next' onclick="goNext('${page.pageNo?js_string}','${page.totalPage?js_string}')">下一页</a>
                            <a href='javascript:void(0);' class='last' onclick="goLast('${page.pageNo?js_string}','${page.totalPage?js_string}')">末 页</a>
                            跳至&nbsp;<input type='text' value='<#if page.pageNo gt 1>${page.pageNo}</#if>' style="width: 40px;text-align: center" id="goPage"/>&nbsp;页&nbsp;
                            <a href="javascript:void(0);" class="go" onclick="goPage('${page.totalPage?js_string}')">GO</a>
                        </form>
                    </div>
                </#if>
            </div>
        </div>
    </div>
</body>
</html>