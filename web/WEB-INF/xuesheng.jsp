<%@ page import="com.resAccount" %><%--
  Created by IntelliJ IDEA.
  User: 宣纸凉
  Date: 2022/11/5
  Time: 10:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!--学生平台-->
    <title>学生平台</title>
    <style>
        *{margin: 0;padding: 0;}
        .div1{
            height: calc(8vh);font-size:40px;color: #308d7a;margin-left: 10px;
        }
        .div2{
            height: calc(3vh);color: #308d7a;margin-left: 10px;
        }
        .div3 {
            height: calc(89vh);
            width: 20%;
            background-color: #308d7a;
            float: left;
        }
        .a1{text-decoration: none;color: #308d7a ;}
        .zx:hover{
            color: #fcfffc;background: #308d7a;
        }
        .menu:hover{
            background-color: darkcyan;
        }
    </style>
    <link rel="stylesheet" href="https://cdn.staticfile.org/font-awesome/4.7.0/css/font-awesome.css">
</head>
<body>

<div class="div1">欢迎使用学生成绩管理系统</div>
<div class="div2">当前用户&nbsp;&nbsp;学生：${sessionScope.stuName} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a class="zx a1" href="${pageContext.request.contextPath}/logout">注销登录</a></div>
<div class="div3">
    <div class="fa fa-list-alt" style=" color:#fcfffc; font-size:32px;margin-left: 40px;margin-top: 20px">&nbsp;&nbsp;菜单列表</div>
    <a href="/grxx.jsp" target="stu" class="menu fa fa-file-text-o" aria-hidden="true" style=" color:#fcfffc; font-size:18px; display: block;height: 40px;text-decoration: none;padding-left: 40px;padding-top: 25px;margin-top: 20px;">&nbsp;个人信息</a>
    <a href="/cjcx?stuName=${sessionScope.stuName}" target="stu" class="menu fa fa-pencil" aria-hidden="true" style=" color:#fcfffc; font-size:18px; display: block;text-decoration: none;height: 40px;padding-left: 40px;padding-top: 25px;">&nbsp;成绩查询</a>
   </div>

<iframe src="../denglu/moren.jsp" name="stu" frameborder="no" th:src="" style="overflow: visible" scrolling="yes" width="80%" height="89%" marginwidth="220px" marginheight="50px"></iframe>
</div>
</body>
</html>
