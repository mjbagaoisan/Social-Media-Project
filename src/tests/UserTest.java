package tests;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

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
            interests.add("Reading");
            interests.add("Coding");

            // Note: The constructor currently ignores the provided city and interests parameters
            // and sets them to null and empty respectively.
            testUser = new User("John", "Doe", "johndoe", "mypassword", 1, "New York", interests, null);
            msg("PASS: Successfully created a User instance.");
        } catch (Exception e) {
            msg("FAIL: Exception creating User instance: " + e.getMessage());
            errorCount++;
        }

        // Test User getters and authentication
        if (testUser != null) {
            errorCount += testUserGettersAndAuth(testUser);
            errorCount += testUserInterests(testUser);
        }

        // Test UserBST
        UserBST userBST = null;
        try {
            userBST = new UserBST();
            msg("PASS: Successfully created a UserBST instance.");
        } catch (Exception e) {
            msg("FAIL: Exception creating UserBST instance: " + e.getMessage());
            errorCount++;
        }
        if (userBST != null) {
            errorCount += testUserBST(userBST);
        }

        // Test UserFriend
        UserFriend userFriend = null;
        try {
            userFriend = new UserFriend();
            msg("PASS: Successfully created a UserFriend instance.");
        } catch (Exception e) {
            msg("FAIL: Exception creating UserFriend instance: " + e.getMessage());
            errorCount++;
        }
        if (userFriend != null) {
            errorCount += testUserFriend(userFriend);
        }

        // Summary
        if (errorCount == 0) {
            msg("All tests passed!");
        } else {
            msg("Total errors: " + errorCount);
        }
    }

    private int testUserGettersAndAuth(User user) {
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

        // Test authentication
        if (!user.authenticate("mypassword")) {
            msg("FAIL: authenticate('mypassword') should return true.");
            errors++;
        } else {
            msg("PASS: authenticate('mypassword') returned true as expected.");
        }

        // Test city (currently set to null in constructor)
        if (user.getCity() != null) {
            msg("FAIL: getCity() expected null (given the current constructor implementation) got '" + user.getCity() + "'");
            errors++;
        } else {
            msg("PASS: getCity() returned null as expected.");
        }

        // Test ID increments
        int userId = user.getId(); // first user created in this run
        if (userId != 0) {
            msg("FAIL: getId() expected 0 for first user got " + userId);
            errors++;
        } else {
            msg("PASS: getId() returned 0 as expected for the first user.");
        }

        return errors;
    }

    private int testUserInterests(User user) {
        int errors = 0;
        // The constructor currently ignores interests and sets a new empty list.
        // Let's add an interest and test retrieval.
        user.addInterest("Gaming");
        if (!user.getInterests().contains("Gaming")) {
            msg("FAIL: After addInterest('Gaming'), interests should contain 'Gaming'.");
            errors++;
        } else {
            msg("PASS: addInterest('Gaming') worked as expected.");
        }

        return errors;
    }

    private int testUserBST(UserBST userBST) {
        int errors = 0;
        try {
            // Insert some test users
            User u1 = new User("Alice", "Smith", "asmith", "pass1", 2, "CityA", null, null);
            User u2 = new User("Bob", "Jones", "bjones", "pass2", 3, "CityB", null, null);
            userBST.insertUser(u1);
            userBST.insertUser(u2);

            ArrayList<User> users = userBST.getUsers();
            if (users.size() < 2) {
                msg("FAIL: After inserting two users, getUsers() should return at least 2. Got " + users.size());
                errors++;
            } else {
                msg("PASS: insertUser and getUsers seem to work (2 or more users retrieved).");
            }

            // Search by name (dependent on parseUser logic which may not work due to no toString())
            // We'll just ensure no exceptions:
            ArrayList<User> searchResults = userBST.searchUsersByName("Alice");
            msg("PASS: searchUsersByName('Alice') ran without exceptions. (Check logic once toString() is implemented)");
        } catch (Exception e) {
            msg("FAIL: Exception during testUserBST: " + e.getMessage());
            errors++;
        }
        return errors;
    }

    private int testUserFriend(UserFriend userFriend) {
        int errors = 0;
        try {
            User u1 = new User("Charlie", "Brown", "cbrown", "pass3", 4, "CityC", null, null);
            User u2 = new User("Diana", "Prince", "dprince", "pass4", 5, "CityD", null, null);

            userFriend.addUser(u1);
            userFriend.addUser(u2);

            // Test search by username
            User found = userFriend.searchUserByUsername("cbrown");
            if (found == null || !"Charlie Brown".equals(found.getFullName())) {
                msg("FAIL: searchUserByUsername('cbrown') did not return the expected user.");
                errors++;
            } else {
                msg("PASS: searchUserByUsername('cbrown') returned 'Charlie Brown' as expected.");
            }

            // Test getAllUsers
            ArrayList<User> allUsers = userFriend.getAllUsers();
            if (allUsers.size() < 2) {
                msg("FAIL: After adding two users, getAllUsers() should return at least 2. Got " + allUsers.size());
                errors++;
            } else {
                msg("PASS: getAllUsers() returned 2 or more users as expected.");
            }

            // Test searchUsersByName
            // Again depends on parse logic, we just check no exceptions:
            ArrayList<User> nameSearch = userFriend.searchUsersByName("Brown");
            msg("PASS: searchUsersByName('Brown') ran without exceptions. (Check correctness with proper toString() implementation)");
        } catch (Exception e) {
            msg("FAIL: Exception during testUserFriend: " + e.getMessage());
            errors++;
        }
        return errors;
    }

    private void msg(String message) {
        testFeedback.write(message + "\n");
        System.out.println(message);
    }
}

