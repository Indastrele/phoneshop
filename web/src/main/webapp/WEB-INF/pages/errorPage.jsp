<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<tags:master>
    <h1>Error</h1>
    <hr>
    <span>${message}</span>
    <br>
    <a href="${pageContext.request.contextPath}/productList">&larr; Back to product list</a>
</tags:master>
