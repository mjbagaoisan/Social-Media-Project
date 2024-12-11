package main;

import services.FileManager;
import services.UserInterface;

public class SocialMediaApp {

    private UserInterface userInterface;
    private FileManager fileManager;
    private UserBST userBST;
    private DataTables dataTables;

    public SocialMediaApp() {
        this.userInterface = new UserInterface();
        this.fileManager = new FileManager();
        this.userBST = new UserBST();
        this.dataTables = new DataTables(30);
    }

    public void loadUsers() {
        FileManager.loadData(userBST, dataTables);
    }

    public void saveUsers() {
        FileManager.saveData(userBST, dataTables);
    }

    public void start() {
        loadUsers();
        userInterface.startUI();  // Launch user interface
        saveUsers();  // Save data when exiting
    }

    public static void main(String[] args) {
        SocialMediaApp app = new SocialMediaApp();
        System.out.println("Welcome to the Social Media App!");
        app.start();  // Start the app
    }
}
