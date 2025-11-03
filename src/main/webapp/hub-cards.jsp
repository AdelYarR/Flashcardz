<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="training" uri="http://itis.example.ru/training/functions" %>

<html>
<head>
    <title>Hub cards</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <script src="${pageContext.request.contextPath}/js/hub-card.js"></script>

    <div>
        <form method="get" action="${pageContext.request.contextPath}/training-start">
            <input type="hidden" name="card_group_id" value="${card_group_id}">
            <button type="submit">Тренировать</button>
        </form>

        <c:if test="${training:hasActiveTrainingSession(pageContext.request, card_group_id)}">
            <form method="get" action="${pageContext.request.contextPath}/training-continue">
                <input type="hidden" name="card_group_id" value="${card_group_id}">
                <button type="submit">Продолжить</button>
            </form>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/training-reset">
            <input type="hidden" name="card_group_id" value="${card_group_id}">
            <button type="submit" onclick="return confirm('Вы уверены, что хотите сбросить время карточек?')">Сбросить</button>
        </form>
    </div>

    <button type="button" id="show_button">Показать карты</button>

    <div id="cards" style="display: none;">
        <c:forEach var="card" items="${cards}">
            <my:hub-card card="${card}"/>
        </c:forEach>
    </div>
</body>