public class CoinbaseTx {
	
    //Simplified compared to Bitcoin (nothing significant missing)
	private String coinbase; // "The Times 03/Jan/2009 Chancellor 
	                         //  on brink of second bailout for banks"
	private Output output;
	private String txHash;

	public CoinbaseTx(String coinbase, int value, String address) {
	    this.coinbase = coinbase;
	    this.txHash = HashUtil.base64Encode(HashUtil.sha256Hash(coinbase + output));
        output = new Output(value, address);
	    //TODO
	    //Remember to calculate txHash
	}

    public String getCoinbase() {
        return coinbase;
    }

    public void setCoinbase(String coinbase) {
        this.coinbase = coinbase;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    @Override
    public String toString() {
        return "CoinbaseTx{" +
                "coinbase='" + coinbase + '\'' +
                ", output=" + output +
                ", txHash='" + txHash + '\'' +
                '}';
    }
    //TODO Getters?
	
}
