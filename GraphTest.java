public class GraphTest {
    public static void main(String[] args) {
        Graph<String, String> relationships = new AdjacencyMapGraph<String, String>();

        relationships.insertVertex("A");
        relationships.insertVertex("B");
        relationships.insertVertex("C");
        relationships.insertVertex("D");
        relationships.insertVertex("E");
        relationships.insertDirected("A", "B", "related");
        relationships.insertDirected("A", "C", "related");
        relationships.insertDirected("A", "D", "related");
        relationships.insertDirected("A", "E", "related");
        relationships.insertDirected("B", "A", "related");
        relationships.insertDirected("B", "C", "related");
        relationships.insertDirected("C", "A", "related");
        relationships.insertDirected("C", "B", "related");
        relationships.insertDirected("C", "D", "related");
        relationships.insertDirected("E", "B", "related");
        relationships.insertDirected("E", "C", "related");

        System.out.println("\nThe graph:\n");
        System.out.println(relationships);

        System.out.println("\nLinks to A = " + relationships.inDegree("A"));
        System.out.println("Links to B = " + relationships.inDegree("B"));
        System.out.println("Links to C = " + relationships.inDegree("C"));
        System.out.println("Links to D = " + relationships.inDegree("D"));
        System.out.println("Links to E = " + relationships.inDegree("E"));

        GraphLib.randomWalk(relationships, "A", 3);
        GraphLib.verticesByInDegree(relationships);
    }
}
