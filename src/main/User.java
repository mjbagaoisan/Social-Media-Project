package main;

import dataStructures.BST;

import java.io.Serializable;
import java.util.LinkedList;

public class User implements Serializable {
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String passwordHash;
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
    }

    private String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }

    public boolean authenticate(String password) {
        return passwordHash.equals(hashPassword(password));
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


}
