package main;

import dataStructures.BST;
import java.util.ArrayList;
import java.util.Comparator;
import dataStructures.LinkedList;

public class UserBST {
    private BST<User> userNameBST;
    private Comparator<User> nameComparator = (u1, u2) ->
            u1.getFullName().compareToIgnoreCase(u2.getFullName());

    public UserBST() {
        userNameBST = new BST<>();
        System.out.println("DEBUG: Created new UserBST");
    }

    public void insertUser(User user) {
        System.out.println("DEBUG: Attempting to insert user: " + user.getFullName());
        if (user == null) {
            System.out.println("DEBUG: User is null!");
            return;
        }
        userNameBST.insert(user, nameComparator);
        System.out.println("DEBUG: User inserted. Current BST inorder traversal: " + userNameBST.inOrderString());
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();

        if (userNameBST.isEmpty()) {
            return users;
        }

        // Get the inorder string representation
        String inOrderStr = userNameBST.inOrderString();
        String[] lines = inOrderStr.split("\n");

        for (String line : lines) {
            if (line != null && !line.trim().isEmpty()) {
                try {
                    // Parse the User details from the toString() format
                    // Assuming format: User[ID=x, Username=y, FullName=z]
                    String[] parts = line.substring(line.indexOf("[") + 1, line.indexOf("]")).split(", ");
                    int id = Integer.parseInt(parts[0].split("=")[1]);
                    String username = parts[1].split("=")[1];
                    String fullName = parts[2].split("=")[1];
                    String[] names = fullName.split(" ");

                    if (names.length >= 2) {
                        User user = new User(
                                names[0],           // firstName
                                names[1],           // lastName
                                username,           // username
                                "",                // password (empty since it's hashed)
                                id,                // id
                                "City",            // city
                                new LinkedList<>(), // interests
                                new BST<>()        // friends
                        );
                        users.add(user);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing user string: " + line);
                }
            }
        }
        return users;
    }

    public ArrayList<User> searchUsersByName(String name) {
        ArrayList<User> matchingUsers = new ArrayList<>();
        ArrayList<User> allUsers = getUsers();
        String searchName = name.toLowerCase();

        for (User user : allUsers) {
            if (user.getFullName().toLowerCase().contains(searchName)) {
                matchingUsers.add(user);
            }
        }
        return matchingUsers;
    }

    public ArrayList<User> searchUsersById(int id) {
        ArrayList<User> matchingUsers = new ArrayList<>();
        ArrayList<User> allUsers = getUsers();

        for (User user : allUsers) {
            if (user.getId() == id) {
                matchingUsers.add(user);
            }
        }
        return matchingUsers;
    }
}