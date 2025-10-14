<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="false" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="value" required="true" %>
<%@ attribute name="dimension" required="false" %>

<tr>
    <td>${name}</td>
    <td>
        <c:if test="${not empty value}">
            ${value} ${dimension}
        </c:if>
    </td>
</tr>
