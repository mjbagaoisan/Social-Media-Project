package main;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Interests{

    private int interest;
    private String interestName;
    private User user;

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
	
}