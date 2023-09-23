import java.util.ArrayList;
import java.util.List;

public class MerkleTree {
    private MerkleNode root;

    public MerkleTree(List<String> transactionData) {
        List<MerkleNode> leaves = createLeafNodes(transactionData);
        root = constructMerkleTree(leaves);
    }

    public String getRootHash() {
        return root.getHash();
    }

    private List<MerkleNode> createLeafNodes(List<String> transactionData) {
        List<MerkleNode> leaves = new ArrayList<>();

        for (String data : transactionData) {
            leaves.add(new MerkleNode(data));
        }

        return leaves;
    }

    private MerkleNode constructMerkleTree(List<MerkleNode> leaves) {
        if (leaves.isEmpty()) {
            return new MerkleNode("");
        }

        List<MerkleNode> treeNodes = new ArrayList<>(leaves);

        while (treeNodes.size() > 1) {
            List<MerkleNode> nextLevel = new ArrayList<>();

            for (int i = 0; i < treeNodes.size(); i += 2) {
                MerkleNode left = treeNodes.get(i);
                MerkleNode right = (i + 1 < treeNodes.size()) ? treeNodes.get(i + 1) : left;

                MerkleNode parent = combineNodes(left, right);
                nextLevel.add(parent);
            }

            treeNodes = nextLevel;
        }

        return treeNodes.get(0);
    }


    private MerkleNode combineNodes(MerkleNode left, MerkleNode right) {
        String combinedValue = (right != null) ? left.getHash() + right.getHash() : left.getHash();
        return new MerkleNode(combinedValue);

    }
}
