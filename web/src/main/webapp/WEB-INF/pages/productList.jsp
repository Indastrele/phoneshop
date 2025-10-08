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
    Found <c:out value="${phones.size()}"/> phones.
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
  </p>
</body>
