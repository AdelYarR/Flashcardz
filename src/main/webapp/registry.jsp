<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registry</title>
</head>
<body>
    <form method="post" action="registry">
        <label for="login">Логин:</label>
        <input type="text" id="login" name="user_name" required>

        <label for="password">Пароль:</label>
        <input type="password" id="password" name="user_password" required>

        <label for="password_confirm">Подтвердите пароль:</label>
        <input type="password" id="password_confirm" name="user_password_confirm" required>

        <input type="submit" value="Зарегистрироваться">
    </form>
</body>
</html>
