package main;

import dataStructures.HashTable;
import dataStructures.LinkedList;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Manages the association between users and their interests.
 */
public class InterestManager {
    private HashTable<Interests> interestsTable;
    private LinkedList<Interests> interestsList;

    /**
     * Constructor initializes both the HashTable and the auxiliary LinkedList.
     *
     * @param hashTableSize the size of the HashTable
     */
    public InterestManager(int hashTableSize) {
        interestsTable = new HashTable<>(hashTableSize);
        interestsList = new LinkedList<>();
    }

    /**
     * Adds an interest for a user.
     *
     * @param interest the interest to add
     * @param user     the User object
     */
    public void addInterest(String interest, User user) {
        Interests newInterest = new Interests(interest, user);
        interestsTable.add(newInterest);
        interestsList.addLast(newInterest);
    }

    /**
     * Retrieves a LinkedList of interests for a specific userID.
     *
     * @param userID the ID of the user whose interests are to be retrieved
     * @return a LinkedList of Interests associated with the userID
     */
    public LinkedList<Interests> getInterestsByUserID(int userID) {
        LinkedList<Interests> userInterests = new LinkedList<>(); // Correct initialization

        // Initialize the iterator to the start of the LinkedList
        interestsList.positionIterator();

        // Iterate through the LinkedList
        while (!interestsList.offEnd()) {
            try {
                Interests currentInterest = interestsList.getIterator();
                if (currentInterest.getUser().getId() == userID) {
                    userInterests.addLast(currentInterest); // Use addLast for custom LinkedList
                }
                interestsList.advanceIterator();
            } catch (NoSuchElementException | NullPointerException e) {
                System.err.println("Error while retrieving interests: " + e.getMessage());
                break;
            }
        }

        return userInterests;
    }


    /**
     * Removes an interest for a user.
     *
     * @param interest the interest to remove
     * @param user     the User object
     * @return true if the interest was successfully removed, false otherwise
     */
    public boolean removeInterest(String interest, User user) {
        Interests targetInterest = new Interests(interest, user);

        // Attempt to delete from HashTable
        boolean removedFromTable = interestsTable.delete(targetInterest);

        // Attempt to delete from LinkedList
        boolean removedFromList = removeFromList(targetInterest);

        return removedFromTable && removedFromList;
    }

    /**
     * Helper method to remove an interest from the auxiliary LinkedList.
     *
     * @param targetInterest the Interests object to remove
     * @return true if removed successfully, false otherwise
     */
    private boolean removeFromList(Interests targetInterest) {
        // Initialize the iterator to the start of the LinkedList
        interestsList.positionIterator();

        // Iterate through the LinkedList to find and remove the targetInterest
        while (!interestsList.offEnd()) {
            try {
                Interests currentInterest = interestsList.getIterator();
                if (currentInterest.equals(targetInterest)) {
                    interestsList.removeIterator();
                    return true;
                }
                interestsList.advanceIterator();
            } catch (NoSuchElementException | NullPointerException e) {
                System.err.println("Error while removing interest from list: " + e.getMessage());
                break;
            }
        }

        return false;
    }

    /**
     * Clears all interests from both the HashTable and the LinkedList.
     */
    public void clearAllInterests() {
        interestsTable.clear();
        interestsList.clear();
    }


    /**
     * Provides a string representation of all interests.
     *
     * @return a string listing all interests
     */
    @Override
    public String toString() {
        return interestsTable.toString();
    }
}
