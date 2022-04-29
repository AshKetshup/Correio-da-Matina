package pt.ubi.sd.g16.server;

import com.ashketshup.Landmark.TUI.Input;
import com.ashketshup.Landmark.TUI.Output;
import pt.ubi.sd.g16.shared.FileManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;

public class ServerMain extends UnicastRemoteObject {
    private static String serverName = "";

    public ServerMain() throws RemoteException {
        super();
    }

    public static void main(String[] args) {

        System.out.println(FileManager.PATH_DATA);

        System.setSecurityManager(new SecurityManager());

        if (args.length == 0) {
            serverName = Input.readString("Please insert server domain or IP (Empty for localhost):");
            if (serverName.isEmpty())
                serverName = getIP();
        } else
            serverName = args[0];

        Output.writeln("Using server backup in domain " + serverName + "\n");


        try {
            // Iniciar a execução do registry no porto desejado
            LocateRegistry.createRegistry(1099);
            System.out.println("RMI registry ready.");

            // instanciar objeto remoto
            ServerInterface sv = new ServerImp();

            // registar o objeto remoto no Registry
            Naming.rebind("CorreioDaMatina", sv);
            System.out.println("Remote Object Ready");

        } catch (RemoteException e) {
            System.out.println("Exception in the server" + e.getMessage());
        } catch (java.net.MalformedURLException u) {
            System.out.println("Exception in the server - URL");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        testIP();
    }

    public static void testIP(){
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements())
            {
                NetworkInterface n = e.nextElement();
                Enumeration<InetAddress> ee = n.getInetAddresses();
                while (ee.hasMoreElements())
                {
                    InetAddress i = ee.nextElement();
                    System.out.println(i.getHostAddress());
                }
            }
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String getIP(){
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            NetworkInterface n = e.nextElement();
            Enumeration<InetAddress> ee = n.getInetAddresses();
            InetAddress i = ee.nextElement();
            System.out.println(i.getHostAddress());
            return i.getHostAddress();
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static String getServerName() {
        return serverName;
    }
}
