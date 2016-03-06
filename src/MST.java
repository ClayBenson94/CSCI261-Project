import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Main class
 *
 * @author Clay Benson
 */
public class MST {


    /**
     * Handles input processing, and holds the instance of the generated graph
     * Additionally runs the public methods to print Matrix, List, and DFS information
     * @param args space separated command line arguments
     */
    public static void main(String[] args) {

        String fileName = "";
        if (args.length > 0) {
            fileName = args[0];
        }
        String line = null;

        FileReader reader = null;
        try {
            reader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found");
            System.exit(0);
        }
        BufferedReader bufferedReader = new BufferedReader(reader);

        int n = 0;
        long seed = 0;
        try {
            n = Integer.parseInt(bufferedReader.readLine());
            seed = Long.parseLong(bufferedReader.readLine());
        } catch (IOException|NumberFormatException e) {
            System.out.println("n and seed must be integers");
            System.exit(0);
        }

        double p = 0;
        try {
            p = Double.parseDouble(bufferedReader.readLine());
        } catch (IOException|NumberFormatException e) {
            System.out.println("p must be a real number");
            System.exit(0);
        }

        valueErrorCheck(n, p);
        System.out.print("\n");
        System.out.println(String.format("TEST: n=%d, seed=%d, p=%s", n, seed, Double.toString(p)));

        Graph myGraph = new Graph(n, seed, p);
        if (n < 10) {
            myGraph.printAdjacencyMatrix();
            myGraph.printAdjacencyList();
            myGraph.printDFSInformation();
        }
        System.out.println();
    }

    /**
     * Exits the program if the conditions for n and p are not met
     * @param n the number of vertices (must be greater than 1)
     * @param p the probability (0 to 1) that any given edge will be created between 2 nodes (must be between 0 and 1)
     */
    private static void valueErrorCheck(int n, double p) {
        if (n < 2) {
            System.out.println("n must be greater than 1");
            System.exit(0);
        }
        if (p > 1.0 || p < 0.0) {
            System.out.println("p must be between 0 and 1");
            System.exit(0);
        }
    }
}
