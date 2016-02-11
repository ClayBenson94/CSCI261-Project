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

    public Graph(int n) {
        System.out.println(String.format("Making graphs of size %d!\n",n));

        adjMatrix = new ArrayList<ArrayList<Integer>>();

        makeAdjMatrix(n);
    }

    public ArrayList<ArrayList<Integer>> makeAdjMatrix(int n) {

        //Initialize the matrix to all zeroes to begin
        for (int i = 0; i < n; ++i) {
            adjMatrix.add(new ArrayList<Integer>(Collections.nCopies(n, 0))); //Add a blank row (zeroes)
        }

        //Fill the matrix with random values
        Random randGen = new Random();
        double p = .5; //Hard coded right now, will be taken from input later
        double connectRand;
        for (int column = 0; column < n; ++column) {
            for (int row = column; row < n; ++row) {
                connectRand = (double)(randGen.nextInt(101))/100;
                if (connectRand < p) {
                    int randomNum = randGen.nextInt(n) + 1 ;
                    adjMatrix.get(row).set(column, randomNum);
                    adjMatrix.get(column).set(row, randomNum);
                }
            }
        }

        //Print the matrix
        for (int i = 0; i < n; ++i) {
            System.out.println(adjMatrix.get(i));
        }

        return adjMatrix;
    }
}
