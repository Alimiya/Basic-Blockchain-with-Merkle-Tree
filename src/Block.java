import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Block {
    private int index;
    private String previousHash;
    private String hash;
    private long timestamp;
    private String data;
    private int nonce;
    private List<Transaction> transactions;
    private MerkleTree merkleTree;
    private String merkleRoot;
    private Blockchain blockchain;

    public Block(int index, String previousHash, String data, Blockchain blockchain) {
        this.previousHash = previousHash;
        this.index = index;
        this.data = data;
        this.timestamp = new Date().getTime();
        this.nonce = 0;
        this.transactions = new ArrayList<>();
        this.merkleTree = new MerkleTree(new ArrayList<>());
        this.merkleRoot = "";
        this.hash = calculateHash();
        this.blockchain = blockchain;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }


    public String calculateHash() {
        try {
            String transactionHashes = transactions.stream()
                    .map(Transaction::getHash)
                    .collect(Collectors.joining());
            String input = index + previousHash + timestamp + data + nonce + transactionHashes;
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

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        updateMerkleTree();
        recalculateHash();
    }


    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String calculateMerkleRoot() {
        return merkleTree.getRootHash();
    }

    public void updateMerkleTree() {
        List<String> transactionData = transactions.stream()
                .map(Transaction::getHash)
                .collect(Collectors.toList());
        merkleTree = new MerkleTree(transactionData);
    }

    public void recalculateHash() {
        hash = calculateHash();
        if (index < blockchain.getChain().size() - 1) {
            blockchain.getChain().get(index + 1).recalculateHash();
        }
    }


    public void supervoid(int nonce) {
    }
}


