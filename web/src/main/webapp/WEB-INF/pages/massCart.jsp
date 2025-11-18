<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master>
    <a href="${pageContext.request.contextPath}/productList">&larr; Back to product list</a>
    <div>
        <c:if test="${param.addedCount != null && param.addedCount > 0}">
            <span id="result" style="color: green;">${param.addedCount} were (was) added</span>
        </c:if>
        <br>
        <c:if test="${errorsFlag == true}">
            <span id="error" style="color: red;">There were some errors</span>
        </c:if>
    </div>
    <form:form method="POST" action="${pageContext.request.contextPath}/massCart" modelAttribute="massCartForm">
        <table>
            <thead>
            <tr>
                <td>Product code</td>
                <td>Quantity</td>
            </tr>
            </thead>
            <tbody>
                    <c:forEach var="i" begin="0" end="7" step="1">
                        <tr>
                            <td>
                                <form:input path="massCartItemFormList[${i}].code" />
                                <br>
                                <form:errors path="massCartItemFormList[${i}].code" element="div" cssStyle="color: red;"/>
                                <c:if test="${not empty productErrors}">
                                    <span style="color: red;">
                                        ${productErrors[massCartForm.massCartItemFormList[i].code]}
                                    </span>
                                </c:if>
                            </td>
                            <td>
                                <form:input path="massCartItemFormList[${i}].quantity" />
                                <form:errors path="massCartItemFormList[${i}].code" element="div" cssStyle="color: red;"/>
                                <br>
                                <c:if test="${not empty stockErrors}">
                                    <span style="color: red;">
                                            ${stockErrors[massCartForm.massCartItemFormList[i].code]}
                                    </span>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
            </tbody>
        </table>
        <button>Add to cart</button>
    </form:form>
</tags:master>
