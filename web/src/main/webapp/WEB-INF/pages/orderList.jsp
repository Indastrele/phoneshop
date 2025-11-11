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
    <div>
        <tags:account-header />
        <br/>
        <a onclick="history.back()" style="color: blue; text-decoration: underline; cursor: pointer;">
            &larr; Back to previous page
        </a>
        <br/>
        <span>Orders</span>
    </div>
    <div>
        <table class="small-phone-info-block">
            <thead>
            <tr>
                <td>Order numbers</td>
                <td>Customer</td>
                <td>Phone</td>
                <td>Address</td>
                <td>Date</td>
                <td>Total price</td>
                <td>Status</td>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="order" items="${orderList}">
                <tr>
                    <td><a href="${pageContext.request.contextPath}/admin/orders/${order.id}">${order.id}</a></td>
                    <td>${order.firstName} ${order.lastName}</td>
                    <td>${order.contactPhoneNo}</td>
                    <td>${order.deliveryAddress}</td>
                    <td>${order.timeOfCreation}</td>
                    <td>${order.totalPrice}</td>
                    <td>${order.status}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
