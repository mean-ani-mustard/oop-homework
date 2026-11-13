import data.DataBase;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class ShoppingListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        DataBase database = new DataBase();
        event.getServletContext().setAttribute("database", database);
        System.out.println("[ShoppingListener] Database initialized and set in context.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        event.getServletContext().removeAttribute("database");
        System.out.println("[ShoppingListener] Database removed from context.");
    }
}
