<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title>Options</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <%@ include file="header.jsp" %>

    <h2>Интервалы повторения</h2>

    <form method="post" action="${pageContext.request.contextPath}/options">
        <my:interval interval="${very_easy}"></my:interval>
        <my:interval interval="${easy}"></my:interval>
        <my:interval interval="${medium}"></my:interval>
        <my:interval interval="${hard}"></my:interval>

        <button type="submit">Изменить</button>
    </form>
</body>
</html>
