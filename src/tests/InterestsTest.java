package tests;

import main.Interests;
import main.User;
import dataStructures.LinkedList;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class InterestsTest {
    private PrintWriter testFeedback;

    public static void main(String[] args) throws Exception {
        File file = new File("interests_test_feedback.txt");
        PrintWriter testFeedback = new PrintWriter(file);
        InterestsTest tester = new InterestsTest(testFeedback);

        tester.runTests();

        testFeedback.close();

        // Print the feedback file contents to the console
        List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
        lines.forEach(System.out::println);
    }

    public InterestsTest(PrintWriter testFeedback) {
        this.testFeedback = testFeedback;
    }

    public void runTests() {
        msg("Starting Interests Tests...");

        int errorCount = 0;

        errorCount += testInterestHashing();
        errorCount += testInterestEquality();
        errorCount += testSearchUsersByInterest();

        // Summary
        if (errorCount == 0) {
            msg("All tests passed!");
        } else {
            msg("Total errors: " + errorCount);
        }
    }

    private int testInterestHashing() {
        int errors = 0;
        try {
            User user = new User("Alice", "Smith", "asmith", "password1", 1, "New York", new LinkedList<>(), null);
            Interests interest = new Interests("Reading", user);

            int expectedHash = Interests.strToInteger("Reading");
            if (interest.hashCode() != expectedHash) {
                msg("FAIL: Hash code for interest 'Reading' did not match expected value.");
                errors++;
            } else {
                msg("PASS: Hash code for interest 'Reading' matched expected value.");
            }
        } catch (Exception e) {
            msg("FAIL: Exception during testInterestHashing: " + e.getMessage());
            errors++;
        }
        return errors;
    }

    private int testInterestEquality() {
        int errors = 0;
        try {
            User user1 = new User("Alice", "Smith", "asmith", "password1", 1, "New York", new LinkedList<>(), null);
            User user2 = new User("Alice", "Smith", "asmith", "password1", 1, "New York", new LinkedList<>(), null);

            Interests interest1 = new Interests("Reading", user1);
            Interests interest2 = new Interests("Reading", user2);

            if (!interest1.equals(interest2)) {
                msg("FAIL: Interests with the same user and interest name should be equal.");
                errors++;
            } else {
                msg("PASS: Interests with the same user and interest name are equal.");
            }

            Interests interest3 = new Interests("Writing", user1);
            if (interest1.equals(interest3)) {
                msg("FAIL: Interests with different interest names should not be equal.");
                errors++;
            } else {
                msg("PASS: Interests with different interest names are not equal.");
            }
        } catch (Exception e) {
            msg("FAIL: Exception during testInterestEquality: " + e.getMessage());
            errors++;
        }
        return errors;
    }

    private int testSearchUsersByInterest() {
        int errors = 0;
        try {
            LinkedList<String> aliceInterests = new LinkedList<>();
            aliceInterests.addLast("Reading");
            aliceInterests.addLast("Gaming");

            LinkedList<String> bobInterests = new LinkedList<>();
            bobInterests.addLast("Writing");

            User alice = new User("Alice", "Smith", "asmith", "password1", 1, "New York", aliceInterests, null);
            User bob = new User("Bob", "Jones", "bjones", "password2", 2, "Los Angeles", bobInterests, null);

            LinkedList<User> users = new LinkedList<>();
            users.addLast(alice);
            users.addLast(bob);

            Interests interest = new Interests("Reading", alice);
            // Manually set the users list for testing

            User user1 = new User("Alice", "Smith", "asmith", "password1", 1, "New York", new LinkedList<>(), null);

            interest = new Interests("Reading", user1);
            interest.users.addLast(user1);

            LinkedList<User> result = interest.searchUsersByInterest("Reading");

            if (result.getLength() != 1 || !result.getFirst().getFullName().equals("Alice Smith")) {
                msg("FAIL: searchUsersByInterest('Reading') should return Alice Smith.");
                errors++;
            } else {
                msg("PASS: searchUsersByInterest('Reading') returned Alice Smith as expected.");
            }
        } catch (Exception e) {
            msg("FAIL: Exception during testSearchUsersByInterest: " + e.getMessage());
            errors++;
        }
        return errors;
    }

    private void msg(String message) {
        testFeedback.write(message + "\n");
        System.out.println(message);
    }
}
