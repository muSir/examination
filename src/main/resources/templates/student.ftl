<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>考生:${user.username} 首页</title>
    <link rel="stylesheet" type="text/css" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-1.8.0.min.js"></script>
</head>
<body>
    <div>
        <div align="center">
            <p><h3>考生基本信息</h3></p>
            <div style="border: 1px;border-color: #000000;border-style: solid;width: 80%;height: 50px">
                考生姓名：${user.username}
                &nbsp;&nbsp;
                身份证号码：${idCard}
                &nbsp;&nbsp;
                手机号码：${user.phoneNum}
                &nbsp;&nbsp;
                邮箱：${user.email}
                &nbsp;&nbsp;
                当前时间：${currentTime}
            </div>
        </div>
        <div>
            <div align="center" style="width: 100%">
                <p><h3>历史记录</h3></p>
                <p><a href="/examinations/${user.userId}/papers">参加考试</a></p>
            </div>
            <table width="100%" class="tab2">
                <tbody>
                <tr>
                    <th width="20"><input type="checkbox" id="all"  onclick="checkAll();"/></th>
                    <th width="40">编号</th>
                    <th width="20%">试卷名称</th>
                    <th width="20%">得分</th>
                    <th width="20%">答题时间</th>
                    <th width="20%">用时(分钟)</th>
                    <th width="80">操作</th>
                </tr>
                <#if record?exists >
                    <#list record as r>
                    <tr>
                        <td><input type="checkbox" name="id" value="${r.recordId}"/></td>
                        <td>${r_index?if_exists+1}</td>
                        <td><#if r.paperName??>${r.paperName}</#if></td>
                        <td><#if r.score??>${r.score}</#if></td>
                        <td><#if r.startTime??>${r.startTime?string("dd.MM.yyyy HH:mm")}</#if></td>
                        <td><#if r.useTime??>${r.useTime}</#if></td>
                        <td align="center">
                            <a href="/papers/${r.recordId}/detail">查看详情</a>&nbsp;
                        </td>
                    </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
            <br/>
            <div class='page fix' align="center">
                共 <b><#if totalCount?exists>${totalCount}</#if></b> 条
                <a href='/users/welcome' class='first'>首页</a>
                <a href='###' class='pre'>上一页</a>
                当前第<span>1/1</span>页
                <a href='###' class='next'>下一页</a>
                <a href='###' class='last'>末页</a>
                跳至&nbsp;<input type='text' value='1'/>&nbsp;页&nbsp;
                <a href='###' class='go'>GO</a>
            </div>
        </div>

    </div>
</body>
</html>

