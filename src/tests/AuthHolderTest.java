package tests;

import main.AuthHolder;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.security.MessageDigest;


public class AuthHolderTest {
    private PrintWriter testFeedback;

    public static void main(String[] args) throws Exception {
        File file = new File("authholder_test_feedback.txt");
        PrintWriter testFeedback = new PrintWriter(file);
        AuthHolderTest tester = new AuthHolderTest(testFeedback);

        tester.runTests();

        testFeedback.close();

        // Print the feedback file contents to the console
        List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
        lines.forEach(System.out::println);
    }

    public AuthHolderTest(PrintWriter testFeedback) {
        this.testFeedback = testFeedback;
    }

    public void runTests() {
        msg("Starting AuthHolder Tests...");

        int errorCount = 0;

        // Test creation and password hashing
        errorCount += testAuthHolderCreation();

        // Test password verification
        errorCount += testPasswordVerification();

        // Test equals and compareTo methods
        errorCount += testEqualsAndCompareTo();

        // Summary
        if (errorCount == 0) {
            msg("All tests passed!");
        } else {
            msg("Total errors: " + errorCount);
        }
    }

    private int testAuthHolderCreation() {
        int errors = 0;
        try {
            AuthHolder authHolder = new AuthHolder("johndoe", "mypassword");

            // Check if username is correctly set
            if (!"johndoe".equals(authHolder.getUsername())) {
                msg("FAIL: getUsername() expected 'johndoe', got '" + authHolder.getUsername() + "'");
                errors++;
            } else {
                msg("PASS: getUsername() returned expected value 'johndoe'.");
            }

            // Check if the password hash is generated
            String expectedHash = hashPassword("mypassword");
            if (!authHolder.getPasswordHash().equals(expectedHash)) {
                msg("FAIL: Password hash did not match expected hash.");
                errors++;
            } else {
                msg("PASS: Password hash matched expected hash.");
            }

        } catch (Exception e) {
            msg("FAIL: Exception during testAuthHolderCreation: " + e.getMessage());
            errors++;
        }
        return errors;
    }

    private int testPasswordVerification() {
        int errors = 0;
        try {
            AuthHolder authHolder = new AuthHolder("johndoe", "mypassword");

            // Verify correct password
            if (!authHolder.verifyPassword("mypassword")) {
                msg("FAIL: verifyPassword('mypassword') should return true.");
                errors++;
            } else {
                msg("PASS: verifyPassword('mypassword') returned true as expected.");
            }

            // Verify incorrect password
            if (authHolder.verifyPassword("wrongpassword")) {
                msg("FAIL: verifyPassword('wrongpassword') should return false.");
                errors++;
            } else {
                msg("PASS: verifyPassword('wrongpassword') returned false as expected.");
            }

        } catch (Exception e) {
            msg("FAIL: Exception during testPasswordVerification: " + e.getMessage());
            errors++;
        }
        return errors;
    }

    private int testEqualsAndCompareTo() {
        int errors = 0;
        try {
            AuthHolder authHolder1 = new AuthHolder("johndoe", "mypassword");
            AuthHolder authHolder2 = new AuthHolder("johndoe", "mypassword");
            AuthHolder authHolder3 = new AuthHolder("janedoe", "password123");

            // Test equality
            if (!authHolder1.equals(authHolder2)) {
                msg("FAIL: authHolder1 should be equal to authHolder2.");
                errors++;
            } else {
                msg("PASS: authHolder1 is equal to authHolder2.");
            }

            if (authHolder1.equals(authHolder3)) {
                msg("FAIL: authHolder1 should not be equal to authHolder3.");
                errors++;
            } else {
                msg("PASS: authHolder1 is not equal to authHolder3.");
            }

            // Test compareTo
            if (authHolder1.compareTo(authHolder2) != 0) {
                msg("FAIL: compareTo should return 0 for equal AuthHolders.");
                errors++;
            } else {
                msg("PASS: compareTo returned 0 for equal AuthHolders.");
            }

            if (authHolder1.compareTo(authHolder3) >= 0) {
                msg("FAIL: compareTo should return a negative value for authHolder1 compared to authHolder3.");
                errors++;
            } else {
                msg("PASS: compareTo returned a negative value as expected for authHolder1 compared to authHolder3.");
            }

        } catch (Exception e) {
            msg("FAIL: Exception during testEqualsAndCompareTo: " + e.getMessage());
            errors++;
        }
        return errors;
    }

    private String hashPassword(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private void msg(String message) {
        testFeedback.write(message + "\n");
        System.out.println(message);
    }
}
