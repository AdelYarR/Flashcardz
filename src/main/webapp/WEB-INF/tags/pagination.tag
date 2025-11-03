<%@ tag description="Pagination Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="page" required="true" type="java.lang.Integer" %>
<%@ attribute name="totalPages" required="true" type="java.lang.Integer" %>

<c:if test="${empty page}">
    <c:set var="page" value="1" />
</c:if>

<div class="pagination">
    <c:if test="${page > 1}">
        <a href="?page=${page - 1}">Предыдущая</a>
    </c:if>
    <c:if test="${page < total_pages}">
        <a href="?page=${page + 1}">Следующая</a>
    </c:if>
</div>
