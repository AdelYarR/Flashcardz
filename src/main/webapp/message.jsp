<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Message</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <%@ include file="header.jsp" %>

    <div>
        <c:choose>
            <c:when test="${not empty message}">
                ${message}
            </c:when>
            <c:otherwise>
                Произошла непредвиденная ошибка.
            </c:otherwise>
        </c:choose>
    </div>
    <a href="${pageContext.request.contextPath}/hub/groups">Вернуться к группам</a>
</body>
</html>
