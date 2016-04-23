import java.util.ArrayList;

/**
 * Created by Clay on 4/23/2016.
 */
public class PriorityQueue {
    private ArrayList<pqItem> pq;
    private int numItems;

    public PriorityQueue(int numItems) {
        this.numItems = numItems;
        for (int i = 1; i < numItems; ++i) {
            pq.add(new pqItem(i));
        }
        heapify();
    }

    public void heapify() {
        for (int k = numItems/2; k >= 1; k--) {
            sink(k);
        }
    }

    public void sink(int k) {
        while (2*k <= numItems) {
            int j = 2*k;

            if (j < numItems && greater(j, j+1)) {
                j++;
            }

            if (!greater(k, j)) {
                break;
            }

            swap(k,j);
            k = j;
        }
    }

    public void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            swap(k, k/2);
            k = k/2;
        }
    }

    public boolean greater(int i, int j) {
        return pq.get(i).getKey() > pq.get(j).getKey();
    }

    public void swap(int i, int j) {
        pqItem item1 = new pqItem(pq.get(i));
        pqItem item2 = new pqItem(pq.get(j));
        pq.set(i,item2);
        pq.set(j,item1);
    }

}

class pqItem {
    private int key;
    private Vertex vertex;

    public pqItem(int vertex) {
        this.key = Integer.MAX_VALUE;
        Vertex negativeParent = new Vertex(-1,null,0);
        negativeParent.setParent(negativeParent);
        this.vertex = new Vertex(vertex,negativeParent,0);
    }

    public pqItem(pqItem clone) {
        this.key = clone.getKey();
        this.vertex = new Vertex(clone.getVertex());
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Vertex getVertex() {
        return this.vertex;
    }
}