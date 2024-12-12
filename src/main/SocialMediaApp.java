package main;

import services.FileManager;
import services.UserInterface;

import java.util.ArrayList;


public class SocialMediaApp {
    private UserInterface userInterface;
    private FileManager fileManager;
    private UserBST userBST;
    private DataTables dataTables;
    private FriendGraph friendGraph;
    private InterestManager interestManager;

    public SocialMediaApp() {
        this.interestManager = new InterestManager(30);
        this.dataTables = new DataTables(100, this.interestManager);// Initialize InterestManager first
        this.userBST = new UserBST();
         // Now pass the initialized interestManager
        this.friendGraph = new FriendGraph();
        this.fileManager = new FileManager();
    }



    public void loadUsers() {
        FileManager.loadData(userBST, dataTables, friendGraph, interestManager);
        System.out.println("DEBUG: After loading data, checking user friends:");
        ArrayList<User> allUsers = userBST.getUsers();
        for (User u : allUsers) {
            String friendsStr = u.getFriends().inOrderString();
            System.out.println(u.getFullName() + " friends in BST: " + friendsStr);
        }

    }

    public void saveUsers() {
        FileManager.saveData(userBST, dataTables);
    }

    public void start() {
        loadUsers();
        // Now create UserInterface with the loaded data
        userInterface = new UserInterface(userBST, dataTables, friendGraph, interestManager);
        userInterface.startUI();
        saveUsers();
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Social Media App!");
        SocialMediaApp app = new SocialMediaApp();
        app.start();
    }
}
