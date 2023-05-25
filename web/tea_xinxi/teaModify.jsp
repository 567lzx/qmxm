<%--
  Created by IntelliJ IDEA.
  User: 宣纸凉
  Date: 2022/11/10
  Time: 16:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!--教师-改-->
    <title>修改教师信息</title>
    <style>
        .a{margin-top: 10px;margin-bottom60:px;margin-left: 340px;color: #308d7a;text-decoration: none;}
        .a:hover{
            color: #fcfffc;background: #308d7a;
        }
        .div1{
            font-size: 40px;margin-bottom: 24px;color: #308d7a;
        }
        .div2{
            font-size: 24px;margin-top: 12px;color: #308d7a;
        }
    </style>
    <link rel="stylesheet" href="https://cdn.staticfile.org/font-awesome/4.7.0/css/font-awesome.css">
</head>
<body   style="width:80%; height:89%; padding-left: 150px">
<a class="a fa fa-reply" href="${pageContext.request.contextPath}/tea">返回</a>
<div class="div1">修改教师信息</div>
<form action="/teaModify" method="post">
    <div class="div2">教师工号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input readonly type="text" name="teaId" value="${param.get("teaId")}"><br></div>
    <div class="div2">教师姓名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="teaName" value="${param.get("teaName")}"><br></div>
<div class="div2">教师性别&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="teaSex" value="${param.get("teaSex")}" ><br></div>
<div class="div2">教师电话&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="teaTele" value="${param.get("teaTele")}" ><br></div>
<div><input  style="width: 60px ;height:35px;margin-top: 20px ;margin-left: 190px" type="submit" method="post">&nbsp;&nbsp;<input style="width: 60px ;height:35px ;margin-top: 12px" type="reset"></div>
</form>
</body>
</html>
