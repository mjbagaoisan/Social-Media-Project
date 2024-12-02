package main;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class SocialMediaApp {
    private Scanner scanner;
    private BST<User> allUsers;
    private Graph friendGraph; 
    private Map<String, User> loginTable;
    private User loggedInUser;
    private boolean loggedIn;

    public SocialMediaApp() {
        scanner = new Scanner(System.in);
        allUsers = new BST<>();
        friendGraph = new Graph();
        loginTable = new HashMap<>();
    }

    public void start() {
        loadUsersFromFile("users.txt");
        showMainMenu();
    }

    private void showMainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("Welcome to the Social Media App");
            System.out.println("1. Login");
            System.out.println("2. Create a New Account");
            System.out.println("3. Quit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    createNewAccount();
                    break;
                case 3:
                    quit();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        User user = loginTable.get(username + password);
        if (user != null) {
            loggedInUser = user;
            loggedIn = true;
            showUserMenu();
        } else {
            System.out.println("Invalid login credentials.");
        }
    }

    private void createNewAccount() {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        User newUser = new User(firstName, lastName, username, password);
        allUsers.insert(newUser);
        loginTable.put(username + password, newUser);
        System.out.println("Account created successfully.");
    }

    private void showUserMenu() {
        while (loggedIn) {
            System.out.println("Welcome, " + loggedInUser.getFirstName());
            System.out.println("1. View My Friends");
            System.out.println("2. Make New Friends");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    viewFriends();
                    break;
                case 2:
                    makeNewFriends();
                    break;
                case 3:
                    loggedIn = false;
                    loggedInUser = null;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewFriends() {
        System.out.println("Your friends:");
        loggedInUser.getFriends().inOrderTraversal();
    }

    private void makeNewFriends() {
        System.out.println("Make New Friends");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Interest");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        switch (choice) {
            case 1:
                searchByName();
                break;
            case 2:
                searchByInterest();
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void searchByName() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        List<User> users = allUsers.searchByName(name);
        displayUserOptions(users);
    }

    private void searchByInterest() {
        System.out.print("Enter interest: ");
        String interest = scanner.nextLine();
        List<User> usersWithInterest = interestTable.get(interest); // Assuming there's an interestTable
        displayUserOptions(usersWithInterest);
    }

    private void displayUserOptions(List<User> users) {
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i).getFullName());
        }
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (choice > 0 && choice <= users.size()) {
            User selectedUser = users.get(choice - 1);
            loggedInUser.addFriend(selectedUser);
            friendGraph.addEdge(loggedInUser, selectedUser);
            System.out.println(selectedUser.getFullName() + " has been added to your friends.");
        } else {
            System.out.println("Invalid option.");
        }
    }

    private void quit() {
        System.out.println("Goodbye!");
        saveUsersToFile("users.txt");
    }

    private void loadUsersFromFile(String filename) {
        // Load user data from file and populate the classes
    }

    private void saveUsersToFile(String filename) {
        // Save current user data to file
    }

    public static void main(String[] args) {
        SocialMediaApp app = new SocialMediaApp();
        app.start();
    }
}

