import java.util.NoSuchElementException;

public class LinkedList<T> {
    private class Node {
        private T data;
        private Node next;
        private Node prev;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private int length;
    private Node first;
    private Node last;
    private Node iterator;

    /**** CONSTRUCTORS ****/

    /**
     * Instantiates a new LinkedList with default values
     * @postcondition The list is empty (first, last, and iterator are null, and length is 0)
     */
    public LinkedList() {
        first = null;
        last = null;
        iterator = null;
        length = 0;
    }

    /**
     * Converts the given array into a LinkedList
     * @param array the array of values to insert into this LinkedList
     * @postcondition The LinkedList contains the same elements as the array, in the same order, and the length of the LinkedList is equal to the length of the array.
     */
    public LinkedList(T[] array) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                addLast(array[i]);
            }
        }
    }


    /**
     * Instantiates a new LinkedList by copying another List
     * @param original the LinkedList to copy
     * @postcondition a new List object, which is an identical,
     * but separate, copy of the LinkedList original
     */
    public LinkedList(LinkedList<T> original) {
        this();
        if (original != null && original.length != 0) {
            Node temp = original.first;
            while (temp != null) {
                addLast(temp.data);
                temp = temp.next;
            }
            iterator = null;
        }
    }

    /**** ACCESSORS ****/

    /**
     * Returns the value stored in the first node
     * @precondition length > 0
     * @return the value stored at node first
     * @throws NoSuchElementException length == 0
     */
    public T getFirst() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("List is empty.");
        }
        return first.data;
    }

    /**
     * Returns the value stored in the last node
     * @precondition length > 0
     * @return the value stored in the node last
     * @throws NoSuchElementException length == 0
     */
    public T getLast() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("List is empty.");
        }
        return last.data;
    }

    /**
     * Returns the data stored in the iterator node
     * @precondition iterator != null
     * @return the data stored in the iterator node
     * @throw NullPointerException If the iterator is off the list
     */
    public T getIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("getIterator: Iterator is null");
        }
        return iterator.data;
    }

    /**
     * Returns the current length of the LinkedList
     * @return the length of the LinkedList from 0 to n
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns whether the LinkedList is currently empty
     * @return whether the LinkedList is empty
     */
    public boolean isEmpty() {
        if (length == 0){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns whether the iterator is offEnd, i.e. null
     * @return whether the iterator is null
     */
    public boolean offEnd() {
        return iterator == null;
    }

    /**** MUTATORS ****/

    /**
     * Creates a new first element
     * @param data the data to insert at the front of the LinkedList
     * @postcondition A new node is added to the front of the list.
     * if the list empty the first and last node is pointed to the new node. Length increases
     */
    public void addFirst(T data) {
        Node newNode = new Node(data);

        if (length == 0) {
            first = newNode;
            last = newNode;
        } else {
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }

        length++;
    }


    /**
     * Creates a new last element
     * @param data the data to insert at the end of the LinkedList
     * @postcondition A new node is added to the end of the list. If the list was previously empty,
     */
    public void addLast(T data) {
        Node newNode = new Node(data);

        if (length == 0) {
            first = newNode;
            last = newNode;
        } else {
            last.next = newNode;
            newNode.prev = last;
            last = newNode;
        }

        length++;
    }


    /**
     * Inserts a new element after the iterator
     * @param data the data to insert
     * @precondition iterator != null
     * @throws NullPointerException  If the iterator is off the list
     */
    public void addIterator(T data) throws NullPointerException{
        if(iterator == null) {
            throw new NullPointerException("addIterator: iterator is null");
        } else if(iterator == last) {
            addLast(data);
        } else {
            Node newNode = new Node(data);
            newNode.next = iterator.next;
            newNode.prev = iterator;
            iterator.next.prev = newNode;
            iterator.next = newNode;
            length++;
        }
    }

    /**
     * removes the element at the front of the LinkedList
     * @precondition list is not empty (length > 0)
     * @postcondition The first node is removed. If list had one node, the list becomes empty.
     * If the list had more than one element, first node points to the new first node. Length is decremented by 1.
     * @throws NoSuchElementException if length is empty (length == 0)
     */
    public void removeFirst() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("List is empty");
        } else if (length == 1) {
            first = null;
            last = null;
            iterator = null;
            length = 0;
        } else {
            if (iterator == first) {
                iterator = null;
            }
            first = first.next;
            first.prev = null;
            length--;
        }
    }

    /**
     * removes the element at the end of the LinkedList
     * @precondition List is not empty (length > 0)
     * @postcondition The last node is removed. If list had one node, the list becomes empty.
     * If the list had more than one element, last node points to the new last node. Length is decremented by 1.
     * @throws NoSuchElementException if the list is empty (length == 0)
     */
    public void removeLast() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("List is empty");
        } else if (length == 1) {
            first = null;
            last = null;
            iterator = null;
            length = 0;
        } else {
            if (iterator == last) {
                iterator = null;
            }
            last = last.prev;
            last.next = null;
            length--;
        }
    }

    /**
     * removes the element referenced by the iterator
     * @precondition iterator != null
     * @postcondition The node referenced by the iterator is removed from the list.
     * @throws NullPointerException If the iterator is off the list
     */
    public void removeIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("removeIterator: Iterator is null");
        } else if (iterator == first) {  // Removing the first element
            if (first == last) {  // If it's the only element
                first = last = null;
            } else {
                first = iterator.next;
                first.prev = null;
            }
        } else if (iterator == last) {  // Removing the last element
            last = iterator.prev;
            last.next = null;
        } else {  // Removing from the middle
            iterator.prev.next = iterator.next;
            iterator.next.prev = iterator.prev;
        }
        iterator = null;
        length--;
    }


    /**
     * places the iterator at the first node
     * @postcondition The iterator is positioned at the first node of the list
     */
    public void positionIterator() {
        iterator = first;
    }

    /**
     * Moves the iterator one node towards the last
     * @precondition iterator != null
     * @postcondition The iterator is advanced to the next node in the list.
     * @throws NullPointerException If the iterator is off the list
     */
    public void advanceIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("advanceIterator: Iterator is null");
        } else {
            iterator = iterator.next;
        }

    }

    /**
     * Moves the iterator one node towards the first
     * @precondition iterator != null
     * @postcondition The iterator is moved to the previous node in the list
     * @throws NullPointerException If the iterator is off the list
     */
    public void reverseIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("reverseIterator: Iterator is null");
        } else {
            iterator = iterator.prev;
        }

    }

    /**** ADDITIONAL OPERATIONS ****/

    /**
     * Re-sets LinkedList to empty as if the
     * default constructor had just been called
     */
    public void clear() {
        first = null;
        last = null;
        iterator = null;
        length = 0;
    }

    /**
     * Converts the LinkedList to a String, with each value separated by a blank
     * line At the end of the String, place a new line character
     * @return the LinkedList as a String
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Node temp = first;

        while (temp != null) {
            result.append(temp.data + " ");
            temp = temp.next;
        }

        return result.toString() + "\n";
    }

    /**
     * Determines whether the given Object is
     * another LinkedList, containing
     * the same data in the same order
     * @param obj another Object
     * @return whether there is equality
     */
    @SuppressWarnings("unchecked") //good practice to remove warning here
    @Override public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof LinkedList)) {
            return false;
        } else {
            LinkedList<T> L = (LinkedList<T>) obj;
            if (length != L.length) {
                return false;
            } else {
                Node temp1 = this.first;
                Node temp2 = L.first;
                while (temp1 != null) {
                    if (temp1.data == null || temp2.data == null) {
                        if (temp1.data != temp2.data) {
                            return false;
                        }
                    } else if (!(temp1.data.equals(temp2.data))) {
                        return false;
                    }
                    temp1 = temp1.next;
                    temp2 = temp2.next;
                }
                return true;
            }
        }
    }

    /**CHALLENGE METHODS*/

    /**
     * Moves all nodes in the list towards the end
     * of the list the number of times specified
     * Any node that falls off the end of the list as it
     * moves forward will be placed the front of the list
     * For example: [1, 2, 3, 4, 5], numMoves = 2 -> [4, 5, 1, 2 ,3]
     * For example: [1, 2, 3, 4, 5], numMoves = 4 -> [2, 3, 4, 5, 1]
     * For example: [1, 2, 3, 4, 5], numMoves = 7 -> [4, 5, 1, 2 ,3]
     * @param numMoves the number of times to move each node.
     * @precondition numMoves >= 0
     * @postcondition iterator position unchanged (i.e. still referencing
     * the same node in the list, regardless of new location of Node)
     * @throws IllegalArgumentException when numMoves < 0
     */
    public void spinList(int numMoves) throws IllegalArgumentException {
        if (numMoves < 0) {
            throw new IllegalArgumentException("numMoves must be >= 0");
        }
        if (length <= 1 || numMoves == 0) {
            return;
        }
        for (int i = 0; i < numMoves % length; i++) {
            last.next = first;
            first.prev = last;
            first = last;
            last = last.prev;
            first.prev = null;
            last.next = null;
        }
    }


    /**
     * Splices together two LinkedLists to create a third List
     * which contains alternating values from this list
     * and the given parameter
     * For example: [1,2,3] and [4,5,6] -> [1,4,2,5,3,6]
     * For example: [1, 2, 3, 4] and [5, 6] -> [1, 5, 2, 6, 3, 4]
     * For example: [1, 2] and [3, 4, 5, 6] -> [1, 3, 2, 4, 5, 6]
     * @param list the second LinkedList
     * @return a new LinkedList, which is the result of
     * interlocking this and list
     * @postcondition this and list are unchanged
     */
    public LinkedList<T> altLists(LinkedList<T> list) {
        if (list == null) {
            LinkedList<T> result = new LinkedList<>();
            Node temp = first;
            while (temp != null) {
                result.addLast(temp.data);
                temp = temp.next;
            }
            return result;
        }

        LinkedList<T> result = new LinkedList<>();

        Node temp1 = first;
        Node temp2 = list.first;

        while (temp1 != null || temp2 != null) {
            if (temp1 != null) {
                result.addLast(temp1.data);
                temp1 = temp1.next;
            }
            if (temp2 != null) {
                result.addLast(temp2.data);
                temp2 = temp2.next;
            }
        }

        return result;
    }
    /**
     * Returns each element in the LinkedList along with its
     * numerical position from 1 to n, followed by a newline.
     * @return the LinkedList as a String
     */
    public String numberedListString() {
        Node temp = first;
        int i = 1;
        StringBuilder result = new StringBuilder();
        while (temp != null) {
            result.append(i + ". " + temp.data + "\n");
            temp = temp.next;
            i++;
        }
        return result.toString() + "\n";
    }

    /**
     * Searches the LinkedList for a given element's index.
     * @param data the data whose index to locate.
     * @return the index of the data or -1 if the data is not contained
     * in the LinkedList.
     */
    public int findIndex(T data) {
        Node temp = first;
        int i = 0;
        while (temp != null) {
            if (temp.data.equals(data)) {
                return i;
            }
            temp = temp.next;
            i++;
        }
        return -1;
    }

    /**
     * Advances the iterator to location within the LinkedList
     * specified by the given index.
     * @param index the index at which to place the iterator.
     * @precondition index >= 0,  index < length
     * @throws IndexOutOfBoundsException when index is out of bounds
     */
    public void advanceIteratorToIndex(int index)  throws IndexOutOfBoundsException {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("advanceIteratorToIndex: index out of bounds");
        }
        for (int i = 0; i < index; i++) {
            iterator = iterator.next;
        }

    }
}
