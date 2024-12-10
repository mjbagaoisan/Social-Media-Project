package dataStructures;

/**
 * HashTable.java
 * @author Marc Jowell Bagaoisan
 * @author Partner's name
 * CIS 22C, Lab 13.2
 */

import java.util.ArrayList;
import java.util.LinkedList;

public class HashTable<T> {

    private int numElements;
    private ArrayList<LinkedList<T> > table;

    /**
     * Constructor for the HashTable class. Initializes the Table to be sized
     * according to value passed in as a parameter. Inserts size empty Lists into
     * the table. Sets numElements to 0
     *
     * @param size the table size
     * @precondition size > 0
     * @throws IllegalArgumentException when size <= 0
     */
    public HashTable(int size) throws IllegalArgumentException {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }
        numElements = 0;
        table = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            table.add(new LinkedList<T>());
        }
    }

    /**
     * Constructor for HashTable class.
     * Inserts the contents of the given array
     * into the Table at the appropriate indices
     * @param array an array of elements to insert
     * @param size the size of the Table
     * @precondition size > 0
     * @throws IllegalArgumentException when <=0
     */
    public HashTable(T[] array, int size) throws IllegalArgumentException {
        this(size);

        if (size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }
        if (array != null) {
            numElements = 0;
            table = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                table.add(new LinkedList<T>());
            }
            for (int i = 0; i < array.length; i++) {
                this.add(array[i]);
            }
        }
    }

    /** Accessors */

    /**
     * Returns the hash value in the table for a given Object.
     * @param obj the Object
     * @return the index in the table
     */
    private int hash(T obj) {
        int code = obj.hashCode();
        return code % table.size();
    }

    /**
     * Counts the number of elements at this index.
     * @param index the index in the table
     * @precondition 0 <= index < table.size()
     * @return the count of elements at this index
     * @throws IndexOutOfBoundsException when the precondition is violated
     */
    public int countBucket(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= table.size()) {
            throw new IndexOutOfBoundsException("countBucket: index is out of bounds");
        }
        return table.get(index).size();
    }

    /**
     * Determines total number of elements in the table
     * @return total number of elements
     */
    public int getNumElements() {
        return numElements;
    }

    /**
     * Accesses a specified key in the Table
     * @param t the key to search for
     * @return the value to which the specified key is mapped,
     * or null if this table contains no mapping for the key.
     * @precondition elmt != null
     * @throws NullPointerException when the precondition is violated.
     */
    public T get(T elmt) throws NullPointerException {
        if (elmt == null) {
            throw new NullPointerException("get: elmt is null");
        }
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).contains(elmt)) {
                return table.get(i).get(table.get(i).indexOf(elmt));
            }
        }
        return null;
    }
    
    public LinkedList getRow(int rowNum){
        return table.get(rowNum);
    }

    /**
     * Accesses a specified element in the table.
     * @param elmt the element to locate
     * @return the bucket number where the element
     * is located or -1 if it is not found.
     * @precondition elmt != null
     * @throws NullPointerException when the precondition is violated.
     */
    public int find(T elmt) throws NullPointerException{
        if (elmt == null) {
            throw new NullPointerException("find: elmt is null");
        }
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).contains(elmt)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Determines whether a specified element is in the table.
     * @param elmt the element to locate
     * @return whether the element is in the table
     * @precondition <you fill in here>
     * @throws NullPointerException when the precondition is violated
     */
    public boolean contains(T elmt) throws NullPointerException {
        if (elmt == null) {
            throw new NullPointerException("contains: elmt is null");
        }

        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).contains(elmt)) {
                return true;
            }
        }
        return false;
    }

    /** Mutators */

    /**
     * Inserts a new element in the table at the end of the chain
     * of the correct bucket.
     * @param elmt the element to insert
     * @precondition elmt != null
     * @throws NullPointerException when the precondition is violated.
     */
    public void add(T elmt) throws NullPointerException {
        if (elmt == null) {
            throw new NullPointerException("add: elmt is null");
        }
        int bucket = hash(elmt);
        table.get(bucket).addLast(elmt);
        numElements++;
    }

    /**
     * Removes the given element from the table.
     * @param elmt the element to remove
     * @precondition elmt != null
     * @return whether elmt exists and was removed from the table
     * @throws NullPointerException when the precondition is violated
     */
    public boolean delete(T elmt) throws NullPointerException {
        if (elmt == null) {
            throw new NullPointerException("delete: elmt is null");
        }
        int bucket = hash(elmt);
        return table.get(bucket).remove(elmt);
    }

    /**
     * Resets the hash table back to the empty state, as if the one argument
     * constructor has just been called.
     */
    public void clear() {
        for (int i = 0; i < table.size(); i++) {
            table.get(i).clear();
        }
        numElements = 0;
    }

    /** Additional Methods */

    /**
     * Computes the load factor.
     * @return the load factor
     */
    public double getLoadFactor() {
        return (double) numElements / table.size();
    }

    /**
     * Creates a String of all elements at a given bucket
     * @param bucket the index in the table
     * @return a String of elements, separated by spaces with a new line character
     *         at the end
     * @precondition 0 <= bucket < table.size()
     * @throws IndexOutOfBoundsException when bucket is
     * out of bounds
     */
    public String bucketToString(int bucket) throws IndexOutOfBoundsException {
        if (bucket < 0 || bucket >= table.size()) {
            throw new IndexOutOfBoundsException("bucketToString: bucket is out of bounds");
        }

        LinkedList<T> list = table.get(bucket);
        StringBuilder result = new StringBuilder();
        for (T elmt : list) {
            result.append(elmt).append(" ");
        }

        return result.toString() + "\n";

    }

    /**
     * Creates a String of the bucket number followed by a colon followed by
     * the first element at each bucket followed by a new line. For empty buckets,
     * add the bucket number followed by a colon followed by empty.
     *
     * @return a String of all first elements at each bucket.
     */
    /**
     * Creates a String of the bucket number followed by a colon followed by
     * the first element at each bucket followed by a new line. For empty buckets,
     * add the bucket number followed by a colon followed by empty.
     *
     * @return a String of all first elements at each bucket.
     */
    public String rowToString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < table.size(); i++) {
            LinkedList<T> list = table.get(i);
            if (list.isEmpty()) {
                result.append("Bucket ").append(i).append(": empty\n");
            } else {
                // Only append the first element in the bucket
                result.append("Bucket ").append(i).append(": ").append(list.getFirst()).append("\n");
            }
        }
        return result.toString();
    }





    /**
     * Starting at the 0th bucket, and continuing in order until the last
     * bucket, concatenates all elements at all buckets into one String, with
     * a new line between buckets and one more new line at the end of the
     * entire String.
     * @return a String of all elements in this HashTable.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (LinkedList<T> list : table) {
            if (!list.isEmpty()) {
                for (T element : list) {
                    result.append(element).append(" ");
                }
                result.append("\n");
            }
        }

        return result.toString() + "\n";
    }
}