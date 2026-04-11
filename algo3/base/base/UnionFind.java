import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;

public class UnionFind {
    int[] height, parent, size;
    int count;
    int capacity;
    int nbCommunautes;

    public UnionFind(int n) {
        this.capacity = n;
        this.height = new int[capacity];
        this.parent = new int[capacity];
        this.size = new int[capacity];
        this.count = 0;
        this.nbCommunautes = 0;
    }

    public int find(int i) {
        int root = parent[i];
      
        if (parent[root] != root) {
            return parent[i] = find(root);
        }
      
        return root;
    }

    public void union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);

        if (rootI == rootJ) {
            return;
        }

        if (height[rootI] == height[rootJ]) {
            parent[rootJ] = rootI;
            height[rootI] = height[rootI] + 1;
            size[rootI] += size[rootJ];
        }

        else if (height[rootI] > height[rootJ]) {
            parent[rootJ] = rootI;
            size[rootI] += size[rootJ];
        } else {
            parent[rootI] = rootJ;
            size[rootJ] += size[rootI];
        }
        nbCommunautes--;
    }

    public int add() {

        if (count == capacity) {
            capacity *= 2;
            height = Arrays.copyOf(height, capacity);
            parent = Arrays.copyOf(parent, capacity);
            size = Arrays.copyOf(size, capacity);
        }
        
        int id = count;
        parent[id] = id;
        height[id] = 0;
        size[id] = 1;
        
        nbCommunautes++;
        count++;
        return id;
    }

    public int getSize(int i) {
        return size[find(i)];
    }
    
    public int getNombreDeCommunautes() {
        return nbCommunautes;
    }

    public List<Integer> getTop10Tailles() {
        PriorityQueue<Integer> top10Heap = new PriorityQueue<>();

        for (int i = 0; i < count; i++) {
            if (parent[i] == i) {
                
                top10Heap.offer(size[i]);
                
                if (top10Heap.size() > 10) {
                    top10Heap.poll(); 
                }
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!top10Heap.isEmpty()) {
            result.add(0, top10Heap.poll());
        }

        return result;
    }

}
