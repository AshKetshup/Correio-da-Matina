package pt.ubi.sd.g16.shared;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.UUID;

public class Publisher extends Account {

    public Publisher(String username, String password, String passwordConfirm, int rank)
            throws PasswordNotMatchingException, NoSuchAlgorithmException, NoSuchProviderException, UsernameTakenException {
        super(username, password, passwordConfirm, rank);
    }

    public static Publisher getPublisherFromID(String publisherID) throws NullPointerException {
        Publisher x = (Publisher) Account.getAccountHashMap().get(publisherID);

        if (x == null)
            throw new NullPointerException("Given Publisher ID does not exist.");

        return x;
    }



}
