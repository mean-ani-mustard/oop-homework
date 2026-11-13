import junit.framework.TestCase;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BankTests extends TestCase {

    private static final String TEST_FILE = "test_transactions.txt";
    private static final int NUM_THREADS = 2;
    private final PrintStream originalOut = System.out;

    @Override
    protected void setUp() throws Exception {
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Override
    protected void tearDown() throws Exception {
        Files.deleteIfExists(Paths.get(TEST_FILE));
        System.setOut(originalOut);  // Restore original System.out
    }

    private String runBankAndCaptureOutput(List<String> transactions) throws Exception {
        // Write transactions to test file
        Files.write(Paths.get(TEST_FILE), transactions);

        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Bank bank = new Bank();
        bank.run(TEST_FILE, NUM_THREADS);

        System.setOut(originalOut);
        return outContent.toString();
    }

    public void testBasicTransactionProcessing() throws Exception {
        List<String> lines = Arrays.asList("0 1 200");
        String output = runBankAndCaptureOutput(lines);

        assertTrue(output.contains("Account 0: balance=800"));
        assertTrue(output.contains("Account 1: balance=1200"));
    }

    public void testInvalidTransactionIgnored() throws Exception {
        List<String> lines = Arrays.asList("0 1 -100");  // Negative transaction
        String output = runBankAndCaptureOutput(lines);

        assertTrue(output.contains("Account 0: balance=1000"));
        assertTrue(output.contains("Account 1: balance=1000"));
    }

    public void testMultipleTransactions() throws Exception {
        List<String> lines = Arrays.asList(
                "0 1 100",  // 0 → 1: 100
                "1 2 50",   // 1 → 2: 50
                "2 0 25"    // 2 → 0: 25
        );
        String output = runBankAndCaptureOutput(lines);

        assertTrue(output.contains("Account 0: balance=925"));
        assertTrue(output.contains("Account 1: balance=1050"));
        assertTrue(output.contains("Account 2: balance=1025"));
    }
    public void testMain() throws Exception {
        String testFile = "test_main_transactions.txt";

        // Prepare sample transaction file
        List<String> lines = Arrays.asList("0 1 250", "1 2 100");
        Files.write(Paths.get(testFile), lines);

        // Capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Run Bank.main with test arguments
        Bank.main(new String[]{testFile, "2"});

        // Restore original output stream
        System.setOut(originalOut);

        String output = outContent.toString();

        // Assert balances from expected transactions:
        // Account 0: 1000 - 250 = 750
        // Account 1: 1000 + 250 - 100 = 1150
        // Account 2: 1000 + 100 = 1100
        assertTrue(output.contains("Account 0: balance=750"));
        assertTrue(output.contains("Account 1: balance=1150"));
        assertTrue(output.contains("Account 2: balance=1100"));

        Files.deleteIfExists(Paths.get(testFile));
    }
    public void testMainEmpty() throws InterruptedException{
        OutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os));
        Bank.main(new String[]{});
        String out = os.toString();
        out = out.substring(0, out.length() - 1);
        assertEquals("Args: transaction-file [num-workers [limit]]", out);
    }



}
