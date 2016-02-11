package graphObjects;

/**
 * Created by Clay on 2/9/2016.
 */
public class Vertex {

    private int index;

    public Vertex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return String.format("I am a Vertex with ID %d!", getIndex());
    }
}
