//package tests;
//
//import main.DataTables;
//import main.User;
//import dataStructures.LinkedList;
//
//import java.io.File;
//import java.io.PrintWriter;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//
//public class DataTablesTest {
//    private PrintWriter testFeedback;
//
//    public static void main(String[] args) throws Exception {
//        File file = new File("datatables_test_feedback.txt");
//        PrintWriter testFeedback = new PrintWriter(file);
//        DataTablesTest tester = new DataTablesTest(testFeedback);
//
//        tester.runTests();
//
//        testFeedback.close();
//
//        // Print the feedback file contents to the console
//        List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
//        lines.forEach(System.out::println);
//    }
//
//    public DataTablesTest(PrintWriter testFeedback) {
//        this.testFeedback = testFeedback;
//    }
//
//    public void runTests() {
//        msg("Starting DataTables Tests...");
//
//        int errorCount = 0;
//
//        // Instantiate DataTables
//        DataTables dt = null;
//        try {
//            dt = new DataTables(100);
//            msg("PASS: DataTables successfully instantiated.");
//        } catch (Exception e) {
//            msg("FAIL: Exception instantiating DataTables: " + e.getMessage());
//            errorCount++;
//        }
//
//        // Test 1: Register new users
//        if (dt != null) {
//            errorCount += testRegister(dt);
//            errorCount += testAuthenticate(dt);
//            errorCount += testUserHasInterest(dt);
//        }
//
//        // Summary
//        if (errorCount == 0) {
//            msg("All tests passed!");
//        } else {
//            msg("Total errors: " + errorCount);
//        }
//    }
//
//    private int testRegister(DataTables dt) {
//        int errors = 0;
//        try {
//            boolean result1 = dt.register("user1", "password1");
//            if (!result1) {
//                msg("FAIL: Registration for 'user1' with 'password1' should succeed.");
//                errors++;
//            } else {
//                msg("PASS: Registration for 'user1' with 'password1' succeeded.");
//            }
//
//            // Attempt to register the same username again
//            boolean result2 = dt.register("user1", "password2");
//            if (result2) {
//                msg("FAIL: Registration for duplicate username 'user1' should fail.");
//                errors++;
//            } else {
//                msg("PASS: Registration for duplicate username 'user1' failed as expected.");
//            }
//        } catch (Exception e) {
//            msg("FAIL: Exception during testRegister: " + e.getMessage());
//            errors++;
//        }
//        return errors;
//    }
//
//    private int testAuthenticate(DataTables dt) {
//        int errors = 0;
//        try {
//            // Successful authentication
//            String result1 = dt.authenticate("user1", "password1");
//            if (!"valid".equals(result1)) {
//                msg("FAIL: Authentication for 'user1' with 'password1' should return 'valid'.");
//                errors++;
//            } else {
//                msg("PASS: Authentication for 'user1' with 'password1' returned 'valid'.");
//            }
//
//            // Failed authentication with wrong password
//            String result2 = dt.authenticate("user1", "wrongpassword");
//            if (!"invalid".equals(result2)) {
//                msg("FAIL: Authentication for 'user1' with 'wrongpassword' should return 'invalid'.");
//                errors++;
//            } else {
//                msg("PASS: Authentication for 'user1' with 'wrongpassword' returned 'invalid'.");
//            }
//
//            // Failed authentication for non-existent user
//            String result3 = dt.authenticate("nonexistent", "password");
//            if (!"invalid".equals(result3)) {
//                msg("FAIL: Authentication for non-existent user should return 'invalid'.");
//                errors++;
//            } else {
//                msg("PASS: Authentication for non-existent user returned 'invalid'.");
//            }
//        } catch (Exception e) {
//            msg("FAIL: Exception during testAuthenticate: " + e.getMessage());
//            errors++;
//        }
//        return errors;
//    }
//
//    private int testUserHasInterest(DataTables dt) {
//        int errors = 0;
//        try {
//            // Create a user instance
//            LinkedList<String> interests = new LinkedList<>();
//            User user = new User("Alice", "Smith", "asmith", "password1", 1, "New York", interests, null);
//
//            // Add an interest for the user
//            dt.userHasInterest("Reading", user);
//
//            msg("PASS: userHasInterest ran without exceptions (manual check for interest in HashTable).");
//        } catch (Exception e) {
//            msg("FAIL: Exception during testUserHasInterest: " + e.getMessage());
//            errors++;
//        }
//        return errors;
//    }
//
//    private void msg(String message) {
//        testFeedback.write(message + "\n");
//        System.out.println(message);
//    }
//
//    /**
//     * Adds an interest for a user
//     * @param interest the interest to add
//     * @param user the user to associate with the interest
//     */
//    public void userHasInterest(String interest, User user) {
//        Interests newInterest = new Interests(interest, user);
//        ih.add(newInterest);
//    }
//}
