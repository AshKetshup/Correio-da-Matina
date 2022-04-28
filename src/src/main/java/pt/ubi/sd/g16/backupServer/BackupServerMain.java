package pt.ubi.sd.g16.backupServer;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.concurrent.*;


public class BackupServerMain {

    public static void main(String[] args) throws IOException {

        TestIP();
        ServerSocket ss = null;
        Socket s;
        //final String pathConfig = "config.json";
        final String pathData = System.getProperty("user.dir") + File.separator + "data";

        final String pathNews = pathData + File.separator + "backup_news";

        Files.createDirectories((Paths.get(pathNews))); // Cria pasta data/news

        try {
            ss = new ServerSocket(5432);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        ExecutorService executor = Executors.newFixedThreadPool(100);
        int i = 0;
        //all the threads
        //for(int i = 0; i < ; i++) {

        try {
            for (; ; ) {
                i++;
                Runnable worker = new Connection(ss);
                // executor.execute(worker);
                Future<?> future = executor.submit(worker);
                future.get(60, TimeUnit.SECONDS);
                // System.out.println("Execução: " + i);
                // System.out.println(((ThreadPoolExecutor) executor).getActiveCount());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            executor.shutdown();
        }
    }

    public static void TestIP(){

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
}
