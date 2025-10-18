<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title>Update group</title>
</head>
<body>
    <script src="${pageContext.request.contextPath}/js/profile-card.js"></script>

    <%@ include file="header.jsp" %>

    <form method="post" action="${pageContext.request.contextPath}/profile/update-group">
        <input type="hidden" name="card_group_id" value="${card_group_id}">

        <div>
            <label>Название группы</label>
            <input type="text" name="card_group_name" value="${card_group_name}" required>
        </div>

        <h3>Карточки:</h3>
        <div id="cards_container">
            <div class="card_item">
                <label>Вопрос:</label>
                <textarea id="question_textarea" placeholder="Введите текст вопроса..."></textarea>
                <label>Ответ:</label>
                <textarea id="answer_textarea" placeholder="Введите текст ответа..."></textarea>
                <button type="button" onclick="addCard()">+</button>
            </div>

            <c:forEach var="card" items="${cards}">
                <my:profile-card card="${card}"/>
            </c:forEach>
        </div>

        <button type="submit">Сохранить все изменения</button>
        <a href="${pageContext.request.contextPath}/profile/groups">Отменить</a>
    </form>
</body>
</html>