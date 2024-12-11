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

    /**
     * Inner class to store information about a friend's recommendation.
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
     * Constructor initializes the friend network graph, user names list,
     * user IDs list, and the interest manager.
     *
     * Note: Graph is initialized with 78 vertices to accommodate User IDs up to 77.
     */
    public FriendGraph() {
        try {
            friendNetwork = new Graph(78);
        } catch (IllegalArgumentException e) {
            System.err.println("Failed to initialize Graph: " + e.getMessage());
            friendNetwork = null;
        }
        names = new ArrayList<>();
        userIds = new ArrayList<>();
        interestManager = new InterestManager(100); // Initialize with desired HashTable size
    }

    /**
     * Adds a new user to the friend network.
     * Maps User ID directly to Graph Vertex ID.
     *
     * @param user The User object to add.
     */
    public void addUser(User user) {
        if (friendNetwork == null) {
            System.err.println("Graph not initialized. Cannot add user.");
            return;
        }

        int userId = user.getId(); // Assuming User class has getId() method
        if (userId < 0 || userId >= 78) { // Ensure User ID is within Graph bounds
            System.err.println("User ID " + userId + " is out of bounds. Must be between 0 and 77.");
            return;
        }

        // Map User ID to internal index (same as User ID)
        if (userIds.contains(userId)) {
            System.err.println("User ID " + userId + " already exists.");
            return;
        }

        userIds.add(userId);
        names.add(user.getFullName());

        // Interests are managed separately via InterestManager
    }

    /**
     * Prints the friend graph, displaying each user and their friends.
     * Maps User IDs directly to Graph vertex IDs.
     */
    public void getFriendGraph() {
        if (friendNetwork == null) {
            System.err.println("Graph not initialized.");
            return;
        }

        System.out.println("Friend Graph:");
        for (int i = 0; i < userIds.size(); i++) {
            int userId = userIds.get(i);
            String userName = names.get(i);
            LinkedList<Integer> friendsList;
            try {
                friendsList = friendNetwork.getAdjacencyList(userId);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Error retrieving adjacency list for User ID " + userId + ": " + e.getMessage());
                continue;
            }

            System.out.print(userName + " (ID: " + userId + ") has friends: ");
            if (friendsList.getLength() == 0) {
                System.out.println("No friends");
                continue;
            }

            friendsList.positionIterator();
            boolean first = true;
            while (!friendsList.offEnd()) {
                try {
                    int friendVertexId = friendsList.getIterator();
                    String friendName = getUserNameById(friendVertexId);

                    if (friendName == null) {
                        System.err.println("Invalid friend vertex ID: " + friendVertexId);
                        friendsList.advanceIterator();
                        continue;
                    }

                    if (!first) {
                        System.out.print(", ");
                    }
                    System.out.print(friendName + " (ID: " + friendVertexId + ")");
                    first = false;
                    friendsList.advanceIterator();
                } catch (NoSuchElementException e) {
                    System.err.println("Error iterating friends: " + e.getMessage());
                    break;
                }
            }
            System.out.println();
        }
    }

    /**
     * Adds a friend to the user's friend list.
     * Maps User IDs directly to Graph vertex IDs.
     *
     * @param user1 The ID of the first user.
     * @param user2 The ID of the second user.
     */
    public void addFriend(int user1, int user2) {
        if (friendNetwork == null) {
            System.err.println("Graph not initialized.");
            return;
        }

        // Validate user IDs
        if (!userIds.contains(user1) || !userIds.contains(user2)) {
            System.err.println("One or both User IDs provided do not exist.");
            return;
        }

        try {
            LinkedList<Integer> friendsList = friendNetwork.getAdjacencyList(user1);
            if (!friendsList.contains(user2)) { // Check if already friends
                friendNetwork.addUndirectedEdge(user1, user2);
                System.out.println(getUserNameById(user1) + " and " + getUserNameById(user2) + " are now friends.");
            } else {
                System.out.println(getUserNameById(user1) + " and " + getUserNameById(user2) + " are already friends.");
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error adding friends: " + e.getMessage());
        }
    }

    /**
     * Removes a friend from the user's friend list.
     * Maps User IDs directly to Graph vertex IDs.
     *
     * @param user1 The ID of the first user.
     * @param user2 The ID of the second user.
     */
    public void removeFriend(int user1, int user2) {
        if (friendNetwork == null) {
            System.err.println("Graph not initialized.");
            return;
        }

        // Validate user IDs
        if (!userIds.contains(user1) || !userIds.contains(user2)) {
            System.err.println("One or both User IDs provided do not exist.");
            return;
        }

        boolean removedFromUser1 = false;
        boolean removedFromUser2 = false;

        try {
            LinkedList<Integer> friendsUser1 = friendNetwork.getAdjacencyList(user1);
            friendsUser1.positionIterator();
            while (!friendsUser1.offEnd()) {
                if (friendsUser1.getIterator() == user2) {
                    friendsUser1.removeIterator(); // Remove friend2 from user1's list
                    removedFromUser1 = true;
                    break;
                }
                friendsUser1.advanceIterator();
            }
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.err.println("Error removing friend from User ID " + user1 + "'s list: " + e.getMessage());
        }

        try {
            LinkedList<Integer> friendsUser2 = friendNetwork.getAdjacencyList(user2);
            friendsUser2.positionIterator();
            while (!friendsUser2.offEnd()) {
                if (friendsUser2.getIterator() == user1) {
                    friendsUser2.removeIterator(); // Remove friend1 from user2's list
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

    /**
     * Prints the list of friends for the specified user.
     * Maps User IDs directly to Graph vertex IDs.
     *
     * @param user The ID of the user whose friends are to be displayed.
     */
    public void getFriends(int user) {
        if (friendNetwork == null) {
            System.err.println("Graph not initialized.");
            return;
        }

        // Validate user ID
        if (!userIds.contains(user)) {
            System.err.println("User ID " + user + " does not exist.");
            return;
        }

        try {
            LinkedList<Integer> friendsList = friendNetwork.getAdjacencyList(user);
            System.out.println(getUserNameById(user) + "'s Friends:");
            if (friendsList.getLength() == 0) {
                System.out.println("No friends");
                return;
            }

            friendsList.positionIterator();
            while (!friendsList.offEnd()) {
                int friendVertexId = friendsList.getIterator();
                String friendName = getUserNameById(friendVertexId);

                if (friendName == null) {
                    System.err.println("Invalid friend vertex ID: " + friendVertexId);
                    friendsList.advanceIterator();
                    continue;
                }

                System.out.println("- " + friendName + " (ID: " + friendVertexId + ")");
                friendsList.advanceIterator();
            }
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.err.println("Error retrieving friends: " + e.getMessage());
        }
    }

    /**
     * Finds and prints the mutual friends between two users.
     * Maps User IDs directly to Graph vertex IDs.
     *
     * @param user1 The ID of the first user.
     * @param user2 The ID of the second user.
     */
    public void getMutualFriends(int user1, int user2) {
        if (friendNetwork == null) {
            System.err.println("Graph not initialized.");
            return;
        }

        // Validate user IDs
        if (!userIds.contains(user1) || !userIds.contains(user2)) {
            System.err.println("One or both User IDs provided do not exist.");
            return;
        }

        ArrayList<Integer> mutualFriends = new ArrayList<>();

        try {
            LinkedList<Integer> friendsUser1 = friendNetwork.getAdjacencyList(user1);
            LinkedList<Integer> friendsUser2 = friendNetwork.getAdjacencyList(user2);

            // Collect friends of user1
            ArrayList<Integer> user1Friends = new ArrayList<>();
            friendsUser1.positionIterator();
            while (!friendsUser1.offEnd()) {
                int friendVertexId = friendsUser1.getIterator();
                user1Friends.add(friendVertexId);
                friendsUser1.advanceIterator();
            }

            // Collect friends of user2 and find mutuals
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
                    System.out.println("- " + getUserNameById(friendVertexId) + " (ID: " + friendVertexId + ")");
                }
            } else {
                System.out.println("No mutual friends found.");
            }
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.err.println("Error finding mutual friends: " + e.getMessage());
        }
    }

    /**
     * Checks if two users are friends.
     * Maps User IDs directly to Graph vertex IDs.
     *
     * @param user1 The ID of the first user.
     * @param user2 The ID of the second user.
     * @return True if the users are friends, otherwise false.
     */
    public boolean isFriend(int user1, int user2) {
        if (friendNetwork == null) {
            System.err.println("Graph not initialized.");
            return false;
        }

        // Validate user IDs
        if (!userIds.contains(user1) || !userIds.contains(user2)) {
            System.err.println("One or both User IDs provided do not exist.");
            return false;
        }

        try {
            LinkedList<Integer> friends = friendNetwork.getAdjacencyList(user1);
            friends.positionIterator();
            while (!friends.offEnd()) {
                if (friends.getIterator() == user2) { // Check if user2 is in user1's friends
                    return true;
                }
                friends.advanceIterator();
            }
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.err.println("Error checking friendship: " + e.getMessage());
        }

        return false;
    }

    /**
     * Processes friend recommendations for the selected user based on network connections and shared interests.
     * Maps User IDs directly to Graph vertex IDs.
     *
     * @param input  Scanner for user input.
     * @param userId Selected user ID.
     */
    public void processUserFriendRecommendations(Scanner input, int userId) {
        if (friendNetwork == null) {
            System.err.println("Graph not initialized.");
            return;
        }

        // Validate user ID
        if (!userIds.contains(userId)) {
            System.err.println("Invalid user ID provided.");
            return;
        }

        // Perform BFS to calculate distances
        try {
            friendNetwork.BFS(userId);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error performing BFS: " + e.getMessage());
            return;
        }

        ArrayList<FriendRecommendation> recommendations = new ArrayList<>();
        LinkedList<Integer> directFriendsList;

        try {
            directFriendsList = friendNetwork.getAdjacencyList(userId);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error retrieving direct friends: " + e.getMessage());
            return;
        }

        ArrayList<Integer> currentFriends = new ArrayList<>();
        try {
            directFriendsList.positionIterator();
            while (!directFriendsList.offEnd()) {
                int friendVertexId = directFriendsList.getIterator();
                currentFriends.add(friendVertexId);
                directFriendsList.advanceIterator();
            }
        } catch (NoSuchElementException e) {
            System.err.println("Error processing current friends: " + e.getMessage());
        }

        for (int i = 0; i < userIds.size(); i++) {
            int potentialUserId = userIds.get(i);
            if (potentialUserId == userId || currentFriends.contains(potentialUserId)) {
                continue;
            }

            int distance = -1;
            try {
                distance = friendNetwork.getDistance(potentialUserId);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Error retrieving distance for User ID " + potentialUserId + ": " + e.getMessage());
                continue;
            }

            int sharedInterests = calculateSharedInterests(userId, potentialUserId);

            if (distance > 0 && distance < Integer.MAX_VALUE) {
                recommendations.add(new FriendRecommendation(potentialUserId, distance, sharedInterests));
            }
        }

        // Sort recommendations: first by distance ascending, then by sharedInterests descending
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

        System.out.println("\nEnter the number of a friend to add or -1 to quit:");
        System.out.print("Enter your choice: ");
        try {
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
        } catch (Exception e) {
            System.err.println("Invalid input. Please enter a valid number.");
            input.nextLine(); // Clear the invalid input
        }
    }

    /**
     * Calculates the number of shared interests between the user and a potential friend.
     * Uses nested loops instead of a HashSet for comparison.
     *
     * @param userId   The ID of the current user.
     * @param friendId The ID of the potential friend.
     * @return The number of shared interests.
     */
    private int calculateSharedInterests(int userId, int friendId) {
        int sharedCount = 0;

        LinkedList<Interests> interestsUser = getInterests(userId);
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
                        break; // Avoid counting duplicate interests
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

    /**
     * Retrieves the interests for a specific user.
     *
     * @param userId The ID of the user whose interests are to be retrieved.
     * @return The LinkedList of Interests for the user.
     */
    private LinkedList<Interests> getInterests(int userId) {
        if (!userIds.contains(userId)) {
            System.err.println("Invalid user ID for interests retrieval.");
            return null;
        }

        return interestManager.getInterestsByUserID(userId);
    }

    /**
     * Helper method to get a user's name by their User ID.
     *
     * @param userId The User ID.
     * @return The full name of the user, or null if not found.
     */
    private String getUserNameById(int userId) {
        int index = userIds.indexOf(userId);
        if (index != -1 && index < names.size()) {
            return names.get(index);
        }
        return null;
    }
}
