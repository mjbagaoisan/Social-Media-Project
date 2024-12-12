package main;

import dataStructures.Graph;
import dataStructures.LinkedList;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FriendGraph {
    private Graph friendNetwork;
    private ArrayList<String> names; // Maps internal index to user name
    private ArrayList<Integer> userIds; // Maps internal index to User ID

    private InterestManager interestManager;

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

    public FriendGraph() {
        try {
            friendNetwork = new Graph(100);
        } catch (IllegalArgumentException e) {
            System.err.println("Failed to initialize Graph: " + e.getMessage());
            friendNetwork = null;
        }
        names = new ArrayList<>();
        userIds = new ArrayList<>();
    }

    public void setInterestManager(InterestManager interestManager) {
        this.interestManager = interestManager;
    }

    public void addUser(User user) {
        if (friendNetwork == null) {
            System.err.println("Graph not initialized. Cannot add user.");
            return;
        }

        int userId = user.getId();
        userIds.add(userId);
        names.add(user.getFullName());
    }

    public void addFriend(int user1, int user2) {
        int vertex1 = user1 + 1; // Convert to 1-based index
        int vertex2 = user2 + 1;
        if (friendNetwork == null) {
            System.err.println("Graph not initialized.");
            return;
        }

        try {
            friendNetwork.addUndirectedEdge(vertex1, vertex2);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error adding friends: " + e.getMessage());
        }
    }


    public void removeFriend(int user1, int user2) {
        int vertex1 = user1 + 1;
        int vertex2 = user2 + 1;
        if (friendNetwork == null) {
            System.err.println("Graph not initialized.");
            return;
        }

        if (!userIds.contains(user1) || !userIds.contains(user2)) {
            System.err.println("One or both User IDs provided do not exist.");
            return;
        }

        boolean removedFromUser1 = false;
        boolean removedFromUser2 = false;

        try {
            LinkedList<Integer> friendsUser1 = friendNetwork.getAdjacencyList(vertex1);
            friendsUser1.positionIterator();
            while (!friendsUser1.offEnd()) {
                if (friendsUser1.getIterator() == vertex2) {
                    friendsUser1.removeIterator();
                    removedFromUser1 = true;
                    break;
                }
                friendsUser1.advanceIterator();
            }
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.err.println("Error removing friend from User ID " + user1 + "'s list: " + e.getMessage());
        }

        try {
            LinkedList<Integer> friendsUser2 = friendNetwork.getAdjacencyList(vertex2);
            friendsUser2.positionIterator();
            while (!friendsUser2.offEnd()) {
                if (friendsUser2.getIterator() == vertex1) {
                    friendsUser2.removeIterator();
                    removedFromUser2 = true;
                    break;
                }
                friendsUser2.advanceIterator();
            }
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.err.println("Error removing friend from User ID " + user2 + "'s list: " + e.getMessage());
        }

        if (removedFromUser1 && removedFromUser2) {
            System.out.println(getUserNameById(user1) + " and " + getUserNameById(user2) + " are no longer friends.");
        } else {
            System.out.println("One or both users were not friends.");
        }
    }

    public void getFriends(int userId) {
        int vertexId = userId + 1; // Convert user ID to graph vertex ID
        if (friendNetwork == null) {
            System.err.println("Graph not initialized.");
            return;
        }

        // Validate user ID
        if (!userIds.contains(userId)) {
            System.err.println("User ID " + userId + " does not exist.");
            return;
        }

        try {
            LinkedList<Integer> friendsList = friendNetwork.getAdjacencyList(vertexId);
            System.out.println(getUserNameById(userId) + "'s Friends:");

            if (friendsList.isEmpty()) {
                System.out.println("No friends.");
                return;
            }

            friendsList.positionIterator();
            while (!friendsList.offEnd()) {
                int friendVertexId = friendsList.getIterator();
                int friendUserId = friendVertexId - 1; // Convert vertex ID back to user ID

                if (friendUserId != userId) { // Avoid self-loop errors
                    String friendName = getUserNameById(friendUserId);
                    if (friendName != null) {
                        System.out.println("- " + friendName + " (ID: " + friendUserId + ")");
                    } else {
                        System.err.println("Invalid friend vertex ID: " + friendVertexId);
                    }
                }
                friendsList.advanceIterator();
            }
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.err.println("Error retrieving friends: " + e.getMessage());
        }
    }




    public boolean isFriend(int user1, int user2) {
        int vertex1 = user1 + 1;
        int vertex2 = user2 + 1;
        if (friendNetwork == null) {
            System.err.println("Graph not initialized.");
            return false;
        }

        if (!userIds.contains(user1) || !userIds.contains(user2)) {
            System.err.println("One or both User IDs provided do not exist.");
            return false;
        }

        try {
            LinkedList<Integer> friends = friendNetwork.getAdjacencyList(vertex1);
            friends.positionIterator();
            while (!friends.offEnd()) {
                if (friends.getIterator() == vertex2) {
                    return true;
                }
                friends.advanceIterator();
            }
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.err.println("Error checking friendship: " + e.getMessage());
        }

        return false;
    }

    public void processUserFriendRecommendations(Scanner input, int userId) {
        int vertexId = userId + 1;  // Changed from userId - 1 to userId + 1 to match rest of the code
        if (friendNetwork == null) {
            System.err.println("Graph not initialized.");
            return;
        }

        if (!userIds.contains(userId)) {
            System.err.println("Invalid user ID provided.");
            return;
        }

        try {
            friendNetwork.BFS(vertexId);  // Using corrected vertexId
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error performing BFS: " + e.getMessage());
            return;
        }

        ArrayList<FriendRecommendation> recommendations = new ArrayList<>();
        LinkedList<Integer> currentFriends;

        try {
            currentFriends = friendNetwork.getAdjacencyList(vertexId);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error retrieving direct friends: " + e.getMessage());
            return;
        }

        // Get current friends into ArrayList for easier lookup
        ArrayList<Integer> friendsList = new ArrayList<>();
        try {
            currentFriends.positionIterator();
            while (!currentFriends.offEnd()) {
                friendsList.add(currentFriends.getIterator());
                currentFriends.advanceIterator();
            }
        } catch (NoSuchElementException e) {
            System.err.println("Error processing current friends: " + e.getMessage());
        }

        // Process potential friend recommendations
        for (int i = 0; i < userIds.size(); i++) {
            int potentialUserId = userIds.get(i);

            // Skip if it's the user themselves
            if (potentialUserId == userId) {
                continue;
            }

            // Skip if they're already friends
            boolean isAlreadyFriend = false;
            for (Integer friendId : friendsList) {
                if (friendId == potentialUserId + 1) {  // Convert potential user ID to vertex ID for comparison
                    isAlreadyFriend = true;
                    break;
                }
            }
            if (isAlreadyFriend) {
                continue;
            }

            // Check if this user ID is already in recommendations
            boolean alreadyRecommended = false;
            for (FriendRecommendation rec : recommendations) {
                if (rec.userId == potentialUserId) {
                    alreadyRecommended = true;
                    break;
                }
            }
            if (alreadyRecommended) {
                continue;
            }

            int distance = -1;
            try {
                distance = friendNetwork.getDistance(potentialUserId + 1);  // Convert to vertex ID
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Error retrieving distance for User ID " + potentialUserId + ": " + e.getMessage());
                continue;
            }

            if (distance > 0 && distance < Integer.MAX_VALUE) {
                int sharedInterests = calculateSharedInterests(vertexId, potentialUserId);
                recommendations.add(new FriendRecommendation(potentialUserId, distance, sharedInterests));
            }
        }

        // Sort recommendations
        for (int i = 0; i < recommendations.size() - 1; i++) {
            for (int j = 0; j < recommendations.size() - i - 1; j++) {
                FriendRecommendation rec1 = recommendations.get(j);
                FriendRecommendation rec2 = recommendations.get(j + 1);

                if (rec1.distance > rec2.distance ||
                        (rec1.distance == rec2.distance && rec1.sharedInterests < rec2.sharedInterests)) {
                    recommendations.set(j, rec2);
                    recommendations.set(j + 1, rec1);
                }
            }
        }

        // Display recommendations
        System.out.println("\nHere are your recommended friends:");
        if (recommendations.isEmpty()) {
            System.out.println("\nSorry! We don't have any recommendations for you at this time.");
            return;
        }

        for (int i = 0; i < recommendations.size(); i++) {
            FriendRecommendation rec = recommendations.get(i);
            String recommendedName = getUserNameById(rec.userId);
            if (recommendedName != null) {
                System.out.println((i + 1) + ". " + recommendedName +
                        " (Distance: " + rec.distance + ", Shared Interests: " +
                        rec.sharedInterests + ")");
            }
        }
    }

    private int calculateSharedInterests(int vertexId, int friendId) {
        if (interestManager == null) {
            return 0;
        }

        // Convert vertex IDs to user IDs
        int userId = vertexId - 1;
        int friendUserId = friendId; // friendId is already a user ID

        // Get interests using InterestManager's display method
        LinkedList<String> userInterests = interestManager.getInterestNamesForDisplay(userId);
        LinkedList<String> friendInterests = interestManager.getInterestNamesForDisplay(friendUserId);

        int sharedCount = 0;

        // If either list is empty, return 0
        if (userInterests.isEmpty() || friendInterests.isEmpty()) {
            return 0;
        }

        // Compare interests
        userInterests.positionIterator();
        while (!userInterests.offEnd()) {
            String userInterest = userInterests.getIterator();

            friendInterests.positionIterator();
            while (!friendInterests.offEnd()) {
                String friendInterest = friendInterests.getIterator();

                if (userInterest.equalsIgnoreCase(friendInterest)) {
                    sharedCount++;
                }
                friendInterests.advanceIterator();
            }
            userInterests.advanceIterator();
        }

        return sharedCount;
    }

    private LinkedList<Interests> getInterests(int userId) {
        if (!userIds.contains(userId)) {
            System.err.println("Invalid user ID for interests retrieval.");
            return new LinkedList<>(); // Return empty list instead of null
        }

        return interestManager.getInterestsByUserID(userId);
    }

    private String getUserNameById(int userId) {
        int index = userIds.indexOf(userId);
        if (index != -1 && index < names.size()) {
            return names.get(index);
        }
        return null;
    }
}
