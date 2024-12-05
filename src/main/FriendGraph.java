package main;

import dataStructures.Graph;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class FriendGraph {
    private Graph friendNetwork;
    private ArrayList<String> names;
    private ArrayList<Integer> currentFriends;

    public FriendGraph() {
        friendNetwork = new Graph(0); 
        names = new ArrayList<>();
        currentFriends = new ArrayList<>();
    }
    public void getFriendGraph() {
        
    }

    public static void addFriend(int user1, int user2) {
        friendNetwork.addUndirectedEdge(user1, user2);
    }

    public void removeFriend(int user1, int user2) {
        friendNetwork.removeUndirectedEdge(user1, user2);
    }
    public void getFriends(int user) {
        LinkedList<Integer> friendsList = friendNetwork.getAdjacencyList(user);
        for (int friendId : friendsList) {
            System.out.println(names.get(friendId)); 
        }
    }

    public void getMutualFriends(int user1, int user2) {
        ArrayList<Integer> mutualFriends = new ArrayList<>();
        LinkedList<Integer> friendsUser1 = friendNetwork.getAdjacencyList(user1);
        LinkedList<Integer> friendsUser2 = friendNetwork.getAdjacencyList(user2);

        for (int friendId1 : friendsUser1) {
            for (int friendId2 : friendsUser2) {
                if (friendId1 == friendId2) {
                    mutualFriends.add(friendId1);
                    break;
                }
            }
    }

    public void getReccomendedFriends(int user) {
        ArrayList<Integer> recommendedFriends = findRecommendedFriends(user);
        if (recommendedFriends.isEmpty()) {
            System.out.println("\nSorry! We don't have any recommendations for you at this time.");
        } else {
            System.out.println("\nHere are your recommended friends: ");
            for (Integer recommendedId : recommendedFriends) {
                System.out.println(names.get(recommendedId));
            }
        }
    }
    public void suggestFriend(int user) {
        getRecommendedFriends(user);
    }

    public boolean isFriend(int user1, int user2) {
        LinkedList<Integer> friends = friendNetwork.getAdjacencyList(user1);
        for (int friendId : friends) {
            if (friendId == user2) {
                return true;
            }
        }
        return false;
    }
    private ArrayList<Integer> findRecommendedFriends(int user) {
        
        return new ArrayList<>();
    }
}
  
