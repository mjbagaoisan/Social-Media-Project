/**
 * Graph.java
 * @author Marc Jowell Bagaoisan
 * @author Partner's name
 * CIS 22C, Lab 15
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
            throw new IllegalArgumentException("Number of vertices must be > 0.");
        }
        this.vertices = numVtx;
        this.edges = 0;
        adj = new ArrayList<LinkedList<Integer>>(numVtx + 1);
        color = new ArrayList<Character>(numVtx + 1);
        distance = new ArrayList<Integer>(numVtx + 1 );
        parent = new ArrayList<Integer>(numVtx + 1);
        discoverTime = new ArrayList<Integer>(numVtx + 1);
        finishTime = new ArrayList<Integer>(numVtx + 1);

        for (int i = 0; i <= numVtx; i++) {
            adj.add(new LinkedList<Integer>());
            color.add('W');
            distance.add(-1);
            parent.add(0);
            discoverTime.add(-1);
            finishTime.add(-1);
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
        if (v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException();
        }
        return distance.get(v);
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
        if (v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException();
        }
        return parent.get(v);
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
        if (v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException("getColor: vertex out of bounds" + v );
        }
        return color.get(v);
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
        if (v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException();
        }
        return discoverTime.get(v);
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
        if (v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException();
        }
        return finishTime.get(v);
    }

    /**
     * Returns the LinkedList stored at index v
     *
     * @param v a vertex in the graph
     * @return the adjacency LinkedList at v
     * @precondition 0 < v <= vertices
     * @throws IndexOutOfBoundsException when v is out of bounds
     */
    public String getAdjacencyList(Integer v)
            throws IndexOutOfBoundsException {
        if (v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException();
        }
        StringBuilder result = new StringBuilder();
        LinkedList<Integer> list = adj.get(v);

        list.positionIterator();
        while (!list.offEnd()) {
            Integer vertex = list.getIterator();
            result.append(vertex).append(" ");
            list.advanceIterator();
        }
        result.append("\n");

        return result.toString();
    }


    /*** Manipulation Procedures ***/

    /**
     * Inserts vertex v into the adjacency list of vertex u (i.e. inserts v into
     * the list at index u) @precondition, 0 < u, v <= vertices
     *
     * @param u a vertex in the graph
     * @param v a vertex in the graph
     * @throws IndexOutOfBoundsException when u or v is out of bounds
     */
    public void addDirectedEdge(Integer u, Integer v)
            throws IndexOutOfBoundsException {
        if (u <= 0 || u > vertices || v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException();
        }
        adj.get(u).addLast(v);
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
    public void addUndirectedEdge(Integer u, Integer v) throws IndexOutOfBoundsException {
        if (u <= 0 || u > vertices || v <= 0 || v > vertices) {
            throw new IndexOutOfBoundsException();
        }
        adj.get(v).addLast(u);
        adj.get(u).addLast(v);
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
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= vertices; i++) {
            result.append(i).append(": ");
            LinkedList<Integer> neighbors = adj.get(i);

            neighbors.positionIterator();
            while (!neighbors.offEnd()) {
                result.append(neighbors.getIterator()).append(" ");
                neighbors.advanceIterator();
            }
            result.append("\n");
        }
        return result.toString();
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
        if (source <= 0 || source > vertices) {
            throw new IndexOutOfBoundsException("BFS: source out of bounds");
        }

        for (int i = 0; i <= vertices; i++) {
            color.set(i, 'W');
            parent.set(i, 0);
            distance.set(i, -1);
        }

        color.set(source, 'G');
        parent.set(source, 0);
        distance.set(source, 0);

        LinkedList<Integer> queue = new LinkedList<>();
        queue.addLast(source);

        while (!queue.isEmpty()) {
            int u = queue.getFirst();
            queue.removeFirst();

            LinkedList<Integer> neighbors = adj.get(u);
            neighbors.positionIterator();

            while (!neighbors.offEnd()) {
                int v = neighbors.getIterator();
                if (color.get(v) == 'W') {
                    color.set(v, 'G');
                    parent.set(v, u);
                    distance.set(v, distance.get(u) + 1);
                    queue.addLast(v);
                }
                neighbors.advanceIterator();
            }
            color.set(u, 'B');
        }
    }

    /**
     * Performs depth first search on this Graph in order of vertex lists
     */
    public void DFS() {
        // Initialize vertices
        for (int i = 0; i <= vertices; i++) {
            color.set(i, 'W');
            parent.set(i, 0);
            discoverTime.set(i, -1);
            finishTime.set(i, -1);
        }

        time = 0; // Reset the static time variable

        // Visit each unvisited vertex
        for (int i = 1; i <= vertices; i++) {
            if (color.get(i) == 'W') {
                visit(i);
            }
        }
    }

    /**
     * Private recursive helper method for DFS
     *
     * @param vertex the vertex to visit
     */
    private void visit(int vertex) {
        time++;
        discoverTime.set(vertex, time);
        color.set(vertex, 'G');

        LinkedList<Integer> neighbors = adj.get(vertex);
        neighbors.positionIterator();
        while (!neighbors.offEnd()) {
            int neighbor = neighbors.getIterator();
            if (color.get(neighbor) == 'W') {
                parent.set(neighbor, vertex);
                visit(neighbor);
            }
            neighbors.advanceIterator();
        }

        color.set(vertex, 'B');
        time++;
        finishTime.set(vertex, time);
    }
}
