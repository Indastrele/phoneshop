<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<head>
  <title>Phonify</title>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script src="${pageContext.request.contextPath}/resources/scripts/script.js"></script>
</head>
<body>
  <p>
    Hello from product list!
  </p>
  <span>
    <h3>
      Cart: <span id="cart-total-quantity"></span> items, <span id="cart-total-cost"></span>$
    </h3>
  </span>
  <form action="${pageContext.request.contextPath}/productList" method="get">
    <label>
      Search <input id="query" name="query" type="text" value="${param.query}">
    </label>
    <button>Search</button>
  </form>
  <p>
    Found <c:out value="${phones.size()}"/> phones from <c:out value="${numberOfPhones}"/>.
    <table border="1px">
      <thead>
        <tr>
          <td>Image</td>
          <td>Brand
            <tags:sortLink order="asc" sort="brand" symbol="^"/>
            <tags:sortLink order="desc" sort="brand" symbol="v"/>
          </td>
          <td>Model
            <tags:sortLink order="asc" sort="model" symbol="^"/>
            <tags:sortLink order="desc" sort="model" symbol="v"/>
          </td>
          <td>Color</td>
          <td>Display size
            <tags:sortLink order="asc" sort="display_size" symbol="^"/>
            <tags:sortLink order="desc" sort="display_size" symbol="v"/>
          </td>
          <td>Price
            <tags:sortLink order="asc" sort="price" symbol="^"/>
            <tags:sortLink order="desc" sort="price" symbol="v"/>
          </td>
          <td>Quantity</td>
          <td></td>
        </tr>
      </thead>
      <c:forEach var="phone" items="${phones}">
        <tr>
          <td>
            <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
          </td>
          <td>${phone.brand}</td>
          <td>${phone.model}</td>
          <td><c:forEach var="color" items="${phone.colors}">
            ${color.code}
          </c:forEach></td>
          <td>${phone.displaySizeInches}"</td>
          <td>$ ${phone.price}</td>
          <td><input id="quantity${phone.id}" min="1" type="text" value="1"></td>
          <td><button class="add-to-cart-button"
                      onclick="clickToAdd(${phone.id},'${pageContext.request.contextPath}/ajaxCart')">
            Add to cart
          </button></td>
        </tr>
      </c:forEach>
    </table>

  <c:choose>
    <c:when test="${currentPage < 6}">
      <c:set var="start" value="1"/>
    </c:when>
    <c:otherwise>
      <c:set var="start" value="${currentPage-5}"/>
    </c:otherwise>
  </c:choose>

  <c:choose>
    <c:when test="${currentPage > numberOfPages - 5}">
      <c:set var="end" value="${numberOfPages}"/>
    </c:when>
    <c:otherwise>
      <c:set var="end" value="${currentPage+5}"/>
    </c:otherwise>
  </c:choose>

  <table border="1" cellpadding="5" cellspacing="5">
    <tr>
      <c:if test="${currentPage != 1}">
        <td><tags:pageLink pageNumber="1" symbol="&laquo;"/></td>
        <td><tags:pageLink pageNumber="${currentPage-1}" symbol="&lt;"/></td>
        <td>...</td>
      </c:if>
      <c:forEach begin="${start}" end="${end}" var="i">
        <c:choose>
          <c:when test="${currentPage eq i}">
            <td>${i}</td>
          </c:when>
          <c:otherwise>
            <td><tags:pageLink pageNumber="${i}" symbol="${i}"/></td>
          </c:otherwise>
        </c:choose>
      </c:forEach>
      <c:if test="${currentPage lt numberOfPages}">
        <td>...</td>
        <td><tags:pageLink pageNumber="${currentPage+1}" symbol="&gt;"/></td>
        <td><tags:pageLink pageNumber="${numberOfPages}" symbol="&raquo;"/></td>
      </c:if>
    </tr>
  </table>
  </p>
</body>
