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
    <!--学生-增-->
    <title>添加学生信息</title>
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
<body  style="width:80%; height:89%; padding-left: 150px">
<a class="a fa fa-reply" href="${pageContext.request.contextPath}/stu">返回</a>
<div class="div1">添加学生信息</div>
<form action="/stuAdd">
<div class="div2">学生姓名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="stuName" ><br></div>
    <div class="div2">学生班级&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="stuCla"  ><br></div>
        <div class="div2">学生性别&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="stuSex" ><br></div>
            <div class="div2">学生电话&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="stuTele" ><br></div>
<div><input style="width: 60px ;height:35px;margin-top: 20px ;margin-left: 190px" type="submit" method="post">&nbsp;&nbsp;<input style="width: 60px ;height:35px ;margin-top: 12px" type="reset"></div>
</form>

</body>
</html>
