package pt.ubi.sd.g16.client;

import pt.ubi.sd.g16.server.ServerInterface;
import pt.ubi.sd.g16.shared.Account;
import pt.ubi.sd.g16.shared.Exceptions.WrongPasswordException;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

public class Session {
    private final Account sessionAccount;

    public Session(String username, String password, ServerInterface serverInterface) throws NoSuchAlgorithmException, RemoteException, WrongPasswordException {
        sessionAccount = serverInterface.login(username, password);
    }

    public Account getSessionAccount() {
        return sessionAccount;
    }
}
