<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Create Account</title>
    </head>
    <body>
      <h1>Create New Account</h1>
      <p>Please enter proposed name and password.</p>

        <form action = "CreationServlet" method = "post">
            <label for = "username">Username:</label>
            <input type = "text" id = "username" name = "username"><br/><br/>

            <label for = "password">Password:</label>
            <input type = "password" id = "password" name = "password">
            <input type = "submit"  name = "Login" value = "Login"><br/><br/>
        </form>
    </body>
</html>