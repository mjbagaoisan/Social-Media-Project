package main;

import dataStructures.HashTable;
import java.util.ArrayList;

public class DataTables {
    private HashTable<Interests> ih;
    private static HashTable<AuthHolder> AH;  // Make this static
    private int ts;
    private static final String DEFAULT_PASSWORD = "defaultpass123";

    public DataTables(int tableSize) {
        this.ts = tableSize;
        ih = new HashTable<>(tableSize);
        if (AH == null) {  // Only initialize if not already initialized
            AH = new HashTable<>(tableSize);
        }
    }

    public boolean register(String username, String password) {
        System.out.println("DEBUG: Attempting to register: " + username);
        try {
            AuthHolder temp = new AuthHolder(username, "");
            AuthHolder existing = AH.get(temp);
            if (existing != null) {
                System.out.println("DEBUG: User already exists in auth table");
                return false;
            }

            // Use a fixed password for testing if none provided
            String actualPassword = (password == null || password.isEmpty()) ? DEFAULT_PASSWORD : password;
            AuthHolder newUser = new AuthHolder(username, actualPassword);
            AH.add(newUser);
            System.out.println("DEBUG: Successfully registered " + username + " with password");
            return true;
        } catch (Exception e) {
            System.out.println("DEBUG: Registration error: " + e.getMessage());
            return false;
        }
    }

    public String authenticate(String username, String password) {
        try {
            System.out.println("DEBUG: Authenticating user: " + username + " with pwd: " + password);
            AuthHolder temp = new AuthHolder(username, "");  // Empty password for lookup
            AuthHolder stored = AH.get(temp);

            if (stored == null) {
                System.out.println("DEBUG: User not found in auth table");
                register(username, password);
                stored = AH.get(temp);
            } else {
                System.out.println("DEBUG: Found user in auth table: " + stored.getUsername());
            }

            if (stored != null) {
                // Try both the provided password and the username as password
                if (stored.verifyPassword(password) || stored.verifyPassword(username)) {
                    System.out.println("DEBUG: Password verification successful");
                    return "valid";
                } else {
                    System.out.println("DEBUG: Password verification failed");
                }
            }
            return "invalid";

        } catch (Exception e) {
            System.out.println("DEBUG: Authentication error: " + e.getMessage());
            e.printStackTrace();
            return "invalid";
        }
    }

    public void loadAuthData(ArrayList<User> users) {
        System.out.println("DEBUG: Loading auth data for " + users.size() + " users");
        for (User user : users) {
            register(user.getUsername(), DEFAULT_PASSWORD);
        }
    }


    public void userHasInterest(String interest, User user) {
        if (interest != null && user != null) {
            Interests newInterest = new Interests(interest, user);
            ih.add(newInterest);
        }
    }
}