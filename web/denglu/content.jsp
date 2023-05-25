<%--
  Created by IntelliJ IDEA.
  User: 宣纸凉
  Date: 2022/11/4
  Time: 23:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Title</title>
    <style>
        *{
            margin:0;padding: 0;
        }

        .div2{
            background-color:  #308d7a;height: calc(68vh);
        }
        .div3{
            background-color: #fcfffc;width: 480px;height: 440px;float: right;margin: 24px 70px;text-align: center;
        }
        .p{
            font-size:95px;color: #fcfffc;float: left;margin: calc(22vh) calc(14vw);
        }
        .test1{
            font-size: 40px;margin: 40px auto ;
        }
        .test2{
            font-size: 36px;margin-top: 30px;
        }

        .test3{
            width: 200px;height: 44px;
        }
        .button{margin: 18px auto;color: #308d7a;width:280px;height: 60px;}

        .div4{margin-top: 32px}
    </style>
</head>
<body>
<div class="div2">
    <p class="p">金榜题名</p>
    <div class="div3">
        <div class="test1">欢迎登录</div>
        <form action="/login" method="post">
            <div class="test2">用户账号:&nbsp;&nbsp;&nbsp;<input class="test3" type="text" name="userid"></div>
            <div class="test2">用户密码:&nbsp;&nbsp;&nbsp;<input class="test3" type="password" name="password"></div>
            <div class="div4"><input class="button" type="submit" value="登录"></div>
            <div id="msgtips">
            <%
            String err = request.getParameter("err");
            if(err!=null){%>
            <script>
            var ele=document.getElementById("msgtips");
            ele.innerHTML="<font color='red'>账号或密码错误</font>"
            </script>

            <%}
            %>
            </div>
        </form>
    </div>
</div>
</body>
</html>
