<%@ tag description="Card Group Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="card_group" required="true" type="ru.itis.example.models.CardGroup" %>

<div class="card_group">
    <a href="${pageContext.request.contextPath}/profile/cards?card_group_id=${card_group.id()}">
        <h3>${card_group.name()}</h3>
    </a>

    <c:choose>
        <c:when test="${not card_group.uploaded()}">
            <form method="post" action="${pageContext.request.contextPath}/profile/publish-group">
                <input type="hidden" name="card_group_id" value="${card_group.id()}">
                <button type="submit" onclick="return confirm('Вы уверены, что хотите опубликовать эту группу?')">Опубликовать</button>
            </form>
        </c:when>
        <c:otherwise>
            <p>Опубликовано</p>
        </c:otherwise>
    </c:choose>

    <form method="get" action="${pageContext.request.contextPath}/profile/update-group">
        <input type="hidden" name="card_group_id" value="${card_group.id()}">
        <input type="hidden" name="card_group_name" value="${card_group.name()}">
        <button type="submit">Изменить</button>
    </form>

    <form method="post" action="${pageContext.request.contextPath}/profile/delete-group">
        <input type="hidden" name="card_group_id" value="${card_group.id()}">
        <button type="submit" onclick="return confirm('Вы уверены, что хотите удалить эту группу?')">Удалить</button>
    </form>
</div>
