package pt.ubi.sd.g16.backupServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import src.main.java.pt.ubi.sd.g16.shared.News;

public interface Connection_inter {
	
	  ArrayList<News> getNews();

	
	  ArrayList<String> seekTopics();
	
	  ArrayList<News> getNews_Topic(String id_topic);
	
	  void receiveNews(News n) throws IOException;
	
	  void updateNews(News n) throws IOException;
	
	
	// Carrega as notícias para o arrayList
	  boolean loadNews();
	
	  News readNews(String filename);
}
