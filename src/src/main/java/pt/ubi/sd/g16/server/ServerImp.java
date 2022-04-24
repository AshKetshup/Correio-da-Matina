package pt.ubi.sd.g16.server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import pt.ubi.sd.g16.shared.News;
import pt.ubi.sd.g16.shared.Settings;
import pt.ubi.sd.g16.shared.Topic;

public class ServerImp extends UnicastRemoteObject implements ServerInterface {
	private ArrayList<News> news_list; // ArrayList com todas as notícias
	private ArrayList<Topic> topics_list; // ArrayList com todos os tópicos
	private int limit_topic; // Limite de notícias por tópico antes de serem enviadas para o servidorBackup

	private static final String pathConfig = "config.json";
	private static final String pathData = System.getProperty("user.dir") + File.separator + "data";
	private static final String pathNews = pathData + File.separator + "news";
	private static final String pathTopics = pathData + File.separator + "topics";

	public ServerImp() throws IOException {
		super();
		if (!loadNews()) // caso falhe a leitura das lista
			news_list = new ArrayList<>();
		if (!loadTopic()) // caso falhe a leitura da lista
			topics_list = new ArrayList<>();

		loadConfig(); // inicializa as variáveis topics_size, news_size e limit_topic

		//System.out.println("FICHEIROS CONFIG: " + limit_topic + " " + news_size + " " + topics_size);

		Files.createDirectories((Paths.get(pathNews))); // Cria pasta data/news
		Files.createDirectories((Paths.get(pathTopics))); // Cria pasta data/topics
	}

//---------------- Operações de I/O:   ----------------

	public void loadConfig() throws IOException { // Inicializar configurações
		File f = new File(pathConfig);
		Settings config = new Settings(); // Iniciar class configurações
		try {
			if (!f.isFile()) {  // Caso o ficheiro de configurações não exista
				FileWriter fout;
				fout = new FileWriter(f);
				config.setLimit_topics(16);
				fout.write(config.serialize()); // Cria um objecto Settings com as configurações pretendidas,
				fout.flush();                    // transforma-o numa string Gson e escreve para um ficheiro.
				fout.close();
			} else {
				try (FileReader fr = new FileReader(f)) { // Lê uma string Gson e transfere o seu conteúdo para variáveis
					char[] chars = new char[(int) f.length()];
					fr.read(chars);
					String GsonLine = new String(chars);
					config.deserialize(GsonLine);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		limit_topic = config.getLimit_topics(); // Actualiza os valores das configs.
	}

	public void updateConfig() throws IOException {
		File f = new File(pathConfig);
		Settings config = new Settings(); // Iniciar objecto configurações configurações
		config.setLimit_topics(limit_topic);
		try {
			FileWriter fout;
			fout = new FileWriter(f);
			fout.write(config.serialize()); // Cria um objecto Settings com as configurações pretendidas,
			fout.flush();                    // transforma-o numa string Gson e escreve para um ficheiro.
			fout.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void delNewsFile(News n) { // Apaga ficheiro da notícia
		File dir = new File(pathNews);
		File f = new File(dir, n.getId() + ".json");
		try {
			f.delete();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void backupNews(News n) { // Envia só para o servidor de backup
		// Envia sv de backup
		delNewsFile(n);
	}

	public void updateTopic(Topic t) throws IOException {
		File dir = new File(pathTopics);
		String filenameTopico = t.getId() + ".json"; // Escrita do tópico para um ficheiro
		File ft = new File(dir, filenameTopico);
		FileWriter fout;
		fout = new FileWriter(ft);
		fout.write(t.serialize()); // transforma-o numa string Gson e escreve para um ficheiro.
		fout.flush();
		fout.close();
	}

	public void updateNews(News n) throws IOException {
		File dir = new File(pathNews);
		String filenameNews = n.getId() + ".json"; // Escrita da News para um ficheiro
		File f = new File(dir, filenameNews);
		FileWriter fout;
		fout = new FileWriter(f);
		fout.write(n.serialize()); // transforma-o numa string Gson e escreve para um ficheiro.
		fout.flush();
		fout.close();
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

	public Topic readTopic(String filename) {
		File dir = new File(pathTopics); // Pasta
		File f = new File(dir, filename); // Nome do ficheiro
		Topic t = null;
		try (FileReader fr = new FileReader(f)) { // Lê uma string Gson e transfere o seu conteúdo para variáveis
			char[] chars = new char[(int) f.length()];
			fr.read(chars);
			String GsonLine = new String(chars);
			t = new Topic(GsonLine);  // Cria novo tópico com os conteúdos lidos
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}

	public boolean loadTopic() {                // Carrega as notícias para o arrayList
		topics_list = new ArrayList<Topic>();
		File folder = new File(pathTopics);
		File[] listOfFiles = folder.listFiles();
		String[] fileAux;
		Topic t;

		try {
			for (File listOfFile : listOfFiles) {
				if (listOfFile.isFile()) {
					fileAux = listOfFile.getName().split("\\."); // Separa o nome do ficheiro da extensão
					if (Objects.equals(fileAux[1], "json")) {
						t = readTopic(listOfFile.getName()); // Lê o ficheiro para um tópico
						topics_list.add(t); // Adiciona o tópico à lista
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	public boolean loadNews() {                // Carrega as notícias para o arrayList
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

	public void loadTopics() {

	}

//---------------- PUBLISHER ----------------

	public void addTopic(String id_topic, String title_topic, String desc_topic) throws IOException {  // P1, recebe id, título e descrição para criar um tópico novo.
		Topic new_t = new Topic(id_topic, title_topic, desc_topic); // Cria um objecto do tipo tópico
		topics_list.add(new_t); // Adiciona à lista de tópicos
		updateConfig(); // Actualiza o ficheiro de configuração

		updateTopic(new_t);

	}

	public ArrayList<Topic> getTopics() { // P2, devolve a arrayList com os tópicos
		return topics_list;
	}

	public void addNews(News n) throws IOException { // P3, adiciona uma notícia ao servidor
		String id_topico = n.getTopic().getId(); // Obtem o id do tópico da notícia
		news_list.add(n); // Adiciona a notícia à lista de notícias
		UUID news_id;
		News aux = null;
		updateNews(n); // Actualiza o ficheiro notícia
		Topic taux = null;

		for (int i = 0; i < topics_list.size() ; i++) {  // Actualiza o tópico da notícia e a lista de tópicos
			if (Objects.equals(topics_list.get(i).getId(), id_topico)) {
				taux = topics_list.get(i);
				taux.addNews(n);

				if (topics_list.get(i).getNewsIDStock().size() > limit_topic) { // Verifica se a quantidade de notícias ultrapassa o limite
					int tam = topics_list.get(i).getNewsIDStock().size();
					int count = 0;
					while (count < tam / 2) {
						news_id = topics_list.get(i).getNewsIDStock().get((0));
						for (int j = 0; j < news_list.size(); j++) {
							if (news_list.get(j).getId() == news_id) {
								aux = news_list.get(j);
								break;
							}
						}
						if (aux != null) {
							backupNews(aux); // Envia para servidor backUp
							System.out.println("A remover: " + topics_list.get(i).getNewsIDStock().get((0)));
							taux.delNewsStock(taux.getNewsIDStock().get((0))); // Remove a metade mais recente
							updateTopic(taux); // Actualiza o ficheiro de tópicos
						}
						count += 1;
					}
				}
				break;
			}
		}
		updateTopic(taux); // Actualiza o ficheiro de tópicos
		updateConfig(); // Actualiza ficheiro de configuração
	}

	public ArrayList<News> getNews() {
		return news_list;
	} // P4 Envia a lista de notícias

//---------------- SUBSCRIBER ----------------


//----------- NOTIFICAÇÃO DE PUBLICAÇÃO DE NOTICIA A PESSOAS SUBSCRITAS A UM CERTO TÓPICO

/*
	//TERMINAR
	//callback -> investigar isso!!!!
	@Override
	public void notificar() throws RemoteException {
		// TODO Auto-generated method stub

	}

//------------------- BACKUP -------------------


	//-------------- LOGIN -----------------------

	public ArrayList<login> abrir_login() {
		return null;
	}


	@Override
	public boolean user_login(String username, String password) throws RemoteException {

		return false;
	}

	@Override
	public void registar_user(String username, String password, String confirm_pwd) throws RemoteException {
		// TODO Auto-generated method stub

	}*/
}