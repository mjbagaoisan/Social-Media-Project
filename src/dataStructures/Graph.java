package dataStructures;
/**
 * Graph.java
 * @author Marc Jowell Bagaoisan
 * CIS 22C, Lab 17
 */
import java.util.ArrayList;

public class Graph {
    private int vertices;
    private int edges;
    private ArrayList<LinkedList<Integer>> adj;
    private ArrayList<Character> color;
    private ArrayList<Integer> distance;
    private ArrayList<Integer> parent;
    private ArrayList<Integer> discoverTime;
    private ArrayList<Integer> finishTime;
    private static int time = 0;

    /** Constructors and Destructors */

    /**
     * initializes an empty graph, with n vertices and 0 edges
     *
     * @param numVtx the number of vertices in the graph
     * @precondition numVtx > 0
     * @throws IllegalArgumentException when numVtx <= 0
     */
    public Graph(int numVtx) throws IllegalArgumentException {
        if (numVtx <= 0) {
            throw new IllegalArgumentException("Number of vertices must be greater than zero");
        }

        edges = 0;
        vertices = numVtx;

        adj = new ArrayList<>(numVtx);
        color = new ArrayList<>(numVtx);
        distance = new ArrayList<>(numVtx);
        parent = new ArrayList<>(numVtx);
        discoverTime = new ArrayList<>(numVtx);
        finishTime = new ArrayList<>(numVtx);

        for (int i = 0; i < vertices; i++) {
            adj.add(new LinkedList<>());
            color.add('W'); // White
            distance.add(-1); // Uninitialized
            parent.add(0); // Uninitialized
            discoverTime.add(-1); // Uninitialized
            finishTime.add(-1); // Uninitialized
        }
    }

    /*** Accessors ***/

    /**
     * Returns the number of edges in the graph
     *
     * @return the number of edges
     */
    public int getNumEdges() {
        return edges;
    }

    /**
     * Returns the number of vertices in the graph
     *
     * @return the number of vertices
     */
    public int getNumVertices() {
        return vertices;
    }

    /**
     * returns whether the graph is empty (no edges)
     *
     * @return whether the graph is empty
     */
    public boolean isEmpty() {
        return edges == 0;
    }

    /**
     * Returns the value of the distance[v]
     *
     * @param v a vertex in the graph
     * @precondition 0 < v <= vertices
     * @return the distance of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     */
    public Integer getDistance(Integer v) throws IndexOutOfBoundsException {
        if (v < 1 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex index out of bounds");
        }
        return distance.get(v - 1);
    }

    /**
     * Returns the value of the parent[v]
     *
     * @param v a vertex in the graph
     * @precondition 0 < v <= vertices
     * @return the parent of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     */
    public Integer getParent(Integer v) throws IndexOutOfBoundsException {
        if (v < 1 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex index out of bounds");
        }
        return parent.get(v - 1);
    }

    /**
     * Returns the value of the color[v]
     *
     * @param v a vertex in the graph
     * @precondition 0 < v <= vertices
     * @return the color of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     */
    public Character getColor(Integer v) throws IndexOutOfBoundsException {
        if (v < 1 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex index out of bounds");
        }
        return color.get(v - 1);
    }

    /**
     * Returns the value of the discoverTime[v]
     *
     * @param v a vertex in the graph
     * @precondition 0 < v <= vertices
     * @return the discover time of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     */
    public Integer getDiscoverTime(Integer v) throws IndexOutOfBoundsException {
        if (v < 1 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex index out of bounds");
        }
        return discoverTime.get(v - 1);
    }

    /**
     * Returns the value of the finishTime[v]
     *
     * @param v a vertex in the graph
     * @precondition 0 < v <= vertices
     * @return the finish time of vertex v
     * @throws IndexOutOfBoundsException when v is out of bounds
     */
    public Integer getFinishTime(Integer v) throws IndexOutOfBoundsException {
        if (v < 1 || v > vertices) {
            throw new IndexOutOfBoundsException("Vertex index out of bounds");
        }
        return finishTime.get(v - 1);
    }

    /**
     * Returns the LinkedList stored at index v
     *
     * @param v a vertex in the graph
     * @return the adjacency LinkedList at v
     * @precondition 0 < v <= vertices
     * @throws IndexOutOfBoundsException when v is out of bounds
     */
    public LinkedList<Integer> getAdjacencyList(Integer v)
        throws IndexOutOfBoundsException {
            if (v < 1 || v > vertices) {
                throw new IndexOutOfBoundsException("Vertex index out of bounds");
            }
            return adj.get(v - 1);
    }

    /*** Manipulation Procedures ***/

    /**
     * Inserts vertex v into the adjacency list of vertex u (i.e. inserts v into
     * the list at index u) @precondition, 0 < u, v <= vertices
     *
     * @param u a vertex in the graph
     * @param v a vertex in the graph
     * @throws IndexOutOfBounds exception when u or v is out of bounds
     */
    public void addDirectedEdge(Integer u, Integer v)
        throws IndexOutOfBoundsException {
            if (u < 1 || u > vertices || v < 1 || v > vertices) {
                throw new IndexOutOfBoundsException("Vertex index out of bounds");
            }
            adj.get(u - 1).addLast(v); 
            edges++;
        }

    /**
     * Inserts vertex v into the adjacency list of vertex u (i.e. inserts v into
     * the list at index u) and inserts u into the adjacent vertex list of v.
     *
     * @param u a vertex in the graph
     * @param v a vertex in the graph
     * @precondition, 0 < u, v <= vertices
     * @throws IndexOutOfBoundsException when u or v is out of bounds
     */
    public void addUndirectedEdge(Integer u, Integer v)
        throws IndexOutOfBoundsException {
            if (u < 1 || u > vertices || v < 1 || v > vertices) {
                throw new IndexOutOfBoundsException("Vertex index out of bounds");
            }
            adj.get(u - 1).addLast(v); // Use custom LinkedList
            adj.get(v - 1).addLast(u); // Use custom LinkedList
            edges++;
        }

    /*** Additional Operations ***/

    /**
     * Creates a String representation of the Graph Prints the adjacency list of
     * each vertex in the graph, vertex: <space separated list of adjacent
     * vertices>
     * @return a space separated list of adjacent vertices
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vertices; i++) {
            sb.append(i + 1).append(": "); // Print vertex (1-based index)

            LinkedList<Integer> list = adj.get(i);
            list.positionIterator(); // Reset iterator to the start

            while (!list.offEnd()) {
                sb.append(list.getIterator()).append(" "); // Print adjacent vertex (1-based index)
                list.advanceIterator(); // Move to the next adjacent vertex
            }

            sb.append("\n"); // Move to the next line after printing the adjacency list of a vertex
        }
        return sb.toString();
    }

    /**
     * Performs breath first search on this Graph give a source vertex
     *
     * @param source the starting vertex
     * @precondition source is a vertex in the graph
     * @throws IndexOutOfBoundsException when the source vertex is out of bounds
     *     of the graph
     */
    public void BFS(Integer source) throws IndexOutOfBoundsException {
        if (source < 1 || source > vertices) {
            throw new IndexOutOfBoundsException("Source vertex out of bounds");
        }
    
        // Initialize all vertices
        for (int i = 0; i < vertices; i++) {
            color.set(i, 'W');  // White (unvisited)
            distance.set(i, -1); // Uninitialized distance
            parent.set(i, 0);    // Set parent to 0 for unvisited vertices
        }
    
        // Initialize source vertex
        color.set(source - 1, 'G');  // Gray (visiting)
        distance.set(source - 1, 0);
        parent.set(source - 1, 0); // Source has no parent
    
        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<>();
        queue.addLast(source); // Add the source to the queue
    
        while (!queue.isEmpty()) {
            Integer u = queue.getFirst(); // Get the first element in the queue
            queue.removeFirst(); // Remove it from the queue

            
            LinkedList<Integer> adjacentVertices = adj.get(u - 1); // Get the adjacency list
            adjacentVertices.positionIterator(); // Reset iterator to the start

            while (!adjacentVertices.offEnd()) {
                Integer v = adjacentVertices.getIterator(); // Get the adjacent vertex
                if (color.get(v - 1) == 'W') { // If the vertex is unvisited
                    color.set(v - 1, 'G');  // Mark it as visited (Gray)
                    distance.set(v - 1, distance.get(u - 1) + 1); // Set distance
                    parent.set(v - 1, u); // Set parent
                    queue.addLast(v); // Add to the queue
                }
                adjacentVertices.advanceIterator(); // Move to the next adjacent vertex
            }
            color.set(u - 1, 'B');  // Mark it as fully explored (Black)
        }
    }

    /**
     * Performs depth first search on this Graph in order of vertex lists
     */
    public void DFS() {
        for (int i = 0; i < vertices; i++) {
            color.set(i, 'W');  // White
            parent.set(i, -1);   // Uninitialized
        }

        time = 0;  // Reset time

        // Visit each vertex if it hasn't been visited
        for (int i = 0; i < vertices; i++) {
            if (color.get(i) == 'W') {
                visit(i + 1); // Adjust for 1-based index
            }
        }

    }

    /**
     * Private recursive helper method for DFS
     *
     * @param vertex the vertex to visit
     */
    private void visit(int vertex) {
        color.set(vertex - 1, 'G');  // Gray
        discoverTime.set(vertex - 1, ++time);

        LinkedList<Integer> adjacentVertices = adj.get(vertex - 1);
        adjacentVertices.positionIterator();

        while (!adjacentVertices.offEnd()) {
            Integer v = adjacentVertices.getIterator();
            if (color.get(v - 1) == 'W') {
                parent.set(v - 1, vertex);
                visit(v);
            }
            adjacentVertices.advanceIterator();
        }

        color.set(vertex - 1, 'B');  // Black
        finishTime.set(vertex - 1, ++time);
    }
}
