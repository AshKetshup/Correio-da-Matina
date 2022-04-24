package pt.ubi.sd.g16.backupServer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

import pt.ubi.sd.g16.shared.News;
import pt.ubi.sd.g16.shared.Settings;
import pt.ubi.sd.g16.shared.Topic;

public class Connection extends Thread{
	private Socket S;
	
	private ArrayList<News> news_list; // ArrayList com todas as notícias em backup
	
	private static final String pathConfig = "config.json";
	private static final String pathData = System.getProperty("user.dir") + File.separator + "data";
	
	private static final String pathNews = pathData + File.separator + "backup_news";
	
	public Connection(Socket s) throws IOException{
		super();
		S = s;
		Files.createDirectories((Paths.get(pathNews))); // Cria pasta data/news
		start();
	}
	
	public void run(){
		loadNews();
		//apresenta noticias no servidor
		for(int i=0; i<news_list.size(); i++) {
			System.out.println(news_list.get(i).toString());
		}
	}
	
	
	// Carrega as notícias para o arrayList
	public boolean loadNews() {                
		news_list = new ArrayList<News>();
		File folder = new File(pathNews);
		File[] listOfFiles = folder.listFiles();
		String[] fileAux;
		News n;

		try {
			for (File listOfFile : listOfFiles) {
				if (listOfFile.isFile()) {
					fileAux = listOfFile.getName().split("\\."); // Separa o nome do ficheiro da extensão
					if (Objects.equals(fileAux[1], "json")) {
						n = readNews(listOfFile.getName()); // Lê o ficheiro para uma notícia
						news_list.add(n); // Adiciona a notícia à lista
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	public News readNews(String filename) {
		File dir = new File(pathNews); // Pasta
		File f = new File(dir, filename); // Nome do ficheiro
		News n = null;
		
		try (FileReader fr = new FileReader(f)) { // Lê uma string Gson e transfere o seu conteúdo para variáveis
			char[] chars = new char[(int) f.length()];
			fr.read(chars);
			String GsonLine = new String(chars);
			n = new News(GsonLine);  // Cria nova notícia com os conteúdos lidos
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return n;
	}

}