package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String username;
    private String passwordHash;
    private String city;
    private List<String> interests;
    private int id;
    private static int idCounter = 0;

    public User(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.interests = new ArrayList<>();
        this.city = null;
        this.id = idCounter++;
    }

    private String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }

    public boolean authenticate(String password) {
        return passwordHash.equals(hashPassword(password));
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void addInterest(String interest) {
        if (!interests.contains(interest)) {
            interests.add(interest);
        }
    }
}
