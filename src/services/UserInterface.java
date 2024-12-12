package services;

import main.*;
import java.util.*;
import dataStructures.BST;
import dataStructures.LinkedList;
import main.InterestManager;
import services.FileManager;

public class UserInterface {
    private Scanner scanner;
    private DataTables dataTables;
    private FriendGraph friendGraph;
    private UserBST userBST;
    private InterestManager interestManager;
    private User loggedInUser;

    public UserInterface(UserBST userBST, DataTables dataTables, FriendGraph friendGraph, InterestManager interestManager) {
        this.scanner = new Scanner(System.in);
        this.userBST = userBST;
        this.dataTables = dataTables;
        this.friendGraph = friendGraph;
        this.interestManager = interestManager;

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
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    createAccount();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break; // Add break here
            }
            System.out.println("Users in userBST: " + userBST.getUsers());
        }
    }

    private void login() {
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

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
        System.out.print("ID (Jersey Number): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume the leftover newline

        System.out.println("DEBUG: Attempting to register user: " + username);

        if (!dataTables.register(username, password)) {
            System.out.println("Username already exists. Please choose another.");
            return;
        }

        System.out.println("DEBUG: Registration successful in DataTables");

        LinkedList<String> interests = new LinkedList<>();
        System.out.println("Enter interests (type 'done' to finish): ");
        while (true) {
            String interest = scanner.nextLine().trim();
            if (interest.equalsIgnoreCase("done")) {
                break;
            }
            if (!interest.isEmpty()) {
                interests.addLast(interest);
            }
        }

        User newUser = new User(firstName, lastName, username, password, id, "City", interests, new BST<>());
        System.out.println("DEBUG: Created new user object: " + newUser.toString());

        userBST.insertUser(newUser);
        System.out.println("DEBUG: BST size after insertion: " + userBST.getUsers().size());

        interests.positionIterator();
        while (!interests.offEnd()) {
            String interest = interests.getIterator();
            interestManager.addInterest(interest, newUser);
            interests.advanceIterator();
        }

        System.out.println("Account created successfully!");
        loggedInUser = newUser;

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
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewFriends();
                    break;
                case 2:
                    makeNewFriend();
                    break;
                case 3:
                    FileManager.saveData(userBST, dataTables);
                    loggedInUser = null;
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break; // Add break
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
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewFriendsByName();
                    break;
                case 2:
                    searchForFriend();
                    break;
                case 3:
                    return; // Go back to userMenu
                default:
                    System.out.println("Invalid choice.");
                    break; // Add break
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
            scanner.nextLine(); // consume newline

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
                    return; // Go back to userMenu
                default:
                    System.out.println("Invalid choice.");
                    break; // Add break
            }
        }
    }

    private void viewFriendsByName() {
        System.out.println("Your friends (sorted by name):");
        System.out.println(loggedInUser.getFullName() + "'s Friends:");

        BST<User> friendsBST = loggedInUser.getFriends();
        if (friendsBST == null || friendsBST.isEmpty()) {
            System.out.println("No friends found.");
            return;
        }

        // Use a LinkedList to track displayed friends
        LinkedList<String> displayedFriends = new LinkedList<>();
        String[] friendsList = friendsBST.inOrderString().split("\n");

        for (String friendStr : friendsList) {
            if (!friendStr.trim().isEmpty()) {
                // Check if we've already displayed this friend
                if (!displayedFriends.contains(friendStr)) {
                    System.out.println("- " + friendStr);
                    displayedFriends.addLast(friendStr);
                }
            }
        }
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
                    return; // go back to viewFriends
                default:
                    System.out.println("Invalid choice.");
                    break; // Add break
            }
        }
    }

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
                        return; // go back to the search prompt
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break; // Add break
                }
                // break is not needed here as 'return' ends the method anyway
            }
        }
    }


    public void viewInterest(User friend) {
        if (friend == null) {
            System.out.println("Invalid user.");
            return;
        }
        System.out.println("Interests:");
        LinkedList<String> interests = interestManager.getInterestNamesForDisplay(friend.getId());  // Get interests

        if (interests.isEmpty()) {
            System.out.println(friend.getFullName() + " has no interests listed.");
        } else {
            interests.positionIterator();
            while (!interests.offEnd()) {
                System.out.println("- " + interests.getIterator());
                interests.advanceIterator();
            }
        }
    }




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
            return;
        }

        System.out.println("\nUsers with interest '" + interest + "':");
        usersWithInterest.positionIterator();

        // Track displayed users to prevent duplicates
        LinkedList<String> displayedUsers = new LinkedList<>();

        while (!usersWithInterest.offEnd()) {
            String userName = usersWithInterest.getIterator();

            if (!displayedUsers.contains(userName)) {
                ArrayList<User> matchingUsers = userBST.searchUsersByName(userName);
                if (!matchingUsers.isEmpty()) {
                    User user = matchingUsers.get(0);
                    if (user.getId() != loggedInUser.getId()) {  // Don't show current user
                        boolean isFriend = friendGraph.isFriend(loggedInUser.getId(), user.getId());
                        System.out.println("- " + user.getFullName() +
                                (isFriend ? " (Already a friend)" : ""));
                        displayedUsers.addLast(userName);
                    }
                }
            }
            usersWithInterest.advanceIterator();
        }

        if (displayedUsers.isEmpty()) {
            System.out.println("No other users found with this interest.");
            return;
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
                    break; // Add break
            }
        }

    private void viewFriendProfile(User friend) {
        if (friend == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("\nProfile for " + friend.getFullName());
        System.out.println("Username: " + friend.getUsername());
        System.out.println("ID: " + friend.getId());
        System.out.println("Full Name: " + friend.getFullName());
        System.out.println("City: " + friend.getCity());

        // Display interests using the updated viewInterest method
        viewInterest(friend);
    }


    private void addFriend(User friend) {
        friendGraph.addFriend(loggedInUser.getId(), friend.getId());
        System.out.println(friend.getFullName() + " added as a friend.");
    }

    private void removeFriend(User friend) {
        friendGraph.removeFriend(loggedInUser.getId(),friend.getId());
        // Create a comparator that compares users by their ID (or any other field you prefer)
        Comparator<User> userComparator = (u1, u2) -> Integer.compare(u1.getId(), u2.getId());

        // Remove the friend from the logged-in user's friends BST
        loggedInUser.getFriends().remove(friend, userComparator);

        // Also remove the logged-in user from the friend's friends list
        friend.getFriends().remove(loggedInUser, userComparator);
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
                break; // Add break
        }
    }

}
