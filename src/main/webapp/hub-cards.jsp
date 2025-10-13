<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title>Hub cards</title>
    <link rel="stylesheet" href="css/group-menu.css">
</head>
<body>
<header>
    <a href="#" class="logo">LOGO</a>

    <div class="profile_box">
        <a href="#" class="dropdown_profile">${user_name}</a>
        <ul>
            <li><a href="#">Профиль</a></li>
            <li><a href="#">Настройки</a></li>
            <li><a href="#">Выход</a></li>
        </ul>
    </div>
</header>

<div class="cards">
    <c:forEach var="card" items="${cards}">
        <my:hub-card card="${card}"/>
    </c:forEach>
</div>
</body>