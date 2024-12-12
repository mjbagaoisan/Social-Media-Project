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
        this.interestManager = interestManager;  // Use the passed InterestManager
    }


    public boolean register(String username, String password) {
        try {
            AuthHolder temp = new AuthHolder(username, "");
            AuthHolder existing = AH.get(temp);
            if (existing != null) {
                return false;
            }
            AuthHolder newUser = new AuthHolder(username, password);
            AH.add(newUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String authenticate(String username, String password) {
        try {
            AuthHolder temp = new AuthHolder(username, "");  // Empty password for lookup
            AuthHolder stored = AH.get(temp);

            if (stored == null) {
                register(username, password);
                stored = AH.get(temp);
            } else {
            }

            if (stored != null) {
                // Try both the provided password and the username as password
                if (stored.verifyPassword(password) || stored.verifyPassword(username)) {
                    return "valid";
                } else {
                }
            }
            return "invalid";

        } catch (Exception e) {
            e.printStackTrace();
            return "invalid";
        }
    }

    public void loadAuthData(ArrayList<User> users) {
        for (User user : users) {
            register(user.getUsername(), user.getPasswordHash());
        }
    }


    public void userHasInterest(String interestName, User user) {
        // Add to InterestManager
        interestManager.addInterest(interestName, user);
    }



}