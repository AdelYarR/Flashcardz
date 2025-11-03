<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registry</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <form method="post" action="registry">
        <div id="registry_form">
            <div>
                <label for="login">Логин:</label>
                <input type="text" id="login" name="user_name" required>
            </div>

            <div>
                <label for="password">Пароль:</label>
                <input type="password" id="password" name="user_password" required>
            </div>

            <div>
                <label for="password_confirm">Подтвердите пароль:</label>
                <input type="password" id="password_confirm" name="user_password_confirm" required>
            </div>

            <input type="submit" value="Зарегистрироваться">
        </div>
    </form>
</body>
</html>
