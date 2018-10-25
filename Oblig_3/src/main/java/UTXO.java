import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UTXO {
    
    //Why is this a Map and not a Set?
    //  The values in this map are the UTXOs (unspent Outputs)
    //  When removing UTXOs, we need to identify which to remove.
    //  Since the Inputs are references to UTXOs, we can use those
    //  as keys.
	private Map<Input, Output> map = new HashMap<>();
	
	public void printUTXO() {
        for (Map.Entry<Input, Output> map : map.entrySet())
            System.out.println(map.getKey() + " " + map.getValue());


	}
	
	public void addOutputFrom(CoinbaseTx ctx) {
        map.put(new Input("", -1), ctx.getOutput());

	}

    public void addAndRemoveOutputsFrom(Transaction tx) {
        List<Input> inputs = tx.getInputs();
        List<Output> outputs = tx.getOutputs();

        for (Output o : outputs)
            map.put(inputs.get(outputs.indexOf(o)), o);
        for (Input i : inputs)
            map.remove(i);

	    //TODO
    }

    public Map<Input, Output> getMap() {
        return map;
    }
    //TODO Getters?
}
