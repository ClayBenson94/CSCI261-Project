import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;

/**
 * Holds multiple representations of an undirected weighted graph.
 * Uses an Adjacency Matrix and an Adjacency Array
 * @author Clay Benson
 */
public class Graph {

    private ArrayList<ArrayList<Integer>> adjMatrix;
    private ArrayList<ArrayList<ArrayList<Integer>>> adjList;
    private ArrayList<Integer> visitedList;
    private HashMap<Integer, Integer> predecessors;
    private Random edgeGen;
    private Random weightGen;
    private ArrayList<Edge> matrixEdges;
    private ArrayList<Edge> listEdges;

    /**
     * Constructor for the Graph object. Instantiates the random number generators, and sets their seeds.
     * Additionally, upon creating a Graph object, it will initialize the adjacency list and matrix automatically
     * @param n the number of vertices in the graph
     * @param seed the seed of the edge random number generator to use. Will use 2*seed for the weight generator
     * @param p the probability that 2 nodes are connected
     */
    public Graph(int n, long seed, double p) {
        edgeGen = new Random();
        weightGen = new Random();
        edgeGen.setSeed(seed);
        weightGen.setSeed(seed*2);
    }

    public void execute(int n, double p) {
        initAdjacencies(n, p);

        printAdjacencyMatrix();
        printAdjacencyList();
        printDFSInformation();

        //Sorts
        long start_time, end_time;

        //Insertion sort with MATRIX
        start_time = System.currentTimeMillis();
        edgeInsertionSort(createMatrixEdges(n), n, "MATRIX");
        end_time = System.currentTimeMillis();
        System.out.println(String.format("Runtime: %d milliseconds\n",end_time-start_time));

        //Insertion sort with LIST
        start_time = System.currentTimeMillis();
        edgeInsertionSort(createListEdges(n), n, "LIST");
        end_time = System.currentTimeMillis();
        System.out.println(String.format("Runtime: %d milliseconds\n",end_time-start_time));
    }

    public void edgeInsertionSort(ArrayList<Edge> edgeList, int n, String source) {
        System.out.println("===================================");
        System.out.println(String.format("SORTED EDGES WITH %s USING INSERTION SORT",source));
        int i, j;
        for (i = 1; i < edgeList.size(); ++i) {
            j = i;
            while ((j > 0) && (edgeList.get(j-1).greaterThan(edgeList.get(j)))) {
                swapEdges(edgeList, j, j-1);
                j--;
            }
        }
        printEdgeList(edgeList);
        printEdgeWeightSum(edgeList);
    }

    private ArrayList<Edge> swapEdges(ArrayList<Edge> swapList, int pos1, int pos2) {
        Edge item1 = new Edge(swapList.get(pos1));
        Edge item2 = new Edge(swapList.get(pos2));
        swapList.set(pos1,item2);
        swapList.set(pos2,item1);
        return swapList;
    }

    private void printEdgeWeightSum(ArrayList<Edge> sumList) {
        int sum = 0;
        for (Edge eachEdge : sumList) {
            sum = sum + eachEdge.getWeight();
        }
        System.out.println(String.format("Total weight = %d",sum));
    }

    private void printEdgeList(ArrayList<Edge> printList) {
        for (Edge eachEdge : printList) {
            System.out.println(String.format("%d %d weight = %d",eachEdge.getSourceVertex(),eachEdge.getDestinationVertex(),eachEdge.getWeight()));
        }
    }

    /**
     * Creates a randomly connected, undirected, weighted graph.
     * Is represented as both an Adjacency Matrix and an Adjacency List.
     * @param n the number of vertices in the graph
     * @param p the probability (0 to 1) that any given edge will be created between 2 nodes
     */
    private void initAdjacencies(int n, double p) {
        long start_time = System.currentTimeMillis(); //Should I start the time here, or below?
        do {
            start_time = System.currentTimeMillis();

            //Initialize the matrix to all zeroes to begin
            adjMatrix = new ArrayList<>(); //ArrayList of ArrayLists
            adjList = new ArrayList<>(); //ArrayList of ArrayLists of ArrayLists
            for (int i = 0; i < n; ++i) {
                adjMatrix.add(new ArrayList<>(Collections.nCopies(n, 0))); //ArrayList of Integers
                adjList.add(new ArrayList<>()); //ArrayList of Integers
            }

            //Fill the matrix with random values
            double connectRand;
            int weight;
            ArrayList<Integer> addList;
            for (int column = 0; column < n; ++column) {
                for (int row = column+1; row < n; ++row) { //Dont want elements on the diagonal
                    connectRand = edgeGen.nextDouble();
                    if (connectRand <= p) {

                        //Generate weights
                        weight = weightGen.nextInt(n) + 1;

                        //Add AdjMatrix elements
                        adjMatrix.get(row).set(column, weight);
                        adjMatrix.get(column).set(row, weight);

                        //Add AdjList element
                        addList = new ArrayList<>();
                        addList.add(row);
                        addList.add(weight);
                        adjList.get(column).add(addList);

                        //Add AdjList element
                        addList = new ArrayList<>();
                        addList.add(column);
                        addList.add(weight);
                        adjList.get(row).add(addList);
                    }
                }
            }
        } while (!DFS(0, n)); //While the graph is not connected, keep making more graphs
        long end_time = System.currentTimeMillis();
        System.out.println(String.format("Time to generate the graph: %d milliseconds",end_time-start_time));
    }

    /**
     * Prints out a readable version of this graph's adjacency matrix
     */
    public void printAdjacencyMatrix() {
        System.out.println("\nThe graph as an adjacency matrix:\n");
        for (ArrayList<Integer> row : adjMatrix) {
            System.out.print(" ");
            for (int rowItem : row) {
                System.out.print(Integer.toString(rowItem)+"   ");
            }
            System.out.print("\n\n");
        }
    }

    /**
     * Prints out a readable version of this graph's adjacency list
     */
    public void printAdjacencyList() {

        int nodeVal;
        int weightVal;

        System.out.println("The graph as an adjacency list:");
        for (int i = 0; i < adjList.size(); ++i) {
            System.out.print(String.format("%d-> ",i));
            for (int j = 0; j < adjList.get(i).size(); ++j) {
                nodeVal = adjList.get(i).get(j).get(0);
                weightVal = adjList.get(i).get(j).get(1);
                System.out.print(String.format("%d(%d) ",nodeVal,weightVal));
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    /**
     * Performs a depth first search on the graph (specifically on the adjList, as defined in the DFS_VISIT function),
     * and prints out the Vertices and Predecessors of those vertices
     * @param vertex the ID of the vertex to start with
     * @param n the total number of vertices
     * @return boolean representing whether or not the graph is connected (if # of nodes visited = n)
     */
    public boolean DFS(int vertex, int n) {
        visitedList = new ArrayList<>();
        predecessors = new HashMap<>();
        DFS_VISIT(vertex, -1);
        return (visitedList.size() == n);
    }

    /**
     * Recursive function to visit a node in the adjacency list
     * @param vertex the vertex to visit (will add this to the visited list)
     * @param parent the parent of the node (normally will be the node you're currently on,
     *               except -1 for the first call)
     */
    public void DFS_VISIT(int vertex, int parent) {
        visitedList.add(vertex);
        predecessors.put(vertex, parent);
        for (ArrayList<Integer> neighbor : adjList.get(vertex)) {
            if (!visitedList.contains(neighbor.get(0))) {
                DFS_VISIT(neighbor.get(0), vertex);
            }
        }
    }

    /**
     * Prints out the properly ordered information about the DFS that has been run on the graph
     */
    public void printDFSInformation() {
        System.out.println("Depth-First Search:");

        ArrayList<Integer> printVertices = new ArrayList<>();
        ArrayList<Integer> printPredecessors = new ArrayList<>();

        for (HashMap.Entry<Integer,Integer> entry : predecessors.entrySet()) {
            printVertices.add(entry.getKey());
            printPredecessors.add(entry.getValue());
        }

        System.out.println("Vertices:");
        System.out.print(" "); //Account for the fact that the predecessor below has a - in front (so they line up)
        for (int item : printVertices) {
            System.out.print(String.format("%d ",item));
        }
        System.out.print("\n");

        System.out.println("Predecessors:");
        for (int item : printPredecessors) {
            System.out.print(String.format("%d ",item));
        }
        System.out.println();
    }

    /**
     * Initializes the matrix edges (ArrayList for sorting) with the values from the graph's adjacency matrix
     */
    public ArrayList<Edge> createMatrixEdges(int n){
        int addWeight;
        Edge edgeToAdd;
        matrixEdges = new ArrayList<>();
        for (int column = 0; column < n; ++column) {
            for (int row = column+1; row < n; ++row) {
                addWeight = adjMatrix.get(column).get(row);
                if (addWeight != 0) {
                    edgeToAdd = new Edge(column,row,addWeight);
                    matrixEdges.add(edgeToAdd);
                }
            }
        }
        return matrixEdges;
    }

    /**
     * Initializes the list edges (ArrayList for sorting) with the values from the graph's adjacency matrix
     *
     * ================================================================================================
     * Right now its not very intelligent and will add (like the adjList does) edges in both directions
     * ================================================================================================
     */
    public ArrayList<Edge> createListEdges(int n) {
        int addWeight, addDestination;
        Edge edgeToAdd;
        boolean isDuplicate;
        listEdges = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            for (ArrayList<Integer> pair : adjList.get(i)) {
                isDuplicate = false;
                addDestination = pair.get(0);
                addWeight = pair.get(1);

                edgeToAdd = new Edge(i,addDestination,addWeight);
                for (Edge existingEdge : listEdges) { //Check for duplicates
                    if (edgeToAdd.equals(existingEdge)) {
                        isDuplicate = true;
                    }
                }

                if (!isDuplicate) {
                    listEdges.add(edgeToAdd);
                }
            }
        }
        return listEdges;
    }
}
