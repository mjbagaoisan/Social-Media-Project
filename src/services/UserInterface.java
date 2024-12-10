package services;

import main.User;
import main.FriendGraph;
import java.io.*;
import java.util.*;

public class UserInterface {
    private Scanner scanner;
    private Map<String, User> usernameMap;
    private Map<String, List<User>> interestMap;
    private FriendGraph friendGraph;
    private User loggedInUser;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
        this.usernameMap = new HashMap<>();
        this.interestMap = new HashMap<>();
        this.friendGraph = new FriendGraph();
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

        User newUser = new User(firstName, lastName, username, password, id, city, interests, friends);
        usernameMap.put(username, newUser);
        friendGraph.addUser(newUser.getId(), newUser.getFullName());
        System.out.println("Account created successfully!");
    }

    private void userMenu() {
        while (true) {
            System.out.println("1. View Friends");
            System.out.println("2. Add Friend");
            System.out.println("3. Remove Friend");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewFriends();
                case 2 -> addFriend();
                case 3 -> removeFriend();
                case 4 -> {
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
            friends.forEach(System.out::println);
            /**for (int friendId : friends) {
             User friend = null;
             for (User user : usernameMap.values()) {
             if (user.getId() == friendId) {
             friend = user;
             break;
             }
             }
             if (friend != null) {
             System.out.println(friend.getFullName());
             } else {
             System.out.println("Unknown User");
             }
             }
             }
             while (true) {
             System.out.println();
             System.out.println("1. Remove Friends");
             System.out.println("2. Add Friends");
             int choice = scanner.nextInt();
             scanner.nextLine();
             switch (choice) {
             case 1 -> viewProfile();
             case 2 -> removeFriend();
             case 3 -> addFriend();
             }*/
        }
    }

    private void viewProfile() {
        int userId = scanner.nextInt();
        ArrayList<Integer> profiles = getFriendProfile(userId);
        System.out.println("Here is the profile of your friend");
        for (int profile : profiles) {
            System.out.println((profile) + ". " + usernameMap.get(profile - 1));
        }
        if (profiles.isEmpty()) {
            System.out.println("Sorry! There is no profile to return to you at this time.");
            System.out.println("Goodbye!");
            System.exit(0);
        }
    }

    private ArrayList<Integer> getFriendProfile(int userId) {
        ArrayList<Integer> profile = new ArrayList<>();
        friendGraph.BFS(userId);
        for (int i = 1; i <= friendGraph.getNumVertices; i++) {
            if (friendGraph.getDistance(i) > 1) {
                profile.add(i);
            }
        }
        return profile;
    }

    private void addFriend() {
        System.out.print("Enter username of friend: ");
        String username = scanner.nextLine();
        User friend = usernameMap.get(username);

        if (friend != null && friend != loggedInUser) {
            friendGraph.addFriend(loggedInUser.getId(), friend.getId());
            System.out.println(friend.getFullName() + " added as a friend.");
        } else {
            System.out.println("Invalid username.");
        }
    }

    private void removeFriend() {
        System.out.println("Enter username of friend to remove: ");
        String username = scanner.nextLine();
        User friend = usernameMap.get(username);

        if (friend != null && friend == loggedInUser) {
            friendGraph.removeFriend(loggedInUser.getId(), friend.getId());
            System.out.println(friend.getFullName() + " removed as a friend.");
        } else {
            System.out.println("Invalid username.");
        }
    }
}

