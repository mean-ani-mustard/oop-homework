
<%@ page import="data.DataBase" %>
<%@ page import="data.Product" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Student Store</title>
</head>
<body>
<h1>Student Store</h1>

<p>Items available:</p>
<ul>
    <% data.DataBase db = (data.DataBase) request.getServletContext().getAttribute("database");

        try {
            for(data.Product p : db.getProductList()){
                String s = "<li>" + "<a href=\"Product.jsp?id=" + p.getProductId() + "\"> "+ p.getProductName()+ " </a>" + "</li>";
                out.print(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    %>
</ul>
</body>
</html>