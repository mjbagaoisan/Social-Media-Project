
package main;

import dataStructures.HashTable;
import dataStructures.LinkedList;

public class DataTables {

    private HashTable<Interests> ih;
    private HashTable<AuthHolder> AH;
    private int ts;


    public DataTables(int tableSize) {
        this.ts = tableSize;
        ih = new HashTable<>(tableSize);
        AH =  new HashTable<>(tableSize);
    }

    private int hashPassword(String password) {
        return Math.abs(password.hashCode());
    }

    /**
     * Registers a new user with the provided username and password.
     *
     * @param username The desired username.
     * @param password The desired password.
     * @return True if registration is successful, false if username already exists.
     */
    public boolean register(String username, String password) {
        AuthHolder existingUser = AH.get(new AuthHolder(username, ""));
        if (existingUser != null) {
            return false; // Username already exists
        }
        AuthHolder newUser = new AuthHolder(username, password);
        AH.add(newUser);
        return true;
    }

    /**
     * Authenticates a user with the provided username and password.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @return "valid" if authentication succeeds, "invalid" otherwise.
     */
    public String authenticate(String username, String password) {
        AuthHolder user = AH.get(new AuthHolder(username, ""));
        if (user != null && user.verifyPassword(password)){
            return "valid";
        } else{
            return "invalid";
        }
    }
    
    public void userHasInterest(String interest, User user){
        Interests y = new Interests(interest, user);
        ih.add(y);
    }

}