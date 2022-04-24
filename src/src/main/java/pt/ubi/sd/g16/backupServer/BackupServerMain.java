package pt.ubi.sd.g16.backupServer;

public class BackupServerMain extends java.rmi.server.UnicastRemoteObject implements ServerInterface {
    public static ClientInterface cliente; // <- Interface cliente

    public BackupServerMain() throws java.rmi.RemoteException {
        super();
    }

    public static void main(String[] args) {
        String s;
        System.setSecurityManager(new SecurityManager());
        try { // Iniciar a execução do registry no porto desejado
            java.rmi.registry.LocateRegistry.createRegistry(1200);
            System.out.println("RMI registry ready.");
        } catch (Exception e) {
            System.out.println("Exception starting RMI registry:");
        }

        try {
            // instanciar objeto remoto
            BackupServerMain sv = new BackupServerMain();
            // registar o objeto remoto no Registry
            Naming.rebind("CorreioDaMatina", sv);
            System.out.println("Remote object ready");

            //    Adicionar aqui o código para interação cliente-servidor

        } catch (RemoteException e) {
            System.out.println("Exception in the server" + e.getMessage());
        } catch (java.net.MalformedURLException u) {
            System.out.println("Exception in the server - URL");
        }
    }
}
