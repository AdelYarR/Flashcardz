<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
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
