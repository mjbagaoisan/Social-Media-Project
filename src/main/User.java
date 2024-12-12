package main;

import dataStructures.BST;

import java.io.Serializable;
import dataStructures.LinkedList;
import services.FileManager;
import services.UserInterface;

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
    private final LinkedList<String> interests;
    private String password;



    public User(String firstName, String lastName, String username, String password, int id, String city, LinkedList<String> interests, BST<User> friends) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.friends = friends;
        this.city = city;
        this.id = id;
        this.interests = interests;

        try {
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

    @Override
    public int hashCode() {
        return password == null ? 0 : password.hashCode();
    }
	

    public String getCity() {
        return city;
    }


    public BST<User> getFriends() {
        return friends;
    }

    public LinkedList<String> getInterests() {
        return interests;
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
        // Now only compare based on passwordHash, for instance
        return Objects.equals(this.passwordHash, other.passwordHash);
    }


    @Override
    public String toString() {
        return "User[ID=" + id + ", Username=" + username + ", FullName=" + getFullName() + "]";
    }
}

