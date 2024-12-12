package main;

import dataStructures.HashTable;
import dataStructures.LinkedList;

import java.util.ArrayList;

public class DataTables {
    private HashTable<Interests> ih;
    private static HashTable<AuthHolder> AH;  // Make this static
    private int ts;
    private InterestManager interestManager;

    public DataTables(int tableSize, InterestManager interestManager) {
        this.ts = tableSize;
        ih = new HashTable<>(tableSize);
        if (AH == null) {  // Only initialize if not already initialized
            AH = new HashTable<>(tableSize);
        }
        this.interestManager = new InterestManager(tableSize);
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
            AuthHolder newUser = new AuthHolder(username, password);
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
            register(user.getUsername(), user.getPasswordHash());
        }
    }


    public void userHasInterest(String interest, User user) {
        System.out.println("DEBUG: Associating interest: " + interest + " with user: " + user.getFullName());
        if (interest != null && user != null) {
            Interests newInterest = new Interests(interest, user);
            ih.add(newInterest);
            // Also add to InterestManager
            interestManager.addInterest(interest, user);
            System.out.println("DEBUG: Successfully added interest to hash table and InterestManager");
        }
    }


}