package pt.ubi.sd.g16.server.Server;
import com.google.code.gson;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;

import pt.ubi.sd.g16.shared.News;
import pt.ubi.sd.g16.shared.Settings;
import pt.ubi.sd.g16.shared.Topic;

public class Server_news_impl extends UnicastRemoteObject implements Server_news_interface {
	ArrayList<News> news_list; // ArrayList com todas as notícias
	ArrayList<Topic> topics_list; // ArrayList com todos os tópicos
	int limit_topic; // Limite de notícias por tópico antes de serem enviadas para o servidorBackup

	private static final String pathConfig = "config.json";
	int topics_size; // Quantidade de tópicos
	int news_size; // Quantidade de notícias

	public Server_news_impl() throws IOException {
		super();
		news_list = new ArrayList<News>();
		topics_list = new ArrayList<Topic>();

		loadConfig(); // inicializa as variáveis topics_size, news_size e limit_topic
	}

//---------------- Operações de I/O:   ----------------

	public void loadConfig() throws IOException{ // Inicializar configurações
		File f = new File(pathConfig);
		Settings config = new Settings(); // Iniciar class configurações
		try {
			if(!f.isFile()) {  // Caso o ficheiro de configurações não exista
				FileWriter fout;
				fout = new FileWriter(f);
				config.setNews_size(0);
				config.setTopics_size(0);
				config.setLimit_topics(16);
				fout.write(config.serialize()); // Cria um objecto Settings com as configurações pretendidas,
				fout.flush();					// transforma-o numa string Gson e escreve para um ficheiro.
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
		limit_topic = config.getLimit_topics();
		news_size = config.getNews_size();
		topics_size = config.getTopics_size();
	}

	public void updateConfig() throws IOException{
		File f = new File(pathConfig);
		Settings config = new Settings(); // Iniciar objecto configurações configurações
		config.setNews_size(news_size);
		config.setTopics_size(topics_size);
		config.setLimit_topics(limit_topic);
		try{
			FileWriter fout;
			fout = new FileWriter(f);
			fout.write(config.serialize()); // Cria um objecto Settings com as configurações pretendidas,
			fout.flush();					// transforma-o numa string Gson e escreve para um ficheiro.
			fout.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	// FUNÇÕES POR FAZER:
	// ler todas as noticias para news_list

	// ler todos os topicos para topico_list

	// actualizar topico x

	// mover noticia x

//---------------- Métodos Publishers: ----------------

	public void newTopic(String id_topic, String title_topic, String desc_topic) throws IOException {  // P1, recebe id, título e descrição para criar um tópico novo.
		Topic new_t = new Topic(id_topic,title_topic,desc_topic); // Cria um objecto do tipo tópico
		topics_list.add(new_t); // Adiciona à lista de tópicos
		topics_size += 1;
		updateConfig(); // Actualiza o ficheiro de configuração

		String filename = new_t.getId() + ".json";
		File f = new File(filename);
		FileWriter fout;
		fout = new FileWriter(f);
		fout.write(new_t.serialize()); // transforma-o numa string Gson e escreve para um ficheiro.
		fout.flush();
		fout.close();

	}

	public ArrayList<Topic> getTopics(){ // P2, devolve a arrayList com os tópicos
		return topics_list;
	}

	public void addNews(News n) throws IOException { // P3, adiciona uma notícia ao servidor
		String id_topico = n.getTopic().getId(); // Obtem o id do tópico da notícia
		news_list.add(n); // Adiciona a notícia à lista de notícias
		news_size += 1;

		String filenameNews = id_topico + "_" + n.getId() + ".json"; // Escrita da News para um ficheiro
		File f = new File(filenameNews);
		FileWriter fout;
		fout = new FileWriter(f);
		fout.write(n.serialize()); // transforma-o numa string Gson e escreve para um ficheiro.
		fout.flush();
		fout.close();

		for(int i = 0; i < topics_size; i++){  // Actualiza o tópico da notícia e a lista de tópicos
			if(Objects.equals(topics_list.get(i).getId(), id_topico)) {
				topics_list.get(i).addNews(n);

				if (topics_list.get(i).getNewsIDStock().size() > limit_topic)

					// Enviar 50% para backup
					// Actualizar ArrayList dos topicos

					// Actualizar topico i
					break;
			}
		}

		String filenameTopico = id_topico + ".json"; // Escrita do tópico para um ficheiro
		File ft = new File(filenameTopico);
		FileWriter ftout;
		fout = new FileWriter(ft);
		fout.write(n.getTopic().serialize()); // transforma-o numa string Gson e escreve para um ficheiro.
		fout.flush();
		fout.close();

		updateConfig(); // Actualiza ficheiro de configuração

		// Actualizar topico n.getTopico()
	}

	public ArrayList<News> getNews(){
		return news_list;
	}

//---------------- ARRAYLIST<NOTICIAS> ----------------

	@Override
	public void open_files() throws RemoteException {
		String filename;

		for(int i = 0; i < top_size; i++) {
			filename = top[i] + ".txt";

			File f = new File(filename);
			try {
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream oIs = new ObjectInputStream(fis);

				//colocar noticas no ArrayList para serem manipuladas
				while(oIs.read() != -1){
					news_list.add((News)oIs.readObject());
				}

				oIs.close();
			}catch(IOException | ClassNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}

		//return noticias;
	}

	@Override
	public void update_files() throws RemoteException {
		String filename;

		//TERMINAR!!!

		for(int i=0; i<top_size;i++) {
			filename = top[i] + ".txt";

			File f = new File(filename);
			try {
				FileOutputStream fos = new FileOutputStream(f);
				ObjectOutputStream oOs = new ObjectOutputStream(fos);

				//oOs.writeObject(oo1);

				oOs.close();

				//colocar noticas no ArrayList para serem manipuladas
				//while(oIs.read()!=(-1)){
				//noticias.add((Noticia)oIs.readObject());
				//}

				//oIs.close();
			}catch(IOException e){// | ClassNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}

		//return noticias;
	}

	@Override
	public ArrayList<News> consultar() throws RemoteException {
		open_files();
		return news_list;
	} //?

	@Override
	public void add_news_topic(News n) throws RemoteException { // Adiciona notícia ao tópico
		news_list.add(n);
		update_files();
	}

	@Override
	public ArrayList<News> getTopic() throws RemoteException {

		//declaração tópico que tem que vai ser a noticia
		String topico = null;


		ArrayList<News> aux = new ArrayList<News>();
		System.out.println("Escolha o tópico da sua noticia dos seguintes apresentados: \n");
		for(int i=0; i<top_size; i++) {
			System.out.println((i+1) + " - " + top[i]);
		}

		//escolha do topico
		do {
			int e = Read.aInt();
			for(int i=0; i<top_size; i++) {
				if(i == (e-1)) {
					topico = top[i];
				}
			}
		}while(topico.equals(null));

		String filename = topico + ".txt";

		File f = new File(filename);
		try {
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream oIs = new ObjectInputStream(fis);

			//colocar noticas no ArrayList para serem manipuladas
			while(oIs.read()!=(-1)){
				aux.add((Noticia)oIs.readObject());
			}

			oIs.close();
		}catch(IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		return aux;
	}


	//---------------- NOTICIA -----------------------
	@Override
	public boolean create_topic() throws RemoteException {
		//boolean existe = true;

		String topico = Read.aString();
		int i=0;

		//verificar se o topico já existe e adicionar caso não
		if(top[i].isEmpty()) {
			top[i] = topico;
			top_size = i+1;
			return true;

		} else {
			for(i=0; i<top.length; i++) {
				if(top[i].contains(topico)) {
					System.out.println("Tópico já existe, re-introduza o topico");

					topico = Read.aString();

					//reinicia a verificaçãos
					i=0;
				}
			}
		}

		top[i]=topico;
		top_size = i+1;
		return true;

	}


	@Override
	public void abrir_fich_topico() throws RemoteException {
		File t = new File("topicos.txt");

		int count=0;
		try {
			FileInputStream fis = new FileInputStream(t);
			ObjectInputStream oIs = new ObjectInputStream(fis);

			//colocar topicos ja disponiveis no list
			while(oIs.read()!=(-1)){
				top[count] = (String)oIs.readObject();
				count++;
			}

			//tamanho da lista
			top_size = count;

			oIs.close();
		}catch(IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		//return top;
	}


	@Override
	public void criar_noticia(String produtor) throws RemoteException {

		abrir_fich_topico();
		if(top.length == 0) {
			System.out.println("Não existem topicos criados, por favor crie o primeiro tópico:\n");
			criar_topico();
		}else {
			System.out.println("Escolha o tópico da sua noticia dos seguintes apresentados: \n");
			for(int i=0; i<top_size; i++) {
				System.out.println((i+1) + " - " + top[i]);
			}
		}

		//declaração tópico que tem que vai ser a noticia
		String topico = null;

		//declaração da variável que vai conter texto da noticia
		char[] carateres;

		//escolha do topico
		do {
			int e = Read.aInt();
			for(int i=0; i<top_size; i++) {
				if(i == (e-1)) {
					topico = top[i];
				}
			}
		}while(topico.equals(null));

		carateres = texto_noticia();

		n = new Noticia(topico, produtor);
		n.setCarateres(carateres);

	}

	@Override
	public char[] texto_noticia() throws RemoteException {
		char[] carateres = new char[180];

		System.out.println("Introduza o texto da sua noticia. Tenha atenção para não ultrapassar o limite de 180 carateres, que inclui os espaços");


		//TERMINAR
		//como delimitar ou terminar a introdução da noticia????
		carateres = (Read.aString()).toCharArray();

		return null;
	}

//----------- NOTIFICAÇÃO DE PUBLICAÇÃO DE NOTICIA A PESSOAS SUBSCRITAS A UM CERTO TÓPICO


	//TERMINAR
	//callback -> investigar isso!!!!
	@Override
	public void notificar() throws RemoteException {
		// TODO Auto-generated method stub

	}

//------------------- BACKUP -------------------



	//deve ser automatico a forma como o backup/forma como a transição das noticias acontece!!
	@Override
	public ArrayList<Noticia> backup() throws RemoteException {


		return null;
	}

	//-------------- LOGIN -----------------------
	public ArrayList<login> abrir_login(){
		return null;
	}


	@Override
	public boolean user_login(String username, String password) throws RemoteException {

		return false;
	}

	@Override
	public void registar_user(String username, String password, String confirm_pwd) throws RemoteException {
		// TODO Auto-generated method stub

	}