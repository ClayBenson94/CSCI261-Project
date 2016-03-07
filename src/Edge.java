/**
 * Holds the necessary information to represent an edge in a graph
 *
 * @author Clay Benson
 */
public class Edge {

    private int sourceVertex, destinationVertex;
    private int weight;

    public Edge(int sourceVertex, int destinationVertex, int weight) {
        this.sourceVertex = sourceVertex;
        this.destinationVertex = destinationVertex;
        this.weight = weight;
    }

    /**
     * The messiest function you'll ever see. This determines if 2 edges are equal (a-->b with weight 5 is equal to b-->a with weight 5)
     * @param compareEdge The edge object to compare to
     * @return whether or not the 2 objects are equal
     */
    public boolean equals(Edge compareEdge) {
        if (this.getWeight() == compareEdge.getWeight()) {
            int mySource = this.getSourceVertex();
            int compSource = compareEdge.getSourceVertex();
            int myDestination = this.getDestinationVertex();
            int compDestination = compareEdge.getDestinationVertex();
            if (( (mySource == compSource)&&(myDestination == compDestination) ) || ( (mySource == compDestination)&&(myDestination == compSource) )) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean lessThan(Edge compareEdge) {
        if (this.getWeight() != compareEdge.getWeight()) {
            return this.getWeight() < compareEdge.getWeight();
        } else { //Weights are equal
            if (this.getSourceVertex() != compareEdge.getSourceVertex()) {
                return this.getSourceVertex() < compareEdge.getSourceVertex();
            } else { //Weights are equal, source vertices are equal
                return this.getDestinationVertex() < compareEdge.getDestinationVertex();
            }
        }
    }

    public int getSourceVertex() {
        return sourceVertex;
    }

    public void setSourceVertex(int sourceVertex) {
        this.sourceVertex = sourceVertex;
    }

    public int getDestinationVertex() {
        return destinationVertex;
    }

    public void setDestinationVertex(int destinationVertex) {
        this.destinationVertex = destinationVertex;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return String.format("%d-%d(%d)",
                this.getSourceVertex(),
                this.getDestinationVertex(),
                this.getWeight()
        );
    }
}
