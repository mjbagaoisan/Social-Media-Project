package main;

import dataStructures.BST;

import java.io.Serializable;
import java.util.LinkedList;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;


public class User implements Serializable {
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String passwordHash;
    private int passwordHashID;
    private final String city;
    private BST<User> friends;
    private final int id;
    private static int idCounter = 0;

    public User(String firstName, String lastName, String username, String password, int id, String city,LinkedList<String> interests, BST<User> friends) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.friends = new BST<>();
        this.city = city;
        this.id = idCounter++;

        //For hashing
        try{
            this.passwordHashID = Math.abs(passwordToInteger(password));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found: " + e.getMessage());
        }

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


    public BST<User> getFriends() {
        return friends;
    }

    // Setters

    public void setFriends(BST<User> friends) {
        this.friends = friends;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        return this.id == other.id && Objects.equals(this.firstName, other.firstName)
                && Objects.equals(this.lastName, other.lastName) && Objects.equals(this.username, other.username)
                && Objects.equals(this.passwordHash, other.passwordHash) && Objects.equals(this.city, other.city);
    }

    @Override
    public String toString() {
        return "User[ID=" + id + ", Username=" + username + ", FullName=" + getFullName() + "]";
    }
}

