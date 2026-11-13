<%--
  Created by IntelliJ IDEA.
  User: anighelekva
  Date: 09.06.25
  Time: 18:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="data.ShoppingCart" %>
<%@ page import="data.DataBase" %>
<%@ page import="data.Product" %>
<%@ page import="java.sql.SQLException" %>

<html>
<head>
    <title>Shopping Cart</title>
</head>
<h1>Shopping Cart</h1>
<body>
<form action="SCServlet" method="post">
    <ul>
        <% ShoppingCart sc = (ShoppingCart) request.getSession().getAttribute("shoppingCart");
            DataBase db = (DataBase) request.getServletContext().getAttribute("database");
            out.println("ani");
            for(String key: sc.getShoppingCart().keySet()){


                   String s = "<li>" + "<input type = \"number\" name = \""+ key + "\" value = " + sc.getShoppingCart().get(key) +">" +
                            db.getProduct(key).getProductName() + ", " + sc.getItemCost(db.getProduct(key)) + "</li>";

                out.println(s);
            }
        %>

    </ul>
    <p>Total:$<%=((ShoppingCart)request.getSession().getAttribute("shoppingCart")).getTotalCost()%></p>
    <input type = "submit" value = "Update Cart">
</form>
<a href="index.jsp">Continue Shopping</a>
</body>
</html>