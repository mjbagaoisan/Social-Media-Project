package main;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AuthHolder implements Comparable<AuthHolder> {

    private String passwordHash;
    private String username;

    /**
     * Constructor to create an AuthHolder with username and password.
     *
     * @param password The user's plain text password.
     * @param username The user's username.
     */
    public AuthHolder(String username, String password) {
        this.username = username;
        try {
            this.passwordHash = password.isEmpty() ? "" : hashPassword(password);
            System.out.println("DEBUG: Created AuthHolder for " + username + " with hash: " + this.passwordHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found: " + e.getMessage());
        }
    }


    /**
     * Generates a SHA-256 hash for the given password.
     *
     * @param password The plain text password to hash.
     * @return The hexadecimal representation of the hashed password.
     * @throws NoSuchAlgorithmException If SHA-256 algorithm is not available.
     */
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());
        // Convert byte array into signum representation
        StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
        }

    /**
     * Verifies if the provided password matches the stored password hash.
     *
     * @param password The plain text password to verify.
     * @return True if the password is correct, false otherwise.
     */
    public boolean verifyPassword(String password) {
        try {
            String hashedInput = hashPassword(password);
            System.out.println("DEBUG: Verifying password for " + username);
            System.out.println("DEBUG: Stored hash: " + this.passwordHash);
            System.out.println("DEBUG: Input hash: " + hashedInput);
            return this.passwordHash.equals(hashedInput);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns the username.
     *
     * @return The username.
     */
    public String getUsername(){
        return username;
    }

    /**
     * Returns the password hash.
     * Note: Consider avoiding exposing password hashes.
     *
     * @return The password hash.
     */
    public String getPasswordHash(){
        return passwordHash;
    }

    @Override
    public int hashCode() {
        // Only use username for hashing
        return username.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AuthHolder)) {
            return false;
        }
        AuthHolder other = (AuthHolder) obj;
        boolean result = this.username.equals(other.username);
        System.out.println("DEBUG: Comparing AuthHolders - this: " + this.username +
                " other: " + other.username + " result: " + result);
        return result;
    }


    @Override
    public int compareTo(AuthHolder other) {
        int usernameComparison = this.username.compareTo(other.username);
        if (usernameComparison != 0) {
            return usernameComparison;
        }
        return this.passwordHash.compareTo(other.passwordHash);
    }

    @Override
    public String toString() {
        return "AuthHolder{username='" + username + "'}";
    }


}