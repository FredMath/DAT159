import java.security.KeyPair;
import java.security.PublicKey;
import java.util.*;

public class Wallet {

    private String id;
    private KeyPair keyPair;
    private long newTxOutputs;
    private String address;

    //A refererence to the "global" complete utxo-set
    private Map<Input, Output> utxoMap;
    private long balance;

    public Wallet(String id, UTXO utxo) {
        this.id = id;
        this.utxoMap = utxo.getMap();
        keyPair = DSAUtil.generateRandomDSAKeyPair();

        //TODO
    }

    public String getAddress() {

        return HashUtil.addressFromPublicKey(keyPair.getPublic());
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public Transaction createTransaction(long value, String address) throws Exception {

        //TODO - This is a big one

        // 1. Collect all UTXO for this wallet and calculate balance
        Map<Input, Output> myUTXO = collectMyUtxo();
        long myBalance = calculateBalance(myUTXO.values());
        // 2. Check if there are sufficient funds --- Exception?

        if (myBalance < value)
            System.out.println("Du er en fattig mann!");

        // 3. Choose a number of UTXO to be spent --- Strategy?
        newTxOutputs = 0;
        ArrayList<Input> chosenTxs = new ArrayList<>();
        while (newTxOutputs < value) {
            myUTXO.forEach((input, output) -> {
                newTxOutputs = output.getValue();
                chosenTxs.add(input);
            });
        }

        // 4. Calculate change
        long diff = newTxOutputs - value;

        // 5. Create an "empty" transaction
        Transaction tx = new Transaction(keyPair.getPublic());
        // 6. Add chosen inputs
        chosenTxs.forEach(tx::addInput);

        // 7. Add 1 or 2 outputs, depending on change
        tx.addOutput(new Output(value, address));
        if (diff > 0)
            tx.addOutput(new Output(diff, getAddress()));

        // 8. Sign the transaction
        tx.signTxUsing(keyPair.getPrivate());
        // 9. Calculate the hash for the transaction
        tx.calculateTxHash();

        // 10. return
        return tx;

        // PS! We have not updated the UTXO yet. That is normally done
        // when appending the block to the blockchain, and not here!
        // Do that manually from the Application-main.
    }

    @Override
    public String toString() {
        //TODO
        return null;
    }

    public long getBalance() {
        return balance;
    }

    //TODO Getters?

    private long calculateBalance(Collection<Output> outputs) {
        outputs.forEach((output) -> balance = balance + output.getValue());

        return balance;
    }

    private Map<Input, Output> collectMyUtxo() {
        Map<Input, Output> collected = utxoMap;
        Iterator<Map.Entry<Input, Output>> it = utxoMap.entrySet().iterator();
        try {
            while (it.hasNext()) {
                Map.Entry<Input, Output> entry = it.next();
                if (entry.getValue().getAddress() != HashUtil.addressFromPublicKey(keyPair.getPublic())) {
                    collected.remove(entry.getKey());
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("feil");
        }
        return collected;
    }

}
