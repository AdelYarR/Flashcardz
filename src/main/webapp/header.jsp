<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header>
    <a href="${pageContext.request.contextPath}/hub/groups" class="logo">
        <img src="${pageContext.request.contextPath}/images/logo.png" width="100" height="100">
    </a>

    <form method="get" action="${pageContext.request.contextPath}/hub/groups">
        <div id="search_bar">
            <input type="text" name="input_text" placeholder="Поиск...">
            <button type="submit">Отправить</button>
        </div>
    </form>

    <div class="profile_box">
        <a href="#" class="dropdown_profile">Профиль</a>
        <ul>
            <li>
                <form method="get" action="${pageContext.request.contextPath}/profile/groups">
                    <button type="submit">Мои группы</button>
                </form>
            </li>
            <li>
                <form method="get" action="${pageContext.request.contextPath}/options">
                    <button type="submit">Настройки</button>
                </form>
            </li>
            <li>
                <form method="post" action="${pageContext.request.contextPath}/log-out">
                    <button type="submit" onclick="return confirm('Вы уверены, что хотите выйти из аккаунта?')">Выход</button>
                </form>
            </li>
        </ul>
    </div>
</header>