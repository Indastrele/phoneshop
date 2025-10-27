<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title>Phonify</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles/style.css">
</head>
<body>
    <h2>Order</h2>
    <a href="${pageContext.request.contextPath}/cart">&larr; Back to cart</a>
    <c:if test="${errorMessage != null}">
        <span style="color: red;">${errorMessage}</span>
    </c:if>
    <table class="small-phone-info-block" style="width: 100%;">
        <thead>
        <tr>
            <td>Brand</td>
            <td>Model</td>
            <td>Color</td>
            <td>Display size</td>
            <td>Quantity</td>
            <td>Price</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${order.orderItems}">
            <tr>
                <td>${item.phone.brand}</td>
                <td>${item.phone.model}</td>
                <td>
                    <c:forEach var="color" items="${item.phone.colors}" varStatus="innerLoopStatus">
                        ${color.code}<c:if test="${!innerLoopStatus.last}">, </c:if>
                    </c:forEach>
                </td>
                <td>${item.phone.displaySizeInches} ''</td>
                <td>${item.quantity}</td>
                <td>${item.phone.price} $</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <table class="small-phone-info-block" style="float: right;">
        <tags:parameter name="Subtotal" value="${order.subtotal} $"/>
        <tags:parameter name="Delivery price" value="${order.deliveryPrice} $"/>
        <tags:parameter name="Total cost" value="${order.totalPrice} $"/>
    </table>

    <form:form method="POST" action="${pageContext.request.contextPath}/order" modelAttribute="orderForm">
        <div style="border: 1px solid lightgray; padding: 3%; margin: 1%; width: max-content;">
            <div>
                <label>First name*: <form:input path="firstName"/></label>
                <form:errors path="firstName" cssStyle="color: red;"/>
            </div>
            <br>
            <div>
                <label>Last name*: <form:input path="lastName"/></label>
                <form:errors path="lastName" cssStyle="color: red;"/>
            </div>
            <br>
            <div>
                <label>Address*: <form:input path="address"/></label>
                <form:errors path="address" cssStyle="color: red;"/>
            </div>
            <br>
            <div>
                <label>Phone*: <form:input path="contactPhoneNo"/></label>
                <form:errors path="contactPhoneNo" cssStyle="color: red;"/>
            </div>
            <br>
            <form:textarea path="additionalInformation" rows="5" cols="35"/>
            <form:errors path="additionalInformation" cssStyle="color: red;"/>

            <button>Order</button>
        </div>
    </form:form>
</body>
</html>
