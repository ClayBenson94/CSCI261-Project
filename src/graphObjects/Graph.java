package graphObjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Holds multiple representations of an undirected weighted graph.
 * Uses an Adjacency Matrix and an Adjacency Array
 * @author Clay
 */
public class Graph {

    private ArrayList<ArrayList<Integer>> adjMatrix;
    private ArrayList<ArrayList<Integer>> adjList;

    public Graph(int n, long seed, double p) {
        adjMatrix = new ArrayList<>(); //ArrayList of ArrayLists
        adjList = new ArrayList<>(); //ArrayList of ArrayLists

        initAdjacencies(n, seed, p);
    }


    /**
     * Creates a randomly connected, undirected, weighted graph.
     * Is represented as both an Adjacency Matrix and an Adjacency List.
     * @param n the number of vertices in the graph
     * @param p the probability (0 to 1) that any given edge will be created between 2 nodes
     */
    public void initAdjacencies(int n, long seed, double p) {

        long start_time = System.currentTimeMillis();

        //Initialize the matrix to all zeroes to begin
        for (int i = 0; i < n; ++i) {
            adjMatrix.add(new ArrayList<>(Collections.nCopies(n, 0))); //ArrayList of Integers
            adjList.add(new ArrayList<>()); //ArrayList of Integers
        }

        //Fill the matrix with random values
        Random edgeGen = new Random();
        Random weightGen = new Random();
        edgeGen.setSeed(seed);
        weightGen.setSeed(seed*2);
        double connectRand;
        int weight;
        for (int column = 0; column < n; ++column) {
            for (int row = column; row < n; ++row) {
                weight = weightGen.nextInt(n) + 1;
                connectRand = (double) (edgeGen.nextInt(101)) / 100;
                if (row == column) {
                    weight = 0;
                }
                if (connectRand <= p) {
                    adjMatrix.get(row).set(column, weight);
                    adjMatrix.get(column).set(row, weight);

                    adjList.get(column).add(row);
                    adjList.get(column).add(weight);

                    adjList.get(row).add(column);
                    adjList.get(row).add(weight);
                }
            }
        }

        //Generate AdjacencyList from the AdjacencyMatrix

        long end_time = System.currentTimeMillis();

        System.out.println(String.format("Time to generate the graph: %d milliseconds%n",end_time-start_time));
    }

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
     * REALLY REALLY CRAPPY PLEASE DONT LOOK AT THIS
     */
    public void printAdjacencyList() {
        System.out.println("The graph as an adjacency list:\n");
        for (ArrayList<Integer> row : adjList) {
            System.out.println(String.format("%d-> ",0));
            for (int rowItem : row) {
                System.out.print(Integer.toString(rowItem)+"   ");
            }
            System.out.print("\n\n");
        }
    }
}
