<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master>
    <h2>Cart</h2>
    <a href="${pageContext.request.contextPath}/productList">&larr; Back to product list</a>
    <form:form method="POST" action="${pageContext.request.contextPath}/cart" modelAttribute="cartForm">
        <input type="hidden" name="_method" value="PUT"/>
        <table class="small-phone-info-block">
            <thead>
                <tr>
                    <td>Brand</td>
                    <td>Model</td>
                    <td>Color</td>
                    <td>Display size</td>
                    <td>Price</td>
                    <td>Quantity</td>
                    <td>Action</td>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="cartItem" items="${cart.cartItems}" varStatus="status">
                    <tr>
                        <td>${cartItem.phone.brand}</td>
                        <td>${cartItem.phone.model}</td>
                        <td>
                            <c:forEach var="color" items="${cartItem.phone.colors}" varStatus="innerLoopStatus">
                                ${color.code}<c:if test="${!innerLoopStatus.last}">, </c:if>
                            </c:forEach>
                        </td>
                        <td>${cartItem.phone.displaySizeInches}''</td>
                        <td>${cartItem.phone.price}$</td>
                        <td>
                            <form:hidden path="cartItemFormList[${status.index}].phoneId"/>
                            <form:input path="cartItemFormList[${status.index}].quantity" />
                            <form:errors path="cartItemFormList[${status.index}].quantity" element="div"/>
                        </td>
                        <td>
                            <button name="deleteCartItem" value="${cartItem.phone.id}" form="delete-form">Delete</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <span>
            <button>Update</button>
        </span>
        <c:if test="${errorMessage != null}">
            <span style="color: red;">${errorMessage}</span>
        </c:if>
    </form:form>
    <form action="${pageContext.request.contextPath}/cart" method="post" id="delete-form">
        <input type="hidden" name="_method" value="DELETE"/>
    </form>
    <span>
        <button>Order</button>
    </span>
</tags:master>
