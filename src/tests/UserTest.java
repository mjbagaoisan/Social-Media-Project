package tests;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import dataStructures.LinkedList;
import java.util.ArrayList;
import main.User;
import main.UserBST;



public class UserTest {
    private PrintWriter testFeedback;

    public static void main(String[] args) throws Exception {
        File file = new File("user_test_feedback.txt");
        PrintWriter testFeedback = new PrintWriter(file);
        UserTest tester = new UserTest(testFeedback);

        tester.runTests();

        testFeedback.close();

        // Print the feedback file contents to the console
        List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
        lines.forEach(System.out::println);
    }

    public UserTest(PrintWriter testFeedback) {
        this.testFeedback = testFeedback;
    }

    public void runTests() {
        msg("Starting User Tests...");

        int errorCount = 0;

        // Prepare a test user
        User testUser = null;
        try {
            LinkedList<String> interests = new LinkedList<>();
            interests.addLast("Reading");
            interests.addLast("Coding");

            testUser = new User("John", "Doe", "johndoe", "mypassword", 1, "New York", interests, null);
            msg("PASS: Successfully created a User instance.");
        } catch (Exception e) {
            msg("FAIL: Exception creating User instance: " + e.getMessage());
            errorCount++;
        }

        // Test User getters
        if (testUser != null) {
            errorCount += testUserGetters(testUser);
            errorCount += testUserInterests(testUser);
        }

        // Summary
        if (errorCount == 0) {
            msg("All tests passed!");
        } else {
            msg("Total errors: " + errorCount);
        }
    }

    private int testUserGetters(User user) {
        int errors = 0;

        // Test full name
        if (!"John Doe".equals(user.getFullName())) {
            msg("FAIL: getFullName() expected 'John Doe' got '" + user.getFullName() + "'");
            errors++;
        } else {
            msg("PASS: getFullName() returned expected value 'John Doe'.");
        }

        // Test username
        if (!"johndoe".equals(user.getUsername())) {
            msg("FAIL: getUsername() expected 'johndoe' got '" + user.getUsername() + "'");
            errors++;
        } else {
            msg("PASS: getUsername() returned expected value 'johndoe'.");
        }

        // Test password hash
        String expectedHash = Integer.toHexString("mypassword".hashCode());
        if (!user.getPasswordHash().equals(expectedHash)) {
            msg("FAIL: getPasswordHash() did not return the expected hash for 'mypassword'.");
            errors++;
        } else {
            msg("PASS: getPasswordHash() returned the expected hash for 'mypassword'.");
        }

        // Test city
        if (!"New York".equals(user.getCity())) {
            msg("FAIL: getCity() expected 'New York' got '" + user.getCity() + "'");
            errors++;
        } else {
            msg("PASS: getCity() returned expected value 'New York'.");
        }

        return errors;
    }

    private int testUserInterests(User user) {
        int errors = 0;

        // Verify interests added during user creation
        if (!user.getInterests().contains("Reading")) {
            msg("FAIL: Interests should contain 'Reading'.");
            errors++;
        } else {
            msg("PASS: Interests contain 'Reading' as expected.");
        }

        if (!user.getInterests().contains("Coding")) {
            msg("FAIL: Interests should contain 'Coding'.");
            errors++;
        } else {
            msg("PASS: Interests contain 'Coding' as expected.");
        }

        return errors;
    }

    private void msg(String message) {
        testFeedback.write(message + "\n");
        System.out.println(message);
    }
}