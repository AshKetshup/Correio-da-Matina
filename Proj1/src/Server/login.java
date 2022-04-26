package Server;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class login implements Serializable{
	private String username;
	private int prod_registado;
	private String secure_password;
	private byte[] salt;
	
	public login(String username, String pwd) throws NoSuchProviderException, NoSuchAlgorithmException {
		this.username = username;
		
		//generates the salt -> random array of bytes
		this.salt = Secure_salt();
		
		this.secure_password = SecurePassword(pwd, salt);;
	}

	public int getProd_registado() {
		return prod_registado;
	}

	public void setProd_registado(int prod_registado) {
		this.prod_registado = prod_registado;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getSecure_password() {
		return secure_password;
	}

	public void setSecure_password(String secure_password) {
		this.secure_password = secure_password;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	private static byte[] Secure_salt() throws NoSuchProviderException, NoSuchAlgorithmException {
	    //Always use SecureRandom generator
	    SecureRandom secureRandom = SecureRandom.getInstance("MD5","SUN");

	    //create array for salt
	    byte[] salt = new byte[16]; //size of byte array = 16

	    //get a random salt
	    secureRandom.nextBytes(salt); //
	    return salt;
	}
	
	private static String SecurePassword(String password, byte[] salt){
	    String generatedPassord = null;
	    try{
	        MessageDigest msgDigest = MessageDigest.getInstance("SHA256");
	        msgDigest.update(salt);
	        byte[] bytes = msgDigest.digest(password.getBytes());

	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i<bytes.length;i++) {
	            sb.append(Integer.toString((bytes[i] &0xff) + 0x100, 16).substring(1));
	        }
	        generatedPassord = sb.toString();
	    }catch(NoSuchAlgorithmException e){
	        e.printStackTrace();
	    }
	    return generatedPassord;
	}
}

