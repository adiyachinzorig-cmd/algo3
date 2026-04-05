import java.util.Arrays;

public class UnionFind {
    int[] height, parent, size;
    int count;
    int capacity;

    public UnionFind(int n) {
        this.capacity = n;
        this.height = new int[capacity];
        this.parent = new int[capacity];
        this.size = new int[capacity];
        this.count = 0;
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

        if (height[rootI] > height[rootJ]) {
            parent[rootJ] = rootI;
            size[rootI] += size[rootJ];
        } else {
            parent[rootI] = rootJ;
            size[rootJ] += size[rootI];
        }
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
        
        count++;
        return id;
    }

    public int getSize(int i) {
        return size[find(i)];
    }
    
}
