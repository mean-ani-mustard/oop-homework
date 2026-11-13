<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Please enter another user name and password</title>
    </head>
    <body>

    <h1>The name <%= request.getParameter("username")%> is already in use</h1>

    <p> Please enter another username and password.</p>

    <form action="CreationServlet" method="post">
        <label for = "username"> Username: </label>
        <input type="text" id = "username" name="name"/> <br/><br/>
        <label for = "password"> Password: </label>
        <input type="text" id = "password" name="password"/>
        <input type="submit" value="create"/> <br/><br/>
    </form>
    </body>


</html>