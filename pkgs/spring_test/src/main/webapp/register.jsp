<%--
  Created by IntelliJ IDEA.
  User: JZX
  Date: 2022/12/7
  Time: 8:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>




</head>
<body>
<h1> 员工注册页面</h1>
    <form action="registeremp.do" method="post">

         用户名:<input type="text" name="nickname"> <br/>
         真实姓名:<input type="text" name="ename"> <br/>
         密码:<input type="text" name="epass"> <br/>
         年龄:<input type="text" name="eage"> <br/>

        <input type="submit" value="注册">

    </form>
</body>
</html>
