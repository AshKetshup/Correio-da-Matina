package pt.ubi.sd.g16.server;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

import pt.ubi.sd.g16.server.exceptions.NotFoundOnServerException;

import pt.ubi.sd.g16.shared.*;
import pt.ubi.sd.g16.shared.Exceptions.*;

import static pt.ubi.sd.g16.shared.FileManager.PATH_USERS;

public class ServerImp extends UnicastRemoteObject implements ServerInterface {
    // Limite de notícias por tópico antes de serem enviadas para o servidorBackup
	private int limit_topic;

	private static final String pathConfig = "config.json";
	private static final String pathData = System.getProperty("user.dir") + File.separator + "data";
	private static final String pathNews = pathData + File.separator + "news";
	private static final String pathTopics = pathData + File.separator + "topics";
	private static final String pathUsers = pathData + File.separator + "users";
	private static final String backupServerIP = "127.0.0.1:1200";

	public ServerImp() throws IOException {
		super();

		News.load();
		Topic.load();
		Account.load();

		loadConfig(); // inicializa as variáveis e limit_topic

		Files.createDirectories((Paths.get(pathNews))); // Cria pasta data/news
		Files.createDirectories((Paths.get(pathTopics))); // Cria pasta data/topics
		Files.createDirectories((Paths.get(pathUsers))); // Cria pasta para utilizadores
	}

	public void loadConfig() { // Inicializar configurações
		Settings config = Settings.load();

		limit_topic = config.getLimit_topics(); // Actualiza os valores das configs.
	}

    public ArrayList<String> checkForNotifications(UUID idUser) throws IOException {
			String filename = idUser + ".json";
            File file = new File(new File(PATH_USERS), filename);

			Subscriber sub = (Subscriber) Account.read(file);
			ArrayList<String> notif_list = new ArrayList<>(sub.getNotificationsList()); // Copiar para um novo Array
			sub.getNotificationsList().clear();                                         // Remove notificações do subscriber
			sub.save();                                                                 // Guarda subscriber
			return notif_list;
	}

    // Inicia processo de notificar os subscribers
	public void notify(String idTopic) throws IOException {
		Account aux = null;
		Account.load();                     // Carrega todas as contas
		Topic t_aux = Topic.getTopicFromID(idTopic);
        String notification = "There are unread news in topic " + t_aux.getTitle();

        for (Account user : Account.getAccountHashMap().values()) {
            if (user.getTopicIDList().contains(idTopic)) {
                aux = Account.read(new File(new File(PATH_USERS), user.getID() + ".json"));

                if (!aux.getNotificationsList().contains(notification)) {
                    Subscriber sub = (Subscriber) user;
                    sub.addNotification(notification);
                    sub.save();
                }
            }
        }
	}

    // region Login
	public Account login(String username, String password) throws RemoteException, NoSuchAlgorithmException, WrongPasswordException {
        return Account.login(username, password);
	}
    // endregion

    // region Add
    public void addAccount(Account account) throws IOException {
        account.save();
    }

    // P3, adiciona uma notícia ao servidor
	public void addNews(News news) throws IOException, FailedDeleteException {
		// Actualiza o ficheiro notícia
		news.save();

		Topic topicAux = Topic.getTopicFromID(news.getTopic().getId());
		topicAux.addNews(news);

        // Tenta notificar todos os subscribers deste topico
        notify(topicAux.getId());
        if (topicAux.getNewsIDList().size() > limit_topic)
            for (int i = 0; i < topicAux.getNewsIDList().size() / 2; i++) {
                UUID newsID = topicAux.getNewsIDList().get(0);

                if (newsID != null) {
                    backupNews(newsID);

                    topicAux.deleteNewsID(topicAux.getNewsIDList().get(0));
                }
            }

		topicAux.save(); // Actualiza o ficheiro de tópicos
	}

    // P1, recebe id, título e descrição para criar um tópico novo.
	public void addTopic(Topic topic) throws IOException {
		topic.save();
	}
    // endregion

    // region Create
    public Account createAccount(String username, String password, String passwordConfirm, int rank)
        throws RemoteException, PasswordNotMatchingException, UsernameTakenException, NoSuchAlgorithmException {

        return new Account(username, password, passwordConfirm, rank);
    }

    public News createNews(String title, char[] content, String topicID, Publisher publisher)
        throws RemoteException, NullPointerException, ArrayIndexOutOfBoundsException {

        return new News(title, content, topicID, publisher);
    }

    public Topic createTopic(String id, String title, String description)
        throws RemoteException, TopicIDTakenException {

        return new Topic(id, title, description);
    }
    // endregion

    // region Get
    public Topic getTopicFromID(String topicID) throws RemoteException {
        return Topic.getTopicFromID(topicID);
    }

    public Topic getTopicFromNews(News news) throws RemoteException {
        return news.getTopic();
    }

    public ArrayList<Topic> getAllTopics() throws RemoteException {
        return new ArrayList<>(Topic.getTopicHashMap().values());
    }

    public News getNewsFromID(UUID newsID) throws RemoteException {
        return News.getNewsFromID(newsID);
    }

    public ArrayList<News> getAllNews() throws RemoteException {
        Collection<News> x = News.getNewsHashMap().values();

        ((List<News>) x).sort( (o1, o2) -> o1.getDate().compareTo(o2.getDate()) );

        return new ArrayList<>(x);
    }

    public ArrayList<News> getNewsFromTopic(Topic topic) throws RemoteException {
        return topic.getNewsIDList()
            .stream()
            .map(News::getNewsFromID)
            .sorted((o1, o2) -> o1.getDate().compareTo(o2.getDate()))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Account> getAllAccounts() throws RemoteException {
        return new ArrayList<>(Account.getAccountHashMap().values());
    }

    public Account getAccountFromID(String accountID) throws RemoteException {
        return Account.getAccountFromID(accountID);
    }

    // Retorna o endereço ip do servidor de backup
	public String getBackupIP() throws RemoteException {
        return backupServerIP;
    }

    // C3, envia a notícia mais recente de x tópico
	public News getLastNewsFromTopic(String idTopic) throws IOException, IndexOutOfBoundsException {
		ArrayList<News> x = getNewsFromTopic(Topic.getTopicFromID(idTopic));

        return x.get(x.size() - 1);
	}

     // C2, procura notícias entre o intervalo de tempo
    public ArrayList<News> getNewsInsideTimeInterval(String idTopic, Date startDate, Date finalDate)
            throws RemoteException, NotFoundOnServerException {
        ArrayList<News> toSend = new ArrayList<>();
        ArrayList<UUID> newsIDList = Topic.getTopicFromID(idTopic).getNewsIDList();

        for(UUID newsID : newsIDList) {
            News n = News.getNewsFromID(newsID);

            if (n.getDate().after(startDate) && n.getDate().before(finalDate))
                toSend.add(n);
        }

        // Caso estejam vazias levanta um exceção
        if (toSend.isEmpty())
            throw new NotFoundOnServerException();

        // Retorna ArrayList com todas as notícias satisfatórias ao pedido.
        return toSend;
    }
    // endregion

    // Envia só para o servidor de backup
    public void backupNews(UUID newsID) throws IOException, FailedDeleteException {
        Socket s = new Socket("127.0.0.1", 1200);
        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

        os.writeObject(3);
        os.flush();
        os.writeObject(News.popFromID(newsID));
        os.flush();

        os.close();
        s.close();
	}
}