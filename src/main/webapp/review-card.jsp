<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title>Review card</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <script src="${pageContext.request.contextPath}/js/review-card.js"></script>

    <div id="question">
        <p>${card.question}</p>
    </div>

    <div id="answer" style="display: none;">
        <p>${card.answer}</p>
    </div>

    <button type="button" id="show_button">Показать ответ</button>

    <form method="post" action="${pageContext.request.contextPath}/training-continue">
        <input type="hidden" name="card_id" value="${card.id}">
        <div id="difficulty_choose" style="display: none;">
            <div class="difficulty_div">
                <button type="submit" name="action" value="very_easy">Очень легко</button>
            </div>
            <div class="difficulty_div">
                <button type="submit" name="action" value="easy">Легко</button>
            </div>
            <div class="difficulty_div">
                <button type="submit" name="action" value="medium">Средне</button>
            </div>
            <div class="difficulty_div">
                <button type="submit" name="action" value="hard">Сложно</button>
            </div>
        </div>
    </form>
</body>
</html>
