package pt.ubi.sd.g16.server;

import pt.ubi.sd.g16.server.exceptions.NotFoundOnServerException;
import pt.ubi.sd.g16.shared.*;
import pt.ubi.sd.g16.shared.Exceptions.*;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public interface ServerInterface extends Remote {

    void loadConfig() throws IOException;
    ArrayList<String> checkForNotifications(String idUser) throws IOException;
    void notify(String idTopic) throws IOException;

    // region Login
    Account login(String username, String password) throws RemoteException, NoSuchAlgorithmException, WrongPasswordException, AccountNotFoundException;
    // endregion

    // region Add
    void addAccount(Account account) throws RemoteException, IOException;
    void addNews(News news) throws RemoteException, IOException, FailedDeleteException;
    void addTopic(Topic topic) throws RemoteException, IOException;
    // endregion

    // region Create
    Account createAccount(String username, String password, String passwordConfirm, int rank)
            throws PasswordNotMatchingException, UsernameTakenException, NoSuchAlgorithmException, IOException, NoSuchProviderException;

    News createNews(String title, char[] content, String topicID, Publisher publisher)
            throws NullPointerException, ArrayIndexOutOfBoundsException, IOException;

    Topic createTopic(String id, String title, String description)
        throws IOException, TopicIDTakenException;
    // endregion

    // region Get
    Topic getTopicFromID(String topicID) throws RemoteException;
    Topic getTopicFromNews(News news) throws RemoteException;
    ArrayList<Topic> getAllTopics() throws RemoteException, IndexOutOfBoundsException;

    News getNewsFromID(UUID newsID) throws RemoteException;
    ArrayList<News> getAllNews() throws RemoteException;
    ArrayList<News> getNewsFromTopic(Topic topic) throws RemoteException;

    ArrayList<Account> getAllAccounts() throws RemoteException;

    void saveSubscriber(Subscriber s) throws IOException;
    Account getAccountFromID(String accountID) throws RemoteException, AccountNotFoundException;

    String getBackupIP() throws RemoteException;
    News getLastNewsFromTopic(String idTopic) throws RemoteException, IOException, IndexOutOfBoundsException;
    ArrayList<News> getNewsInsideTimeInterval(String idTopic, Date startDate, Date finalDate)
        throws RemoteException, NotFoundOnServerException;
    // endregion

    void backupNews(UUID newsID) throws IOException, FailedDeleteException;
}