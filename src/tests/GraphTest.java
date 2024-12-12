package tests; 
import main.FriendGraph;
import main.User;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import dataStructures.LinkedList;

public class GraphTest {
    private PrintWriter testFeedback;

    public static void main(String[] args) throws Exception {
        File file = new File("graph_test_feedback.txt");
        PrintWriter testFeedback = new PrintWriter(file);
        GraphTest tester = new GraphTest(testFeedback);

        tester.runTests();

        testFeedback.close();

        // Print the feedback file contents to console
        List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
        lines.forEach(System.out::println);
    }

    public GraphTest(PrintWriter testFeedback) {
        this.testFeedback = testFeedback;
    }

    public void runTests() {
        msg("Starting FriendGraph Tests...");

        int errorCount = 0;

        // Instantiate the class under test
        FriendGraph fg = null;
        try {
            fg = new FriendGraph();
        } catch (Exception e) {
            msg("Error: Could not instantiate FriendGraph: " + e.getMessage());
            errorCount++;
        }

        // Test 1: Constructor behavior
        errorCount += testConstructor(fg);

        // Prepare test data if possible (set names and possibly expand friendNetwork)
        errorCount += setupTestData(fg);

        // Test 2: addFriend and isFriend
        errorCount += testAddAndIsFriend(fg);

        // Test 3: removeFriend
        errorCount += testRemoveFriend(fg);

        // Test 4: getFriends
        errorCount += testGetFriends(fg);


        // Test 6: getReccomendedFriends (typo in given code - assuming it's intended)
        errorCount += testGetRecommendedFriends(fg);

        // Summary:
        if (errorCount == 0) {
            msg("All tests passed!");
        } else {
            msg("Total errors: " + errorCount);
        }
    }

    private int testConstructor(FriendGraph fg) {
        int errors = 0;
        if (fg == null) {
            msg("FAIL: FriendGraph constructor returned null instance.");
            errors++;
        } else {
            msg("PASS: FriendGraph successfully instantiated.");
        }
        return errors;
    }

    private int setupTestData(FriendGraph fg) {
    int errors = 0;
        try {
            // Create User objects with the custom LinkedList
            User u1 = new User("Alice", "Smith", "asmith", "password1", 1, null, new LinkedList<String>(), null);
            User u2 = new User("Bob", "Jones", "bjones", "password2", 2, null, new LinkedList<String>(), null);
            User u3 = new User("Charlie", "Brown", "cbrown", "password3", 3, null, new LinkedList<String>(), null);
            User u4 = new User("Diana", "Prince", "dprince", "password4", 4, null, new LinkedList<String>(), null);

            // Add users to FriendGraph
            fg.addUser(u1);
            fg.addUser(u2);
            fg.addUser(u3);
            fg.addUser(u4);

            msg("PASS: Added 4 users to FriendGraph.");
        } catch (Exception e) {
            msg("FAIL: Unable to set up test data due to: " + e.getMessage());
            errors++;
        }
    return errors;
    }

    private int testAddAndIsFriend(FriendGraph fg) {
        int errors = 0;
        try {
            // Add a friend relation between Alice(1) and Bob(2)
            fg.addFriend(1, 2);
    
            boolean result = fg.isFriend(1, 2);
            if (!result) {
                msg("FAIL: After addFriend(1,2), isFriend(1,2) should return true.");
                errors++;
            } else {
                msg("PASS: addFriend and isFriend work as expected.");
            }
        } catch (Exception e) {
            msg("FAIL: Exception when testing addFriend/isFriend: " + e.getMessage());
            errors++;
        }
        return errors;
    }

    private int testRemoveFriend(FriendGraph fg) {
        int errors = 0;
        try {
            // Currently Alice(1) and Bob(2) are friends
            fg.removeFriend(1, 2);
            boolean result = fg.isFriend(1, 2);
            if (result) {
                msg("FAIL: After removeFriend(1,2), isFriend(1,2) should return false.");
                errors++;
            } else {
                msg("PASS: removeFriend works as expected.");
            }
        } catch (Exception e) {
            msg("FAIL: Exception when testing removeFriend: " + e.getMessage());
            errors++;
        }
        return errors;
    }

    private int testGetFriends(FriendGraph fg) {
        int errors = 0;
        try {
            // Add friends again for testing getFriends
            fg.addFriend(1, 2); // Alice & Bob
            fg.addFriend(1, 3); // Alice & Charlie
            fg.getFriends(1);
            msg("PASS: getFriends ran without exceptions (manual check console output).");
        } catch (Exception e) {
            msg("FAIL: Exception when testing getFriends: " + e.getMessage());
            errors++;
        }
        return errors;
    }



    private int testGetRecommendedFriends(FriendGraph fg) {
        int errors = 0;
        try {
            // The code calls findRecommendedFriends which returns empty list currently.
            // So it should print the "Sorry!..." message.
            fg.processUserFriendRecommendations(new Scanner(System.in), 3); // Diana(3), presumably no recommendations
            msg("PASS: getReccomendedFriends ran without exceptions (check console output).");
        } catch (Exception e) {
            msg("FAIL: Exception when testing getReccomendedFriends: " + e.getMessage());
            errors++;
        }
        return errors;
    }

    private void msg(String message) {
        testFeedback.write(message + "\n");
        System.out.println(message);
    }

}
