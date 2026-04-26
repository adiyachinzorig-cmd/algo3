package Tache1;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Map;


public class Communauté1 {
    //structure de données pour les communautés, 
    //on utilise une union-find pour gérer les communautés et une map pour associer les auteurs à des indices
    private UnionFind unionFind;
    private Map<String, Integer> userToIndex; 
    private int count;

    public  Communauté1(int n) {
        //création de la structure de données pour les communautés
        this.userToIndex = new HashMap<>();
        this.unionFind = new UnionFind(n);
    }

    //ajoute un auteur à la structure de données s'il n'existe pas déjà
    private void add(String author) {
        if (!userToIndex.containsKey(author)) {
            int index = unionFind.add();
            userToIndex.put(author, index);
        }
    }

    //effectue lunion de deux auteurs
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

    public Map<Integer, Integer> getHistogrammeTailles() {
        return this.unionFind.getHistogrammeTailles();
    }
}