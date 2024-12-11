
package main;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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

    public boolean register(String username, String password) {
        AuthHolder x = new AuthHolder(username, password);
        AH.add(x);
        
        return true;
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
    
    
    public boolean removeUser(String username, String password){
        AuthHolder tuser = new AuthHolder(username, password);
        AH.delete(tuser);
        return true;
    }

    public void userHasInterest(String interest, User user){
        Interests y = new Interests(interest, user);
        ih.add(y);
    }

    // public LinkedList<String> getInterests(User user){

    // }

    public ArrayList<User> getUsersWithInterest(String interest){
        
        int hashcode = 0;
        ArrayList<User> users = new ArrayList<>();
        
        try{
            hashcode = Math.abs(strToInteger(interest));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found: " + e.getMessage());
            return users;
        }

        int row = hashcode%ts;
        
        LinkedList rowContent = ih.getRow(row);
        rowContent.positionIterator();

        while (!rowContent.offEnd()) {
            Interests m = (Interests) rowContent.getIterator(); // Access the data at the iterator
            users.add(m.getUser());

            rowContent.advanceIterator(); // Move the iterator to the next node
        }

        return users;
    }

    public static int strToInteger(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());
        BigInteger hashInt = new BigInteger(1, hashBytes);
        return hashInt.intValue();
    }
}