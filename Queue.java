/**
 * The Queue class definition
 * @author Minh Long Hang
 * CIS 22C, Lab 5
 * @param <T> the generic data stored in the Queue
 */
import java.util.NoSuchElementException;

public class Queue<T> implements Q<T> {
    private class Node {
        private T data;
        private Node next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private int size;
    private Node front;
    private Node end;

    /****CONSTRUCTORS****/

    /**
     * Default constructor for the Queue class
     * @postcondition a new Queue object with all fields
     * assigned default values
     */
    public Queue() {
        front = end = null;
        size = 0;
    }

    /**
     * Converts an array into a Queue
     * @param array the array to copy into
     * the Queue
     */
    public Queue(T[] array) {
        if (array == null) {
            front = end = null;
            size = 0;
            return;
        }
        for (T element : array) {
            enqueue(element);
        }
    }

    /**
     * Copy constructor for the Queue class
     * Makes a deep copy of the parameter
     * @param original the Queue to copy
     * @postcondition a new Queue object which is an identical,
     * but separate, copy of the original
     */
    public Queue(Queue<T> original) {
        if (original == null || original.isEmpty()) {
            front = end = null;
            size = 0;
            return;
        }
        Node temp = original.front;
        while (temp != null) {
            enqueue(temp.data);
            temp = temp.next;
        }
    }

    /****ACCESSORS****/

    /**
     * Returns the value stored at the front
     * of the Queue
     * @return the value at the front of the queue
     * @precondition !isEmpty()
     * @throws NoSuchElementException when the
     * precondition is violated
     */
    public T getFront() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("getFront(): Queue is empty!");
        }
        return front.data;
    }

    /**
     * Returns the size of the Queue
     * @return the size from 0 to n
     */
    public int getSize() {
        return size;
    }

    /**
     * Determines whether a Queue is empty
     * @return whether the Queue contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /****MUTATORS****/

    /**
     * Inserts a new value at the end of the Queue
     *
     * @param data the new data to insert
     * @postcondition a new node at the end of the Queue
     */
    public void enqueue(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            front = end = newNode;
        } else {
            end.next = newNode;
            end = newNode;
        }
        size++;
    }

    /**
     * Removes the front element in the Queue
     * @precondition !isEmpty()
     * @throws NoSuchElementException when
     * the precondition is violated
     * @postcondition the front element has been removed
     */
    public void dequeue() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("dequeue(): Queue is empty!");
        }
        front = front.next;
        size--;
        if (isEmpty()) {
            end = null;
        }
    }

    /****ADDITIONAL OPERATIONS****/

    /**
     * Returns the values stored in the Queue
     * as a String, separated by a blank space
     * with a new line character at the end
     * @return a String of Queue values
     */
    @Override 
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node temp = front;
        while (temp != null) {
            sb.append(temp.data).append(" ");
            temp = temp.next;
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Determines whether two Queues contain
     * the same values in the same order
     * @param obj the Object to compare to this
     * @return whether obj and this are equal
     */
    @SuppressWarnings("unchecked") // good practice to remove warning here
    @Override 
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Queue<T> other = (Queue<T>) obj;
        if (size != other.size) {
            return false;
        }
        Node thisTemp = this.front;
        Node otherTemp = other.front;
        while (thisTemp != null) {
            if (!thisTemp.data.equals(otherTemp.data)) {
                return false;
            }
            thisTemp = thisTemp.next;
            otherTemp = otherTemp.next;
        }
        return true;
    }
}