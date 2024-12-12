package services;

import java.io.*;
import java.util.*;

import main.FriendGraph;
import main.User;
import main.UserBST;
import dataStructures.BST;
import dataStructures.LinkedList;
import main.DataTables;

public class FileManager {

    private static class FriendshipData {
        int userId;
        List<Integer> friendIds;
        DataTables dataTables;
        FriendGraph friendGraph;


        FriendshipData(int userId) {
            this.userId = userId;
            this.friendIds = new ArrayList<>();
            this.dataTables = new DataTables(100);
            this.friendGraph = new FriendGraph();

        }
    }

    public static void loadData(UserBST userBST, DataTables dataTables, FriendGraph friendGraph) {
        File file = new File("data.txt");
        List<FriendshipData> friendships = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                try {
                    // Read user ID
                    int id = Integer.parseInt(line);

                    // Read name (ensuring we have input)
                    String firstName = scanner.next().trim();
                    String lastName = scanner.nextLine().trim();

                    // Read username
                    String username = scanner.nextLine().trim();

                    // Read password
                    String password = scanner.nextLine().trim();

                    // Read number of friends
                    int numFriends = Integer.parseInt(scanner.nextLine().trim());

                    // Store friendship data
                    FriendshipData friendshipData = new FriendshipData(id);
                    for (int i = 0; i < numFriends; i++) {
                        if (!scanner.hasNextLine()) break;
                        String friendIdStr = scanner.nextLine().trim();
                        if (!friendIdStr.isEmpty()) {
                            friendshipData.friendIds.add(Integer.parseInt(friendIdStr));
                        }
                    }
                    friendships.add(friendshipData);

                    // Read city
                    String city = scanner.nextLine().trim();

                    // Read interests
                    int numInterests = Integer.parseInt(scanner.nextLine().trim());
                    LinkedList<String> interests = new LinkedList<>();

                    for (int i = 0; i < numInterests; i++) {
                        if (!scanner.hasNextLine()) break;
                        String interest = scanner.nextLine().trim();
                        if (!interest.isEmpty()) {
                            interests.addLast(interest);
                        }
                    }

                    // Create and insert user
                    User user = new User(firstName, lastName, username, password, id, city, interests, new BST<>());
                    userBST.insertUser(user);
                    dataTables.register(username, password);

                    // Register interests
                    interests.positionIterator();
                    while (!interests.offEnd()) {
                        dataTables.userHasInterest(interests.getIterator(), user);
                        interests.advanceIterator();
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error processing user data: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // Establish friendships after all users are loaded
            for (FriendshipData friendshipData : friendships) {
                ArrayList<User> userList = userBST.searchUsersById(friendshipData.userId);
                if (!userList.isEmpty()) {
                    User user = userList.get(0);
                    for (int friendId : friendshipData.friendIds) {
                        ArrayList<User> friendList = userBST.searchUsersById(friendId);
                        if (!friendList.isEmpty()) {
                            user.getFriends().insert(friendList.get(0), (u1, u2) -> u1.getId() - u2.getId());
                            System.out.println("Added friend " + friendList.get(0).getFullName() + " to " + user.getFullName());
                        }
                    }
                }
            }

            // Now that all users and their friendships are in userBST,
            // add them to the friendGraph
            ArrayList<User> allUsers = userBST.getUsers();
            for (User user : allUsers) {
                friendGraph.addUser(user);
            }

            // Add edges (friendships) to the friendGraph
            for (User user : allUsers) {
                BST<User> friendsBST = user.getFriends();
                // We need to get each friend from friendsBST. Assuming you have a method to get the BST contents:
                // If not, you can parse friendsBST.inOrderString(), or if BST has a method inOrderTraversal() that returns an ArrayList:

                // Example using inOrderString (if no direct method is available):
                String friendsInOrder = friendsBST.inOrderString();
                // Each line in friendsInOrder represents a User in format: User[ID=X, Username=..., FullName=...]
                // We'll parse the ID:
                String[] lines = friendsInOrder.split("\n");
                for (String l : lines) {
                    if (l.trim().isEmpty()) continue;
                    // Extract ID from the string:
                    // Format assumed: User[ID=X, Username=..., FullName=...]
                    int start = l.indexOf("ID=") + 3;
                    int end = l.indexOf(",", start);
                    String idStr = l.substring(start, end).trim();
                    int friendId = Integer.parseInt(idStr);

                    friendGraph.addFriend(user.getId(), friendId);
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("Data file not found: " + e.getMessage());
        }
    }

    public static void saveData(UserBST userBST, DataTables dataTables) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("NewData.txt"))) {
            ArrayList<User> users = userBST.getUsers();

            for (User user : users) {
                writer.println(user.getId());
                writer.println(user.getFullName());
                writer.println(user.getUsername());
                writer.println(user.getPasswordHash());

                // Write friends
                BST<User> friendsBST = user.getFriends();
                writer.println(friendsBST.getSize());
                String friendsList = friendsBST.inOrderString();
                String[] friendsArray = friendsList.split("\n");
                for (String friendStr : friendsArray) {
                    if (!friendStr.trim().isEmpty()) {
                        writer.println(friendStr.trim());
                    }
                }

                writer.println(user.getCity());
                LinkedList<String> interests = user.getInterests();
                writer.println(interests.getLength());

                interests.positionIterator();
                while (!interests.offEnd()) {
                    writer.println(interests.getIterator());
                    interests.advanceIterator();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}