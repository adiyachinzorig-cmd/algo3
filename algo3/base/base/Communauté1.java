import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class Communauté1 {
    private UnionFind unionFind;
    private Map<String, Integer> userToIndex; 
    private int count;

    public  Communauté1(int n) {
        this.userToIndex = new HashMap<>();
        this.unionFind = new UnionFind(n);
    }

    private void add(String author) {
        if (!userToIndex.containsKey(author)) {
            int index = unionFind.add();
            userToIndex.put(author, index);
        }
    }

    public void union(String author1, String author2) {
        add(author1);
        add(author2);
        int index1 = userToIndex.get(author1);
        int index2 = userToIndex.get(author2);
        unionFind.union(index1, index2);
    }
    
    public int getNbCommunautes() {
        return unionFind.getNombreDeCommunautes();
    }

    public List<Integer> getTop10Tailles() {
        return unionFind.getTop10Tailles();
    }
}