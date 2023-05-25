<%@ page import="java.util.List" %>
<%@ page import="com.Account" %>
<%@ page import="com.resAccount" %><%--
  Created by IntelliJ IDEA.
  User: 宣纸凉
  Date: 2022/11/6
  Time: 9:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <!--成绩-查-->
    <title>Title</title>
    <style>
        .a1 {

            color: #308d7a;
            float: right;
            margin-right: 275px;
            text-decoration: none;

        }

        .a2 {

            color: #308d7a;
            text-decoration: none;
        }
        .a:hover{
            color: #fcfffc;background: #308d7a;
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
    </style>
    <link rel="stylesheet" href="https://cdn.staticfile.org/font-awesome/4.7.0/css/font-awesome.css">
</head>
<body style="width:80%; height:89%">

<div>
    <a href="/res_xinxi/resAdd.jsp" class="a1 a">添加成绩</a>
    <label>
        <input id="shuru" onkeydown="search1()" type="text" placeholder="请输入学生姓名" value="${requestScope.lastQuery}"><i onclick="search2()" class="fa fa-search box"></i>
    </label>

</div>
<script>
    function search1(){
        if (event.keyCode===13){
            var value = document.getElementById("shuru").value;
            location.href='/resQuery?stuName='+value;
        }
    }
    function search2(){

            var value = document.getElementById("shuru").value;
            location.href='/resQuery?stuName='+value;

    }
</script>
${"——————————————————————————————————————————"}
<table border="0" style="text-align:center;">
    <tr>
        <th width="80">成绩编号</th>
        <th width="120">姓名</th>
        <th width="160">班别</th>
        <th width="40">科目</th>
        <th width="40">分数</th>
        <th width="160">学期</th>
        <th width="80">操作</th>
    </tr>
    <%
        //先把数据取出来，通过request对象
        List<resAccount> resList = (List<resAccount>) request.getAttribute("resList");
        for (int i = 0; i < resList.size(); i++) {
            resAccount resultList = resList.get(i);
    %>
    <tr>
        <td><%=resultList.getId()%>
        </td>
        <td><%=resultList.getName()%>
        </td>
        <td><%=resultList.getCla()%>
        </td>
        <td><%=resultList.getSubName()%>
        </td>
        <td><%=resultList.getNum()%>
        </td>
        <td><%=resultList.getTerm()%>
        </td>
        <td><a class="a2 a" href="/res_xinxi/resModify.jsp?resId=<%=resultList.getId()%>&resStuId=<%=resultList.getStuId()%>&resSubName=<%=resultList.getSubName()%>&resNum=<%=resultList.getNum()%>&resTerm=<%=resultList.getTerm()%>" >修改</a>|<a class="a2 a" href="${pageContext.request.contextPath}/resDelete?resId=<%=resultList.getId()%>" >删除</a></td>
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
${"——————————————————————————————————————————"}
<div  style=" top:400px;left: 500px;margin-top: 20px;font-size: 16px">
    <a href="/resQuery?n=1&stuName=${requestScope.lastQuery}" class="a2 a">首页</a>
    <a href="/resQuery?n=<%=pageNow-1%>&stuName=${requestScope.lastQuery}" class="a2 a">上一页</a>
    <a href="/resQuery?n=<%=pageNow+1%>&stuName=${requestScope.lastQuery}" class="a2 a">下一页</a>
    <a href="/resQuery?n=<%=pageCount%>&stuName=${requestScope.lastQuery}" class="a2 a">尾页</a>
    <select onchange="location.href='/resQuery?n='+this.value+'&stuName =${requestScope.lastQuery}'" class="a2">
        <% for(int i=1;i<=pageCount;i++){
        %>
        <option value="<%=i %>" <%if(i==pageNow){out.print("selected");}%> > <%=i %>  </option>;
        <% }
        %>
    </select>


<div class="a1">
    当前第${requestScope.pageNow}页 共${requestScope.pageCount}页
</div>
</div>
</body>
</html>
