<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>
<%@ attribute name="symbol" required="true" %>

<a href="?field=${sort}&order=${order}&query=${param.query}"
   style="color: black;
    text-decoration: none;
    ${sort eq param.sort and order eq param.order ? 'font-weight: 950;' : ''}">
    ${symbol}
</a>