import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Interests implements Comparable<Interests> {

    private int interest;
    private String user;

    public Interests(String interest, String user){
        this.user = user;
        try{
            this.interest = Math.abs(strToInteger(interest));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found: " + e.getMessage());
        }
    }
    
    
    @Override public int hashCode() {
		return interest;
	}

    public String getUser(){
        return user;
    }

    public static int strToInteger(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());
        BigInteger hashInt = new BigInteger(1, hashBytes);
        return hashInt.intValue(); 
    }
    
    @Override public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		} else if (!(obj instanceof Interests)) {
			return false;
		} else {
			Interests stu = (Interests) obj;
			return this.interest == stu.interest && stu.user.equals(this.user);
		}
	}
	
	@Override public int compareTo(Interests stu) {
		if(this.equals(stu)) {
			return 0;
		} else if (this.interest != stu.interest) {
			return Integer.compare(this.interest, stu.interest);
		} else {
			return this.user.compareTo(stu.user);
		}
	}
	

	
}