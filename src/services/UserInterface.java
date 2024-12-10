package services;

import main.User;
import main.FriendGraph;
//import java.io.*;
import java.util.*;
import main.DataTables;
//import dataStructures.Graph;
import dataStructures.BST;

public class UserInterface {
    private Scanner scanner;
    private DataTables dataTables; // Integration of DataTables
    private FriendGraph friendGraph;
    private User loggedInUser;

    public UserInterface() {
        this.dataTables = new DataTables();
        this.friendGraph = new FriendGraph();
        this.scanner = new Scanner(System.in);

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
        String authenticatedUser = dataTables.authenticate(password);
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
            interests.add(interest);
        }

        int id = generateUserId();
        User newUser = new User(firstName, lastName, username, password, id, password, interests, new BST<>());
        boolean success = dataTables.register(username, password);
        if (success) {
            System.out.println("Account created successfully!");
            loggedInUser = newUser;
        }else{
            System.out.println("Username already exists.");
        }
    }

    private void userMenu() {
        while (true) {
            System.out.println("1. View Friends");
            System.out.println("2. Add Friend");
            System.out.println("3. Remove Friend");
            System.out.println("4. Friend Recommendations");
            System.out.println("5. Logout");
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
                case 5: {
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
    
    private void addFriend() {
        System.out.print("Enter username of friend: ");
        String username = scanner.nextLine();
        User friend = new User("First", "Last", username, "password", generateUserId(), "City", new LinkedList<>(), new BST<>());
        FriendGraph.addFriend(loggedInUser.getId(), friend.getId());
        
    }

    private void removeFriend() {
        System.out.println("Enter username of friend to remove: ");
        String username = scanner.nextLine();
        User friend = new User("First", "Last", username, "password", generateUserId(), "City", new LinkedList<>(), new BST<>());
        friendGraph.removeFriend(loggedInUser.getId(), friend.getId());
        
    }

    private void recommendFriends(){
        friendGraph.processUserFriendRecommendations(scanner, loggedInUser.getId());
    }

    private int generateUserId(){
        return loggedInUser == null ? 1 : loggedInUser.getId() + 1;
    }

    
}
