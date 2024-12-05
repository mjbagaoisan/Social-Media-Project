package main;

import dataStructures.BST;
import java.util.ArrayList;
import java.util.Comparator;
public class UserFriend {
  private BST<User> allUsersBST;

    // Comparator for sorting users by username
    private Comparator<User> usernameComparator = (u1, u2) -> 
        u1.getUsername().compareToIgnoreCase(u2.getUsername());

    /**
     * Default constructor initializes empty BST
     */
    public UserFriend() {
        allUsersBST = new BST<>();
    }

    /**
     * Insert a user into the BST
     * @param user User to insert
     */
    public void addUser(User user) {
        allUsersBST.insert(user, usernameComparator);
    }

    /**
     * Search for users by exact username
     * @param username Username to search for
     * @return User with the matching username, or null if not found
     */
    public User searchUserByUsername(String username) {
        // Create a dummy user with the username to use in search
        User dummyUser = new User(
            "", 
            "", 
            username, 
            "tempPassword", 
            0, 
            null, 
            null, 
            null
        );
        
        return allUsersBST.search(dummyUser, usernameComparator);
    }

    /**
     * Find all users with a matching name
     * @param name Name to search for
     * @return ArrayList of users with the specified name
     */
    public ArrayList<User> searchUsersByName(String name) {
        ArrayList<User> matchingUsers = new ArrayList<>();
        
        // Get users as a string using inOrderString
        String[] userStrings = allUsersBST.inOrderString().split("\n");
        
        // Convert search name to lowercase for case-insensitive search
        String searchName = name.toLowerCase();
        
        // Parse and check each user
        for (String userStr : userStrings) {
            User user = parseUser(userStr);
            if (user != null) {
                // Check if full name contains the search name
                if (user.getFullName().toLowerCase().contains(searchName)) {
                    matchingUsers.add(user);
                }
            }
        }
        
        return matchingUsers;
    }

    /**
     * Get all users in the system
     * @return ArrayList of all users
     */
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        
        // Get users as a string using inOrderString
        String[] userStrings = allUsersBST.inOrderString().split("\n");
        
        // Parse and add each user
        for (String userStr : userStrings) {
            User user = parseUser(userStr);
            if (user != null) {
                users.add(user);
            }
        }
        
        return users;
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
