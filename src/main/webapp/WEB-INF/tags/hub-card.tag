<%@ tag description="Hub Card Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="card" required="true" type="ru.itis.example.models.Card" %>

<div class="card">
    <h3>Вопрос</h3>
    <p>${card.question}</p>
    <h3>Ответ</h3>
    <p>${card.answer}</p>
</div>