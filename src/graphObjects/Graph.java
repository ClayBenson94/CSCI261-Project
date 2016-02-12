package graphObjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Clay on 2/11/2016.
 */
public class Graph {

    private ArrayList<ArrayList<Integer>> adjMatrix;
    private ArrayList<ArrayList<Integer>> adjList;

    public Graph(int n, double p) {
        System.out.println(String.format("Making graphs of size %d!\n",n));

        adjMatrix = new ArrayList<ArrayList<Integer>>();
        adjList = new ArrayList<ArrayList<Integer>>();

        initAdjacencies(n, p);
    }


    /**
     * Creates a randomly connected, undirected, weighted graph.
     * Is represented as both an Adjacency Matrix and an Adjacency List.
     * @param n the number of vertices in the graph
     * @param p the probability (0 to 1) that any given edge will be created between 2 nodes
     */
    public void initAdjacencies(int n, double p) {

        //Intro message
        System.out.println("=====Making Adjacency Matrix=====");

        //Initialize the matrix to all zeroes to begin
        for (int i = 0; i < n; ++i) {
            adjMatrix.add(new ArrayList<Integer>(Collections.nCopies(n, 0))); //Add a blank row (zeroes)
        }

        //Fill the matrix with random values
        Random randGen = new Random();
        double connectRand;
        for (int column = 0; column < n; ++column) {
            for (int row = column; row < n; ++row) {
                connectRand = (double) (randGen.nextInt(101)) / 100;
                if (connectRand <= p) {
                    int randomNum = randGen.nextInt(n) + 1;
                    adjMatrix.get(row).set(column, randomNum);
                    adjMatrix.get(column).set(row, randomNum);
                }
            }
        }

        //Print the matrix
        for (int i = 0; i < n; ++i) {
            System.out.println(adjMatrix.get(i));
        }

        //Outtro message
        System.out.println("====Finished Adjacency Matrix====");

    }
}
