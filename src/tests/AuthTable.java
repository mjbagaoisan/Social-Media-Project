import java.util.Scanner;

public class PasswordTable {

    // Our custom HashMap to store hashed usernames and passwords
    private HashTable<String, String> loginData;

    // Constructor to initialize the HashMap
    public PasswordTable() {
        loginData = new HashTable<>();
    }

    // Hashing function for usernames
    private int hashPassword(String password) {
        return Math.abs(password.hashCode());
    }

    // Register a new user
    public boolean register(String username, String password) {
        int hashedPassword = hashPassword(password);
        String hashedKey = String.valueOf(hashedPassword); // Convert to string for use as key

        if (loginData.get(hashedKey) != null) {
            System.out.println("Error: Username is already taken.");
            return false;
        }

        loginData.put(hashedKey, username);
        System.out.println("Registration successful!");
        return true;
    }

    // Authenticate a user
    public String authenticate(String password) {
        int hashedPassword = hashPassword(password);
        String hashedKey = String.valueOf(hashedPassword);

        String username = loginData.get(hashedKey);
        if (username != null) {
            return username;
        } else {
            return "invalid";
        }
    }

    // Main method for testing the login system
    public static void main(String[] args) {
        Login loginSystem = new Login();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                loginSystem.register(username, password);
            } else if (choice == 2) {
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                System.out.println("username::: "+loginSystem.authenticate(password));
            } else if (choice == 3) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }
}
