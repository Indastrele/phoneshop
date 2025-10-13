<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageNumber" required="true" %>
<%@ attribute name="symbol" required="true" %>

<a href="<c:url value="productList">
<c:param name="page" value="${pageNumber}"/>
<c:param name="query" value="${param.query}"/>
<c:param name="field" value="${param.field}"/>
<c:param name="order" value="${param.order}"/>
</c:url>">
    ${symbol}
</a>
