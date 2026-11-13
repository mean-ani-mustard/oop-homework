import data.ShoppingCart;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class Listener implements HttpSessionListener {

    private static final String CART_ATTRIBUTE = "shoppingCart";

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        // Initialize a new ShoppingCart when the session is created
        event.getSession().setAttribute(CART_ATTRIBUTE, new ShoppingCart());
        System.out.println("Session created: ShoppingCart initialized.");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // Clean up the ShoppingCart attribute when the session is destroyed
        event.getSession().removeAttribute(CART_ATTRIBUTE);
        System.out.println("Session destroyed: ShoppingCart removed.");
    }
}
