<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.es.core.model.order.OrderStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title>Phonify</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles/style.css">
</head>
<body>
<a href="${pageContext.request.contextPath}/admin/orders">&larr; Back to orders</a>
<div style="display: flex; justify-content: space-between;">
    <div>
        Order: ${order.id}
    </div>
    <div>
        Order status: ${order.status}
    </div>
</div>
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
<div style="border: 1px solid lightgray; padding: 3%; margin: 1%; width: max-content;">
    <div>
        <label>First name*: ${order.firstName}</label>
    </div>
    <br>
    <div>
        <label>Last name*: ${order.lastName}</label>
    </div>
    <br>
    <div>
        <label>Address*: ${order.deliveryAddress}</label>
    </div>
    <br>
    <div>
        <label>Phone*: ${order.contactPhoneNo}</label>
    </div>
    <br>
    <textarea rows="5" cols="35" readonly="readonly">${order.additionalInformation}</textarea>
</div>
<br>
<c:if test="${order.status == 'NEW'}">
    <div>
        <form action="${pageContext.request.contextPath}/admin/orders/${order.id}" method="post">
            <input hidden="hidden" name="_method" value="PATCH">
            <button name="status" value="delivered">Delivered</button>
            <button name="status" value="rejected">Rejected</button>
        </form>
    </div>
</c:if>
</body>
</html>
