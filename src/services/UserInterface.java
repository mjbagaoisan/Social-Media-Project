package services;

import main.*;
//import java.io.*;
import java.util.*;

import dataStructures.BST;
import dataStructures.LinkedList;
import main.InterestManager;
import services.FileManager;

public class UserInterface {
    private Scanner scanner;
    private DataTables dataTables; // Integration of DataTables
    private FriendGraph friendGraph;
    private User loggedInUser;
    private UserBST userBST;
    private InterestManager interestManager;
    private FileManager fileManager;
    private static int nextUserId = 1;

    public UserInterface() {
        this.dataTables = new DataTables(30);
        this.friendGraph = new FriendGraph();
        this.scanner = new Scanner(System.in);
        this.userBST = new UserBST();
        this.interestManager = new InterestManager(30);

        // Load saved data
        FileManager.loadData(userBST, dataTables);

        // Synchronize auth data with BST
        ArrayList<User> users = userBST.getUsers();
        dataTables.loadAuthData(users);
        System.out.println("DEBUG: Loaded " + users.size() + " users into auth system");
    }

    public void startUI() {
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
            System.out.println("Users in userBST: " + userBST.getUsers());
        }
    }

    private void login() {
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        // First, try to find the user in the BST
        ArrayList<User> users = userBST.getUsers();
        User matchingUser = null;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                matchingUser = user;
                break;
            }
        }

        if (matchingUser == null) {
            System.out.println("User not found.");
            return;
        }

        // Then authenticate credentials
        String authenticatedUser = dataTables.authenticate(username, password);
        if (authenticatedUser.equals("valid")) {
            loggedInUser = matchingUser;
            System.out.println("Login successful! Welcome, " + loggedInUser.getFullName());
            userMenu();
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

        // Debug output before registration
        System.out.println("DEBUG: Attempting to register user: " + username);

        // Check if registration is successful
        if (!dataTables.register(username, password)) {
            System.out.println("Username already exists. Please choose another.");
            return;
        }

        System.out.println("DEBUG: Registration successful in DataTables");

        LinkedList<String> interests = new LinkedList<>();
        System.out.println("Enter interests (type 'done' to finish): ");
        while (true) {
            String interest = scanner.nextLine();
            if (interest.equals("done")) {
                break;
            }
            interests.addLast(interest);
        }

        User newUser = new User(firstName, lastName, username, password, nextUserId++, "City", interests, new BST<>());
        System.out.println("DEBUG: Created new user object: " + newUser.toString());

        userBST.insertUser(newUser);  // Using correct method name
        System.out.println("DEBUG: BST size after insertion: " + userBST.getUsers().size());

        // Add interests
        interests.positionIterator();
        while (!interests.offEnd()) {
            String interest = interests.getIterator();
            interestManager.addInterest(interest, newUser);
            interests.advanceIterator();
        }

        System.out.println("Account created successfully!");
        loggedInUser = newUser;

        // Debug output before saving
        System.out.println("DEBUG: Attempting to save data...");
        FileManager.saveData(userBST, dataTables);
        System.out.println("DEBUG: Data save attempted");

        userMenu();
    }

    private void userMenu() {
        while (true) {
            System.out.println("1. View Friends");
            System.out.println("2. Make New Friends");
            System.out.println("3. Logout and Save Data");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewFriends();
                break;
                case 2:
                    makeNewFriend();
                break;
                case 3:{
                    FileManager.saveData(userBST, dataTables);
                    loggedInUser = null;
                    return;
                }
                default: System.out.println("Invalid choice.");
                break;
            }
        }
    }

    private void viewFriends() {

        while (true) {
            System.out.println("1. View Friends (Sorted by Name)");
            System.out.println("2. Search for a Friend");
            System.out.println("3. Go Back");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                viewFriendsByName();
                break;
                case 2:
                searchForFriend();
                break;
                case 3:
                    userMenu();
                default:
                System.out.println("Invalid choice.");
            }
        }
    }

    public void makeNewFriend() {
        while (true) {
            System.out.println("1. Search by Name");
            System.out.println("2. Search by Interest");
            System.out.println("3. Get Friend Suggestions");
            System.out.println("4. Go Back");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                searchByName();
                break;
                case 2:
                searchByInterest();
                break;
                case 3:
                recommendFriends();
                break;
                case 4:
                userMenu();
                default:
                System.out.println("Invalid choice.");
            }
        }
    }

    public void viewFriendsByName() {
        System.out.println("Your friends (sorted by name):");
        friendGraph.getFriends(loggedInUser.getId());
    }

    public void searchForFriend() {
        System.out.println("Enter name of friend to search for: ");
        String fullName = scanner.nextLine().trim();

        if (fullName.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        ArrayList<User> matchingUsers = userBST.searchUsersByName(fullName);

        if (matchingUsers.isEmpty()) {
            System.out.println("No friends found with the name: " + fullName);
        } else {
            System.out.println("Friend found with the name: " + fullName);
            for (User user : matchingUsers) {
                System.out.println(user.getFullName());
            }
        }


        while (true) {
            System.out.println("1. View Profile");
            System.out.println("2. Remove Friend");
            System.out.println("3. Go Back");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewFriendProfile(matchingUsers.get(0));
                break;
                case 2:
                    removeFriend(matchingUsers.get(0));
                break;
                case 3:
                    viewFriends();
                default:
                System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Allows the user to search for a person by name, view their profile, or add them as a friend.
     * If the person is already a friend, the user is prompted to search again.
     */
    public void searchByName() {
        while (true) {
            System.out.println("Enter name of person to search for: ");
            String fullName = scanner.nextLine().trim();

            if (fullName.isEmpty()) {
                System.out.println("Name cannot be empty.");
                continue;
            }

            ArrayList<User> matchingUsers = userBST.searchUsersByName(fullName);

            if (matchingUsers.isEmpty()) {
                System.out.println("No users found with the name: " + fullName);
                continue;
            } else {
                System.out.println("User(s) found with the name: " + fullName);
                for (User user : matchingUsers) {
                    System.out.println(user.getFullName());
                }
            }

            User selectedUser = matchingUsers.get(0);
            int selectedUserId = selectedUser.getId();
            boolean isFriend = friendGraph.isFriend(loggedInUser.getId(), selectedUserId);

            if (isFriend) {
                System.out.println(selectedUser.getFullName() + " is already added as a friend.");
                System.out.println("Please search for another user.");
                continue;
            }

            while (true) {
                System.out.println("1. View Profile");
                System.out.println("2. Add Friend");
                System.out.println("3. Go Back");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        viewFriendProfile(selectedUser);
                        break;
                    case 2:
                        addFriend(selectedUser);
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

                break;
            }
        }
    }


    /**
     * Displays the interests of a friend's profile based on the passed User object.
     *
     * @param friend The User object representing the friend whose interests are to be viewed.
     */
    public void viewInterest(User friend) {
        String nameOfFriend = friend.getFullName();
        ArrayList<User> matchingUsers = userBST.searchUsersByName(nameOfFriend);

        if (matchingUsers.isEmpty()) {
            System.out.println("No users found with the name: " + nameOfFriend);
            return;
        }

        User actualFriend = matchingUsers.get(0); // Can be improved if we handle multiple matches
        LinkedList<Interests> friendInterests = interestManager.getInterestsByUserID(actualFriend.getId());

        if (friendInterests.isEmpty()) {
            System.out.println(actualFriend.getFullName() + " has no interests listed.");
        } else {
            System.out.println(actualFriend.getFullName() + "'s interests:");
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

    /**
     * Searches for users with a specific interest and prints their names.
     * If the user is a friend, it appends "Already added" next to their name.
     */
    public void searchByInterest() {
        System.out.println("Enter interest to search for: ");
        String interest = scanner.nextLine().trim();

        if (interest.isEmpty()) {
            System.out.println("Interest cannot be empty.");
            return;
        }

        LinkedList<String> usersWithInterest = interestManager.searchUsersByInterest(interest);

        if (usersWithInterest.isEmpty()) {
            System.out.println("No users found with the interest: " + interest);
        } else {
            System.out.println("Users found with the interest: " + interest);

            usersWithInterest.positionIterator();
            while (!usersWithInterest.offEnd()) {
                String userName = usersWithInterest.getIterator();

                ArrayList<User> matchingUsers = userBST.searchUsersByName(userName);
                if (matchingUsers.isEmpty()) {
                    continue;
                }

                User user = matchingUsers.get(0);

                int userId = user.getId();

                boolean isFriend = friendGraph.isFriend(loggedInUser.getId(), userId);

                if (isFriend) {
                    System.out.println(user.getFullName() + " - Already added");
                } else {
                    System.out.println(user.getFullName());
                }

                usersWithInterest.advanceIterator();
            }
            System.out.println("\nEnter the name of the user to interact with: ");
            String selectedUserName = scanner.nextLine().trim();

            ArrayList<User> selectedUsers = userBST.searchUsersByName(selectedUserName);
            if (selectedUsers.isEmpty()) {
                System.out.println("No user found with the name: " + selectedUserName);
                return;
            }

            User selectedUser = selectedUsers.get(0);

            System.out.println("Choose an option for " + selectedUser.getFullName() + ":");
            System.out.println("1. View Profile");
            System.out.println("2. Add as Friend");
            System.out.println("3. Go Back");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewFriendProfile(selectedUser);
                    break;
                case 2:
                    addFriend(selectedUser);
                    break;
                case 3:
                    makeNewFriend();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }



    private void viewFriendProfile(User friendName) {
        System.out.println("Username: " + friendName.getUsername());
        System.out.println("ID: " + friendName.getId());
        System.out.println("Full Name: " + friendName.getFullName());
        System.out.println("City: " + friendName.getCity());
        System.out.println("Interests:");
        viewInterest(friendName);
    }


    private void addFriend(User friend) {
        friendGraph.addFriend(loggedInUser.getId(), friend.getId());
        System.out.println(friend.getFullName() + " added as a friend.");
        
    }

    private void removeFriend(User friend) {
        friendGraph.removeFriend(loggedInUser.getId(),friend.getId());
        System.out.println(friend.getFullName() + " removed as a friend.");
    }

    private void recommendFriends(){
        friendGraph.processUserFriendRecommendations(scanner, loggedInUser.getId());
        System.out.println("Enter the name of the user to interact with: ");
        String selectedUserName = scanner.nextLine().trim();

        ArrayList<User> selectedUsers = userBST.searchUsersByName(selectedUserName);
        if (selectedUsers.isEmpty()) {
            System.out.println("No user found with the name: " + selectedUserName);
            return;
        }

        User selectedUser = selectedUsers.get(0);

        System.out.println("Choose an option for " + selectedUser.getFullName() + ":");
        System.out.println("1. View Profile");
        System.out.println("2. Add as Friend");
        System.out.println("3. Go Back");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                viewFriendProfile(selectedUser);
                break;
            case 2:
                addFriend(selectedUser);
                break;
            case 3:
                makeNewFriend();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private int generateUserId() {
        return nextUserId++;
    }
}
