import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

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
    private int numEdges;
    private int numVertices;
    private Random edgeGen;
    private Random weightGen;
    private ArrayList<Edge> matrixEdges;
    private ArrayList<Edge> listEdges;
    private long start_time;

    //Copies of the edge lists after they're insertion sorted
    private ArrayList<Edge> insertionSorted_listEdges;
    private ArrayList<Edge> insertionSorted_matrixEdges;

    //Copies of the edge lists after they're count sorted
    private ArrayList<Edge> countSorted_listEdges;
    private ArrayList<Edge> countSorted_matrixEdges;

    //Copies of the edge lists after they're quick sorted
    private ArrayList<Edge> quickSorted_listEdges;
    private ArrayList<Edge> quickSorted_matrixEdges;


    private boolean print_info;

    /**
     * Constructor for the Graph object. Instantiates the random number generators, and sets their seeds.
     * Additionally, upon creating a Graph object, it will initialize the adjacency list and matrix automatically
     * @param seed the seed of the edge random number generator to use. Will use 2*seed for the weight generator
     */
    public Graph(long seed) {
        edgeGen = new Random();
        weightGen = new Random();
        edgeGen.setSeed(seed);
        weightGen.setSeed(seed*2);
        numEdges = 0;
    }

    /**
     * Performs the main duties of the project. It will initialize the graph, print the representations, and run sorts
     * @param n the number of vertices in the graph
     * @param p the probability that any specific edge will be created
     */
    public void generate(int n, double p) {
        numVertices = n;
        print_info = (n < 10);
        initAdjacencies(n, p);

        if (print_info) {
            printAdjacencyMatrix();
            printAdjacencyList();
            printDFSInformation();
        }

        runMatrixAlgorithms();
        runListAlgorithms();
        runPrimAlgorithm();
    }

    /**
     * Uses count sourt to sort a list of edge objects
     * @param sortList the list to sort
     */
    public ArrayList<Edge> edgeCountSort(ArrayList<Edge> sortList) {
        int len = sortList.size();
        int r = edgeWeightMax(sortList)+1; //0 to 5, r = 6
        ArrayList<Edge> aux = new ArrayList<>();
        ArrayList<Integer> count = new ArrayList<>();
        for (int i = 0; i < r+1; i++) { //Add R+1 elements
            count.add(0);
        }
        for (int i = 0; i < len; i++) { //Make aux same size as sortList
            aux.add(new Edge()); //Blank edges (0,0,0)
        }

        for (int i = 0; i < len; i++) { //Fill count array
            count.set(sortList.get(i).getWeight()+1,count.get(sortList.get(i).getWeight()+1)+1); //count[[sortList]+1}++
        }


        for (int i = 0; i < r; i++) { //Cumulative sum
            count.set(i+1,count.get(i+1)+count.get(i));
        }


        for (int i = 0; i < len; i++) { //Find correct location in aux
            aux.set(count.get(sortList.get(i).getWeight()),sortList.get(i));
            count.set(sortList.get(i).getWeight(),count.get(sortList.get(i).getWeight())+1);
        }

        for (int i = 0; i < len; i++) { //Copy back to sortList
            sortList.set(i, aux.get(i));
        }

        return sortList;

    }

    /**
     * Quick sort function
     * @param sortList the list to sort
     * @param lo the low index of the section
     * @param hi the high index of the section
     */
    public ArrayList<Edge> edgeQuickSort(ArrayList<Edge> sortList, int lo, int hi) {
        if (lo < hi) {
            int j = partition(sortList, lo, hi);
            edgeQuickSort(sortList, lo, j-1);
            edgeQuickSort(sortList, j+1, hi);
        }
        return sortList;
    }

    /**
     * Function used by edgeQuickSort to partition the ArrayList of edges
     * @param partitionList the list to partition
     * @param lo the low index of the section
     * @param hi the high index of the section
     * @return the pivot index
     */
    private int partition(ArrayList<Edge> partitionList, int lo, int hi) {
        int i = lo;
        int j = hi + 1;

        while (true) {
            while (partitionList.get(++i).lessThan(partitionList.get(lo))) {
                if (i == hi) break;
            }
            while (partitionList.get(lo).lessThan(partitionList.get(--j))) {
                if (j == lo) break;
            }

            if (i >= j) break;
            swapEdges(partitionList, i, j);
        }


        swapEdges(partitionList, lo, j);
        return j;
    }

    public void printHeader(String algorithm, String listType, String sortType) {
        System.out.println("===================================");
        System.out.println(String.format("%s WITH %s USING %s",algorithm,listType,sortType));
    }

    public void printPrimHeader(String algorithm, String listType) {
        System.out.println("===================================");
        System.out.println(String.format("%s WITH %s",algorithm,listType));
    }

    /**
     * Performs an insertion sort on the given list and prints out the necessary information
     * @param sortList the list to do the sort on
     */
    public ArrayList<Edge> edgeInsertionSort(ArrayList<Edge> sortList) {
        int i, j;
        int len = sortList.size();

        /**
         * Implementation of Insertion sort for Roxanne Canosa's in class slides. See commented function below for
         * more details.
         *
         * With about 300 items, this will run in about 3 seconds (on my computer)
         */
//        for (i = 1; i < len; ++i) {
//            j = i;
//            while ((j > 0) && (sortList.get(j-1).greaterThan(sortList.get(j)))) {
//                swapEdges(sortList, j, j-1);
//                j--;
//            }
//        }

        /**
         * The below implementation of Insertion sort tends to be much faster than the above one.
         * The above function is from Roxanne Canosa's slides, and I left it commented in here in case the grade of this
         * project is at all based around which implementation of Insertion sort we chose. If need be, the above one,
         * while it is slower, can be uncommented and used, as it is the exact one listed in the lecture slides.
         *
         * With about 300 items, this will run in about 1 second (on my computer)
         */
        for (i = 1; i < len; ++i) {
            Edge curEdge = new Edge(sortList.get(i));
            j = i - 1;
            while ((j >= 0) && (sortList.get(j).greaterThan(curEdge))) {
                sortList.set(j+1, sortList.get(j) ); //sortList[j+1] = sortList[j], move element up 1
                j--;
            }
            sortList.set(j+1, curEdge);
        }

        return sortList;
    }

    /**
     * Swaps 2 edge objects by their indeces
     * @param swapList the list to perform the swap on
     * @param pos1 index 1 of an element to swap
     * @param pos2 index 2 of the other element to swap
     */
    private void swapEdges(ArrayList<Edge> swapList, int pos1, int pos2) {
        Edge item1 = new Edge(swapList.get(pos1));
        Edge item2 = new Edge(swapList.get(pos2));
        swapList.set(pos1,item2);
        swapList.set(pos2,item1);
    }

    /**
     * Sums the weights of all the Edge objects in an ArrayList of Edge objects
     * @param sumList the list of Edge objects whose weights to sum
     */
    private void printEdgeWeightSum(ArrayList<Edge> sumList, String method) {
        int sum = 0;
        for (Edge eachEdge : sumList) {
            sum = sum + eachEdge.getWeight();
        }
        System.out.println(String.format("\nTotal weight of MST using %s: %d",method,sum));
    }

    /**
     * Returns the largest weight in a list of Edge objects
     * @param maxList the list to check the max weight of
     * @return the max weight
     */
    private int edgeWeightMax(ArrayList<Edge> maxList) {
        int max = 0;
        for (Edge eachEdge : maxList) {
            if (eachEdge.getWeight() > max) {
                max = eachEdge.getWeight();
            }
        }
        return max;
    }

    /**
     * Prints a proper listing of edges, given an ArrayList of Edge objects
     * @param printList the list to print
     */
    private void printEdgeList(ArrayList<Edge> printList) {
        if (print_info) {
            for (Edge eachEdge : printList) {
                System.out.println(String.format("%d %d weight = %d", eachEdge.getSourceVertex(), eachEdge.getDestinationVertex(), eachEdge.getWeight()));
            }
        }
    }

    /**
     * Creates a randomly connected, undirected, weighted graph.
     * Is represented as both an Adjacency Matrix and an Adjacency List.
     * @param n the number of vertices in the graph
     * @param p the probability (0 to 1) that any given edge will be created between 2 nodes
     */
    private void initAdjacencies(int n, double p) {
        startTimer(); //Start up here, or below?
        do {
            startTimer();

            //Initialize the matrix to all zeroes to begin
            adjMatrix = new ArrayList<>(); //ArrayList of ArrayLists
            adjList = new ArrayList<>(); //ArrayList of ArrayLists of ArrayLists
            numEdges = 0;
            for (int i = 0; i < n; ++i) {
                adjMatrix.add(new ArrayList<>(Collections.nCopies(n, 0))); //ArrayList of Integers
                adjList.add(new ArrayList<>()); //ArrayList of Integers
            }

            //Fill the matrix with random values
            double connectRand;
            int weight;
            ArrayList<Integer> addList;
            for (int column = 0; column < n; ++column) {
                for (int row = column+1; row < n; ++row) { //Don't want elements on the diagonal
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

                        //Count edges
                        numEdges++;
                    }
                }
            }
        } while (!DFS(0, n)); //While the graph is not connected, keep making more graphs
        System.out.println(String.format("Time to generate the graph: %d milliseconds",System.currentTimeMillis()-start_time));
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
    public ArrayList<Edge> createMatrixEdges(){
        int addWeight;
        Edge edgeToAdd;
        int boardSize = adjMatrix.size();
        matrixEdges = new ArrayList<>();
        for (int column = 0; column < boardSize; ++column) {
            for (int row = column+1; row < boardSize; ++row) {
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
     */
    public ArrayList<Edge> createListEdges() {
        int addWeight, addDestination;
        Edge edgeToAdd;
        int boardSize = adjList.size();
        listEdges = new ArrayList<>();
        for (int i = 0; i < boardSize; ++i) {
            for (ArrayList<Integer> pair : adjList.get(i)) {
                addDestination = pair.get(0);
                addWeight = pair.get(1);

                if (i < addDestination) { //If we haven't done this edge yet (they go in increasing order)
                    edgeToAdd = new Edge(i,addDestination,addWeight);
                    listEdges.add(edgeToAdd);
                }
            }
        }
        return listEdges;
    }

    /**
     * Records the current time to the private start_time variable
     */
    private void startTimer() {
        start_time = System.currentTimeMillis();
    }

    /**
     * Uses the start_time private variable to print out how long it has been since the timer was started
     */
    private void stopTimer() {
        System.out.println(String.format("Runtime: %d milliseconds\n",System.currentTimeMillis()-start_time));
    }

    private void union(Vertex u, Vertex v) {
        Vertex i,j;
        i = find(u);
        j = find(v);
        if (i.getRank() > j.getRank()) {
            j.setParent(i);
        } else {
            i.setParent(j);
            if (i.getRank() == j.getRank()) {
                j.setRank(j.getRank()+1);
            }
        }
    }

    private ArrayList<Edge> kruskal(ArrayList<Edge> kruskList) {
        ArrayList<Vertex> disjointVertices = new ArrayList<>();
        ArrayList<Edge> MST = new ArrayList<>();
        Vertex vertexToAdd;
        for (int i = 0; i < numVertices; ++i) {
            vertexToAdd = new Vertex(i,null,0);
            vertexToAdd.setParent(vertexToAdd); //Make it its own parent
            disjointVertices.add(vertexToAdd);
        }

        Vertex root1,root2;
        int includedCount = 0;
        for (Edge curEdge : kruskList) {
            root1 = find(disjointVertices.get(curEdge.getSourceVertex()));
            root2 = find(disjointVertices.get(curEdge.getDestinationVertex()));
            if (root1.getIndex() != root2.getIndex()) {
                MST.add(new Edge(curEdge)); //Add copy of edge to MST
                includedCount++;
                union(root1,root2);
                if (includedCount >= numVertices-1) {
                    return MST;
                }
            }
        }
        return new ArrayList<>();
    }

    private ArrayList<Edge> prim(ArrayList<Edge> primList) {
        //TODO Implement Prim's Algorithm
        ArrayList<Edge> MST = new ArrayList<>();
        ArrayList<Vertex> primVertices = new ArrayList<>();

        Vertex zeroVertex = new Vertex(0, null, 0);
        zeroVertex.setParent(zeroVertex);

        primVertices.add(zeroVertex);

        PriorityQueue primPQ = new PriorityQueue(numVertices-1); //Don't include 0
        HashMap<Integer, ArrayList<Integer>> neighbors = new HashMap<>();
        for (int i = 0; i < numVertices; ++i) {
            neighbors.put(i, new ArrayList<>());
        }
        for (Edge curEdge : primList) {
            neighbors.get(curEdge.getSourceVertex()).add(curEdge.getDestinationVertex());
            neighbors.get(curEdge.getDestinationVertex()).add(curEdge.getSourceVertex());
        }


        int num = numVertices-1;
        while (num > 0) {
            //Step 1
            //For each item in PQ
                //For each neighbor of that vertex
                    //Find edge item-neighbor OR neighbor-item
                    //If that edge < item.getKey(), update it and the parent (based off edge)
            for (pqItem item : primPQ.getPq()) {
                if (item != null) {
                    Vertex lastVertex = primVertices.get(primVertices.size() - 1);
                    for (int curNeighbor : neighbors.get(lastVertex.getIndex())) { //For neighbors of most recent
                        int weight = adjMatrix.get(curNeighbor).get(lastVertex.getIndex());
                        if (weight != 0) {
                            if (weight < item.getKey()) {
                                if (item.getVertex() == curNeighbor) {
                                    item.setParent(lastVertex.getIndex());
                                    item.setKey(weight);
                                }
                            }
                        }
                    }
                }
            }

            //Step 2
            primPQ.heapify();


//            num--;
            num = 0;
        }

//        PriorityQueue myPQ = new PriorityQueue(6);
//
//        pqItem testItem;
//
//        testItem = myPQ.getPq().get(1);
//        testItem.setKey(6);
//        myPQ.getPq().set(1, testItem);
//        testItem = myPQ.getPq().get(2);
//        testItem.setKey(3);
//        myPQ.getPq().set(2, testItem);
//        testItem = myPQ.getPq().get(3);
//        testItem.setKey(1);
//        myPQ.getPq().set(3, testItem);
//        testItem = myPQ.getPq().get(4);
//        testItem.setKey(5);
//        myPQ.getPq().set(4, testItem);
//        testItem = myPQ.getPq().get(5);
//        testItem.setKey(2);
//        myPQ.getPq().set(5, testItem);
//        testItem = myPQ.getPq().get(6);
//        testItem.setKey(4);
//        myPQ.getPq().set(6, testItem);
//
        System.out.println("Hi");
        primPQ.heapify();
        System.out.println("Hi");

        return MST;
    }

    private Vertex find(Vertex findVertex) {
        if (findVertex.getParent().getIndex() == findVertex.getIndex()) {
            return findVertex;
        }
        return find(findVertex.getParent());
    }

    /**
     * Run the 3 algorithms (in this case Kruskal's) on an edgeList generated by the adjacency matrix
     */
    private void runMatrixAlgorithms() {
//        String algorithm = "SORTED EDGES";
        String algorithm = "KRUSKAL";
        String source = "MATRIX";

        //Insertion sort with MATRIX
        startTimer();
        insertionSorted_matrixEdges = edgeInsertionSort(createMatrixEdges());
        insertionSorted_matrixEdges = kruskal(insertionSorted_matrixEdges);
        printHeader(algorithm, source, "INSERTION SORT");
        printEdgeList(insertionSorted_matrixEdges);
        printEdgeWeightSum(insertionSorted_matrixEdges,"Kruskal");
        stopTimer();

        //Count sort with MATRIX
        startTimer();
        countSorted_matrixEdges = edgeCountSort(createMatrixEdges());
        countSorted_matrixEdges = kruskal(countSorted_matrixEdges);
        printHeader(algorithm, source, "COUNT SORT");
        printEdgeList(countSorted_matrixEdges);
        printEdgeWeightSum(countSorted_matrixEdges,"Kruskal");
        stopTimer();

        //Quicksort with MATRIX
        startTimer();
        quickSorted_matrixEdges = edgeQuickSort(createMatrixEdges(),0,numEdges-1);
        quickSorted_matrixEdges = kruskal(quickSorted_matrixEdges);
        printHeader(algorithm, source, "QUICKSORT");
        printEdgeList(quickSorted_matrixEdges);
        printEdgeWeightSum(quickSorted_matrixEdges,"Kruskal");
        stopTimer();
    }

    /**
     * Run the 3 algorithms (in this case Kruskal's) on an edgeList generated by the adjacency list
     */
    private void runListAlgorithms() {
//        String algorithm = "SORTED EDGES";
        String algorithm = "KRUSKAL";
        String source = "LIST";

        //Insertion sort with LIST
        startTimer();
        insertionSorted_listEdges = edgeInsertionSort(createListEdges());
        insertionSorted_listEdges = kruskal(insertionSorted_listEdges);
        printHeader(algorithm, source, "INSERTION SORT");
        printEdgeList(insertionSorted_listEdges);
        printEdgeWeightSum(insertionSorted_listEdges,"Kruskal");
        stopTimer();

        //Count sort with LIST
        startTimer();
        countSorted_listEdges = edgeCountSort(createListEdges());
        countSorted_listEdges = kruskal(countSorted_listEdges);
        printHeader(algorithm, source, "COUNT SORT");
        printEdgeList(countSorted_listEdges);
        printEdgeWeightSum(countSorted_listEdges,"Kruskal");
        stopTimer();

        //Quicksort with LIST
        startTimer();
        quickSorted_listEdges = edgeQuickSort(createListEdges(),0,numEdges-1);
        quickSorted_listEdges = kruskal(quickSorted_listEdges);
        printHeader(algorithm, source, "QUICKSORT");
        printEdgeList(quickSorted_listEdges);
        printEdgeWeightSum(quickSorted_listEdges,"Kruskal");
        stopTimer();
    }

    private void runPrimAlgorithm() {
        //TODO All the stuff I put off until now
        String algorithm = "PRIM";
        startTimer();
        printPrimHeader(algorithm, "ADJACENCY MATRIX");
        prim(createMatrixEdges());
        stopTimer();

        startTimer();
        printPrimHeader(algorithm, "ADJACENCY LIST");
        prim(createListEdges());
        stopTimer();

    }

}
