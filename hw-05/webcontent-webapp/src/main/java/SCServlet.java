import data.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Map;

@WebServlet("/SCServlet")
public class SCServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to shopping cart JSP
        request.getRequestDispatcher("ShoppingCart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get database from servlet context
        DataBase db = (DataBase) request.getServletContext().getAttribute("database");

        // Get or create shopping cart in session
        HttpSession session = request.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("shoppingCart", cart);
        }

        String productId = request.getParameter("id");

        RequestDispatcher dispatcher;
        if (productId != null) {
            // Add a product by ID
            try {
                Product product = db.getProduct(productId);
                if (product != null) {
                    cart.addItem(product);
                }
            } catch (SQLException e) {
                throw new ServletException("Error fetching product", e);
            }
        } else {
            // Update quantities
            Map<String, String[]> paramMap = request.getParameterMap();
            for (String key : paramMap.keySet()) {
                try {
                    int quantity = Integer.parseInt(paramMap.get(key)[0]);
                    Product product = db.getProduct(key);
                    if (product != null) {
                        cart.updateItem(product, quantity);
                    }
                } catch (NumberFormatException | SQLException e) {
                    e.printStackTrace(); // Invalid input or DB error
                }
            }
        }

        dispatcher = request.getRequestDispatcher("ShoppingCart.jsp");
        dispatcher.forward(request, response);
    }
}
