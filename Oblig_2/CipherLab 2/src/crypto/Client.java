/**
 * 
 */
package crypto;


import javax.crypto.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author Preben
 *
 */
public class Client implements IParent {

	private static SecretKey secretKey;
	private static Cipher encryptionCipher;
	private static Cipher decryptionCipher;

	Socket client;
	ObjectOutputStream oos;
	ObjectInputStream ois;

	public static void main(String[] args){
		Client client = new Client();
		client.setup();
		client.sendKey();
		client.sendAndReceive();
	}



	private void setup(){
		try {
			secretKey = KeyGenerator.getInstance("DES").generateKey();
			encryptionCipher = Cipher.getInstance("DES");
			decryptionCipher = Cipher.getInstance("DES");

			encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey);


			client = new Socket("localhost", PORT);
			System.out.println("Connected to Server on " + client.getInetAddress());
			oos = new ObjectOutputStream(client.getOutputStream());
			ois = new ObjectInputStream(client.getInputStream());

		}catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException e) {
			e.printStackTrace();
		}
	}

	private void sendKey() {


		try {
			// send key to server
			System.out.println("Key: "+secretKey);
			oos.writeObject(new String(secretKey.getEncoded(),"ISO-8859-1"));
			oos.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendAndReceive() {
	    try {
	    	// send a plaintext message to server
	    	String plaintxt = "Hello from client";
	    	byte[] encryptedTxt = encryptMessage(plaintxt.getBytes("ISO-8859-1"));
	    	// send message to server
	    	oos.writeObject(encryptedTxt);
	    	oos.flush();
	    	
	    	// receive response from server
	    	byte[] response = (byte[]) ois.readObject();
	    	String decryptedMsg = new String(decryptMessage(response));
	    	System.out.println("Response from server: "+ decryptedMsg);
	    	
	    	// close cliet socket
	    	client.close();
	    	ois.close();
	    	oos.close();
	    	
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public byte[] encryptMessage(byte[] plaintext) {
		byte[] enc = null;
		try{
			enc = encryptionCipher.doFinal(plaintext);
			enc = Base64.getEncoder().encode(enc);
            System.out.println("Encrypted message: " + new String(enc));
		}catch (IllegalBlockSizeException |BadPaddingException e){
			e.printStackTrace();
		}
		return enc;
	}

	@Override
	public byte[] decryptMessage(byte[] ciphertext) {
		byte[] dec = null;
		try{
			dec = Base64.getDecoder().decode(ciphertext);
			dec = decryptionCipher.doFinal(dec);

		}catch (IllegalBlockSizeException | BadPaddingException e){
			e.printStackTrace();
		}

		return dec;
	}

}
