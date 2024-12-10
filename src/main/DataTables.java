
package main;

import dataStructures.HashTable;

public class DataTables {

    private HashTable<Interests> ih;
    private HashTable<AuthHolder> AH;
    private int ts;
    

    public DataTables(int tableSize) {
        this.ts = tableSize;
        ih = new HashTable<>(tableSize);
        AH =  new HashTable<>(tableSize);
    }

    private int hashPassword(String password) {
        return Math.abs(password.hashCode());
    }

    public void register(String username, String password) {
        AuthHolder x = new AuthHolder(username, password);
        AH.add(x);
    }

    // Authenticate a user
    public String authenticate(String username, String password) {
        AuthHolder res = AH.get(new AuthHolder(username, password));
        if(AH.username.equals(username)){
            return "valid";
        }else{
            return "invalid";
        }
    }
    

    public void userHasInterest(String interest, User user){
        Interest y = new Interest(interest, user);
        ih.add(y);
    }

    public ArrayList<User> getUsersWithInterest(String interest){

        ArrayList<User> users = new ArrayList<>();
        try{
            interest = Math.abs(strToInteger(interest));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found: " + e.getMessage());
        }

        int row = interest%ts;
        LinkedList _ = ih.getRow(row);
        _.positionIterator();
        while (!_.offEnd()) {
            Interests m = (Interests) _.getIterator(); // Access the data at the iterator
            users.add(_.getUser());
            _.advanceIterator(); // Move the iterator to the next node
        }

        return users;

    }
    

    public static int strToInteger(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());
        BigInteger hashInt = new BigInteger(1, hashBytes);
        return hashInt.intValue(); 
    }

}