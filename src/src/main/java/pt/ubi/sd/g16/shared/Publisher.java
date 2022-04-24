package pt.ubi.sd.g16.shared;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;

public class Publisher extends Account {
	private final static HashMap<String, Publisher> PUBLISHER_HASH_MAP = new HashMap<>();

    public Publisher(String username, String password, String passwordConfirm, int rank)
            throws PasswordNotMatchingException, NoSuchAlgorithmException, NoSuchProviderException, UsernameTakenException {
        super(username, password, passwordConfirm, rank);

        PUBLISHER_HASH_MAP.put(username, this);
    }

    public static Publisher getPublisherFromID(String publisherID) {
        Publisher x = PUBLISHER_HASH_MAP.get(publisherID);

        if (x == null)
            throw new NullPointerException("Given Publisher ID does not exist.");

        return x;
    }
}
