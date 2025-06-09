import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    public static final String ATTRIBUTE_NAME = "Account_Manager";

    private final Map<String, String> accounts;

    public AccountManager() {
        accounts = new HashMap<>();
        preloadAccounts();
    }

    private void preloadAccounts() {
        // Preloaded test users
        addAccount("Patrick", "1234");
        addAccount("Molly", "FloPup");
    }

    public boolean accountExists(String username) {
        return accounts.containsKey(username);
    }

    public boolean checkCredentials(String username, String password) {
        return accountExists(username) && accounts.get(username).equals(password);
    }

    public void addAccount(String username, String password) {
        accounts.put(username, password);
    }
}
