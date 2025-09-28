<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 28.09.2025
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Title</title>
    <%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
  </head>
  <body>
    <c:forEach var="user" items="${users}">
      <li>
        ID: ${user.id}, Name: ${user.name}
      </li>
    </c:forEach>
  </body>
</html>
