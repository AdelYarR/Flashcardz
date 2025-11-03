<%@ tag description="Profile Card Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="card" required="true" type="ru.itis.example.models.Card" %>

<div class="card">
    <input type="hidden" name="card_ids[]" value="${card.id}">
    <label>Вопрос:</label>
    <textarea type="text" name="questions[]" placeholder="Введите текст вопроса..." required>${card.question}</textarea>
    <label>Ответ:</label>
    <textarea type="text" name="answers[]" placeholder="Введите текст ответа..." required>${card.answer}</textarea>
    <button type="button" onclick="removeCard(this)">-</button>
</div>