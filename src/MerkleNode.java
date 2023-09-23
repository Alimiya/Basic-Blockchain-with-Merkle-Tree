import java.security.MessageDigest;

public class MerkleNode {
    private String value;
    private MerkleNode right;
    private MerkleNode left;
    private String hash;

    public MerkleNode(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.hash = calculateHash();
    }

    public String getValue() {
        return value;
    }

    public MerkleNode getLeft() {
        return left;
    }

    public void setLeft(MerkleNode left) {
        this.left = left;
        this.hash = calculateHash();
    }

    public MerkleNode getRight() {
        return right;
    }

    public void setRight(MerkleNode right) {
        this.right = right;
        this.hash = calculateHash();
    }

    public String getHash() {
        return hash;
    }

    private String calculateHash() {
        try {
            String input = (left != null ? left.getHash() : "") + (right != null ? right.getHash() : "") + value;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
