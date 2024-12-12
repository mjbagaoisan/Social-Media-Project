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
        interestManager = new InterestManager(100);
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
        int vertex1 = user1 + 1;
        int vertex2 = user2 + 1;
        if (friendNetwork == null) {
            System.err.println("Graph not initialized.");
            return;
        }

        try {
            LinkedList<Integer> friendsList = friendNetwork.getAdjacencyList(vertex1);
            if (!friendsList.contains(vertex2)) {
                friendNetwork.addUndirectedEdge(vertex1, vertex2);
                System.out.println(getUserNameById(user1) + " and " + getUserNameById(user2) + " are now friends.");
            } else {
                System.out.println(getUserNameById(user1) + " and " + getUserNameById(user2) + " are already friends.");
            }
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



    public void getMutualFriends(int user1, int user2) {
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

        ArrayList<Integer> mutualFriends = new ArrayList<>();

        try {
            LinkedList<Integer> friendsUser1 = friendNetwork.getAdjacencyList(vertex1);
            LinkedList<Integer> friendsUser2 = friendNetwork.getAdjacencyList(vertex2);

            ArrayList<Integer> user1Friends = new ArrayList<>();
            friendsUser1.positionIterator();
            while (!friendsUser1.offEnd()) {
                user1Friends.add(friendsUser1.getIterator());
                friendsUser1.advanceIterator();
            }

            friendsUser2.positionIterator();
            while (!friendsUser2.offEnd()) {
                int friendVertexId = friendsUser2.getIterator();
                if (user1Friends.contains(friendVertexId)) {
                    mutualFriends.add(friendVertexId);
                }
                friendsUser2.advanceIterator();
            }

            if (!mutualFriends.isEmpty()) {
                System.out.println("Mutual friends between " + getUserNameById(user1) + " and " + getUserNameById(user2) + ":");
                for (Integer friendVertexId : mutualFriends) {
                    int friendUserId = friendVertexId - 1;
                    System.out.println("- " + getUserNameById(friendUserId) + " (ID: " + friendUserId + ")");
                }
            } else {
                System.out.println("No mutual friends found.");
            }
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.err.println("Error finding mutual friends: " + e.getMessage());
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
        int vertexId = userId + 1;
        if (friendNetwork == null) {
            System.err.println("Graph not initialized.");
            return;
        }

        if (!userIds.contains(userId)) {
            System.err.println("Invalid user ID provided.");
            return;
        }

        try {
            friendNetwork.BFS(vertexId);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error performing BFS: " + e.getMessage());
            return;
        }

        ArrayList<FriendRecommendation> recommendations = new ArrayList<>();
        LinkedList<Integer> directFriendsList;

        try {
            directFriendsList = friendNetwork.getAdjacencyList(vertexId);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error retrieving direct friends: " + e.getMessage());
            return;
        }

        ArrayList<Integer> currentFriends = new ArrayList<>();
        try {
            directFriendsList.positionIterator();
            while (!directFriendsList.offEnd()) {
                currentFriends.add(directFriendsList.getIterator());
                directFriendsList.advanceIterator();
            }
        } catch (NoSuchElementException e) {
            System.err.println("Error processing current friends: " + e.getMessage());
        }

        for (int i = 0; i < userIds.size(); i++) {
            int potentialUserId = userIds.get(i);
            if (potentialUserId == userId || currentFriends.contains(potentialUserId + 1)) {
                continue;
            }

            int distance = -1;
            try {
                distance = friendNetwork.getDistance(potentialUserId + 1);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Error retrieving distance for User ID " + potentialUserId + ": " + e.getMessage());
                continue;
            }

            int sharedInterests = calculateSharedInterests(vertexId, potentialUserId);

            if (distance > 0 && distance < Integer.MAX_VALUE) {
                recommendations.add(new FriendRecommendation(potentialUserId, distance, sharedInterests));
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
            System.out.println((i + 1) + ". " + getUserNameById(rec.userId) +
                    " (Distance: " + rec.distance + ", Shared Interests: " + rec.sharedInterests + ")");
        }
    }

    private int calculateSharedInterests(int vertexId, int friendId) {
        int sharedCount = 0;

        LinkedList<Interests> interestsUser = getInterests(vertexId - 1);
        LinkedList<Interests> interestsFriend = getInterests(friendId);

        if (interestsUser == null || interestsFriend == null) {
            return sharedCount;
        }

        try {
            interestsUser.positionIterator();
            while (!interestsUser.offEnd()) {
                Interests interestUser = interestsUser.getIterator();
                String interestUserName = interestUser.getInterestName();

                interestsFriend.positionIterator();
                while (!interestsFriend.offEnd()) {
                    Interests interestFriend = interestsFriend.getIterator();
                    String interestFriendName = interestFriend.getInterestName();

                    if (interestUserName.equalsIgnoreCase(interestFriendName)) {
                        sharedCount++;
                        break;
                    }
                    interestsFriend.advanceIterator();
                }

                interestsUser.advanceIterator();
            }
        } catch (NoSuchElementException | NullPointerException e) {
            System.err.println("Error while calculating shared interests: " + e.getMessage());
        }

        return sharedCount;
    }

    private LinkedList<Interests> getInterests(int userId) {
        if (!userIds.contains(userId)) {
            System.err.println("Invalid user ID for interests retrieval.");
            return null;
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
