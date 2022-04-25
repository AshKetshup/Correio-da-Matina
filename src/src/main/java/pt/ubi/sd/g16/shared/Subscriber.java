package pt.ubi.sd.g16.shared;

import com.google.gson.JsonSyntaxException;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.UUID;

public class Subscriber extends Account {
    public Subscriber(String username, String password, String passwordConfirm, int rank)
            throws PasswordNotMatchingException, NoSuchAlgorithmException, NoSuchProviderException, UsernameTakenException {
        super(username, password, passwordConfirm, rank);
    }

    public Subscriber(String jsonLine) throws JsonSyntaxException {
        super(jsonLine);
    }

    public void subscribeTopic(String topicID) {
        getTopicIDList().add(topicID);
    }

    public void unsubscribeTopid(String topicID){
        getTopicIDList().remove(topicID);
    }
}