package services;

import main.*;
import java.util.*;
import dataStructures.BST;
import dataStructures.LinkedList;
import services.FileManager;

public class UserInterface {
    private Scanner scanner;
    private DataTables dataTables;
    private FriendGraph friendGraph;
    private UserBST userBST;
    private InterestManager interestManager;
    private User loggedInUser;
    private static int nextUserId = 100;

    public UserInterface(UserBST userBST, DataTables dataTables, FriendGraph friendGraph, InterestManager interestManager) {
        this.scanner = new Scanner(System.in);
        this.userBST = userBST;
        this.dataTables = dataTables;
        this.friendGraph = friendGraph;
        this.interestManager = interestManager;

        ArrayList<User> users = userBST.getUsers();
        dataTables.loadAuthData(users);
    }

    public void startUI() {
        mainMenu();
    }

    private void mainMenu() {
    int choice;
    while (true) {
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        System.out.println("3. Exit and Save Data");
        System.out.print("Choose an option: ");
        if (scanner.hasNextInt()) {
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            break;
        } else {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // consume invalid input
        }
    }

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                createAccount();
                break;
            case 3:
                System.out.println("Saving data and exiting.... Goodbye!");
                FileManager.saveData(userBST, dataTables, friendGraph, interestManager);
                return;
            default:
                System.out.println("Invalid choice.");
                break;
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
        while (username.isEmpty()) {
            System.out.println("Error: Username cannot be blank.");
            System.out.print("Username: ");
            username = scanner.nextLine().trim();
        }
        System.out.print("Password: ");
        String password = scanner.nextLine();
        while (password.isEmpty()) {
            System.out.println("Error: Password cannot be blank.");
            System.out.print("Password: ");
            password = scanner.nextLine().trim();
        }


        if (!dataTables.register(username, password)) {
            System.out.println("Username already exists. Please choose another.");
            return;
        }

        System.out.print("City: ");
        String city = scanner.nextLine();


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

        int generatedId = nextUserId++;

        User newUser = new User(firstName, lastName, username, password, generatedId, city, interests, new BST<>());

        userBST.insertUser(newUser);

        interests.positionIterator();
        while (!interests.offEnd()) {
            String interest = interests.getIterator();
            interestManager.addInterest(interest, newUser);
            interests.advanceIterator();
        }

        friendGraph.addUser(newUser);


        System.out.println("Account created successfully!");
        loggedInUser = newUser;


        FileManager.saveData(userBST, dataTables, friendGraph, interestManager);

        userMenu();
    }

    private void userMenu() {
        while (true) {
            System.out.println("1. View Friends");
            System.out.println("2. Make New Friends");
            System.out.println("3. Logout");
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
                    System.out.println("Logging out!... Logged out.");
                    FileManager.saveData(userBST, dataTables, friendGraph, interestManager);
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
                    searchForFriend(scanner);
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
                    searchByName(scanner);
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
        System.out.println(loggedInUser.getFullName() + "'s Friends:\n");

        BST<User> friendsBST = loggedInUser.getFriends();
        if (friendsBST == null || friendsBST.isEmpty()) {
            System.out.println("No friends found.");
            return;
        }

        // Print header
        System.out.println("Full Name                Username         ID");
        System.out.println("------------------------------------------------");

        // Use LinkedList to track displayed friends
        LinkedList<String> displayedFriends = new LinkedList<>();
        String[] friendsList = friendsBST.inOrderString().split("\n");

        for (String friendStr : friendsList) {
            if (!friendStr.trim().isEmpty()) {
                // Check if we've already displayed this friend
                if (!displayedFriends.contains(friendStr)) {
                    // Parse the friend string
                    // Format: User[ID=X, Username=Y, FullName=Z]
                    int idStart = friendStr.indexOf("ID=") + 3;
                    int idEnd = friendStr.indexOf(",", idStart);
                    int usernameStart = friendStr.indexOf("Username=") + 9;
                    int usernameEnd = friendStr.indexOf(",", usernameStart);
                    int nameStart = friendStr.indexOf("FullName=") + 9;
                    int nameEnd = friendStr.indexOf("]");

                    String id = friendStr.substring(idStart, idEnd);
                    String username = friendStr.substring(usernameStart, usernameEnd);
                    String fullName = friendStr.substring(nameStart, nameEnd);

                    // Format and print with proper spacing
                    String formattedName = String.format("%-23s", fullName);
                    String formattedUsername = String.format("%-19s", username);
                    String formattedId = String.format("%-5s", id);

                    System.out.println(formattedName + formattedUsername + formattedId);
                    displayedFriends.addLast(friendStr);
                }
            }
        }
        System.out.println("------------------------------------------------");
    }


    public void searchForFriend(Scanner input) {
        System.out.println("Enter name of friend to search for: ");
        String searchName = input.nextLine();

        // Create a list to store matching users
        ArrayList<User> matchingUsers = new ArrayList<>();

        // Search through all users
        ArrayList<User> allUsers = userBST.getUsers();
        for (User user : allUsers) {
            if (user.getFullName().toLowerCase().contains(searchName.toLowerCase())) {
                matchingUsers.add(user);
            }
        }

        if (matchingUsers.isEmpty()) {
            System.out.println("No users found with that name.");
            return;
        }

        // Display header
        System.out.println("\nFound " + matchingUsers.size() + " users with the name '" + searchName + "':");
        System.out.println("Full Name                Username            ID");
        System.out.println("------------------------------------------------");

        // Display matching users
        for (User user : matchingUsers) {
            String formattedName = String.format("%-23s", user.getFullName());
            String formattedUsername = String.format("%-19s", user.getUsername());
            String formattedId = String.format("%-5d", user.getId());
            System.out.println(formattedName + formattedUsername + formattedId);
        }
        System.out.println("------------------------------------------------");

        // Select user
        System.out.println("\nSelect user by number (or 0 to go back): ");
        try {
            int selection = Integer.parseInt(input.nextLine());

            if (selection == 0) {
                return;
            }

            if (selection < 1 || selection > matchingUsers.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            User selectedUser = matchingUsers.get(selection - 1);

            System.out.println("\nSelected: " + selectedUser.getFullName());
            System.out.println("1. View Profile");
            System.out.println("2. Remove Friend");
            System.out.println("3. Go Back");

            int choice = Integer.parseInt(input.nextLine());

            switch (choice) {
                case 1:
                    viewFriendProfile(selectedUser);
                    break;
                case 2:
                    removeFriend(selectedUser);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }



    public void searchByName(Scanner input) {
        System.out.println("Enter name of person to search for: ");
        String searchName = input.nextLine();

        // Create a list to store matching users
        ArrayList<User> matchingUsers = new ArrayList<>();

        // Search through all users
        ArrayList<User> allUsers = userBST.getUsers();
        for (User user : allUsers) {
            if (user.getFullName().toLowerCase().contains(searchName.toLowerCase())) {
                matchingUsers.add(user);
            }
        }

        if (matchingUsers.isEmpty()) {
            System.out.println("No users found with that name.");
            return;
        }

        // Display header
        System.out.println("\nFound " + matchingUsers.size() + " users with the name '" + searchName + "':");
        System.out.println("Full Name                Username            ID");
        System.out.println("------------------------------------------------");

        // Display matching users
        for (User user : matchingUsers) {
            String formattedName = String.format("%-23s", user.getFullName());
            String formattedUsername = String.format("%-19s", user.getUsername());
            String formattedId = String.format("%-5d", user.getId());
            System.out.println(formattedName + formattedUsername + formattedId);
        }
        System.out.println("------------------------------------------------");

        // Select user
        System.out.println("\nSelect user by number (or 0 to go back): ");
        try {
            int selection = Integer.parseInt(input.nextLine());

            if (selection == 0) {
                return;
            }

            if (selection < 1 || selection > matchingUsers.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            User selectedUser = matchingUsers.get(selection - 1);

            System.out.println("\nSelected: " + selectedUser.getFullName());
            System.out.println("1. View Profile");
            System.out.println("2. Add Friend");
            System.out.println("3. Go Back");

            int choice = Integer.parseInt(input.nextLine());

            switch (choice) {
                case 1:
                    viewPersonProfile(selectedUser);
                    break;
                case 2:
                    addFriend(selectedUser);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
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

        // Get the list of users associated with the given interest
        LinkedList<String> usersWithInterest = interestManager.searchUsersByInterest(interest);
        if (usersWithInterest == null || usersWithInterest.isEmpty()) {
            System.out.println("No users found with the interest: " + interest);
            return;
        }

        System.out.println("\nUsers with interest '" + interest + "':");

        // Print header for output
        System.out.println("Full Name                Username            ID              Status");
        System.out.println("--------------------------------------------------------------------");

        LinkedList<String> displayedUsers = new LinkedList<>();

        // Iterate through users with the interest
        usersWithInterest.positionIterator();
        while (!usersWithInterest.offEnd()) {
            String userName = usersWithInterest.getIterator().trim();

            // Ensure the user is not duplicated in the display
            if (!displayedUsers.contains(userName)) {
                ArrayList<User> matchingUsers = userBST.searchUsersByName(userName);

                for (User user : matchingUsers) {
                    boolean isFriend = friendGraph.isFriend(loggedInUser.getId(), user.getId());

                    // Format user details
                    String formattedName = String.format("%-23s", user.getFullName());
                    String formattedUsername = String.format("%-19s", user.getUsername());
                    String formattedId = String.format("%-15s", user.getId());
                    String status = isFriend ? "(Already a friend)" : "";

                    System.out.println(formattedName + formattedUsername + formattedId + status);
                }
                displayedUsers.addLast(userName);
            }
            usersWithInterest.advanceIterator();
        }

        System.out.println("--------------------------------------------------------------------");

        System.out.println("\nEnter the name of the user to interact with: ");
        String selectedUserName = scanner.nextLine().trim();

        // Find and interact with the selected user
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
                viewPersonProfile(selectedUser);
                break;
            case 2:
                addFriend(selectedUser);
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }




    private void viewFriendProfile(User friend) {
        if (friend == null) {
            System.out.println("User not found.");
            return;
        }

        // Display friend's profile information
        System.out.println("\nProfile for " + friend.getFullName());
        System.out.println("Username: " + friend.getUsername());
        System.out.println("ID: " + friend.getId());
        System.out.println("Full Name: " + friend.getFullName());
        System.out.println("City: " + friend.getCity());

        // Display interests using the updated viewInterest method
        System.out.println("Interests:");
        LinkedList<String> interests = interestManager.getInterestNamesForDisplay(friend.getId());
        if (interests.isEmpty()) {
            System.out.println(friend.getFullName() + " has no interests listed.");
        } else {
            interests.positionIterator();
            while (!interests.offEnd()) {
                System.out.println("- " + interests.getIterator());
                interests.advanceIterator();
            }
        }

        // Provide options to remove friend or go back
        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Remove Friend");
            System.out.println("2. Go Back");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    removeFriend(friend);
                    return; // Exit after removing friend
                case 2:
                    return; // Go back
                default:
                    System.out.println("Invalid option. Please choose again.");
                    break;
            }
        }
    }

    private void viewPersonProfile(User friend) {
        if (friend == null) {
            System.out.println("User not found.");
            return;
        }

        // Display friend's profile information
        System.out.println("\nProfile for " + friend.getFullName());
        System.out.println("Username: " + friend.getUsername());
        System.out.println("ID: " + friend.getId());
        System.out.println("Full Name: " + friend.getFullName());
        System.out.println("City: " + friend.getCity());

        // Display interests using the updated viewInterest method
        System.out.println("Interests:");
        LinkedList<String> interests = interestManager.getInterestNamesForDisplay(friend.getId());
        if (interests.isEmpty()) {
            System.out.println(friend.getFullName() + " has no interests listed.");
        } else {
            interests.positionIterator();
            while (!interests.offEnd()) {
                System.out.println("- " + interests.getIterator());
                interests.advanceIterator();
            }
        }

        // Provide options to remove friend or go back
        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Add Friend");
            System.out.println("2. Go Back");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                   addFriend(friend);
                    return; // Exit after removing friend
                case 2:
                    return; // Go back
                default:
                    System.out.println("Invalid option. Please choose again.");
                    break;
            }
        }
    }


    private void addFriend(User friend) {
        boolean isFriend = friendGraph.isFriend(loggedInUser.getId(), friend.getId());
        if(!isFriend) {
            friendGraph.addFriend(loggedInUser.getId(), friend.getId());

            Comparator<User> userComparator = (u1, u2) -> Integer.compare(u1.getId(), u2.getId());
            loggedInUser.getFriends().insert(friend, userComparator);

            friend.getFriends().insert(loggedInUser, userComparator);
            System.out.println(friend.getFullName() + " added as a friend.");
        } else {
            System.out.println("You are already friends!");
        }
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
                viewPersonProfile(selectedUser);
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