import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        AccountManager manager = (AccountManager) getServletContext().getAttribute(AccountManager.ATTRIBUTE_NAME);
        if (manager == null) {
            System.err.println("AccountManager is not available in servlet context.");
        }


        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();


        String targetPage;
        if (manager != null && manager.checkCredentials(username, password)) {
            targetPage = "WelcomePage.jsp";
        } else {
            targetPage = "retry.jsp";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(targetPage);
        dispatcher.forward(request, response);
    }
}
