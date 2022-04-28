package pt.ubi.sd.g16.shared;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import pt.ubi.sd.g16.shared.Exceptions.FailedDeleteException;

import java.io.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.UUID;

import static pt.ubi.sd.g16.shared.FileManager.PATH_NEWS;

public class News implements Serializable {
	private static final HashMap<UUID, News> NEWS_HASH_MAP = new HashMap<>();

	private final UUID id;
	private final String title;
    private final char[] content = new char[180];
	private final Topic topic;
	private final Publisher publisher;
	private Date date;
	private final static String datePattern = "dd-mm-yyyy hh:mm:ss";

	public News(String title, char[] content, String topicID, Publisher publisher) throws NullPointerException, ArrayIndexOutOfBoundsException {
		this.date = new Date();
		this.id = UUID.randomUUID();

		this.title = title;
		setContent(content);
		this.topic = Topic.getTopicFromID(topicID);
		this.publisher = publisher;

		NEWS_HASH_MAP.put(id, this);
	}

	public News(String jsonLine) throws JsonSyntaxException {
		News x = new Gson().fromJson(jsonLine, News.class);

		this.id = x.getId();
		this.title = x.getTitle();
		setContent(x.getContent());
		this.topic = x.getTopic();
		this.publisher = x.getPublisher();
		this.date = x.getDate();

		NEWS_HASH_MAP.put(this.id, this);
	}

	public UUID getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public Topic getTopic() {
		return topic;
	}

	public char[] getContent() {
		return content;
	}

	public String getTitle() {
		return title;
	}

	public void setDateFromString(String dateString) throws ParseException {
		this.date = new SimpleDateFormat(datePattern).parse(dateString);
	}

	public void setContent(char[] newContent) throws ArrayIndexOutOfBoundsException {
		for (int i = 0; i < newContent.length; i++)
			this.content[i] = newContent[i];
	}

	public String getNewsDate() {
		return new SimpleDateFormat(datePattern).format(date);
	}

	public static News getNewsFromID(UUID newsID) {
		return NEWS_HASH_MAP.get(newsID);
	}

	@Override
	public String toString() {
		String stringTitle = ((title.length() > 30) ? title.substring(0, 30) : title);
		String stringTopic = ((topic.getTitle().length() > 15) ? topic.getTitle().substring(0, 15) : topic.getTitle());
		String stringPublisher = ((publisher.getID().length() > 10) ? publisher.getID().substring(0, 10) : publisher.getID());

		return id.toString() + "\t" + stringTitle + "\t" + stringTopic + "\t" + stringPublisher + "\t" + getNewsDate();
	}

	// region "Read, Load and Save"
	public static void read(File file) throws IOException {
		FileReader fileReader = new FileReader(file);
		char[] content = new char[(int) file.length()];
		fileReader.read(content);

		new News(new String(content));
	}

	public static boolean load() throws FileNotFoundException {
		File folder = new File(PATH_NEWS);
		File[] listOfFiles = folder.listFiles();

		if (listOfFiles == null) {
			System.out.println("No news to load.");
			return false;
		}

		try {
			for (File file : listOfFiles)
				// Caso seja um ficheiro com extensão json
				if (file.isFile() && file.getName().endsWith(".json"))
					read(file);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}

		return true;
	}

	public void save() throws IOException {
		File fileDir = new File(PATH_NEWS);
		String fileName = this.id + ".json";
		File file = new File(fileDir, fileName);

		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(this.serialize());
		fileWriter.flush();
		fileWriter.close();
	}
	// endregion

	public String serialize() {
		return new Gson().toJson(this);
	}

	public boolean delNewsFile() { // Apaga ficheiro da notícia
        File dir = new File(PATH_NEWS);
        File f = new File(dir, getId() + ".json");
        return f.delete();
    }

	public static HashMap<UUID, News> getNewsHashMap() { return NEWS_HASH_MAP; }

	public static News popFromID(UUID newsID) throws FailedDeleteException {
        // Guardar numa variavel a Noticia vinda do ID
        News toPop = getNewsFromID(newsID);

		// Tentar apagar dos publishers || topicos || ficheiros
		if (!toPop.publisher.deleteNewsID(newsID) || !toPop.topic.deleteNewsID(newsID) || !toPop.delNewsFile())
			throw new FailedDeleteException();

		// Apagar do registo da HashMap
		NEWS_HASH_MAP.remove(newsID);

		return toPop;
    }
}