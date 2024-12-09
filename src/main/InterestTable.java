import java.util.Scanner;

public class InterestTable {

    // Our custom HashMap to store hashed usernames and passwords
    private HashTable<String, String> intrestTable;

    // Constructor to initialize the HashMap
    public Login() {
        intrestTable = new HashTable<>();
    }
    
    public T get(key){ //key is interest
        return intrestTable.get(key);
    }
}
