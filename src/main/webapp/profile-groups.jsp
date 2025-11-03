<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title>Profile groups</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <div>
        <a href="${pageContext.request.contextPath}/profile/groups">Мои группы</a>
        <a href="${pageContext.request.contextPath}/profile/add-group">Добавить</a>
    </div>

    <div class="card_groups">
        <c:forEach var="card_group" items="${card_groups}">
            <my:profile-card-group card_group="${card_group}"/>
        </c:forEach>
    </div>

    <my:pagination page="${page}" totalPages="${total_pages}" />
</body>
</html>
