import java.util.*;
import java.util.stream.Collectors;

public class App {

    private class CyclicDependencyException extends Throwable {
    }

    // I implement Comparable because I want a nice sorted list when printing.
    class Node implements Comparable<Node> {
        Set<Node> children = new HashSet<Node>();
        String name;

        public Node(String name) {
            this.name = name;
        }

        public boolean addChild(Node n) {
            // if we add a child, all the children of the that node cannot be this node,
            // otherwise we create a cycle.
            if (n.allDescendants().contains(this)) {
                return false;
            } else {
                this.children.add(n);
                return true;
            }
        }

        public Set<Node> allDescendants() {
            Set<Node> all = new TreeSet<>();

            for (Node c : children) {
                all.add(c);
                all.addAll(c.allDescendants());
            }
            return all;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Node node = (Node) o;
            return name != null ? name.equals(node.name) : node.name == null;

        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        public String toString() {
            return name + " depends transitively on "
                    + allDescendants().stream().map(x -> x.name).collect(Collectors.joining(","));
        }

        @Override
        public int compareTo(Node o) {
            return name.compareTo(o.name);
        }
    }

    private Map<String, Node> nodesMap = new HashMap<String, Node>();

    private void print() {
        for (String name : nodesMap.keySet()) {
            System.out.println(nodesMap.get(name).toString());
        }
    }

    /**
     * Get a node from the Map if it already exists, else create it and put it in
     * the map.
     */
    public Node getOrCreateNode(String nodeName) {
        Node currentNode;
        if (!nodesMap.containsKey(nodeName)) {
            currentNode = new Node(nodeName);
            nodesMap.put(nodeName, currentNode);
        } else {
            currentNode = nodesMap.get(nodeName);
        }
        return currentNode;
    }

    public boolean tryDirectDependency(String rawDependency) throws CyclicDependencyException {
        String[] elements = rawDependency.split(" ");

        String nodeName = elements[0];

        Node node = getOrCreateNode(nodeName);

        for (int i = 1; i < elements.length; i++) {
            if (!node.addChild(getOrCreateNode(elements[i]))) {
                throw new CyclicDependencyException();
            }
        }
       // System.out.println(nodesMap);
        return true;
    }

    public static void main(String args[]) {

        try {
            App a = new App();
            a.tryDirectDependency("A B C");
            a.tryDirectDependency("B C E");
            a.tryDirectDependency("C G");
            a.tryDirectDependency("D A F");
            a.tryDirectDependency("E F");
            a.tryDirectDependency("F H");

            a.print();
        } catch (CyclicDependencyException e) {
            e.printStackTrace();
        }
        // CyclicDependencyException example
        try {
            App aa = new App();
            aa.tryDirectDependency("A B");
            aa.tryDirectDependency("B A");
            aa.print();
        } catch (CyclicDependencyException e) {
            System.err.println("Cyclic Dependency");
            e.printStackTrace();
        }
    }
}
