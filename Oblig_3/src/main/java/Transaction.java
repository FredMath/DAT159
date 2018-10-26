
import javax.rmi.CORBA.Util;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Transaction {

    //Simplified compared to Bitcoin
    private List<Input> inputs = new ArrayList<>();
    private List<Output> outputs = new ArrayList<>();


    //If we make the assumption that all the inputs belong to the
    //same key, we can have one signature for the entire transaction,
    //and not one for each input. This simplifies things a lot
    //(more than you think)!
    private PublicKey senderPublicKey;
    private byte[] signature;

    private String txHash;

    public Transaction(PublicKey senderPublicKey) {

        this.setSenderPublicKey(senderPublicKey);
        calculateTxHash();
    }

    public void addInput(Input input) {
        inputs.add(input);

    }

    public void addOutput(Output output) {
        outputs.add(output);

    }

    public void signTxUsing(PrivateKey privateKey) {
        signature = DSAUtil.signWithDSA(privateKey, inputToString() + outputToString());
    }

    public void calculateTxHash() {
        txHash = HashUtil.base64Encode(HashUtil.sha256Hash(inputToString() + outputToString()));

    }

    public boolean isValid() {


        long ov = 0;
        for (Output o : outputs) {
            ov += o.getValue();
            if (o.getValue() < 0)

                return false;
        }

        long iv = 0;


        for (Input i : inputs) {
            iv += UTXO.getMap().get(i).getValue();
        }


        return (!inputs.isEmpty() && iv == ov
                && DSAUtil.verifyWithDSA(senderPublicKey, inputToString() + outputToString(), signature));

    }


    public List<Input> getInputs() {
        return inputs;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Output> outputs) {
        this.outputs = outputs;
    }

    public PublicKey getSenderPublicKey() {
        return senderPublicKey;
    }

    public void setSenderPublicKey(PublicKey senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String inputToString() {
        return inputs.stream().map(Input::toString).collect(Collectors.joining("n\t\t"));
    }

    public String outputToString() {
        return outputs.stream().map(Output::toString).collect(Collectors.joining("n\t\t"));
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "inputs=" + inputs +
                ", outputs=" + outputs +
                ", senderPublicKey=" + senderPublicKey +
                ", signature=" + Arrays.toString(signature) +
                ", txHash='" + txHash + '\'' +
                '}';
    }

}
