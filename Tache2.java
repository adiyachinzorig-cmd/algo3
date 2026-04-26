import base.DblpPublicationGenerator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Tache2 {

    // Classe interne pour gérer la traduction Nom <-> ID plus rapide et moins gourmande en mémoire
    static class AuthorIndexer {
        private Map<String, Integer> nameToId = new HashMap<>();
        private List<String> idToName = new ArrayList<>();

        public int getOrCreateId(String name) {
            return nameToId.computeIfAbsent(name, k -> {
                idToName.add(k);
                return idToName.size() - 1;
            });
        }

        public String getName(int id) {
            return idToName.get(id);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java -Xmx2g Tache2 <xml_file> <dtd_file>");
            System.exit(1);
        }

        Path xmlPath = Paths.get(args[0]);
        Path dtdPath = Paths.get(args[1]);

        AuthorIndexer indexer = new AuthorIndexer();
        // Integer pour les IDs d'auteurs, Integer pour les compteurs de co-publications
        Map<Integer, Map<Integer, Integer>> counters = new HashMap<>();

        try (DblpPublicationGenerator gen = new DblpPublicationGenerator(xmlPath, dtdPath, 0)) {      
            while (true) {
                Optional<DblpPublicationGenerator.Publication> opt = gen.nextPublication();
                if (opt.isEmpty()) break;

                List<String> authors = opt.get().authors;
                if (authors != null && authors.size() >= 2) {
                    // Convertit l'auteur principal en ID
                    int idA = indexer.getOrCreateId(authors.get(0));
                    
                    for (int i = 1; i < authors.size(); i++) {
                        int idB = indexer.getOrCreateId(authors.get(i));
                        if (idA != idB) {
                            counters.computeIfAbsent(idA, k -> new HashMap<>())
                                    .merge(idB, 1, Integer::sum);
                        }
                    }
                }
            }
        }

        // Structure d'adjacence 
        Map<Integer, List<Integer>> adj = new HashMap<>();
        Set<Integer> nodes = new HashSet<>();

        for (var entry : counters.entrySet()) {
            int u = entry.getKey();
            for (var target : entry.getValue().entrySet()) {
                if (target.getValue() >= 6) {
                    adj.computeIfAbsent(u, k -> new ArrayList<>()).add(target.getKey());
                    nodes.add(u);
                    nodes.add(target.getKey());
                }
            }
        }
        counters = null; 

        List<List<Integer>> sccs = findSCCs(nodes, adj);
        sccs.sort((a, b) -> Integer.compare(b.size(), a.size()));

        System.out.println("=== TOP 10 COMMUNAUTÉS ===");
        for (int i = 0; i < Math.min(10, sccs.size()); i++) {
            List<Integer> community = sccs.get(i);
            int diameter = computeDiameter(community, adj);
            
            // Convertit les IDs en noms pour l'affichage
            List<String> memberNames = new ArrayList<>();
            for(int id : community) memberNames.add(indexer.getName(id));

            System.out.println("Rang: " + (i + 1));
            System.out.println("Taille: " + community.size());
            System.out.println("Diamètre: " + diameter);
            System.out.println("Membres: " + memberNames);
            System.out.println("---------------------------");
        }

        System.out.println("\n=== HISTOGRAMME DES TAILLES ===");
        printHistogram(sccs);
    }

// Utulise Integer pour les IDs d'auteurs, ce qui est plus rapide et moins gourmand en mémoire que les chaînes de caractères
    private static List<List<Integer>> findSCCs(Set<Integer> nodes, Map<Integer, List<Integer>> adj) {
        Stack<Integer> stack = new Stack<>();
        Set<Integer> visited = new HashSet<>();
        for (Integer node : nodes) {
            if (!visited.contains(node)) fillOrder(node, visited, stack, adj);
        }

        Map<Integer, List<Integer>> revAdj = new HashMap<>();
        for (var entry : adj.entrySet()) {
            for (Integer neighbor : entry.getValue()) {
                revAdj.computeIfAbsent(neighbor, k -> new ArrayList<>()).add(entry.getKey());
            }
        }

        List<List<Integer>> sccs = new ArrayList<>();
        visited.clear();
        while (!stack.isEmpty()) {
            Integer node = stack.pop();
            if (!visited.contains(node)) {
                List<Integer> component = new ArrayList<>();
                dfsRev(node, visited, component, revAdj);
                sccs.add(component);
            }
        }
        return sccs;
    }

    private static void fillOrder(Integer u, Set<Integer> visited, Stack<Integer> stack, Map<Integer, List<Integer>> adj) {
        visited.add(u);
        List<Integer> neighbors = adj.get(u);
        if (neighbors != null) {
            for (Integer v : neighbors) {
                if (!visited.contains(v)) fillOrder(v, visited, stack, adj);
            }
        }
        stack.push(u);
    }

    private static void dfsRev(Integer u, Set<Integer> visited, List<Integer> comp, Map<Integer, List<Integer>> revAdj) {
        visited.add(u);
        comp.add(u);
        List<Integer> neighbors = revAdj.get(u);
        if (neighbors != null) {
            for (Integer v : neighbors) {
                if (!visited.contains(v)) dfsRev(v, visited, comp, revAdj);
            }
        }
    }

    private static int computeDiameter(List<Integer> community, Map<Integer, List<Integer>> adj) {
        int maxDist = 0;
        Set<Integer> members = new HashSet<>(community);
        for (Integer start : community) {
            Map<Integer, Integer> dists = new HashMap<>();
            Queue<Integer> q = new LinkedList<>();
            q.add(start);
            dists.put(start, 0);
            while (!q.isEmpty()) {
                Integer u = q.poll();
                int d = dists.get(u);
                maxDist = Math.max(maxDist, d);
                List<Integer> neighbors = adj.get(u);
                if (neighbors != null) {
                    for (Integer v : neighbors) {
                        if (members.contains(v) && !dists.containsKey(v)) {
                            dists.put(v, d + 1);
                            q.add(v);
                        }
                    }
                }
            }
        }
        return maxDist;
    }

    private static void printHistogram(List<List<Integer>> sccs) {
        Map<Integer, Integer> distribution = new TreeMap<>();
        for (List<Integer> scc : sccs) {
            int size = scc.size();
            distribution.put(size, distribution.getOrDefault(size, 0) + 1);
        }
        if (distribution.isEmpty()) return;
        double maxLog = Math.log(distribution.values().stream().max(Integer::compare).get());
        for (var entry : distribution.entrySet()) {
            int count = entry.getValue();
            int barLength = (int) ((Math.log(count) / maxLog) * 40);
            System.out.printf("Taille %4d | %-7d | %s%n", entry.getKey(), count, "█".repeat(Math.max(1, barLength)));
        }
    }
}