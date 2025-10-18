<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title>Hub cards</title>
</head>
<body>
    <%@ include file="header.jsp" %>

    <script src="${pageContext.request.contextPath}/js/hub-card.js"></script>

    <div>
        <form method="get" action="${pageContext.request.contextPath}/training-start">
            <input type="hidden" name="card_group_id" value="${card_group_id}">
            <button type="submit">Тренировать</button>
        </form>

        <form method="post" action="${pageContext.request.contextPath}/drop">
            <input type="hidden" name="card_group_id" value="${card_group_id}">
            <button type="submit">Сбросить</button>
        </form>
    </div>

    <button type="button" id="show_button">Показать карты</button>

    <div id="cards" style="display: none;">
        <c:forEach var="card" items="${cards}">
            <my:hub-card card="${card}"/>
        </c:forEach>
    </div>
</body>