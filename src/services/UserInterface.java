package services;

import main.*;
//import java.io.*;
import java.util.*;

import dataStructures.BST;
import dataStructures.LinkedList;
import main.InterestManager;

public class UserInterface {
    private Scanner scanner;
    private DataTables dataTables; // Integration of DataTables
    private FriendGraph friendGraph;
    private User loggedInUser;
    private UserBST userBST;
    private InterestManager interestManager;

    public UserInterface() {
        this.dataTables = new DataTables(30);
        this.friendGraph = new FriendGraph();
        this.scanner = new Scanner(System.in);
        this.userBST = userBST;
        this.interestManager = interestManager;

        mainMenu();

    }


    private void mainMenu() {
        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: 
                login();
                break;
                case 2: 
                createAccount();
                break;
                case 3: {
                    System.out.println("Goodbye!");
                    return;
                }
                default: 
                System.out.println("Invalid choice.");
            }
        }
    }

    private void login() {
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        //Authenticate user using the DataTables class
        String authenticatedUser = dataTables.authenticate(username,password);
        if (!authenticatedUser.equals("invalid")) {
            loggedInUser = new User("First", "Last", username, password, loggedInUser.getId(), "City", new LinkedList<>(), new BST<>());
            if (loggedInUser != null) {
                System.out.println("Login successful! Welcome, " + loggedInUser.getFullName());
                userMenu();
            }else{
                System.out.println("User data not found. Please contact support.");
            }
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private void createAccount() {
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        LinkedList<String> interests = new LinkedList<>();
        System.out.println("Enter interests (type 'done' to finish): ");
        while (true) {
            String interest = scanner.nextLine();
            if (interest.equals("done")) {
                break;
            }
            interests.addLast(interest);
        }

        int id = generateUserId();
        User newUser = new User(firstName, lastName, username, password, id, "City", interests, new BST<>());
        // Assuming register doesn't return anything, remove the success check.
        dataTables.register(username, password);
        interests.positionIterator();
        while (!interests.offEnd()) {
            String interest = interests.getIterator();
            dataTables.userHasInterest(interest, newUser);
            interests.advanceIterator();
        }
        System.out.println("Account created successfully!");
        loggedInUser = newUser;
    }

    private void userMenu() {
        while (true) {
            System.out.println("1. View Friends");
            System.out.println("2. Add Friend");
            System.out.println("3. Remove Friend");
            System.out.println("4. Friend Recommendations");
            System.out.println("5. View Friends' Interests");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: 
                viewFriends();
                break;
                case 2: 
                addFriend();
                break;
                case 3:
                removeFriend();
                break;
                case 4: 
                recommendFriends();
                break;
                case 5:
                viewInterest(friendName); // Pass the friend's full name as a parameter and make a local variable
                break; 
                case 6:{
                    loggedInUser = null;
                    return;
                }
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void viewFriends() {
        System.out.println("Your friends:");
        friendGraph.getFriends(loggedInUser.getId());
        
    }

    /**
     * Displays the interests of a friend's profile based on their full name.
     *
     * @param friendName The full name of the friend whose interests are to be viewed.
     */
    public void viewInterest(String friendName) {
        ArrayList<User> matchingUsers = userBST.searchUsersByName(friendName);

        if (matchingUsers.isEmpty()) {
            System.out.println("No users found with the name: " + friendName);
            return;
        }

        User friend = matchingUsers.get(0);
        LinkedList<Interests> friendInterests = interestManager.getInterestsByUserID(friend.getId());

        if (friendInterests.isEmpty()) {
            System.out.println(friend.getFullName() + " has no interests listed.");
        } else {
            System.out.println(friend.getFullName() + "'s interests:");
            friendInterests.positionIterator();
            while (!friendInterests.offEnd()) {
                try {
                    Interests interest = friendInterests.getIterator();
                    System.out.println(interest.getInterestName());
                    friendInterests.advanceIterator();
                } catch (NoSuchElementException e) {
                    System.err.println("Error while iterating through interests: " + e.getMessage());
                    break;
                }
            }
        }
    }


    private void addFriend() {
        System.out.print("Enter username of friend: ");
        String username = scanner.nextLine();
        User friend = new User("First", "Last", username, "password", generateUserId(), "City", new LinkedList<>(), new BST<>());
        friendGraph.addFriend(loggedInUser.getId(), friend.getId());
        System.out.println(friend.getFullName() + " added as a friend.");
        
    }

    private void removeFriend() {
        System.out.println("Enter username of friend to remove: ");
        String username = scanner.nextLine();
        User friend = new User("First", "Last", username, "password", generateUserId(), "City", new LinkedList<>(), new BST<>());
        friendGraph.removeFriend(loggedInUser.getId(), friend.getId());
        System.out.println(friend.getFullName() + " removed as a friend.");
        
    }

    private void recommendFriends(){
        friendGraph.processUserFriendRecommendations(scanner, loggedInUser.getId());
    }

    private int generateUserId(){
        return loggedInUser == null ? 1 : loggedInUser.getId() + 1;
    }
    

    
}
