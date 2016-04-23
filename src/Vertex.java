/**
 * Created by Clay on 3/31/2016.
 */
public class Vertex {

    private int index;
    private Vertex parent;
    private int rank;

    public Vertex(int index, Vertex parent, int rank) {
        this.index = index;
        this.parent = parent;
        this.rank = rank;
    }

    public Vertex(Vertex clone) {
        this.index = clone.getIndex();
        this.parent = new Vertex(clone.getParent());
        this.rank = clone.getRank();
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return String.format("%d(%d)",
                this.getIndex(),
                this.getParent().getIndex()
        );
    }

}
