<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 28.09.2025
  Time: 17:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isThreadSafe="false"  %>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <form method="post" action="log">
        <label for="login">Логин:</label>
        <input type="text" id="login" name="user_name" required>

        <label for="password">Пароль:</label>
        <input type="password" id="password" name="user_password" required>

        <input type="submit" value="Войти">
    </form>
</body>
</html>
