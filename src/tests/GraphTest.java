package tests;
import main.FriendGraph;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;

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

        // Test 5: getMutualFriends
        errorCount += testGetMutualFriends(fg);

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
            // Access private fields for testing: 'names' and 'friendNetwork'
            Field namesField = FriendGraph.class.getDeclaredField("names");
            namesField.setAccessible(true);
            @SuppressWarnings("unchecked")
            ArrayList<String> names = (ArrayList<String>) namesField.get(fg);

            // Add some dummy names
            names.add("Alice");
            names.add("Bob");
            names.add("Charlie");
            names.add("Diana");

            // Access friendNetwork field (Graph)
            Field graphField = FriendGraph.class.getDeclaredField("friendNetwork");
            graphField.setAccessible(true);
            Object graphObj = graphField.get(fg);

            // We assume graph is something like 'new Graph(0)', we might need to reinit:
            // If the Graph class allows setting the number of vertices, we might need reflection here too.
            // For now, assume Graph supports dynamic addition via addUndirectedEdge or a known method.
            // Without the Graph code, we'll just hope addUndirectedEdge works after resizing internally.
            msg("PASS: Test data (names) set up successfully.");
        } catch (Exception e) {
            msg("FAIL: Unable to set up test data due to: " + e.getMessage());
            errors++;
        }
        return errors;
    }

    private int testAddAndIsFriend(FriendGraph fg) {
        int errors = 0;
        try {
            // Add a friend relation between Alice(0) and Bob(1)
            fg.addFriend(0, 1);

            boolean result = fg.isFriend(0, 1);
            if (!result) {
                msg("FAIL: After addFriend(0,1), isFriend(0,1) should return true.");
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
            // Currently Alice(0) and Bob(1) are friends
            fg.removeFriend(0, 1);
            boolean result = fg.isFriend(0, 1);
            if (result) {
                msg("FAIL: After removeFriend(0,1), isFriend(0,1) should return false.");
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
            fg.addFriend(0, 1); // Alice & Bob
            fg.addFriend(0, 2); // Alice & Charlie
            // This should print Bob, Charlie to console.
            // We can't easily check console output here, but we can ensure no exceptions are thrown.
            fg.getFriends(0);
            msg("PASS: getFriends ran without exceptions (manual check console output).");
        } catch (Exception e) {
            msg("FAIL: Exception when testing getFriends: " + e.getMessage());
            errors++;
        }
        return errors;
    }

    private int testGetMutualFriends(FriendGraph fg) {
        int errors = 0;
        try {
            // Setup:
            // Alice(0) is friends with Bob(1) and Charlie(2).
            // Make Bob(1) also friends with Charlie(2), so that
            // mutual friends between Alice(0) and Bob(1) would be Charlie(2).
            fg.addFriend(1, 2);

            // This method prints mutual friends. We can’t easily verify printout without redirecting output.
            // We just ensure it doesn't throw an exception.
            fg.getMutualFriends(0, 1);
            msg("PASS: getMutualFriends ran without exceptions (check console for correctness).");
        } catch (Exception e) {
            msg("FAIL: Exception when testing getMutualFriends: " + e.getMessage());
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
