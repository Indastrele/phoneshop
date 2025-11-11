<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ tag trimDirectiveWhitespaces="true" %>

<security:authorize access= "hasAnyRole('ROLE_ADMIN')" var="isUser"/>
<div>
    <c:if test= "${not isUser}">
        <span>
            <a style= "color: Green;" href= "<c:url value= "/login"/>">
                Login
            </a>
        </span>
    </c:if>

    <c:if test= "${isUser}">
        <span style= "padding-top: 15px; padding-bottom: 15px; color: green">
            admin
        </span>
        <span>
            <a href="${pageContext.request.contextPath}/admin/orders">Admin</a>
        </span>
        <span> <a style= "color: red;" href= "<c:url value= "/logout"/>">Logout</a> </span>
    </c:if>
</div>
<br/>