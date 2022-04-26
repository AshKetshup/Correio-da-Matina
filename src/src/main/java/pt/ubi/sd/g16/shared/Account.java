package pt.ubi.sd.g16.shared;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import pt.ubi.sd.g16.shared.Exceptions.PasswordNotMatchingException;
import pt.ubi.sd.g16.shared.Exceptions.UsernameTakenException;

import java.io.*;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static pt.ubi.sd.g16.shared.FileManager.PATH_NEWS;
import static pt.ubi.sd.g16.shared.FileManager.PATH_USERS;

public class Account implements Serializable {
	private final static HashMap<String, Account> ACCOUNT_HASH_MAP = new HashMap<>();

    private final String id;
    private final int rank;
    private final byte[] salt;
    private final String saltedPassword;
    private final ArrayList<UUID> newsIDList = new ArrayList<>();
    private final ArrayList<String> topicIDList = new ArrayList<>();

	public Account(String username, String password, String passwordConfirm, int rank)
		throws PasswordNotMatchingException, NoSuchAlgorithmException, UsernameTakenException {
        if (!password.equals(passwordConfirm))
            throw new PasswordNotMatchingException();

		if (Account.isUsernameUsed(username))
			throw new UsernameTakenException();

		this.id = username;
		this.rank = rank;
        this.salt = getNextSalt();
		this.saltedPassword = securePassword(password, salt);

		Account.ACCOUNT_HASH_MAP.put(this.id, this);
    }

	public Account(String jsonLine) throws JsonSyntaxException {
		Account x = new Gson().fromJson(jsonLine, Account.class);

		this.id = x.id;
		this.rank = x.rank;
		this.salt = x.salt;
		this.saltedPassword = x.saltedPassword;
		this.newsIDList.addAll(x.newsIDList);

		Account.ACCOUNT_HASH_MAP.put(this.id, this);
    }

	public static HashMap<String, Account> getAccountHashMap() {
		return ACCOUNT_HASH_MAP;
	}

	public String getID() {
		return id;
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

	public ArrayList<String> getTopicIDList() { return topicIDList; }

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

	public static Account getAccountFromID(String accountID) throws NullPointerException {
        Account x = ACCOUNT_HASH_MAP.get(accountID);

        if (x == null)
            throw new NullPointerException();

        return x;
    }

	public static Account login(String username, String password) throws NullPointerException, NoSuchAlgorithmException {
		Account x = getAccountFromID(username);

		if (!x.saltedPassword.equals(securePassword(password, x.getSalt())))
			return null;

		return x;
	}

	// region "Read, Load and Update"
	public static Account read(File file) throws IOException {
		FileReader fileReader = new FileReader(file);
		char[] content = new char[(int) file.length()];
		fileReader.read(content);

		return new Account(Arrays.toString(content));
	}

	public static boolean load() throws FileNotFoundException {
		File folder = new File(PATH_USERS);
		File[] listOfFiles = folder.listFiles();

		if (listOfFiles == null)
			throw new FileNotFoundException();

		try {
			for (File file : listOfFiles)
				// Caso seja um ficheiro com extensão json
				if (file.isFile() && file.getName().endsWith(".json"))
					read(file);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}

		return true;
	}

	public void save() throws IOException {
		File fileDir = new File(PATH_NEWS);
		String fileName = this.id + ".json";
		File file = new File(fileDir, fileName);

		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(this.serialize());
		fileWriter.flush();
		fileWriter.close();
	}
	// endregion

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
