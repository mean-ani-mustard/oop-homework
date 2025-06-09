import junit.framework.TestCase;

public class AccountManagerTester extends TestCase {

    public void testPreloadedAccounts() {
        AccountManager manager = new AccountManager();

        // Check that the preloaded users exist
        assertTrue("'Patrick' should exist", manager.accountExists("Patrick"));
        assertTrue("'Molly' should exist", manager.accountExists("Molly"));

        // Verify their credentials are correct
        assertTrue("'Patrick''s information should be valid", manager.checkCredentials("Patrick", "1234"));
        assertTrue("'Molly''s information should be valid", manager.checkCredentials("Molly", "FloPup"));

        // Sanity check with invalid credentials
        assertFalse("Wrong password for 'Patrick' should fail", manager.checkCredentials("Patrick", "wrongpass"));
        assertFalse("Unknown user - not  found", manager.accountExists("Unknown"));
    }

    public void testAddNewAccounts() {
        AccountManager manager = new AccountManager();

        // Add a few new accounts
        manager.addAccount("Nika", "12345");
        manager.addAccount("Emperor", "Dominance");
        manager.addAccount("nugzari", "Colors");

        // Ensure they were added successfully
        assertTrue("User 'Nika' should now exist", manager.accountExists("Nika"));
        assertTrue("User 'Emperor' should now exist", manager.accountExists("Emperor"));
        assertTrue("User 'nugzari' should now exist", manager.accountExists("nugzari"));

        // Validate credentials for newly added accounts
        assertTrue("Credentials for 'Nika' should be valid", manager.checkCredentials("Nika", "12345"));
        assertTrue("Credentials for 'Emperor' should be valid", manager.checkCredentials("Emperor", "Dominance"));
        assertTrue("Credentials for 'nugzari' should be valid", manager.checkCredentials("nugzari", "Colors"));

        // Negative test cases
        assertFalse("Incorrect password for 'Nika' should fail", manager.checkCredentials("Nika", "Colors"));
        assertFalse("Non-existent user 'blabla' should not exist", manager.accountExists("blabla"));
    }

    public void testOverwritingAccounts() {
        AccountManager manager = new AccountManager();

        // Add an account, then overwrite it
        manager.addAccount("Neo", "Matrix");
        assertTrue("User 'Neo' should be added", manager.accountExists("Neo"));
        assertTrue("Initial credentials for 'Neo' should be valid", manager.checkCredentials("Neo", "Matrix"));

        // Overwrite password
        manager.addAccount("Neo", "Reloaded");
        assertTrue("Overwritten credentials for 'Neo' should be valid", manager.checkCredentials("Neo", "Reloaded"));
        assertFalse("Old credentials for 'Neo' should no longer work", manager.checkCredentials("Neo", "Matrix"));
    }
}
