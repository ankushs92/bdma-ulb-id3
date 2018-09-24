import java.util.Set;

public class DecisionTree {

    private final Set<Node> nodes;

    public DecisionTree(final Set<Node> nodes) {
        this.nodes = nodes;
    }


    public Set<Node> getNodes() {
        return nodes;
    }
}
