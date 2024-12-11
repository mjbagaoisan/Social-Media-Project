package main;

import dataStructures.BST;
import java.util.ArrayList;
import java.util.Comparator;

public class UserBST {
    // BST to store users sorted by full name
    private BST<User> userNameBST;

    // Comparator for sorting users by full name
    private Comparator<User> nameComparator = (u1, u2) -> 
        u1.getFullName().compareToIgnoreCase(u2.getFullName());

    /**
     * Default constructor initializes empty BST
     */
    public UserBST() {
        userNameBST = new BST<>();
    }

    /**
     * Insert a user into the BST
     * @param user User to insert
     */
    public void insertUser(User user) {
        userNameBST.insert(user, nameComparator);
    }

    /**
     * Get all users from the BST in sorted order by name
     * @return List of users sorted by full name
     */
    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        inOrderTraversal(userNameBST, users);
        return users;
    }

    /**
     * In-order traversal of the BST to collect users
     * @param bst Current BST
     * @param users List to store the collected users
     */
    private void inOrderTraversal(BST<User> bst, ArrayList<User> users) {
        // Split the inOrderString into individual user strings
        String[] userStrings = bst.inOrderString().split("\n");
        
        // Attempt to parse each user string
        for (String userStr : userStrings) {
            if (!userStr.trim().isEmpty()) {
                User user = parseUser(userStr);
                if (user != null) {
                    users.add(user);
                }
            }
        }
    }

    /**
     * Search for users by full name
     * @param name Name to search for
     * @return ArrayList of users with matching names
     */
    public ArrayList<User> searchUsersByName(String name) {
        ArrayList<User> matchingUsers = new ArrayList<>();
        
        // Get all users
        ArrayList<User> allUsers = getUsers();
        
        // Convert search name to lowercase for case-insensitive search
        String searchName = name.toLowerCase();
        
        // Find users whose full name contains the search name
        for (User user : allUsers) {
            if (user.getFullName().toLowerCase().contains(searchName)) {
                matchingUsers.add(user);
            }
        }
        
        return matchingUsers;
    }




    /**
     * Helper method to parse a user from a string representation
     * @param userStr String representation of a user
     * @return Parsed User object or null
     */
    private User parseUser(String userStr) {
        // This is a placeholder and should be replaced with actual parsing logic
        // The implementation depends on how User's toString() method is defined
        try {
            // Example parsing (this needs to match your User's toString() format)
            String[] parts = userStr.split(",");
            if (parts.length >= 2) {
                // Assuming toString() format is "FirstName LastName, Username, ..."
                String[] nameParts = parts[0].trim().split(" ");
                if (nameParts.length >= 2) {
                    // Create a new User object with parsed information
                    // Note: This is a simplified example and may need adjustment
                    return new User(
                        nameParts[0], 
                        nameParts[1], 
                        parts[1].trim(), 
                        "tempPassword", 
                        0, 
                        null, 
                        null, 
                        null
                    );
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}