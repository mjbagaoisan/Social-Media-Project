import java.util.LinkedList;

class HashTable<K, V> {

    // Define the structure of the key-value pair
    static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int DEFAULT_CAPACITY = 16; 
    private LinkedList<Entry<K, V>>[] table; 
    private int size = 0;

    // Constructor
    public HashTable() {
        table = new LinkedList[DEFAULT_CAPACITY];
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }
    }

    // Hash function to calculate the index for a given key
    private int hash(K key) {
        return Math.abs(key.hashCode() % table.length);
    }

    // Insert a key-value pair
    public void put(K key, V value) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value; // Update the value if the key already exists
                return;
            }
        }

        bucket.add(new Entry<>(key, value)); // Add a new entry
        size++;
    }

    // Retrieve a value by key
    public V get(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null; // Key not found
    }

    // Remove a key-value pair
    public void remove(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                bucket.remove(entry);
                size--;
                return;
            }
        }
    }

    // Get the number of elements in the HashMap
    public int size() {
        return size;
    }

    // Check if the HashMap is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Display the HashMap
    public void display() {
        for (int i = 0; i < table.length; i++) {
            System.out.print("Bucket " + i + ": ");
            for (Entry<K, V> entry : table[i]) {
                System.out.print("[" + entry.key + " -> " + entry.value + "] ");
            }
            System.out.println();
        }
    }

}
