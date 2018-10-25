/**
 * 
 */
package crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author Preben
 *
 */
public class Server implements IParent {
	/**
	   * Main Method
	   * 
	   * @param args
	   */
	private static SecretKey secretKey;
	private static Cipher encryptionCipher;
	private static Cipher decryptionCipher;

	ServerSocket server;
	Socket client;
	ObjectOutputStream oos;
	ObjectInputStream ois;


	  public static void main(String args[])
	  {
		  Server server = new Server();
		  // Wait for requests
		  while (true) {
			  server.setup();
			  server.receiveAndSend();
		  }

	  }

	private void setup(){
		try {
			server = new ServerSocket(PORT);
			System.out.println("Waiting for client request...");
			client = server.accept();
			System.out.println("Connected to client at: "+client.getInetAddress());

			oos = new ObjectOutputStream(client.getOutputStream());
			ois = new ObjectInputStream(client.getInputStream());
            String temp = (String) ois.readObject();
            System.out.println("SecretKey: "+temp);
			secretKey = new SecretKeySpec(temp.getBytes("ISO-8859-1"),0, temp.getBytes("ISO-8859-1").length,"DES");
			System.out.println(secretKey);
			encryptionCipher = Cipher.getInstance("DES");
			decryptionCipher = Cipher.getInstance("DES");


			encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey);



		}catch (NoSuchAlgorithmException|NoSuchPaddingException|InvalidKeyException | IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	public void receiveAndSend() {
	    try{
	        // Receive message from the client
	        byte[] clientMsg = (byte[]) ois.readObject();

	        byte[] decryptedClientBytes = decryptMessage(clientMsg);
	        String decryptedClientMsg = new String(decryptedClientBytes, "ISO-8859-1");
	        
	        // Print the message in ISO-8859-1 format
	        System.out.println("Message from Client: "+ decryptedClientMsg);
	        
	        // Create a response to client if message received
	        String response = "No message received";
	        
	        if(ois != null){
	        	response = "Message received from client";
	        	byte[] encryptedResponse = encryptMessage(response.getBytes("ISO-8859-1"));
	        	// Send the encrypted response message to the client
	            oos.writeObject(encryptedResponse);
	            oos.flush();
	        }
	        
	        // Close Client and Server sockets
			client.close();
			server.close();
			oos.close();
			ois.close();
	        
	    }catch(IOException | ClassNotFoundException e){
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

		  }catch (IllegalBlockSizeException|BadPaddingException e){
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
