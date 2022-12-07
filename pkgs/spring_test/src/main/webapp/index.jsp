<%--
  Created by IntelliJ IDEA.
  User: JZX
  Date: 2022/12/6
  Time: 16:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

      <%--  get  请求传递参数--%>
        <a href="register.do?sname='张三'&spass='123'">把数据传递给控制器</a>
        <a href="register2.do?sname='张三丰'&spass='123'">把数据传递给控制器</a>


<%--  post  请求--%>
       <form action="register1.do" method="post">

           用户名:<input type="text" name="sname"/> <br/>
           密码:<input type="password" name="spass"><br/>
           <input type="submit" value="提交">


       </form>

</body>
</html>
