package pt.ubi.sd.g16.client;

import pt.ubi.sd.g16.server.ServerInterface;
import pt.ubi.sd.g16.shared.Account;
import pt.ubi.sd.g16.shared.News;
import pt.ubi.sd.g16.shared.Publisher;
import pt.ubi.sd.g16.shared.Topic;

import java.io.IOException;
import java.rmi.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class ClientMain extends java.rmi.server.UnicastRemoteObject implements ClientInterface {
    public ClientMain() throws RemoteException{
        super();
    }
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, Account.UsernameTakenException, NoSuchProviderException, Account.PasswordNotMatchingException {
        ServerInterface si = null;
        System.setSecurityManager(new SecurityManager());
        try {
            si = (ServerInterface) Naming.lookup("CorreioDaMatina");

        } catch( Exception r) {
            System.out.println(" Exception in client" + r.getMessage());
        }

        Topic t = new Topic("tech","Tecnologia","Estamos no mundo da tecnologia!");
        Publisher p = new Publisher("manel","manel123","manel123",1);
        si.addTopic(t.getId(),t.getTitle(),t.getDescription());

        char[] content = new char[180];
        content = "Pato Voador encontrado na UBI!".toCharArray();
        News n = new News("Pato voador!",content,"tech",p);
        News a = new News("Vaca voadora!",content,"tech",p);
        News b = new News("Gato voador!",content,"tech",p);
        News c = new News("Porco voador!",content,"tech",p);
        si.addNews(n);
        si.addNews(a);
        si.addNews(b);
        si.addNews(c);

    }
}
