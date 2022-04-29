package pt.ubi.sd.g16.shared;

import com.google.gson.Gson;
import pt.ubi.sd.g16.shared.Exceptions.PasswordNotMatchingException;
import pt.ubi.sd.g16.shared.Exceptions.UsernameTakenException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.UUID;

public class Publisher extends Account {

    public Publisher(String username, String password, String passwordConfirm, int rank)
            throws PasswordNotMatchingException, NoSuchAlgorithmException, UsernameTakenException, IOException {
        super(username, password, passwordConfirm, rank);
    }

    public Publisher(String jsonLine) {
        super(jsonLine);
    }

    public static Publisher getPublisherFromID(String publisherID) throws NullPointerException {
        Publisher x = (Publisher) Account.getAccountHashMap().get(publisherID);

        if (x == null)
            throw new NullPointerException("Given Publisher ID does not exist.");

        return x;
    }
}
