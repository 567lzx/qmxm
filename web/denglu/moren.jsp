<%--
  Created by IntelliJ IDEA.
  User: 宣纸凉
  Date: 2022/12/13
  Time: 9:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html" />
    <meta name="author" content="" />
    <title>Title</title>
</head>
<body style="font-size: 60px;color: #308d7a;padding-top: 70px;padding-left: 120px">
<DIV>亲爱的用户:</DIV>
<script language="javaScript">
    now = new Date(),hour = now.getHours()
    if (hour < 9){document.write("早上好！")}
    else if (hour < 12){document.write("上午好！")}
    else if (hour < 14){document.write("中午好！")}
    else if (hour < 18){document.write("下午好！")}
    else if (hour < 22){document.write("晚上好！")}
    else {document.write("夜里好！")}
</script>
<div>感谢您使用本系统</div>
<div>当前版本：1.0</div>
</body>
</html>
