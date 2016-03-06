/**
 * Holds the necessary information to represent an edge in a graph
 *
 * @author Clay Benson
 */
public class Edge {

    private Vertex sourceVertex, destinationVertex;

    public Edge(Vertex sourceVertex, Vertex destinationVertex) {
        this.sourceVertex = sourceVertex;
        this.destinationVertex = destinationVertex;
    }

    public Vertex getSourceVertex() {
        return sourceVertex;
    }

    public void setSourceVertex(Vertex sourceVertex) {
        this.sourceVertex = sourceVertex;
    }

    public Vertex getDestinationVertex() {
        return destinationVertex;
    }

    public void setDestinationVertex(Vertex destinationVertex) {
        this.destinationVertex = destinationVertex;
    }

    @Override
    public String toString() {
        return String.format("I am an edge from %d to %d!", getSourceVertex().getIndex(), getDestinationVertex().getIndex());
    }
}
