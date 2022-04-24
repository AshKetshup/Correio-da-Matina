package pt.ubi.sd.g16.shared;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class Login implements Serializable {
    private final String username;
    private int rank;
	private final String securedPassword;
	private byte[] salt;

    public Login(String username, String password) throws NoSuchProviderException, NoSuchAlgorithmException {
		this.username = username;

		//generates the salt -> random array of bytes
		this.salt = secureSalt();

		this.securedPassword = securePassword(password, salt);
	}

    public int getRank() {
		return rank;
	}

    public void setRank(int rank) {
		this.rank = rank;
	}

    public String getUsername() {
		return username;
	}

	public String getSecuredPassword() {
		return securedPassword;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	private static byte[] secureSalt() throws NoSuchProviderException, NoSuchAlgorithmException {
	    //Always use SecureRandom generator
	    SecureRandom secureRandom = SecureRandom.getInstance("MD5","SUN");

	    //create array for salt
	    byte[] salt = new byte[16]; //size of byte array = 16

	    //get a random salt
	    secureRandom.nextBytes(salt); //
	    return salt;
	}

	private static String securePassword(String password, byte[] salt){
	    String generatedPassord = null;

		try {
	        MessageDigest msgDigest = MessageDigest.getInstance("SHA256");
	        msgDigest.update(salt);
	        byte[] bytes = msgDigest.digest(password.getBytes());

	        StringBuilder sb = new StringBuilder();
	        for (int i=0; i<bytes.length; i++)
	            sb.append(Integer.toString((bytes[i] &0xff) + 0x100, 16).substring(1));

	        generatedPassord = sb.toString();
	    } catch(NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }

	    return generatedPassord;
	}
}
