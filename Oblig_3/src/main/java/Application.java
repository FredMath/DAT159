public class Application {
    
    private static UTXO utxo = new UTXO();
	
	public static void main(String[] args) throws Exception {
	    
        /*
         * In this assignment, we are going to look at how to represent and record
         * monetary transactions. We will use Bitcoin as the basis for the assignment,
         * but there will be some simplifications.
         * 
         * We are skipping the whole blockchain this time, and instead focus on the
         * transaction details, the UTXOs and how money movements are represented.
         * 
         * (If you want to, you can of course extend the assignment by collecting the
         * individual transactions into blocks, create a Merkle tree for the block
         * header, validate, mine and add the block to a blockchain.)
         * 
         */

        // 0. To get started, we need a few (single address) Wallets. Create 2 wallets.
        //    Think of one of them as the "miner" (the one collecting "block rewards").
        Wallet fred = new Wallet("1", utxo);
        System.out.println("Lager første wallet:" + "\n" + fred.toString() + "\n");

        Wallet miner = new Wallet("2", utxo);
        System.out.println("Lager andre wallet:" + "\n" + miner.toString()+ "\n");


        // 1. The first "block" (= round of transactions) contains only a coinbase
        //    transaction. Create a coinbase transaction that adds a certain
        //    amount to the "miner"'s address. Update the UTXO-set (add only).
        System.out.println("Block nr 1:");

        CoinbaseTx startTx = new CoinbaseTx("I have all the money", 50, miner.getAddress());
        System.out.println("Lager første coinbase transaction:" + "\n" + startTx.toString() + "\n");
        UTXO.addOutputFrom(startTx);
        System.out.println("BlockUTXO end status:");
        UTXO.printUTXO();
        System.out.println("\n");

        System.out.println("Wallet 1:    " + fred.toString() + "\n");
        System.out.println("Wallet 2:    " + miner.toString() + "\n");

        
        // 2. The second "block" contains two transactions, the mandatory coinbase
        //    transaction and a regular transaction. The regular transaction shall
        //    send ~20% of the money from the "miner"'s address to the other address.
        System.out.println("Block nr 2:");

        CoinbaseTx coinbaseTx = new CoinbaseTx("Shalom!", 50, fred.getAddress());
        System.out.println("Lager andre coinbase transaction:" + "\n" + coinbaseTx.toString());


        Transaction regTx = miner.createTransaction(20 , fred.getAddress());
        if(!regTx.isValid())
            System.out.println("Bad coder is bad, transaction is not good, no");

        UTXO.addOutputFrom(coinbaseTx);
        UTXO.addAndRemoveOutputsFrom(regTx);

        System.out.println("BlockUTXO end status:");
        UTXO.printUTXO();
        System.out.println("\n");
        System.out.println("Block wallet end status: ");
        System.out.println("Wallet 1:    " + fred.toString() + "\n");
        System.out.println("Wallet 2:    " + miner.toString() + "\n");


        //    Update the UTXO-set (both add and remove).

        // 3. Do the same once more. Now, the "miner"'s address should have two or more
        //    unspent outputs (depending on the strategy for choosing inputs) with a
        //    total of 2.6 * block reward, and the other address should have 0.4 ...

        CoinbaseTx coinbaseTax = new CoinbaseTx("The WTF/minute is a measure of success", 70, miner.getAddress());


        regTx = miner.createTransaction(30, fred.getAddress());
        
        //    Validate the regular transaction ...
        if(!regTx.isValid()) System.out.println("You done goofed, transaction is angry");


        //    Update the UTXO-set ...
        UTXO.addOutputFrom(coinbaseTax);
        UTXO.addAndRemoveOutputsFrom(regTx);

        System.out.println("Block 3: ");

        System.out.println("Block UTXO end status: ");
        UTXO.printUTXO();
        System.out.println("Block Wallet end status: ");
        System.out.println("Wallet 1: " + "\n" + fred.toString() + "\n");
        System.out.println("Wallet 2: " + "\n" + miner.toString() + "\n");

    }


}
