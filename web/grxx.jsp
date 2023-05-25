<%--
  Created by IntelliJ IDEA.
  User: 宣纸凉
  Date: 2022/11/15
  Time: 16:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!--个人信息页面-->
    <title>Title</title>
    <style>
        .a:hover{
        color: #fcfffc ;background: #308d7a;
    }
    </style>

</head>
<body style="font-size: 24px;color: #308d7a;">
<div style="font-size: 32px">个人信息</div>
${"———————————————————————————————"}
<%
    String grant = (String) session.getAttribute("grant");
    if(grant==null){
        response.sendRedirect("/main.jsp");
        return;
    }
    switch (grant) {
        case "管理员":
            out.println("管理员编号:" + session.getAttribute("adminId") + "<br>"+ "<br>");
            out.println("管理员姓名:" + session.getAttribute("adminName") + "<br>"+ "<br>");
            out.println("管理员密码:" + session.getAttribute("adminPass") + "<br>"+ "<br>");

            break;
        case "学生":
            out.print("学号:" + session.getAttribute("stuId") + "<br>"+ "<br>");
            out.print("姓名:" + session.getAttribute("stuName") + "<br>"+ "<br>");
            out.print("密码:" + session.getAttribute("stuPass") + "<br>"+ "<br>");
            out.print("班级:" + session.getAttribute("stuClass") + "<br>"+ "<br>");
            out.print("性别:" + session.getAttribute("stuSex") + "<br>"+ "<br>");
            out.print("联系方式:" + session.getAttribute("stuTele") + "<br>"+ "<br>");
            break;
        case "教师":
            out.print("工号:" + session.getAttribute("teaId") + "<br>"+ "<br>");
            out.print("姓名:" + session.getAttribute("teaName") + "<br>"+ "<br>");
            out.print("密码:" + session.getAttribute("teaPass") + "<br>"+ "<br>");
            out.print("性别:" + session.getAttribute("teaSex") + "<br>"+ "<br>");
            out.print("联系方式:" + session.getAttribute("teaTele") + "<br>"+ "<br>");
            break;
    }
%>
<a class="a" href="${pageContext.request.contextPath}/xgmm" style="font-size: 24px;color: #308d7a;text-decoration: none;border:1px solid #308d7a">修改密码</a>
</body>
</html>
