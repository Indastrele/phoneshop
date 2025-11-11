<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Phonify - Login</title>
</head>
<body>
<a href="${pageContext.request.contextPath}/productList">&larr; Back to product list</a>
<br/>
<form action="<c:url value='/login'/>" method="post">
    <div>
        <label>Username:</label>
        <input type="text" name="username"/>
    </div>
    <div>
        <label>Password:</label>
        <input type="password" name="password"/>
    </div>
    <br/>
    <div>
        <c:if test="${param.error != null}">
            <span style="color: red;">Invalid username or password</span>
            <br/>
        </c:if>
        <button>Login</button>
    </div>
</form>
</body>
</html>
