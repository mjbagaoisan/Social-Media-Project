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
        System.out.println("DEBUG: Adding interest to InterestManager: " + interest +
                " for user: " + user.getFullName());

        interestsTable.add(newInterest);
        interestsList.addLast(newInterest);
    }

    public LinkedList<Interests> getInterestsByUserID(int userID) {
        System.out.println("DEBUG: Getting interests from InterestManager for user ID: " + userID);
        LinkedList<Interests> userInterests = new LinkedList<>();

        interestsList.positionIterator();
        while (!interestsList.offEnd()) {
            Interests interest = interestsList.getIterator();
            if (interest.getUser().getId() == userID) {
                userInterests.addLast(interest);
                System.out.println("DEBUG: Found interest: " + interest.getInterestName());
            }
            interestsList.advanceIterator();
        }

        return userInterests;
    }

    public LinkedList<String> getInterestNamesForDisplay(int userID) {
        LinkedList<String> names = new LinkedList<>();
        LinkedList<Interests> interests = getInterestsByUserID(userID);

        interests.positionIterator();
        while (!interests.offEnd()) {
            names.addLast(interests.getIterator().getInterestName());
            interests.advanceIterator();
        }

        return names;
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