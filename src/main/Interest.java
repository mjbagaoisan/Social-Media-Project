package main;

import java.util.Objects;
public class Interest{// implements Comparable<Interest> {
  private String interestName;
  private int interestID;

  /**
   * Constructor for Interest
   * @param interestName of the interest
   * @param interestId aunique indetifer for the interest 
   */
  public Interest(String interestName, int interestID){
    this.interestName =interestName;
    this.interestID = interestID;
  }
}
