<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 28.09.2025
  Time: 17:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registry</title>
</head>
<body>
    <form method="post" action="/registry">
        <label for="login">Логин:</label>
        <input type="text" id="login" name="login" required>

        <label for="password">Пароль:</label>
        <input type="password" id="password" name="password" required>

        <label for="password_confirm">Подтвердите пароль:</label>
        <input type="password" id="password_confirm" name="password_confirm" required>

        <input type="submit" value="Зарегистрироваться">
    </form>
</body>
</html>
