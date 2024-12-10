import java.util.Scanner;
import dataStructures.Graph;
import dataStructures.HashTable;
import main.Interest;

public class DataTables {

    // Our custom HashMap to store hashed usernames and passwords
    private HashTable<String, Interest> interestIterator;
    private HashTable<String, String> authentication;

    // Constructor to initialize the HashMap
    public DataTables() {
        //for interests
        interestIterator = new HashTable<>();
        //for authentication
        authentication = new HashTable<>();
    }


    // purely authentication related

    private int hashPassword(String password) {
        return Math.abs(password.hashCode());
    }

    public boolean register(String username, String password) {
        int hashedPassword = hashPassword(password);
        String hashedKey = String.valueOf(hashedPassword); // Convert to string for use as key

        if (authentication.get(hashedKey) != null) {
            return false;
        }

        authentication.put(hashedKey, username);
        return true;
    }

    public String authenticate(String password) {
        int hashedPassword = hashPassword(password);
        String hashedKey = String.valueOf(hashedPassword);

        String username = authentication.get(hashedKey);
        if (username != null) {
            return username;
        } else {
            return "invalid";
        }
    }





    
}
