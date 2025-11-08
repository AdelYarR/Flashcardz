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
    <div class="message_div">
        <c:choose>
            <c:when test="${not empty message}">
                <p>${message}</p>
            </c:when>
            <c:otherwise>
                <p>Sorry! An unexpected error occurred.</p>
            </c:otherwise>
        </c:choose>
    </div>
    <a class="back_a" href="${pageContext.request.contextPath}/hub/groups">Вернуться</a>
</body>
</html>
