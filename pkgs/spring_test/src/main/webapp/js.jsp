<%--
  Created by IntelliJ IDEA.
  User: JZX
  Date: 2022/12/8
  Time: 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
     <button id="btn_click" onclick="change()">点击</button>
     <h1 id="h_content">内容</h1>

    <script>

        window.onload=function (){

            alert("页面加载完毕")

        }

        function  change(){

            document.getElementById("h_content").innerHTML="我点击之后发生改变了"
        }

    </script>
</body>
</html>
