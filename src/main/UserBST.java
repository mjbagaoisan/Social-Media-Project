package main;

import dataStructures.BST;
import java.util.ArrayList;
import java.util.Comparator;

public class UserBST {
    // Two separate BSTs for different types of searches
    private final BST<User> friendNameBST;
    private final BST<User> userNameBST;

    /**
     * Comparator for sorting users by full name
     */
    private final Comparator<User> nameComparator = new Comparator<User>() {
        @Override
        public int compare(User u1, User u2) {
            String fullName1 = u1.getFirstName() + " " + u1.getLastName();
            String fullName2 = u2.getFirstName() + " " + u2.getLastName();
            return fullName1.compareTo(fullName2);
        }
    };

    /**
     * Comparator for sorting users by username
     */
    private final Comparator<User> usernameComparator = new Comparator<User>() {
        @Override
        public int compare(User u1, User u2) {
            return u1.getUserName().compareTo(u2.getUserName());
        }
    };

    /**
     * Default constructor initializes empty BSTs
     */
    public UserBST() {
        friendNameBST = new BST<>();
        userNameBST = new BST<>();
    }

    /**
     * Insert a user into both BSTs
     * @param user User to insert
     */
    public void insertUser(User user) {
        // Insert into name-based BST
        friendNameBST.insert(user, nameComparator);
        
        // Insert into username-based BST
        userNameBST.insert(user, usernameComparator);
    }

    /**
     * Search for users by name
     * @param name Name to search for
     * @return ArrayList of matching users
     */
    public ArrayList<User> searchUsersByName(String name) {
        ArrayList<User> matchingUsers = new ArrayList<>();
        
        // Traverse the entire username BST to find matches
        String searchName = name.toLowerCase();
        
        // Inorder traversal to find matching users
        String[] userNames = userNameBST.inOrderString().split("\n");
        for (String userStr : userNames) {
            User user = parseUser(userStr);
            if (user != null) {
                String fullName = (user.getFirstName() + " " + user.getLastName()).toLowerCase();
                if (fullName.contains(searchName)) {
                    matchingUsers.add(user);
                }
            }
        }
        
        return matchingUsers;
    }

    /**
     * Search for users by interest
     * @param interest Interest to search for
     * @return ArrayList of users with the interest
     */
    public ArrayList<User> searchUsersByInterest(String interest) {
        ArrayList<User> matchingUsers = new ArrayList<>();
        
        // Traverse the entire username BST to find matches
        String searchInterest = interest.toLowerCase();
        
        // Inorder traversal to find matching users
        String[] userNames = userNameBST.inOrderString().split("\n");
        for (String userStr : userNames) {
            User user = parseUser(userStr);
            if (user != null && user.hasInterest(searchInterest)) {
                matchingUsers.add(user);
            }
        }
        
        return matchingUsers;
    }

    /**
     * Get friend recommendations based on common interests
     * @param user User for whom recommendations are sought
     * @return ArrayList of recommended friends
     */
    public ArrayList<User> getFriendRecommendations(User user) {
        ArrayList<User> recommendations = new ArrayList<>();
        
        // Traverse the entire username BST to find potential friends
        String[] userNames = userNameBST.inOrderString().split("\n");
        for (String userStr : userNames) {
            User potentialFriend = parseUser(userStr);
            if (potentialFriend != null 
                && !user.equals(potentialFriend) 
                && !user.isFriend(potentialFriend) 
                && user.hasCommonInterests(potentialFriend)) {
                recommendations.add(potentialFriend);
            }
        }
        
        return recommendations;
    }

    /**
     * Get friends sorted by name
     * @param user User whose friends are to be sorted
     * @return ArrayList of sorted friends
     */
    public ArrayList<User> getSortedFriends(User user) {
        // Assuming User has a getFriends() method that returns a BST
        BST<User> friendsBST = user.getFriends();
        
        // Create a new BST with the friends, sorted by name
        BST<User> sortedFriendsBST = new BST<>(friendsBST, nameComparator);
        
        // Convert to ArrayList via inOrderString
        ArrayList<User> sortedFriends = new ArrayList<>();
        String[] friendNames = sortedFriendsBST.inOrderString().split("\n");
        for (String friendStr : friendNames) {
            User friend = parseUser(friendStr);
            if (friend != null) {
                sortedFriends.add(friend);
            }
        }
        
        return sortedFriends;
    }

    /**
     * Helper method to parse a user from a string representation
     * @param userStr String representation of a user
     * @return Parsed User object or null
     */
    private User parseUser(String userStr) {
        // This method would need to be implemented based on your User class toString() method
        // Placeholder implementation
        try {
            // Implement parsing logic based on how User's toString() is defined
            // This is a simplistic example and should be replaced with actual parsing
            return null; // or actual parsing logic
        } catch (Exception e) {
            return null;
        }
    }
}