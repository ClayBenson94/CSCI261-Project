import graphObjects.Edge;
import graphObjects.Vertex;
import java.util.ArrayList;

public class MST {

    public static void main(String[] args) {
        //Crappy OO Version of representing a graph?
//        Vertex myVertex = new Vertex(10);
//        Vertex myOtherVertex = new Vertex(12);
//        Edge myCrapEdge = new Edge(myVertex, myOtherVertex);
//        System.out.println(myVertex);
//        System.out.println(myCrapEdge);

        //Adjacency List representation
        ArrayList<ArrayList<Integer>> adjList = new ArrayList<ArrayList<Integer>>();

        //Add node 0
        ArrayList<Integer> adjListRow = new ArrayList<Integer>();
        adjListRow.add(1);
        adjListRow.add(3);
        adjList.add(adjListRow);

        //Add node 1
        adjListRow = new ArrayList<Integer>();
        adjListRow.add(0);
        adjList.add(adjListRow);

        //Add node 2
        adjListRow = new ArrayList<Integer>();
        //adjListRow.add(0);
        adjList.add(adjListRow);

        //Add node 3
        adjListRow = new ArrayList<Integer>();
        adjListRow.add(0);
        adjList.add(adjListRow);

        for (int i = 0; i < adjList.size(); ++i) {
            System.out.print(String.format("The node %d has adjacency: ",i));
            for (int j = 0; j < adjList.get(i).size(); ++j) {
                System.out.print(adjList.get(i).get(j));
                System.out.print(" ");
            }
            System.out.println("");
        }

    }
}
