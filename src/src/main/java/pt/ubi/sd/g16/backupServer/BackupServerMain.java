package src.main.java.pt.ubi.sd.g16.backupServer;





import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BackupServerMain {

	public static void main(String[] args) throws IOException{
		ServerSocket ss = null; 
		Socket s; 
		//final String pathConfig = "config.json";
		final String pathData = System.getProperty("user.dir") + File.separator + "data";
		
		final String pathNews = pathData + File.separator + "backup_news";
		
		Files.createDirectories((Paths.get(pathNews))); // Cria pasta data/news
		
		try {
			ss = new ServerSocket (5432);
			
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		
		ExecutorService executor = Executors.newFixedThreadPool((int) (Runtime.getRuntime().availableProcessors()* (1 + 50 / 5)/0.1));
		
		//all the threads
        //for(int i = 0; i < ; i++) {
        while(true) {
			Runnable worker = new Connection(ss);
            executor.execute(worker);
        }
	}
	
	
	
	
	
	
}


