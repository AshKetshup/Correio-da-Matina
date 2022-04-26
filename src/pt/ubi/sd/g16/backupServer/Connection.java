package pt.ubi.sd.g16.backupServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

import src.main.java.pt.ubi.sd.g16.shared.News;
import src.main.java.pt.ubi.sd.g16.shared.Settings;
import src.main.java.pt.ubi.sd.g16.shared.Topic;

public class Connection extends Thread implements Connection_inter{
	
	private ServerSocket S;
	
	private ArrayList<News> news_list; // ArrayList com todas as notícias em backup
	
	private static final String pathConfig = "config.json";
	private static final String pathData = System.getProperty("user.dir") + File.separator + "data";
	
	private static final String pathNews = pathData + File.separator + "backup_news";
	
	public Connection(ServerSocket ss) throws IOException{
		super();
		S = ss;
		start();
	}
	
	public void run(){
		loadNews();
		//apresenta noticias no servidor
		//for(int i=0; i<news_list.size(); i++) {
		//	System.out.println(news_list.get(i).toString());
		//}
	}
	
	public ArrayList<News> getNews() {
		return news_list;
	} // P4 Envia a lista de notícias

	
	public ArrayList<String> seekTopics(){ //devolve os IDs de todos os tópicos com noticias em arquivo
		ArrayList<String> topics = new ArrayList<>();
		for(News n : news_list) {
			if(!topics.contains(n.getTopic().getId())) {
				topics.add(n.getTopic().getId());
			}
		}
		return topics;
	}
	
	public ArrayList<News> getNews_Topic(String id_topic){ //devolve as noticias do topico pretendido
		ArrayList<News> newsList = new ArrayList<>();
		for(News n : news_list) {
			if(n.getTopic().getId().equals(id_topic)) {
				newsList.add(n);
			}
		}
		return newsList;
	}
	
	public void receiveNews(News n) throws IOException{
		updateNews(n);
	}
	
	public void updateNews(News n) throws IOException { // Guarda ficheiro news na pasta news
		File dir = new File(pathNews);
		String filenameNews = n.getId() + ".json"; // Escrita da News para um ficheiro
		File f = new File(dir, filenameNews);
		FileWriter fout;
		fout = new FileWriter(f);
		fout.write(n.serialize()); // transforma-o numa string Gson e escreve para um ficheiro.
		fout.flush();
		fout.close();
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