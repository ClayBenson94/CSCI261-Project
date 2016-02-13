package graphObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Holds multiple representations of an undirected weighted graph.
 * Uses an Adjacency Matrix and an Adjacency Array
 * @author Clay
 */
public class Graph {

    private ArrayList<ArrayList<Integer>> adjMatrix;
    private ArrayList<ArrayList<ArrayList<Integer>>> adjList;
    private ArrayList<Integer> visitedList;
    private ArrayList<Integer> predecessors;
    private Random edgeGen;
    private Random weightGen;

    public Graph(int n, long seed, double p) {
        edgeGen = new Random();
        weightGen = new Random();
        edgeGen.setSeed(seed);
        weightGen.setSeed(seed*2);

        initAdjacencies(n, seed, p);
        System.out.println(DFS(4, n));
    }


    /**
     * Creates a randomly connected, undirected, weighted graph.
     * Is represented as both an Adjacency Matrix and an Adjacency List.
     * @param n the number of vertices in the graph
     * @param p the probability (0 to 1) that any given edge will be created between 2 nodes
     */
    public void initAdjacencies(int n, long seed, double p) {
        long start_time = System.currentTimeMillis();
        do {
            System.out.println("ATTEMPTING GRAPH GEN");
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
                for (int row = column; row < n; ++row) {
                    connectRand = (double) (edgeGen.nextInt(101)) / 100;
                    if (row != column) {
                        weight = weightGen.nextInt(n) + 1;
                        if (connectRand <= p) {

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
            }
        } while (!DFS(0, n));
        long end_time = System.currentTimeMillis();
        System.out.println(String.format("Time to generate the graph: %d milliseconds%n",end_time-start_time));
    }

    /**
     * Prints out a readable version of this graph's adjacency matrix
     */
    public void printAdjacencyMatrix() {
        System.out.println("The graph as an adjacency matrix:\n");
        for (ArrayList<Integer> row : adjMatrix) {
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

        System.out.println("The graph as an adjacency list:\n");
        for (int i = 0; i < adjList.size(); ++i) {
            System.out.print(String.format("%d-> ",i));
            for (int j = 0; j < adjList.get(i).size(); ++j) {
                nodeVal = adjList.get(i).get(j).get(0);
                weightVal = adjList.get(i).get(j).get(1);
                System.out.print(String.format("%d(%d) ",nodeVal,weightVal));
            }
            System.out.print("\n\n");
        }
    }

    /**
     * Performs a depth first search on the graph, and prints out the Vertices and Predecessors of those vertices
     * @param vertex the ID of the vertex to start with
     * @param n the total number of vertices
     * @return boolean representing whether or not the graph is connected (if # of nodes visited = n)
     */
    public boolean DFS(int vertex, int n) {
        visitedList = new ArrayList<>();
        predecessors = new ArrayList<>();
        DFS_VISIT(vertex, -1);
        return (visitedList.size() == n);
    }

    public void DFS_VISIT(int vertex, int parent) {
        visitedList.add(vertex);
        predecessors.add(parent);
//        System.out.println(String.format("Visiting node %d",vertex));
        for (ArrayList<Integer> neighbor : adjList.get(vertex)) {
            if (!visitedList.contains(neighbor.get(0))) {
                DFS_VISIT(neighbor.get(0), vertex);
            }
        }
    }

    public void printDFSInformation() {
        System.out.println("Depth First Search:");

        System.out.println("Vertices:");
        System.out.println(visitedList);

        System.out.println("Predecessors:");
        System.out.println(predecessors);
    }
}
