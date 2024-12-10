package main;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthHolder implements Comparable<AuthHolder> {

    private int password;
    private String username;

    public AuthHolder(String password, String username){
        this.username = username;
        try{
            this.password = Math.abs(passwordToInteger(password));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found: " + e.getMessage());
        }
    }
    
    public AuthHolder(String password){
        this.username = "";
        try{
            this.password = passwordToInteger(password);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found: " + e.getMessage());
        }
    }
    
    @Override public int hashCode() {
		return password;
	}

    public String getUsername(){
        return username;
    }

    public static int passwordToInteger(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());
        BigInteger hashInt = new BigInteger(1, hashBytes);
        return hashInt.intValue(); 
    }
    
    @Override public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		} else if (!(obj instanceof AuthHolder)) {
			return false;
		} else {
			AuthHolder stu = (AuthHolder) obj;
			return this.password == stu.password && stu.username.equals(this.username);
		}
	}
	
	@Override public int compareTo(AuthHolder stu) {
		if(this.equals(stu)) {
			return 0;
		} else if (this.password != stu.password) {
			return Integer.compare(this.password, stu.password);
		} else {
			return this.username.compareTo(stu.username);
		}
	}
	
	@Override public String toString() {
	    return username + ": " + password;
	}
	
}