<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header>
    <a href="${pageContext.request.contextPath}/hub/groups" class="logo">LOGO</a>

    <form method="get" action="${pageContext.request.contextPath}/hub/groups">
        <input type="text" name="input_text" placeholder="Поиск...">
        <button type="submit">Отправить</button>
    </form>

    <div class="profile_box">
        <a href="#" class="dropdown_profile">${user_name}</a>
        <ul>
            <li><a href="${pageContext.request.contextPath}/profile/groups">Профиль</a></li>
            <li><a href="#">Настройки</a></li>
            <li><a href="#">Выход</a></li>
        </ul>
    </div>
</header>