
package api;

import java.util.ArrayList;
public class MinHeap {

    private ArrayList<node_data> list;

    public ArrayList<node_data> getList() {
		return list;
	}

	public MinHeap() {

        this.list = new ArrayList<node_data>();
    }

    public MinHeap(ArrayList<node_data> items) {
        this.list = items;
        buildHeap();
    }

    public void insert(node_data item) {

        list.add(item);
        int i = list.size() - 1;
        int parent = parent(i);

        while (parent != i &&  ((NodeData)list.get(i)).getWeight() < ((NodeData)list.get(parent)).getWeight()) {
            swap(i, parent);
            i = parent;
            parent = parent(i);
        }
    }

    public void buildHeap() {

        for (int i = list.size() / 2; i >= 0; i--) {
            minHeapify(i);
        }
    }

    public node_data extractMin() {

        if (list.size() == 0) {
            throw new IllegalStateException("MinHeap is EMPTY");
        } else if (list.size() == 1) {

        	node_data min = list.remove(0);
            return min;
        }

        // remove the last item ,and set it as new root
        node_data min = list.get(0);
        node_data lastItem = list.remove(list.size() - 1);
        list.set(0, lastItem);

        // bubble-down until heap property is maintained
        minHeapify(0);

        // return min key
        return min;
    }

    private void minHeapify(int i) {

        int left = left(i);
        int right = right(i);
        int smallest = -1;

        // find the smallest key between current node and its children.
        if (left <= list.size() - 1 && ((NodeData)list.get(left)).getWeight() < ((NodeData)list.get(i)).getWeight()) {
            smallest = left;
        } else {
            smallest = i;
        }

        if (right <= list.size() - 1 && ((NodeData)list.get(right)).getWeight() < ((NodeData)list.get(smallest)).getWeight()) {
            smallest = right;
        }

        // if the smallest key is not the current key then bubble-down it.
        if (smallest != i) {

            swap(i, smallest);
            minHeapify(smallest);
        }
    }

    public node_data getMin() {

        return list.get(0);
    }

    public boolean isEmpty() {

        return list.size() == 0;
    }

    private int right(int i) {

        return 2 * i + 2;
    }

    private int left(int i) {

        return 2 * i + 1;
    }

    private int parent(int i) {

        if (i % 2 == 1) {
            return i / 2;
        }

        return (i - 1) / 2;
    }

    private void swap(int i, int parent) {
    	node_data temp = list.get(parent);
        list.set(parent, list.get(i));
        list.set(i, temp);
    }

	

}