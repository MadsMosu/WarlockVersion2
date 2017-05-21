package data.util;

import java.util.ArrayList;

public class PQHeap {

    private int entityValues;
    private ArrayList<EntityValue> queue;

    public PQHeap(int entityValues) {
        this.entityValues = entityValues;
        queue = new ArrayList<>(entityValues);
    }

    public int getParent(int i) {
        if (i % 2 == 1) {
            return i / 2;
        } else {
            return (i - 1) / 2;
        }
    }

    public int getLeft(int i) {
        return i * 2 + 1;
    }

    public int getRight(int i) {
        return i * 2 + 2;
    }

    public EntityValue extractMin() {
        int i = queue.size() - 1;
        EntityValue min = queue.get(0);
        queue.set(0, queue.get(i));

        queue.remove(i);
        minHeapify(0);
        return min;

    }

    public void insert(EntityValue e) {
        queue.add(e);
        int i = queue.size() - 1;
        while (i > 0 && queue.get(getParent(i)).value > queue.get(i).value) {
            java.util.Collections.swap(queue, i, getParent(i));
            i = getParent(i);
        }

    }

    public void minHeapify(int i) {
        int l = getLeft(i);
        int r = getRight(i);
        int size = queue.size() - 1;
        int smallest;
        if (l <= size && queue.get(l).value < queue.get(i).value) {
            smallest = l;
        } else {
            smallest = i;
        }
        if (r <= size && queue.get(r).value < queue.get(smallest).value) {
            smallest = r;
        }
        if (smallest != i) {
            java.util.Collections.swap(queue, i, smallest);
            minHeapify(smallest);
        }
    }

    public ArrayList<EntityValue> getQueue() {
        return queue;
    }
    
    

}
