<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Information Incorrect</title>
    </head>
    <body>
    <h1>Please Try Again</h1>

    <p>Either your username or password is incorrect. Please try again.</p>

    <form action="LoginServlet" method ="post">
        User Name: <input type="text" name="username"/> <br/>
        Password: <input type = "text" name="password"/>
        <input type="submit" value="login"/> <br/>
    </form>
    <a href="createAccount.jsp">Create New Account</a>
    </body>
</html>