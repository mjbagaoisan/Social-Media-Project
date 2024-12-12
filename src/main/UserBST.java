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
        System.out.println("DEBUG: Created new UserBST");
    }

    public void insertUser(User user) {
        System.out.println("DEBUG: Attempting to insert user: " + user.getFullName());
        if (user == null) {
            System.out.println("DEBUG: User is null!");
            return;
        }
        userNameBST.insert(user, nameComparator);
        userReferences.add(user); // Keep the actual reference here
        System.out.println("DEBUG: User inserted...");
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
}