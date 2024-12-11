package main;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import dataStructures.LinkedList;

public class Interests {

    private int interest;
    private String interestName;
    private User user;
    private LinkedList<User> users;

    public Interests(String interest, User user){
        this.user = user;
        try{
            this.interest = Math.abs(strToInteger(interest));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found: " + e.getMessage());
        }
    }
    
    @Override public int hashCode() {
		return interest;
	}

    public User getUser(){
        return user;
    }

    public String getInterestName() {
        return interestName;
    }

    public static int strToInteger(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());
        BigInteger hashInt = new BigInteger(1, hashBytes);
        return hashInt.intValue(); 
    }
    
    @Override public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		} else if (!(obj instanceof Interests)) {
			return false;
		} else {
			Interests stu = (Interests) obj;
			return this.interest == stu.interest && stu.user.equals(this.user);
		}
	}
    @Override
    public String toString() {
        return interestName;
    }

    /**
     * Searches for users by a given interest.
     *
     * @param interest the interest to search for
     * @return a LinkedList of users who have the specified interest
     */
    public LinkedList<User> searchUsersByInterest(String interest) {
        LinkedList<User> result = new LinkedList<>();

        // Iterate through all users in the users list
        users.positionIterator();
        while (!users.offEnd()) {
            User currentUser = users.getIterator();  // Get the current user
            LinkedList<String> userInterests = currentUser.getInterests();  // Get the user's interests as Strings

            // Check if the user has the interest
            if (userInterests != null) { // Prevent NullPointerException
                userInterests.positionIterator();
                while (!userInterests.offEnd()) {
                    String currentInterest = userInterests.getIterator();
                    if (currentInterest.equalsIgnoreCase(interest)) {  // Case-insensitive match
                        result.addLast(currentUser);  // Add user to result if they have the interest
                        break;  // Move to the next user after finding the interest
                    }
                    userInterests.advanceIterator();
                }
            }
            users.advanceIterator();
        }

        return result;
    }

    }