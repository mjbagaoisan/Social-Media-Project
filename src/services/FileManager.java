package services;

import java.io.*;
import java.util.*;
import main.User;
import main.UserBST;
import dataStructures.BST;
import dataStructures.LinkedList;
import main.DataTables;

public class FileManager {

    /**
     * Loads user data from "data.txt", creates User objects, inserts them into UserBST,
     * and registers their interests with the InterestManager in DataTables.
     *
     * @param userBST The UserBST instance to insert users into.
     * @param dataTables The DataTables instance managing interests.
     */
    public static void loadData(UserBST userBST, DataTables dataTables) {
        File file = new File("data.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {

                // Read and parse user data
                int id = Integer.parseInt(scanner.nextLine().trim());
                String firstName = scanner.nextLine().trim();
                String lastName = scanner.nextLine().trim();
                String username = scanner.nextLine().trim();
                String password = scanner.nextLine().trim();
                int numFriends = Integer.parseInt(scanner.nextLine().trim());

                // Load friends
                BST<User> friends = new BST<>();
                for (int i = 0; i < numFriends; i++) {
                    if (!scanner.hasNextLine()) {
                        System.err.println("Unexpected end of file while reading friends for user ID " + id);
                        break;
                    }
                    String friendName = scanner.nextLine().trim();

                    // Skip empty friend names
                    if (friendName.isEmpty()) {
                        System.err.println("Encountered empty friend name for user ID " + id);
                        continue;
                    }

                    ArrayList<User> matchingFriends = userBST.searchUsersByName(friendName);

                    if (matchingFriends.isEmpty()) {
                        System.err.println("Friend with name \"" + friendName + "\" not found for user ID " + id);
                    } else if (matchingFriends.size() == 1) {
                        friends.insert(matchingFriends.getFirst(), (u1, u2) -> u1.getId() - u2.getId());
                    } else {
                        System.err.println("Multiple users found with name \"" + friendName + "\" for user ID " + id + ". Friendship not established.");
                    }
                }

                String city = scanner.nextLine().trim();
                int numInterests = Integer.parseInt(scanner.nextLine().trim());

                // Load interests
                LinkedList<String> interests = new LinkedList<>();
                for (int i = 0; i < numInterests; i++) {
                    String interest = scanner.nextLine().trim();
                    interests.addLast(interest);
                }

                // Create User object
                User user = new User(firstName, lastName, username, password, id, city, interests, new BST<>());

                // Insert user into UserBST
                userBST.insertUser(user);

                // Register interests with InterestManager
                interests.positionIterator();
                while (!interests.offEnd()) {
                    String interest = interests.getIterator();
                    dataTables.userHasInterest(interest, user);
                    interests.advanceIterator();
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Data file not found: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
        }
    }

    /**
     * Saves user data to "NewData.txt" from UserBST, including hashed passwords.
     *
     * @param userBST The UserBST instance to retrieve users from.
     */
    public static void saveData(UserBST userBST, DataTables dataTables) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("NewData.txt"))) {
            ArrayList<User> users = userBST.getUsers();

            for (User user : users) {
                // Use getter methods for writing the user data to the file
                writer.println(user.getId());  // ID (jersey number)
                writer.println(user.getFullName());  // Full Name (using getFullName())
                writer.println(user.getUsername());  // Username
                writer.println(user.getPasswordHash());  // Password (hashed)
                writer.println(user.getFriends().getSize());  // Number of friends

                // Write each friend's ID
                writeFriends(writer, user.getFriends());

                writer.println(user.getCity());
                writer.println(user.getInterests().getLength());

                // Iterate through the user's interests
                LinkedList<String> interests = user.getInterests();
                interests.positionIterator();
                while (!interests.offEnd()) {
                    String interest = interests.getIterator();
                    writer.println(interest);
                    interests.advanceIterator();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Writes the IDs of friends from the BST to the writer.
     *
     * @param writer The PrintWriter to write to.
     * @param friendsBST The BST containing friends.
     */
    private static void writeFriends(PrintWriter writer, BST<User> friendsBST) {
        String friendsList = friendsBST.inOrderString();
        String[] friendsArray = friendsList.split("\n"); // Assuming each friend's ID is on a separate line
        for (String friendStr : friendsArray) {
            try {
                int friendId = Integer.parseInt(friendStr.trim());
                writer.println(friendId);
            } catch (NumberFormatException e) {
                System.err.println("Invalid friend ID: " + friendStr);
            }
        }
    }
}

class idComparator implements Comparator<User> {
    @Override
    public int compare(User u1, User u2) {
        return Integer.compare(u1.getId(), u2.getId());
    }
}

class nameComparator implements Comparator<User> {
    @Override
    public int compare(User u1, User u2) {
        String fullName1 = u1.getFullName();
        String fullName2 = u2.getFullName();
        return fullName1.compareTo(fullName2);
    }
}
