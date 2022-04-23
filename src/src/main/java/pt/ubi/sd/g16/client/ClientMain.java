package pt.ubi.sd.g16.client;

import pt.ubi.sd.g16.server.ServerInterface;

import java.rmi.*;

public class ClientMain extends java.rmi.server.UnicastRemoteObject implements ClientInterface {
    public ClientMain() throws RemoteException{
        super();
    }
    public static void main(String[] args) {
        System.setSecurityManager(new SecurityManager());
        try {
            ServerInterface si = (ServerInterface) Naming.lookup("CorreioDaMatina");

        } catch( Exception r) {
            System.out.println(" Exception in client" + r.getMessage());
        }
    }
}
