import java.util.Scanner;
import java.util.List;

public class BlockchainApp {
    private static Blockchain blockchain = new Blockchain();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        while (true) {
            System.out.println("Choose Action:");
            System.out.println("1. Create new block");
            System.out.println("2. Send transaction");
            System.out.println("3. Show blockchain");
            System.out.println("4. Check integrity of blockchain");
            System.out.println("5. View Specific Block");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createNewBlock();
                    break;
                case 2:
                    sendTransaction();
                    break;
                case 3:
                    viewBlockchain();
                    break;
                case 4:
                    checkBlockchainIntegrity();
                    break;
                case 5:
                    viewSpecificBlock();
                    break;
                case 6:
                    System.out.println("Exit.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Wrong Input. Please, choose action from 1 to 6.");
            }
        }
    }

    public static void createNewBlock() {
        System.out.println("Add data for new block:");
        String data = scanner.nextLine();

        Block newBlock = new Block(blockchain.getLatestBlock().getIndex() + 1,
                blockchain.getLatestBlock().getHash(),
                data, blockchain);
        newBlock.mineBlock(4);

        blockchain.addBlock(newBlock);
        System.out.println("Added new block.");
    }

    public static void sendTransaction() {
        System.out.println("Input Sender:");
        String sender = scanner.nextLine();
        System.out.println("Input Recipient:");
        String recipient = scanner.nextLine();
        System.out.println("Input amount:");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Input index, which you want to add transaction:");
        int desiredIndex = scanner.nextInt();
        scanner.nextLine();

        Transaction transaction = new Transaction(sender, recipient, amount);

        if (desiredIndex >= 0 && desiredIndex <= blockchain.getChain().size()) {
            Block block = blockchain.getChain().get(desiredIndex);
            block.addTransaction(transaction);
            block.updateMerkleTree();
            block.recalculateHash();
            System.out.println("Transaction added to index " + desiredIndex);
        } else {
            System.out.println("Block index does not exist.");
        }
    }

    public static void viewBlockchain() {
        List<Block> chain = blockchain.getChain();

        for (Block block : chain) {
            System.out.println("Index: " + block.getIndex());
            System.out.println("Previous Hash: " + block.getPreviousHash());
            System.out.println("Hash: " + block.getHash());
            System.out.println("Merkle Root: " + block.calculateMerkleRoot());
            System.out.println("Timestamp: " + block.getTimestamp());
            System.out.println("Data: " + block.getData());
            System.out.println("Nonce: " + block.getNonce());

            List<Transaction> transactions = block.getTransactions();
            if (!transactions.isEmpty()) {
                System.out.println("Transactions:");
                for (Transaction transaction : transactions) {
                    System.out.println("  Sender: " + transaction.getSender());
                    System.out.println("  Recipient: " + transaction.getRecipient());
                    System.out.println("  Amount: " + transaction.getAmount());
                    System.out.println("  Hash: " + transaction.getHash());
                }
            }

            System.out.println();
        }
    }
    public static void viewSpecificBlock() {
        System.out.println("Input block index to view:");
        int blockIndex = scanner.nextInt();
        scanner.nextLine();

        Block block = blockchain.getBlockByIndex(blockIndex);

        if (block != null) {
            System.out.println("Block Index: " + block.getIndex());
            System.out.println("Previous Hash: " + block.getPreviousHash());
            System.out.println("Hash: " + block.getHash());
            System.out.println("Merkle Root: " + block.calculateMerkleRoot());
            System.out.println("Timestamp: " + block.getTimestamp());
            System.out.println("Data: " + block.getData());
            System.out.println("Nonce: " + block.getNonce());

            List<Transaction> transactions = block.getTransactions();
            if (!transactions.isEmpty()) {
                System.out.println("Transactions:");
                for (Transaction transaction : transactions) {
                    System.out.println("  Sender: " + transaction.getSender());
                    System.out.println("  Recipient: " + transaction.getRecipient());
                    System.out.println("  Amount: " + transaction.getAmount());
                    System.out.println("  Hash: " + transaction.getHash());
                }
            }

        } else {
            System.out.println("Block not found.");
        }
    }
    public static void checkBlockchainIntegrity() {
        boolean isChainValid = blockchain.isChainValid();
        if (isChainValid) {
            System.out.println("Blockchain is valid.");
        }
        else {
            System.out.println("Blockchain is defective.");
        }
    }
}
