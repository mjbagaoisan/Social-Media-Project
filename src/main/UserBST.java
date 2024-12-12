package main;

import dataStructures.BST;
import java.util.ArrayList;
import java.util.Comparator;
import dataStructures.LinkedList;

public class UserBST {
    private BST<User> userNameBST;
    private Comparator<User> nameComparator = (u1, u2) ->
            u1.getFullName().compareToIgnoreCase(u2.getFullName());
    private ArrayList<User> userReferences = new ArrayList<>();

    public UserBST() {
        userNameBST = new BST<>();
    }

    public void insertUser(User user) {
        if (user == null) {
            return;
        }
        userNameBST.insert(user, nameComparator);
        userReferences.add(user); // Keep the actual reference here
    }

    public ArrayList<User> getUsers() {
        // Return a copy of the list to avoid accidental modifications outside this class
        return new ArrayList<>(userReferences);
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

    public void addFriendship(User user1, User user2) {
        if (user1 == null || user2 == null) return;

        // Check if friendship already exists
        if (!user1.hasFriend(user2.getId())) {
            user1.getFriends().insert(user2, (u1, u2) -> u1.getId() - u2.getId());
            System.out.println("Added friend " + user2.getFullName() + " to " + user1.getFullName());
        }
    }

    public LinkedList<User> getUniqueUserFriends(User user) {
        LinkedList<User> uniqueFriends = new LinkedList<>();
        if (user.getFriends() == null) return uniqueFriends;

        String[] friendsList = user.getFriends().inOrderString().split("\n");
        for (String friendStr : friendsList) {
            if (!friendStr.trim().isEmpty()) {
                // Extract ID from the friend string
                int start = friendStr.indexOf("ID=") + 3;
                int end = friendStr.indexOf(",", start);
                try {
                    int friendId = Integer.parseInt(friendStr.substring(start, end).trim());
                    ArrayList<User> friend = searchUsersById(friendId);
                    if (!friend.isEmpty() && !containsFriend(uniqueFriends, friendId)) {
                        uniqueFriends.addLast(friend.get(0));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing friend ID: " + e.getMessage());
                }
            }
        }
        return uniqueFriends;
    }

    private boolean containsFriend(LinkedList<User> friends, int userId) {
        friends.positionIterator();
        while (!friends.offEnd()) {
            if (friends.getIterator().getId() == userId) {
                return true;
            }
            friends.advanceIterator();
        }
        return false;
    }

}