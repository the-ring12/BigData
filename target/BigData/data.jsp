<%--
  Created by IntelliJ IDEA.
  User: Tan
  Date: 2020/12/22
  Time: 16:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    String msg = request.getParameter("msg");
    if (msg != null) {
        out.println(msg);
    }
%>
<form action="data.jsp" method="post">
    Msg: <input type="text" name="msg">
    <input type="submit" value="Submit">
</form>
</body>
</html>
