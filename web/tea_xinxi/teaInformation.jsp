<%@ page import="java.util.List" %>
<%@ page import="com.Account" %>
<%@ page import="com.teaAccount" %><%--
  Created by IntelliJ IDEA.
  User: 宣纸凉
  Date: 2022/11/6
  Time: 9:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <!--教师信息-->
    <title>Title</title>
    <style>
        .a1 {
            color: #308d7a;
            float: right;
            margin-right: 335px;
            text-decoration: none;

        }

        .a2 {
            color: #308d7a;
            text-decoration: none;
        }

        table {
            text-align: center;

        }

        tr {
            border: 1px solid black;
            height: 30px;
        }

        .box {
            position: relative;
            left: -20px
        }

        .a:hover{
            color: #fcfffc;background: #308d7a;
        }
    </style>
    <link rel="stylesheet" href="https://cdn.staticfile.org/font-awesome/4.7.0/css/font-awesome.css">
</head>
<body style="width:80%; height:89%">


    <a href="/tea_xinxi/teaAdd.jsp" class="a1 a">添加教师</a>
    <div>
    <label>
        <input id="shuru" onkeydown="search1()" type="text" placeholder="请输入教师名字"><i onclick="search2()" class="fa fa-search box"></i>
    </label>
    </div>
<script>
    function search1(){
        if (event.keyCode===13){
            var value = document.getElementById("shuru").value;
            location.href='/teaQuery?teaName='+value;
        }
    }
    function search2(){

        var value = document.getElementById("shuru").value;
        location.href='/teaQuery?teaName='+value;

    }
</script>
    ${"——————————————————————————————————————"}
<table border="0" style="text-align:center;">
    <tr>
        <th width="92">工号</th>
        <th width="152">姓名</th>
        <th width="72">性别</th>
        <th width="192">电话</th>
        <th width="112">操作</th>
    </tr>
    <%
        //先把数据取出来，通过request对象
        List<teaAccount> teaList = (List<teaAccount>) request.getAttribute("teaList");
        for (int i = 0; i < teaList.size(); i++) {
            teaAccount teacherList = teaList.get(i);
    %>

    <tr>
        <td><%=teacherList.getId()%>
        </td>
        <td><%=teacherList.getName()%>
        </td>
        <td><%=teacherList.getSex()%>
        </td>
        <td><%=teacherList.getTele()%>
        </td>
        <td><a class="a2 a" href="/tea_xinxi/teaModify.jsp?teaId=<%=teacherList.getId()%>&teaName=<%=teacherList.getName()%>&teaSex=<%=teacherList.getSex()%>&teaTele=<%=teacherList.getTele()%>" >修改</a>|<a class="a2 a" href="${pageContext.request.contextPath}/teaDelete?teaId=<%=teacherList.getId()%>" >删除</a></td>
    </tr>

    <%
        }
    %>

    <%
        Integer pageSize = (Integer) request.getAttribute("pageSize");
        Integer lineCount = (Integer) request.getAttribute("lineCount");
        Integer pageCount = (Integer) request.getAttribute("pageCount");
        Integer pageNow = (Integer) request.getAttribute("pageNow");
    %>
</table>
    ${"——————————————————————————————————————"}
<div  style=" top:400px;left: 500px;margin-top: 20px;font-size: 16px" class="a2">
<a href="/tea?n=1" class="a2 a">首页</a>
<a href="/tea?n=<%=pageNow-1%>"class="a2 a">上一页</a>
<a href="/tea?n=<%=pageNow+1%>" class="a2 a" >下一页</a>
<a href="/tea?n=<%=pageCount%>" class="a2 a">尾页</a>

    跳转到第<select onchange="location.href='/tea?n='+this.value" class="a2">
        <% for(int i=1;i<=pageCount;i++){
            %>
        <option value="<%=i %>" <%if(i==pageNow){out.print("selected");}%> > <%=i %>  </option>;
        <% }
           %>
    </select>页

    <div class="a1">
        当前第${requestScope.pageNow}页 共${requestScope.pageCount}页
    </div>
</div>
<br>

</body>
</html>
