package pt.ubi.sd.g16.client;

import pt.ubi.sd.g16.server.ServerInterface;
import pt.ubi.sd.g16.shared.Exceptions.FailedDeleteException;
import pt.ubi.sd.g16.shared.Exceptions.PasswordNotMatchingException;
import pt.ubi.sd.g16.shared.Exceptions.TopicIDTakenException;
import pt.ubi.sd.g16.shared.Exceptions.UsernameTakenException;
import pt.ubi.sd.g16.shared.Publisher;

import java.io.IOException;
import java.rmi.*;
import java.security.NoSuchAlgorithmException;

public class ClientMain extends java.rmi.server.UnicastRemoteObject implements ClientInterface {
    public ClientMain() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        ServerInterface si = null;
        System.setSecurityManager(new SecurityManager());
        try {
            si = (ServerInterface) Naming.lookup("CorreioDaMatina");

        } catch (Exception r) {
            System.out.println(" Exception in client" + r.getMessage());
            System.exit(1);
        }

        try {
            si.addAccount(
                    si.createAccount(
                            "manel",
                            "manel123",
                            "manel123",
                            1
                    )
            );
        } catch (PasswordNotMatchingException | UsernameTakenException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }

        try {
            si.addTopic(
                    si.createTopic(
                            "tech",
                            "Tecnologia",
                            "Estamos no mundo da tecnologia!"
                    )
            );
        } catch (IOException | TopicIDTakenException e) {
            throw new RuntimeException(e);
        }

        char[] content = new char[180];
        content = "Pato Voador encontrado na UBI!".toCharArray();
        try {
            si.addNews(si.createNews("Pato voador!", content, "tech", (Publisher) si.getAccountFromID("manel")));
            si.addNews(si.createNews("Vaca voadora!", content, "tech", (Publisher) si.getAccountFromID("manel")));
            si.addNews(si.createNews("Gato voador!", content, "tech", (Publisher) si.getAccountFromID("manel")));
            si.addNews(si.createNews("Porco voador!", content, "tech", (Publisher) si.getAccountFromID("manel")));
        } catch (FailedDeleteException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
