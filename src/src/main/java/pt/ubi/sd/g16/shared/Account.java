package pt.ubi.sd.g16.shared;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Account implements Serializable {
	private final static HashMap<String, Account> ACCOUNT_HASH_MAP = new HashMap<>();

    private final String username;
    private final int rank;
    private final byte[] salt;
    private final String saltedPassword;
    private final ArrayList<UUID> newsIDList = new ArrayList<>();
    private final ArrayList<String> topicIDList = new ArrayList<>();
	private final ArrayList<String> notificationsList = new ArrayList<>();

	public static class PasswordNotMatchingException extends Exception {
        public PasswordNotMatchingException() {
            super("Password and Confirmation Password do not match.");
        }
    }

    public static class UsernameTakenException extends Exception {
        public UsernameTakenException() {
            super("This Username is already taken.");
        }
    }

	public Account(String username, String password, String passwordConfirm, int rank)
		throws PasswordNotMatchingException, NoSuchAlgorithmException, NoSuchProviderException, UsernameTakenException {
        if (!password.equals(passwordConfirm))
            throw new PasswordNotMatchingException();

		if (Account.isUsernameUsed(username))
			throw new UsernameTakenException();

		this.username = username;
		this.rank = rank;
        this.salt = getNextSalt();
		this.saltedPassword = securePassword(password, salt);

		Account.ACCOUNT_HASH_MAP.put(this.username, this);
    }

	public Account(String jsonLine) throws JsonSyntaxException {
		Account x = new Gson().fromJson(jsonLine, Account.class);

		this.username = x.username;
		this.rank = x.rank;
		this.salt = x.salt;
		this.saltedPassword = x.saltedPassword;
		this.newsIDList.addAll(x.newsIDList);
		this.topicIDList.addAll(x.topicIDList);
		this.notificationsList.addAll(x.notificationsList);

		Account.ACCOUNT_HASH_MAP.put(this.username, this);
    }

	public static HashMap<String, Account> getAccountHashMap() {
		return ACCOUNT_HASH_MAP;
	}

	public String getUsername() {
		return username;
	}

	public int getRank() {
		return rank;
	}

	public byte[] getSalt() {
		return salt;
	}

	public String getSaltedPassword() {
		return saltedPassword;
	}

	public ArrayList<UUID> getNewsIDList() {
		return newsIDList;
	}

	public ArrayList<String> getTopicIDList() {
		return topicIDList;
	}

	public ArrayList<String> getNotificationsList() {
		return notificationsList;
	}

	public void addNotification(String notification){
		notificationsList.add(notification);
	}

	public void remNotification(String notification){
		notificationsList.remove(notification);
	}

	public String serialize() {
        return new Gson().toJson(this);
    }

	public boolean deleteNewsID(UUID newsID) {
		return newsIDList.remove(newsID);
	}

	private static boolean isUsernameUsed(String username) {
		return ACCOUNT_HASH_MAP.containsKey(username);
	}

	public static byte[] getNextSalt() {
		byte[] salt = new byte[16];
		new SecureRandom().nextBytes(salt);
		return salt;
	}

	private static byte[] secureSalt() throws NoSuchProviderException, NoSuchAlgorithmException {
	    //Always use SecureRandom generator
	    SecureRandom secureRandom = SecureRandom.getInstance("MD5","SUN");

	    //create array for salt
	    byte[] salt = new byte[16]; //size of byte array = 16

	    //get a random salt
	    secureRandom.nextBytes(salt);
	    return salt;
	}

    private static String securePassword(String password, byte[] salt) throws NoSuchAlgorithmException {
		MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
		msgDigest.update(salt);
		byte[] bytes = msgDigest.digest(password.getBytes());

		StringBuilder sb = new StringBuilder();
		for (byte aByte : bytes)
			sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));

		return sb.toString();
	}
}
