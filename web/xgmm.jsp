<%--
  Created by IntelliJ IDEA.
  User: 宣纸凉
  Date: 2022/11/17
  Time: 22:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!--修改密码页面-->
    <title>Title</title>
    <style>
        .test{
        width: 200px;height: 40px;
        }
    </style>
</head>
<body style="font-size: 32px ;color: #308d7a">
<div>修改密码</div>
${"———————————————————————"}
<form action="/mmtj" onsubmit="return check()">
    账号：<input type="text" value="${sessionScope.userid}" class="test" name="userId" readonly/><br>
    新密码：<input type="text" class="test" id="password" name="pass1"><br>
    确认密码:<input type="text" class="test" id="passwordConfirm" name="pass2"><br>
    <br>
    <input type="submit" style="width: 150px;height: 40px;" value="保存" />
</form>
</body>
<script>
    function check (){
        var password = document.getElementById("password").value;
        // console.log(password);
        // console.log(password.value);
        var passwordConfirm = document.getElementById("passwordConfirm").value;
        // console.log(password);
        if(password===""){
            alert("现在是空的呢，请输入哦")
            return false;
            <%--location.href="${pageContext.request.contextPath}/mmtj?userId=${sessionScope.userid}&pass1="+password+"&pass2="+passwordConfirm;--%>
        }
        if(password===passwordConfirm){
            alert("修改成功")
            return true;
            <%--location.href="${pageContext.request.contextPath}/mmtj?userId=${sessionScope.userid}&pass1="+password+"&pass2="+passwordConfirm;--%>
        }else {
            alert("两次输入不一样哦，请仔细检查")
            return false;
        }
    }
</script>
</html>
