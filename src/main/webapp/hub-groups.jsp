<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title>Hub groups</title>
    <link rel="stylesheet" href="css/group-menu.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="card_groups">
        <c:forEach var="card_group" items="${card_groups}">
            <my:hub-card-group card_group="${card_group}"/>
        </c:forEach>
    </div>
</body>