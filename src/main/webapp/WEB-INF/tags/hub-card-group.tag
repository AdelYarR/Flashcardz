<%@ tag description="Card Group Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="card_group" required="true" type="ru.itis.example.models.CardGroup" %>

<a href="${pageContext.request.contextPath}/hub/cards?card_group_id=${card_group.id()}">
    <div class="card_group">
        <h3>${card_group.name()}</h3>
        <p>Автор: ${card_group.authorName()}</p>
    </div>
</a>