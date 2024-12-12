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

    public InterestManager(int hashTableSize) {
        interestsTable = new HashTable<>(hashTableSize);
        interestsList = new LinkedList<>();
    }

    public void addInterest(String interest, User user) {
        if (interest == null || user == null) return;

        Interests newInterest = new Interests(interest, user);
        interestsTable.add(newInterest);
        interestsList.addLast(newInterest);

        System.out.println("DEBUG: Adding interest to InterestManager: " + interest +
                " for user: " + user.getFullName());

    }

    public LinkedList<Interests> getInterestsByUserID(int userID) {
        LinkedList<Interests> userInterests = new LinkedList<>();
        interestsList.positionIterator();

        while (!interestsList.offEnd()) {
            try {
                Interests currentInterest = interestsList.getIterator();
                if (currentInterest.getUser().getId() == userID) {
                    userInterests.addLast(currentInterest);
                }
                interestsList.advanceIterator();
            } catch (NoSuchElementException | NullPointerException e) {
                System.err.println("Error while retrieving interests: " + e.getMessage());
                break;
            }
        }

        // Debugging
        System.out.println("DEBUG: Retrieved interests for user ID: " + userID);
        userInterests.positionIterator();
        while (!userInterests.offEnd()) {
            System.out.println("DEBUG: Interest: " + userInterests.getIterator().getInterestName());
            userInterests.advanceIterator();
        }

        return userInterests;
    }


    /**
     * Searches for users who have a specific interest using a LinkedList.
     *
     * @param interestName the name of the interest to search for
     * @return a LinkedList of user names who have the specified interest
     */
    public LinkedList<String> searchUsersByInterest(String interestName) {
        LinkedList<String> usersWithInterest = new LinkedList<>();

        interestsList.positionIterator();


        while (!interestsList.offEnd()) {
            try {
                Interests currentInterest = interestsList.getIterator();


                if (currentInterest.getInterestName().equalsIgnoreCase(interestName)) {
                    usersWithInterest.addLast(currentInterest.getUser().getFullName());
                }

                interestsList.advanceIterator();
            } catch (NoSuchElementException | NullPointerException e) {
                System.err.println("Error while searching for users with interest: " + e.getMessage());
                break;
            }
        }

        return usersWithInterest;
    }

    public LinkedList<String> getInterestNamesForDisplay(int userID) {
        LinkedList<String> userInterests = new LinkedList<>();
        interestsList.positionIterator();

        while (!interestsList.offEnd()) {
            Interests currentInterest = interestsList.getIterator();
            if (currentInterest.getUser().getId() == userID) {
                userInterests.addLast(currentInterest.getInterestName());
            }
            interestsList.advanceIterator();
        }

        return userInterests;
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