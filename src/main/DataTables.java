
package main;

import dataStructures.LinkedList;
import dataStructures.HashTable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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

    public void register(String username, String password) {
        AuthHolder x = new AuthHolder(username, password);
        AH.add(x);
    }

    // Authenticate a user
    public String authenticate(String username, String password) {
        AuthHolder res = AH.get(new AuthHolder(username, password));
        if (res != null && res.getUsername().equals(username)){
            return "valid";
        }else{
            return "invalid";
        }
    }
    

    public void userHasInterest(String interest, User user){
        Interests y = new Interests(interest, user);
        ih.add(y);
    }

    public ArrayList<User> getUsersWithInterest(String interest) {
        ArrayList<User> users = new ArrayList<>();
        int hashedInterest;

        try {
            hashedInterest = Math.abs(strToInteger(interest));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found: " + e.getMessage());
            return users;
        }

        int row = hashedInterest % ts;

        // missing logic

        return users;
    }


    public static int strToInteger(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());
        BigInteger hashInt = new BigInteger(1, hashBytes);
        return hashInt.intValue();
    }
}