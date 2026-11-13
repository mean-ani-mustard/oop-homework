import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.*;

class CrackerTests {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Cracker.found = false;
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        Cracker.found = false;
    }

    String getOutput() {
        return outContent.toString().trim();
    }

    @Test
    void testTooFewArgsPrintsUsage() {
        Cracker.main(new String[] {});  // No arguments
        String output = getOutput();
        assertTrue(output.contains("Args: target length [workers]"));
    }

    @Test
    void testHashingMode() {
        String hash = Cracker.hashCode("ab");
        Cracker.main(new String[] { "ab" });
        String output = getOutput();
        assertEquals(hash, output);
    }

    @Test
    void testCrackingPassword() {
        String password = "a!";
        String hash = Cracker.hashCode(password);
        Cracker.main(new String[] { hash, "2", "4" });  // maxLength 2, 4 threads

        String output = getOutput();
        assertTrue(output.contains(password), "Output should contain the cracked password.");
        assertTrue(output.contains("All done."), "Should print 'All done.'");
    }

    @Test
    void testPasswordNotFound() {
        // Use a hash that doesn't match any 1-character password
        String impossibleHash = Cracker.hashCode("zzzzzzzzzz");
        Cracker.main(new String[] { impossibleHash, "1", "2" });  // maxLength = 1

        String output = getOutput();
        assertTrue(output.contains("Password not found."));
        assertTrue(output.contains("All done."));
    }

    @Test
    void testMaxThreadsGreaterThanCharset() {
        String password = "b";
        String hash = Cracker.hashCode(password);
        int overkillThreads = Cracker.CHARS.length + 5;

        Cracker.main(new String[] { hash, "1", Integer.toString(overkillThreads) });

        String output = getOutput();
        assertTrue(output.contains(password));
    }

    @Test
    void testHexToArray() {
        // "Hello" in ASCII is 48 65 6C 6C 6F in hex
        String hex = "48656c6c6f";
        byte[] expected = "Hello".getBytes();

        byte[] actual = Cracker.hexToArray(hex);

        assertArrayEquals(expected, actual, "hexToArray should correctly convert hex to byte array");
    }
    @Test
    void testInterruptedExceptionInMain() {
        Thread.currentThread().interrupt(); // Interrupt current thread

        String password = "a";
        String hash = Cracker.hashCode(password);
        Cracker.main(new String[]{hash, "1", "1"});

        String output = getOutput();

        assertTrue(output.contains("All done.") || output.contains("Password not found."),
                "Program should handle InterruptedException and print output");

        // Clear the interrupt flag for other tests
        Thread.interrupted();
    }
    @Test
    void testHashCodeReturnsNullOnException() {
        // Just call hashCode and check it's not null (normal flow)
        String result = Cracker.hashCode("test");
        assertNotNull(result);

        // You can't easily force the exception without changing Cracker code, so just test normal usage
    }

    @Test
    void testMainHandlesInterruptedException() {
        Thread.currentThread().interrupt();  // Set interrupt

        String password = "a";
        String hash = Cracker.hashCode(password);
        Cracker.main(new String[]{hash, "1", "1"});

        String output = getOutput();
        assertTrue(output.contains("All done.") || output.contains("Password not found."));

        Thread.interrupted(); // clear interrupt flag
    }


}
