package pt.ubi.sd.g16.server;

import pt.ubi.sd.g16.client.ClientInterface;

import java.io.IOException;
import java.rmi.*;

public class ServerMain extends java.rmi.server.UnicastRemoteObject {
    public static ClientInterface cliente; // <- Interface cliente

    public ServerMain() throws java.rmi.RemoteException {
        super();
    }

    public static void main(String[] args) {
        String s;
        System.setSecurityManager(new SecurityManager());
        try { // Iniciar a execução do registry no porto desejado
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            System.out.println("RMI registry ready.");
        } catch (Exception e) {
            System.out.println("Exception starting RMI registry:");
        }

        try {
            // instanciar objeto remoto
            ServerInterface sv = new ServerImp();
            // registar o objeto remoto no Registry
            Naming.rebind("CorreioDaMatina", sv);
            System.out.println("Remote object ready");

            //    Adicionar aqui o código para interação cliente-servidor

        } catch (RemoteException e) {
            System.out.println("Exception in the server" + e.getMessage());
        } catch (java.net.MalformedURLException u) {
            System.out.println("Exception in the server - URL");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
