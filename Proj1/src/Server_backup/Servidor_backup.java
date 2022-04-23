package Server_backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Server.Noticia;

public class Servidor_backup {
	public Servidor_backup() {
		
		ArrayList<Noticia> noticias_antigas = new ArrayList<Noticia>();
		int i=0;
		//abrir o ficheiro do backup -> noticias guardadas -> passam para um ArrayList para poderem ser manipuladas/visualizadas
		File f = new File("backup_data.txt");
		//diferentes ficheiros para diferentes tipos de topicos. 
		//deviamos ter um ficheiro que guarda todos os tópicos disponiveis e que podem ser usados
		//ou delimitamos os tópicos que podem ser usados... -> solução mais simples
		
		try {
			while(true){
				System.out.println("Servidor Backup");
				ServerSocket ss = new ServerSocket(2222);
				
				Socket s = ss.accept();
				
				//abrir o ficheiro 
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream oIs = new ObjectInputStream(fis);
				
				//colocar noticas no ArrayList para serem manipuladas
				while(oIs.read()!=(-1)){
					noticias_antigas.add((Noticia)oIs.readObject());
				}
				
				
				//por terminar!!!
				//recebe pedido do consumidor -> envia arraylist com noticias
				ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream()); 
				ObjectInputStream is = new ObjectInputStream(s.getInputStream());
				
				
				//lado do consumidor é que processa os dados em termos de periodo de data de publicação e tópico ou a 
				
				
				is.close();
				os.close();
				
				s.close();
				ss.close();
			}
		}catch(IOException e) {
			System.out.println("IO: " + e.getMessage());
		}catch(ClassNotFoundException e) {
			System.out.println("Not Found " + e.getMessage());
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//multi-thread server -> i dont know this shit...
		//usar sockets para consumidores poderem comunicar com o mesmo
		
		Servidor_backup sb = new Servidor_backup();

	}

}
