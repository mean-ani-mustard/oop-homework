<%--
  Created by IntelliJ IDEA.
  User: anighelekva
  Date: 09.06.25
  Time: 18:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="data.DataBase" %>
<%@ page import="data.Product" %>

<html>
<%  String productId = request.getParameter("id");
    data.DataBase db = (DataBase) request.getServletContext().getAttribute("database");
    Product curr = db.getProduct(productId);
%>
<head>
    <title> <%=curr.getProductName()%></title>
</head>
<body>
<h1> <%=curr.getProductName()%></h1>
<img src="store-images/<%=curr.getImageFileName()%>" alt="Not Found">
<form action = "SCServlet" method = "post">
    <p> <%=Double.toString(curr.getProductPrice()) + "$"%></p>
    <input type="hidden" id="id" name="id" value="<%=curr.getProductId()%>">
    <input type = "submit" value = "Add to Cart">
</form>
</body>
</html>