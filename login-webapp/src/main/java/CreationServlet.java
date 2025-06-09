import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreationServlet", urlPatterns = {"/CreationServlet"})
public class CreationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("createAccount.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AccountManager manager = (AccountManager) request.getServletContext()
                .getAttribute(AccountManager.ATTRIBUTE_NAME);

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        RequestDispatcher rd;
        if (!manager.accountExists(username)) {
            manager.addAccount(username, password);
            rd = request.getRequestDispatcher("WelcomePage.jsp");
        } else {
            rd = request.getRequestDispatcher("nameUsed.jsp");
        }
        rd.forward(request, response);
    }
}
