import dataStructures.HashTable;

public class DataTables {

    private HashTable<String> interestIterator;
    private HashTable<AuthHolder> AH;
    

    public DataTables(int tableSize) {
        interestIterator = new HashTable<>(tableSize);
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
}