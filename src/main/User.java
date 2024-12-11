package main;

import dataStructures.BST;

import java.io.Serializable;
import java.util.LinkedList;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class User implements Serializable {
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String passwordHash;
    private int passwordHashID;
    private final String city;
    private BST<User> friends;
    private final LinkedList<String> interests;
    private final int id;
    private static int idCounter = 0;

    public User(String firstName, String lastName, String username, String password, int id, String city,LinkedList<String> interests, BST<User> friends) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.friends = new BST<>();
        this.interests = new LinkedList<String>();
        this.city = null;
        this.id = idCounter++;

        //For hashing
        try{
            this.passwordHashID = Math.abs(passwordToInteger(password));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found: " + e.getMessage());
        }

    }

    //constructor option 2
    public User(String username, String password){
        this.username = username;
        try{
            this.passwordHashID = Math.abs(passwordToInteger(password));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found: " + e.getMessage());
        }

        this.firstName = "";
        this.lastName = "";
        this.passwordHash = hashPassword(password);
        this.friends = new BST<>();
        this.interests = new LinkedList<String>();
        this.city = null;
        this.id = idCounter++;
    }

    private String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }

    // Getters
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public int getId() {
        return id;
    }

    //for hashtables
    @Override public int hashCode() {
		return passwordHashID;
	}
	

    public String getCity() {
        return city;
    }


    public LinkedList<String> getInterests() {
        return interests;
    }

    public BST<User> getFriends() {
        return friends;
    }

    // Setters

    public void setFriends(BST<User> friends) {
        this.friends = friends;
    }

    public void addInterest(String interest) {
        if (!interests.contains(interest)) {
            interests.add(interest);
        }
    }


    public static int passwordToInteger(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());
        BigInteger hashInt = new BigInteger(1, hashBytes);
        return hashInt.intValue(); 
    }

    public int compareTo(User user) {
        return this.getFullName().compareTo(user.getFullName());
    }
}
