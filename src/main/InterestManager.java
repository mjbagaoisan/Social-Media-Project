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
        if (interest == null || user == null) {
            return;
        }

        // Check if this exact interest-user combination already exists
        boolean exists = false;
        interestsList.positionIterator();
        while (!interestsList.offEnd()) {
            Interests current = interestsList.getIterator();
            if (current.getInterestName().equals(interest) &&
                    current.getUser().getId() == user.getId()) {
                exists = true;
                break;
            }
            interestsList.advanceIterator();
        }

        // Only add if it doesn't already exist
        if (!exists) {
            Interests newInterest = new Interests(interest, user);
            interestsTable.add(newInterest);
            interestsList.addLast(newInterest);
        }
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


        userInterests.positionIterator();
        while (!userInterests.offEnd()) {
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
            try {
                Interests currentInterest = interestsList.getIterator();
                User interestUser = currentInterest.getUser();

                if (interestUser != null && interestUser.getId() == userID) {
                    String interestName = currentInterest.getInterestName();
                    userInterests.addLast(interestName);
                }
                interestsList.advanceIterator();
            } catch (Exception e) {
                System.err.println("Error retrieving interest: " + e.getMessage());
            }
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