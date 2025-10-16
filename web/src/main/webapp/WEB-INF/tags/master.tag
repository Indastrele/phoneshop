<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>

<html>
<head>
    <title>Phonify</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/scripts/script.js"></script>
    <link href="${pageContext.request.contextPath}/resources/styles/style.css" rel="stylesheet">
</head>
<body>
<div>
    <a href="${pageContext.request.contextPath}/cart" id="minicart-hyperlink">
        <b>Cart: <span id="cart-total-quantity"></span> items, <span id="cart-total-cost"></span>$</b>
    </a>
    <hr>
    <main>
        <jsp:doBody/>
    </main>
</div>
</body>
</html>
