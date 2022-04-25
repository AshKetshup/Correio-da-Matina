package pt.ubi.sd.g16.server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import pt.ubi.sd.g16.server.exceptions.NotFoundOnServerException;

import pt.ubi.sd.g16.shared.*;

import static java.util.Collections.addAll;

public class ServerImp extends UnicastRemoteObject implements ServerInterface {
	private ArrayList<News> news_list; // ArrayList com todas as notícias
	private ArrayList<Topic> topics_list; // ArrayList com todos os tópicos
	private int limit_topic; // Limite de notícias por tópico antes de serem enviadas para o servidorBackup

	private static final String pathConfig = "config.json";
	private static final String pathData = System.getProperty("user.dir") + File.separator + "data";
	private static final String pathNews = pathData + File.separator + "news";
	private static final String pathTopics = pathData + File.separator + "topics";
	private static final String pathUsers = pathData + File.separator + "users";
	private static final String backupServerIP = "127.0.0.1:1200";

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
		Files.createDirectories((Paths.get(pathUsers))); // Cria pasta para utilizadores
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

	public void updateTopic(Topic t) throws IOException { // Guarda ficheiro topic na pasta topics
		File dir = new File(pathTopics);
		String filenameTopico = t.getId() + ".json"; // Escrita do tópico para um ficheiro
		File ft = new File(dir, filenameTopico);
		FileWriter fout;
		fout = new FileWriter(ft);
		fout.write(t.serialize()); // transforma-o numa string Gson e escreve para um ficheiro.
		fout.flush();
		fout.close();
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

	public News readNews(String filename) { // Lê ficheiro news da pasta news e transforma em objecto
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

	public Topic readTopic(String filename) { // Lê ficheiro topic da pasta news e transforma em objecto
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

	public boolean loadTopic() {                // Carrega todos os ficheiros topic para o arrayList
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

	public boolean loadNews() {                // Carrega todos os ficheiros notícias para o arrayList
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

	public ArrayList<Account> loadAccount(){
		ArrayList<Account> AccountList = new ArrayList<>();
		File folder = new File(pathUsers);
		File[] listOfFiles = folder.listFiles();
		String[] fileAux;
		Account user;

		try {
			for (File file : listOfFiles) {
				if (file.isFile()) {
					fileAux = file.getName().split("\\."); // Separa o nome do ficheiro da extensão
					if (Objects.equals(fileAux[1], "json")) {
						user = readAccount(file.getName()); // Lê o ficheiro para uma notícia
						AccountList.add(user); // Adiciona a notícia à lista
					}
				}
			}
			if (!AccountList.isEmpty())
				return AccountList;
			else
				return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

//---------------- PUBLISHER ----------------

	public void addTopic(String id_topic, String title_topic, String desc_topic) throws IOException, Topic.TopicIDTakenException {  // P1, recebe id, título e descrição para criar um tópico novo.
		Topic new_t = new Topic(id_topic, title_topic, desc_topic); // Cria um objecto do tipo tópico
		topics_list.add(new_t); // Adiciona à lista de tópicos
		updateConfig(); // Actualiza o ficheiro de configuração

		updateTopic(new_t);

	}

	public ArrayList<Topic> getTopics() { // P2, devolve a arrayList com os tópicos
		return topics_list;
	} // P2, devolve lista tópicos

	public ArrayList<String> checkForNotifications(UUID idUser) throws IOException {
			String filename = idUser + ".json";
			Subscriber sub = (Subscriber) readAccount(filename);
			ArrayList<String> notif_list = new ArrayList<>();
			notif_list.addAll(sub.getNotificationsList());
			sub.getNotificationsList().clear(); // Remove notificações do subscriber
			saveAccount(sub); // Guarda subscriber
			return notif_list;
	}

	public void notify(String idTopic) throws IOException { // Inicia processo de notificar os subscribers
		Account aux;
		ArrayList<Account> AccountList = loadAccount(); // Carrega todas as contas para uma lista
		String notification = "";
		Topic t_aux = null;
		for (Topic t_a : topics_list) {    // Procura o título do tópico
			if (t_a.getId() == idTopic)
				notification = "There are unread news in topic " + t_aux.getTitle(); // Cria a mensagem de notificação
		}
		if (AccountList != null) { // Se existirem contas de utilizador
			for (Account user : AccountList) {
				if (user.getTopicIDList().contains(idTopic)) ; // Vai buscar aqueles que estão subscritos ao tópico
				aux = readAccount((user.getUsername() + ".json"));
				if (!aux.getNotificationsList().contains(notification)) { // Verifica se já não foi notificado
					Subscriber sub = (Subscriber) user;
					sub.addNotification(notification);
					saveAccount(sub);
				}
			}
		}
	}

	public void addNews(News n) throws IOException { // P3, adiciona uma notícia ao servidor
		String idTopic = n.getTopic().getId(); // Obtem o id do tópico da notícia
		news_list.add(n); // Adiciona a notícia à lista de notícias
		UUID news_id;
		News aux = null;
		updateNews(n); // Actualiza o ficheiro notícia
		Topic taux = null;

		for (int i = 0; i < topics_list.size() ; i++) {  // Actualiza o tópico da notícia e a lista de tópicos
			if (Objects.equals(topics_list.get(i).getId(), idTopic)) {
				taux = topics_list.get(i);
				taux.addNews(n);

				notify(taux.getId()); // Tenta notificar todos subscribers deste tópico

				if (topics_list.get(i).getNewsIDStock().size() > limit_topic) { // Verifica se a quantidade de notícias ultrapassa o limite
					int tam = topics_list.get(i).getNewsIDStock().size(); // Quantidade de notícias em stock do tópico pretendido
					int count = 0;
					while (count < tam / 2) { // Itera metade para ser movido para o backup
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

public ArrayList<News> getNewsInsideTimeInterval(String idTopic, Date startDate, Date finalDate) throws NotFoundOnServerException { // C2, procura notícias entre o intervalo de tempo
	ArrayList<UUID> ln = new ArrayList<>();
	ArrayList<News> toSend = new ArrayList<>();
	for(Topic t : topics_list) {
		if (t.getId().equals(idTopic)) {
			ln = t.getNewsIDStock(); // Salva os ID's de todas as notícias do tópico pretendido em ln
			break;
		}
	}
	for(UUID nID : ln){
		for(News nList : news_list ){ // Verifica se as notícias do tópico pretendido estão entre as duas datas
			if(nList.getId() == nID && ( nList.getDate().after(startDate)  && nList.getDate().before(finalDate) )){
				toSend.add(nList);
			}
		}
	}
	if(toSend.isEmpty()){ // Caso estejam vazias levanta um exceção
		throw new NotFoundOnServerException();
	}
	return toSend; // Retorna ArrayList com todas as notícias satisfatórias ao pedido.
}

public String getBackupIP(){ // Retorna o endereço ip do servidor de backup
	return backupServerIP;
}
public News getLastNewsTopic(String idTopic){ // C3, envia a notícia mais recente de x tópico
		UUID ln = null;
	for(Topic t : topics_list){
		if (Objects.equals(t.getId(), idTopic)) {
			ln = t.getNewsIDStock().get(t.getNewsIDStock().size() - 1);
			break;
		}
	}
	if (ln != null)
		return readNews(ln + ".json");
	return null;
}
//-------------- LOGIN I/O-----------------------

public void saveAccount(Account a) throws IOException { // Guarda objecto account em ficheiro, na pasta users
	File dir = new File(pathUsers);
	String filenameUser = a.getUsername() + ".json"; // Escrita do User para um ficheiro
	File f = new File(dir, filenameUser);
	FileWriter fout;
	fout = new FileWriter(f);
	fout.write(a.serialize()); // transforma-o numa string Gson e escreve para um ficheiro.
	fout.flush();
	fout.close();
}

public Account readAccount(String filename){ // Transforma ficheiro account em objecto account
	File dir = new File(pathUsers); // Pasta
	File f = new File(dir, filename); // Nome do ficheiro
	Account a = null;
	try (FileReader fr = new FileReader(f)) { // Lê uma string Gson e transfere o seu conteúdo para variáveis
		char[] chars = new char[(int) f.length()];
		fr.read(chars);
		String GsonLine = new String(chars);
		a = new Account(GsonLine);  // Cria novo tópico com os conteúdos lidos
	} catch (IOException e) {
		e.printStackTrace();
	}
	return a;
}

//-------------- LOGIN -----------------------

public ArrayList<Login> open_login_file(){
	ArrayList<Login> dados = new ArrayList<Login>();

	File t = new File("login.txt");

	try {
		FileInputStream fis = new FileInputStream(t);
		ObjectInputStream oIs = new ObjectInputStream(fis);

		//colocar topicos ja disponiveis no list
		//colocar noticas no ArrayList para serem manipuladas
		while(oIs.read()!=(-1)){
			dados.add((Login)oIs.readObject());
		}

		oIs.close();
	}catch(IOException | ClassNotFoundException e) {
		System.out.println(e.getMessage());
	}
	return dados;
}
	//after
	public void update_login_file(Login l) {

		File f = new File("login.txt");

		try {
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oOs = new ObjectOutputStream(fos);

			oOs.writeObject(l);

			oOs.close();

		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}

	public boolean user_login(String username, String password) throws RemoteException {
		ArrayList<Login> users = open_login_file();

		for(int i=0; i<users.size(); i++) {

			//verifies if username is in file
			if(users.get(i).getUsername().equals(username)) {

				//compares the secure_pwd stored in file with the secure password created using the pwd and the salt that was used to secure the original password
				if(users.get(i).getSecuredPassword().equals(SecurePassword(password, users.get(i).getSalt()))){

					System.out.println("Login was successful\n");
					return true;
					//login successful
				}else {
					System.out.println("Incorrect password\n");
					return false;
				}
			}else {
				System.out.println("Incorrect username\n");
				return false;
			}
		}
		return false;
	}

	private static String SecurePassword(String password, byte[] salt){
		String generatedPassord = null;
		try{
			MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
			msgDigest.update(salt);
			byte[] bytes = msgDigest.digest(password.getBytes());

			StringBuilder sb = new StringBuilder();
			for (byte aByte : bytes) {
				sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassord = sb.toString();
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return generatedPassord;
	}
	public void register_user(String username, String password, String confirm_pwd) throws RemoteException {
		try {
			if(password.equals(confirm_pwd)) {
				Login l = new Login(username, password);
				update_login_file(l);
				System.out.println("Resgistration successful\n");
				user_login(username, password);
			}else {
				System.out.println("Resgistration unsuccessful\n");
			}
		}catch(NoSuchProviderException | NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		}
	}
}