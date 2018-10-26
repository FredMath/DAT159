public class Input {

    //Simplified compared to Bitcoin
    //The signature is moved to Transaction, see comment there.
	private String prevTxHash;
	private int prevOutputIndex;

    @Override
    public String toString() {
        return "Input{" +
                "prevTxHash='" + prevTxHash + '\'' +
                ", prevOutputIndex=" + prevOutputIndex +
                '}';
    }

    public Input(String prevTxHash, int prevOutputIndex) {
	    this.prevTxHash  = prevTxHash;
	    this.prevOutputIndex = prevOutputIndex;
	}

}
