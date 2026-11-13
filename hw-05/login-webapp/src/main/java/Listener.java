import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Listener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        AccountManager manager = new AccountManager();
        event.getServletContext().setAttribute(AccountManager.ATTRIBUTE_NAME, manager);
        System.out.println("AccountManager initialized and stored in context.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        event.getServletContext().removeAttribute(AccountManager.ATTRIBUTE_NAME);
        System.out.println("AccountManager removed from context.");
    }
}
