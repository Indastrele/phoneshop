<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<head>
  <title>Phonify</title>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script>
    function clickToAdd(phoneId, url) {
      let quantity = $("#quantity"+phoneId).val();
      console.log(quantity);
      if (isNumericParseInt(quantity)) {
        $.ajax({
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          type: "POST",
          url: url,
          data: JSON.stringify({phoneId: phoneId, quantity: parseInt(quantity)}),
          dataType: "json",
          success: function (response) {
            $("#cart-quantity").text(response.quantity);
            alert("Item successfully added to cart");
          }
        }).fail(function () {
          alert("Cannot add item to cart");
        });
        return;
      }
      alert("Quantity can be only integer value");
    }

    function isNumericParseInt(str) {
      return String(parseInt(str)) === str;
    }
  </script>
</head>
<body>
  <p>
    Hello from product list!
  </p>
  <span>Cart: <span id="cart-quantity">${cartQuantity}</span></span>
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
        <td><a href="${pageContext.request.contextPath}/productList?page=${currentPage - 1}&query=${param.query}&field=${param.field}&order=${param.order}">&lt;</a></td>
        <td><a href="${pageContext.request.contextPath}/productList?page=${1}&query=${param.query}&field=${param.field}&order=${param.order}">&laquo;</a></td>
        <td>...</td>
      </c:if>
      <c:forEach begin="${start}" end="${end}" var="i">
        <c:choose>
          <c:when test="${currentPage eq i}">
            <td>${i}</td>
          </c:when>
          <c:otherwise>
            <td><a href="${pageContext.request.contextPath}/productList?page=${i}&query=${param.query}&field=${param.field}&order=${param.order}">${i}</a></td>
          </c:otherwise>
        </c:choose>
      </c:forEach>
      <c:if test="${currentPage lt numberOfPages}">
        <td>...</td>
        <td><a href="${pageContext.request.contextPath}/productList?page=${numberOfPages}&query=${param.query}&field=${param.field}&order=${param.order}">&raquo;</a></td>
        <td><a href="${pageContext.request.contextPath}/productList?page=${currentPage + 1}&query=${param.query}&field=${param.field}&order=${param.order}">&gt;</a></td>
      </c:if>
    </tr>
  </table>
  </p>
</body>
