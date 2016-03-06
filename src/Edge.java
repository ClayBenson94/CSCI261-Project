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
        return String.format("I am an edge from %d to %d!", this.getSourceVertex(), this.getDestinationVertex());
    }
}
