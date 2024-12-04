package services;

import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.io.File;
import java.util.Comparator;
import main.User;
import main.UserBST;
import dataStructures.BST;

public class FileManager {

    public static void loadData(UserBST userBST) {
        File file = new File("data.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {

                int id = Integer.parseInt(scanner.nextLine().trim());
                String firstName = scanner.nextLine().trim();
                String lastName = scanner.nextLine().trim();
                String username = scanner.nextLine().trim();
                String password = scanner.nextLine().trim();
                int numFriends = Integer.parseInt(scanner.nextLine().trim());


                BST<User> friends = new BST<>();
                for (int i = 0; i < numFriends; i++) {
                    friends.insert(Integer.parseInt(scanner.nextLine().trim()));
                }

                String city = scanner.nextLine().trim();
                int numInterests = Integer.parseInt(scanner.nextLine().trim());


                LinkedList<String> interests = new LinkedList<>();
                for (int i = 0; i < numInterests; i++) {
                    interests.add(scanner.nextLine().trim());
                }

                User user = new User(firstName, lastName, username, password, id, city, interests, friends);

                userBST.insertUser(user);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveData(UserBST userBST) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("NewData.txt"))) {
            ArrayList<User> users = userBST.getUsers();

            for (User user : users) {
                // Use getter methods for writing the user data to the file
                writer.println(user.getId());  // ID (jersey number)
                writer.println(user.getFullName());  // Full Name (using getFullName())
                writer.println(user.getUsername());  // Username
                writer.println(user.getPasswordHash());  // Password (hashed)
                writer.println(user.getFriends().getSize());  // Number of friends

                // Write each friend's ID (using the friends getter method)
                writeFriends(writer, user.getFriends());

                writer.println(user.getCity());
                writer.println(user.getInterests().size());

                for (String interest : user.getInterests()) {
                    writer.println(interest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeFriends(PrintWriter writer, BST<User> friendsBST) {
        String friendsList = friendsBST.inOrderString();
        String[] friendsArray = friendsList.split("\n"); // Assuming comma-separated values
        for (String friendStr : friendsArray) {
            User friend = UserBST.parseUser(friendStr);
            if (friend != null) {
                writer.println(friend.getId());
            }
        }
    }
}

class idComparator implements Comparator<User> {
    @Override
    public int compare(User u1, User u2) {
        return Integer.compare(u1.getId(), u2.getId());
    }
}

 class nameComparator implements Comparator<User> {
    @Override
    public int compare(User u1, User u2) {
        String fullName1 = u1.getFullName();
        String fullName2 = u2.getFullName();
        return fullName1.compareTo(fullName2);
    }
}
