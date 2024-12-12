package tests;

import main.AuthHolder;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
        msg("Starting AuthHolder Tests", true, "");

        int errorCount = 0;

        // Test 1: Create AuthHolder
        try {
            AuthHolder authHolder = new AuthHolder("test_user", "password123");
            msg("AuthHolder created successfully", true, "");
        } catch (Exception e) {
            msg("AuthHolder creation failed", false, e.getMessage());
            errorCount++;
        }

        // Test 2: Verify Password
        try {
            AuthHolder authHolder = new AuthHolder("test_user", "password123");
            if (authHolder.verifyPassword("password123")) {
                msg("Password verification successful", true, "");
            } else {
                msg("Password verification failed", false, "");
                errorCount++;
            }
        } catch (Exception e) {
            msg("Password verification failed", false, e.getMessage());
            errorCount++;
        }

        // Test 3: Incorrect Password
        try {
            AuthHolder authHolder = new AuthHolder("test_user", "password123");
            if (!authHolder.verifyPassword("wrong_password")) {
                msg("Incorrect password verification failed", true, "");
            } else {
                msg("Incorrect password verification succeeded", false, "");
                errorCount++;
            }
        } catch (Exception e) {
            msg("Incorrect password verification failed", false, e.getMessage());
            errorCount++;
        }

        // Summary
        if (errorCount == 0) {
            msg("All tests passed!", true, "");
        } else {
            msg("Total errors: " + errorCount, false, "");
        }
    }

    private void msg(String testName, boolean passed, String additionalInfo) {
        String message = "PASS: " + testName;
        if (!passed) {
            message = "FAIL: " + testName;
        }
        if (!additionalInfo.isEmpty()) {
            message += " (" + additionalInfo + ")";
        }
        testFeedback.write(message + "\n");
        System.out.println(message);
    }
}