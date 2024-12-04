package services;

import services.User;
import java.io.*;
import java.util.*;

class Graph {
    private Map<Integer, Set<Integer>> adjList;

    public Graph() {
        this.adjList = new HashMap<>();
    }

    public void addEdge(int userId1, int userId2) {
        adjList.putIfAbsent(userId1, new HashSet<>());
        adjList.putIfAbsent(userId2, new HashSet<>());
        adjList.get(userId1).add(userId2);
        adjList.get(userId2).add(userId1); // Undirected graph
    }

    public Set<Integer> getFriends(int userId) {
        return adjList.getOrDefault(userId, Collections.emptySet());
    }
}

public class UserInterface {
    private Scanner scanner;
    private Map<String, User> usernameMap;
    private Map<String, List<User>> interestMap;
    private Graph friendGraph;
    private User loggedInUser;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
        this.usernameMap = new HashMap<>();
        this.interestMap = new HashMap<>();
        this.friendGraph = new Graph();
    }

    public void start() {
        loadUsersFromFile("users.json");
        mainMenu();
        saveUsersToFile("users.json");
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
                case 1 -> login();
                case 2 -> createAccount();
                case 3 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = usernameMap.get(username);
        if (user != null && user.authenticate(password)) {
            loggedInUser = user;
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

        if (usernameMap.containsKey(username)) {
            System.out.println("Username already exists.");
            return;
        }

        User newUser = new User(firstName, lastName, username, password);
        usernameMap.put(username, newUser);
        System.out.println("Account created successfully!");
    }

    private void userMenu() {
        while (true) {
            System.out.println("1. View Friends");
            System.out.println("2. Add Friend");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewFriends();
                case 2 -> addFriend();
                case 3 -> {
                    loggedInUser = null;
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewFriends() {
        Set<Integer> friends = friendGraph.getFriends(loggedInUser.getId());
        if (friends.isEmpty()) {
            System.out.println("No friends yet.");
        } else {
            for (int friendId : friends) {
                User friend = null;
                for (User user : usernameMap.values()){
                    if (user.getId() == friendId){
                        friend = user;
                        break;
                    }
                }
                if (friend != null){
                    System.out.println(friend.getFullName());
                }else{
                    System.out.println("Unknown User");
                }
            }
        }
    }

    private void addFriend() {
        System.out.print("Enter username of friend: ");
        String username = scanner.nextLine();
        User friend = usernameMap.get(username);

        if (friend != null && friend != loggedInUser) {
            friendGraph.addEdge(loggedInUser.getId(), friend.getId());
            System.out.println(friend.getFullName() + " added as a friend.");
        } else {
            System.out.println("Invalid username.");
        }
    }

    private void loadUsersFromFile(String filename) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            usernameMap = (Map<String, User>) inputStream.readObject();
            friendGraph = (Graph) inputStream.readObject();
        } catch (Exception e) {
            System.out.println("No existing data found.");
        }
    }

    private void saveUsersToFile(String filename) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(usernameMap);
            outputStream.writeObject(friendGraph);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new UserInterface().start();
    }
}

