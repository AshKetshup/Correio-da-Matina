package pt.ubi.sd.g16.shared;

import com.google.gson.JsonSyntaxException;
import pt.ubi.sd.g16.shared.Exceptions.PasswordNotMatchingException;
import pt.ubi.sd.g16.shared.Exceptions.UsernameTakenException;

import java.security.NoSuchAlgorithmException;

public class Subscriber extends Account {
    public Subscriber(String username, String password, String passwordConfirm, int rank)
            throws PasswordNotMatchingException, NoSuchAlgorithmException, UsernameTakenException {
        super(username, password, passwordConfirm, rank);
    }

    public Subscriber(String jsonLine) throws JsonSyntaxException {
        super(jsonLine);
    }

    public void subscribeTopic(String topicID) {
        getTopicIDList().add(topicID);
    }

    public void unsubscribeTopic(String topicID){
        getTopicIDList().remove(topicID);
    }
}