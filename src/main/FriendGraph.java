package main;

import dataStructures.Graph;
import dataStructures.LinkedList;
import java.util.ArrayList;
import java.util.Scanner;

public class FriendGraph {
    private static Graph friendNetwork;
    private static ArrayList<String> names;
    private ArrayList<Integer> currentFriends;

    public FriendGraph() {
        friendNetwork = new Graph(0);
        names = new ArrayList<>();
        currentFriends = new ArrayList<>();
    }

    /**
     * Stores information about a friend's recommendation.
     */
    public class FriendRecommendation {
        int userId;
        int distance;
        int sharedInterests;

        public FriendRecommendation(int userId, int distance, int sharedInterests) {
            this.userId = userId;
            this.distance = distance;
            this.sharedInterests = sharedInterests;
        }
    }

    /**
     * Prints the friend graph, displaying each user and their friends.
     */
    public void getFriendGraph() {
        System.out.println("Friend Graph:");
        for (int userId = 0; userId < names.size(); userId++) {
            LinkedList<Integer> friendsList = friendNetwork.getAdjacencyList(userId);
            System.out.print(names.get(userId) + " has friends: ");
            if (friendsList.getLength() == 0) {
                System.out.println("No friends");
                continue;
            }
            friendsList.positionIterator();
            boolean first = true;
            while (!friendsList.offEnd()) {
                int friendId = friendsList.getIterator();
                if (!first) {
                    System.out.print(", ");
                }
                System.out.print(names.get(friendId));
                first = false;
                friendsList.advanceIterator();
            }
            System.out.println();
        }
    }

    /**
     * Adds a friend to the user's friend list.
     * @param user1 The ID of the first user.
     * @param user2 The ID of the second user.
     */
    public static void addFriend(int user1, int user2) {
        if (!friendNetwork.getAdjacencyList(user1).contains(user2)) {
            friendNetwork.addUndirectedEdge(user1, user2);
            System.out.println(names.get(user1) + " and " + names.get(user2) + " are now friends.");
        } else {
            System.out.println(names.get(user1) + " and " + names.get(user2) + " are already friends.");
        }
    }


    /**
     * Removes a friend from the user's friend list.
     * @param user1 The ID of the first user.
     * @param user2 The ID of the second user.
     */
    public void removeFriend(int user1, int user2) {
        LinkedList<Integer> friendsUser1 = friendNetwork.getAdjacencyList(user1);
        LinkedList<Integer> friendsUser2 = friendNetwork.getAdjacencyList(user2);
        boolean removedFromUser1 = false;
        friendsUser1.positionIterator();
        while (!friendsUser1.offEnd()) {
            if (friendsUser1.getIterator() == user2) {
                friendsUser1.removeIterator();
                removedFromUser1 = true;
                break;
            }
            friendsUser1.advanceIterator();
        }
        boolean removedFromUser2 = false;
        friendsUser2.positionIterator();
        while (!friendsUser2.offEnd()) {
            if (friendsUser2.getIterator() == user1) {
                friendsUser2.removeIterator();
                removedFromUser2 = true;
                break;
            }
            friendsUser2.advanceIterator();
        }
        if (removedFromUser1 && removedFromUser2) {
            System.out.println(names.get(user1) + " and " + names.get(user2) + " are no longer friends.");
        } else {
            System.out.println("One or both users were not friends.");
        }
    }

    /**
     * Prints the list of friends for the specified user.
     * @param user The ID of the user whose friends are to be displayed.
     */
    public void getFriends(int user) {
        LinkedList<Integer> friendsList = friendNetwork.getAdjacencyList(user);
        friendsList.positionIterator();
        while (!friendsList.offEnd()) {
            int friendId = friendsList.getIterator();
            System.out.println(names.get(friendId));
            friendsList.advanceIterator();
        }
    }

    /**
     * Finds and prints the mutual friends between two users.
     * @param user1 The ID of the first user.
     * @param user2 The ID of the second user.
     */
    public void getMutualFriends(int user1, int user2) {
        ArrayList<Integer> mutualFriends = new ArrayList<>();
        LinkedList<Integer> friendsUser1 = friendNetwork.getAdjacencyList(user1);
        LinkedList<Integer> friendsUser2 = friendNetwork.getAdjacencyList(user2);
        friendsUser1.positionIterator();
        friendsUser2.positionIterator();
        while (!friendsUser1.offEnd()) {
            int friendId1 = friendsUser1.getIterator();
            friendsUser2.positionIterator();
            while (!friendsUser2.offEnd()) {
                int friendId2 = friendsUser2.getIterator();
                if (friendId1 == friendId2) {
                    mutualFriends.add(friendId1);
                    break;
                }
                friendsUser2.advanceIterator();
            }
            friendsUser1.advanceIterator();
        }
        if (!mutualFriends.isEmpty()) {
            System.out.println("Mutual friends between " + names.get(user1) + " and " + names.get(user2) + ":");
            for (Integer friendId : mutualFriends) {
                System.out.println(names.get(friendId));
            }
        } else {
            System.out.println("No mutual friends found.");
        }
    }

    /**
     * Checks if two users are friends.
     * @param user1 The ID of the first user.
     * @param user2 The ID of the second user.
     * @return True if the users are friends, otherwise false.
     */
    public static boolean isFriend(int user1, int user2) {
        LinkedList<Integer> friends = friendNetwork.getAdjacencyList(user1);
        friends.positionIterator();
        while (!friends.offEnd()) {
            if (friends.getIterator() == user2) {
                return true;
            }
            friends.advanceIterator();
        }
        return false;
    }

    /**
     * Processes friend recommendations for the selected user based on network connections and shared interests.
     * @param input Scanner for user input.
     * @param userId Selected user ID.
     */
    private void processUserFriendRecommendations(Scanner input, int userId) {
        friendNetwork.BFS(userId);
        ArrayList<FriendRecommendation> recommendations = new ArrayList<>();
        LinkedList<Integer> directFriends = friendNetwork.getAdjacencyList(userId);
        ArrayList<Integer> currentFriends = new ArrayList<>();
        directFriends.positionIterator();
        while (!directFriends.offEnd()) {
            int friendId = directFriends.getIterator();
            currentFriends.add(friendId);
            directFriends.advanceIterator();
        }
        for (int i = 0; i < names.size(); i++) {
            if (i == userId || currentFriends.contains(i)) {
                continue;
            }
            int distance = friendNetwork.getDistance(i);
            int sharedInterests = calculateSharedInterests(userId, i);
            if (distance > 0 && distance < Integer.MAX_VALUE) {
                recommendations.add(new FriendRecommendation(i, distance, sharedInterests));
            }
        }
        recommendations.sort((rec1, rec2) -> {
            if (rec1.distance != rec2.distance) {
                return Integer.compare(rec1.distance, rec2.distance);
            }
            return Integer.compare(rec2.sharedInterests, rec1.sharedInterests);
        });
        System.out.println("\nHere are your recommended friends:");
        if (recommendations.isEmpty()) {
            System.out.println("\nSorry! We don't have any recommendations for you at this time.");
            return;
        }
        for (int i = 0; i < recommendations.size(); i++) {
            FriendRecommendation rec = recommendations.get(i);
            System.out.println(i + 1 + ". " + names.get(rec.userId) + " (Distance: " + rec.distance + ", Shared Interests: " + rec.sharedInterests + ")");
        }
        System.out.println("\nEnter the number of a friend to add or -1 to quit:");
        System.out.print("Enter your choice: ");
        int choice = input.nextInt();
        if (choice == -1) {
            System.out.println("\nGoodbye!");
            return;
        }
        if (choice > 0 && choice <= recommendations.size()) {
            FriendRecommendation selectedRec = recommendations.get(choice - 1);
            addFriend(userId, selectedRec.userId);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    /**
     * Calculates the number of shared interests between the user and a potential friend.
     * @param userId The ID of the current user.
     * @param friendId The ID of the potential friend.
     * @return The number of shared interests.
     */
    private int calculateSharedInterests(int userId, int friendId) {
        int sharedCount = 0;
        LinkedList<String> interestsUser = getInterests(userId);
        LinkedList<String> interestsFriend = getInterests(friendId);
        interestsUser.positionIterator();
        while (!interestsUser.offEnd()) {
            String interestUser = interestsUser.getIterator();
            interestsFriend.positionIterator();
            while (!interestsFriend.offEnd()) {
                String interestFriend = interestsFriend.getIterator();
                if (interestUser.equals(interestFriend)) {
                    sharedCount++;
                    break;
                }
                interestsFriend.advanceIterator();
            }
            interestsUser.advanceIterator();
        }
        return sharedCount;
    }

    /**
     * Retrieves the interests for a specific user.
     * @param userId The ID of the user whose interests are to be retrieved.
     * @return The LinkedList of interests for the user.
     */
    private LinkedList<String> getInterests(int userId) {
        return Interest.get(userId); // Assuming Interest.get() is a method to retrieve user interests
    }

    /**
     * Finds recommended friends based on network connections.
     * @param userId current user's ID.
     * @return list of recommended friend IDs.
     */
    private ArrayList<Integer> findRecommendedFriends(int userId) {
        ArrayList<Integer> recommendedFriends = new ArrayList<>();
        for (int i = 1; i < names.size(); i++) {
            if (i == userId || currentFriends.contains(i)) {
                continue;
            }
            Integer distance = friendNetwork.getDistance(i);
            if (distance == 2) {
                recommendedFriends.add(i);
            }
        }
        return recommendedFriends;
    }
}
