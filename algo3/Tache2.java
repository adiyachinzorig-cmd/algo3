import base.DblpPublicationGenerator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Tache2 {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java -Xmx2g Tache2 <xml_file> <dtd_file>");
            System.exit(1);
        }

        Path xmlPath = Paths.get(args[0]);
        Path dtdPath = Paths.get(args[1]);

        Map<String, Map<String, Integer>> counters = new HashMap<>();

        try (DblpPublicationGenerator gen = new DblpPublicationGenerator(xmlPath, dtdPath, 0)) {      
            while (true) {
                Optional<DblpPublicationGenerator.Publication> opt = gen.nextPublication();
                if (opt.isEmpty()) break;

                List<String> authors = opt.get().authors;
                if (authors != null && authors.size() >= 2) {
                    String a = authors.get(0);
                    for (int i = 1; i < authors.size(); i++) {
                        String b = authors.get(i);
                        if (!a.equals(b)) {
                            counters.computeIfAbsent(a, k -> new HashMap<>())
                                    .merge(b, 1, Integer::sum);
                        }
                    }
                }
            }
        }

        Map<String, List<String>> adj = new HashMap<>();
        Set<String> nodes = new HashSet<>();

        for (var entry : counters.entrySet()) {
            String u = entry.getKey();
            for (var target : entry.getValue().entrySet()) {
                if (target.getValue() >= 6) {
                    adj.computeIfAbsent(u, k -> new ArrayList<>()).add(target.getKey());
                    nodes.add(u);
                    nodes.add(target.getKey());
                }
            }
        }
        counters = null; 

        List<List<String>> sccs = findSCCs(nodes, adj);
        sccs.sort((a, b) -> Integer.compare(b.size(), a.size()));

        for (int i = 0; i < Math.min(10, sccs.size()); i++) {
            List<String> community = sccs.get(i);
            int diameter = computeDiameter(community, adj);
            System.out.println("Rang: " + (i + 1));
            System.out.println("Taille: " + community.size());
            System.out.println("Diamètre: " + diameter);
            System.out.println("Membres: " + community);
            System.out.println("---------------------------");
        }
    }

    private static List<List<String>> findSCCs(Set<String> nodes, Map<String, List<String>> adj) {
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        for (String node : nodes) {
            if (!visited.contains(node)) fillOrder(node, visited, stack, adj);
        }

        Map<String, List<String>> revAdj = new HashMap<>();
        for (var entry : adj.entrySet()) {
            for (String neighbor : entry.getValue()) {
                revAdj.computeIfAbsent(neighbor, k -> new ArrayList<>()).add(entry.getKey());
            }
        }

        List<List<String>> sccs = new ArrayList<>();
        visited.clear();
        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfsRev(node, visited, component, revAdj);
                sccs.add(component);
            }
        }
        return sccs;
    }

    private static void fillOrder(String u, Set<String> visited, Stack<String> stack, Map<String, List<String>> adj) {
        visited.add(u);
        List<String> neighbors = adj.get(u);
        if (neighbors != null) {
            for (String v : neighbors) {
                if (!visited.contains(v)) fillOrder(v, visited, stack, adj);
            }
        }
        stack.push(u);
    }

    private static void dfsRev(String u, Set<String> visited, List<String> comp, Map<String, List<String>> revAdj) {
        visited.add(u);
        comp.add(u);
        List<String> neighbors = revAdj.get(u);
        if (neighbors != null) {
            for (String v : neighbors) {
                if (!visited.contains(v)) dfsRev(v, visited, comp, revAdj);
            }
        }
    }

    private static int computeDiameter(List<String> community, Map<String, List<String>> adj) {
        int maxDist = 0;
        Set<String> members = new HashSet<>(community);
        for (String start : community) {
            Map<String, Integer> dists = new HashMap<>();
            Queue<String> q = new LinkedList<>();
            q.add(start);
            dists.put(start, 0);
            while (!q.isEmpty()) {
                String u = q.poll();
                int d = dists.get(u);
                maxDist = Math.max(maxDist, d);
                List<String> neighbors = adj.get(u);
                if (neighbors != null) {
                    for (String v : neighbors) {
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
}