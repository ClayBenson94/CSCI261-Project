/**
 * Holds the necessary information to represent a vertex in a graph
 *
 * @author Clay Benson
 */
public class Vertex {

    private int index;
    private Vertex parent;

    public Vertex(int index, Vertex parent) {
        this.index = index;
        this.parent = parent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Vertex getParent() {
        return parent;
    }

    public void setParent(Vertex parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        if (this.getParent() == null) {
            return String.format("%d with no parent", this.getIndex());
        }
        return String.format("%d with parent %s", this.getIndex(), this.getParent());
    }
}
