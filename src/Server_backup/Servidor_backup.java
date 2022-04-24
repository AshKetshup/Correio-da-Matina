package pt.ubi.sd.g16.backupServer;





import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class BackupServerMain {

	private ServerSocket ss; 
	private Socket s; 
	private Connection c; 
	
	public BackupServerMain(){
		try {
			ss = new ServerSocket (5432);
		}catch (IOException e){
			System.out.println(e.getMessage());
		}
		try{
			while(true){
				s = ss.accept(); //aceita uma ligação pedida por um cliente
				c = new Connection(s);
				//o pedido é tratado por uma nova Thread
				//o ServerSocket pode aceitar outras ligações
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	public static void main(String[] args){
		BackupServerMain server = new BackupServerMain();
	}
	
	
	
	
	
	
}


