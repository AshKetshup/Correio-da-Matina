package pt.ubi.sd.g16.shared;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
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

	@Override
	public String toString() {
		return title + "\t" + topic.getTitle() + "\t" + publisher.getUsername() + "\t" + getNewsDate() + "\n";
	}

	public String serialize() {
		return new Gson().toJson(this);
	}

	public static News getNewsFromID(UUID newsID) {
		return NEWS_HASH_MAP.get(newsID);
	}

	public boolean delNewsFile() { // Apaga ficheiro da notícia
        File dir = new File(PATH_NEWS);
        File f = new File(dir, getId() + ".json");
        return f.delete();
    }

	public News popFromID(UUID newsID) throws Exception {
        // Guardar numa variavel a Noticia vinda do ID
        News toPop = getNewsFromID(newsID);

		// Tentar apagar dos publishers
		if (!toPop.publisher.deleteNewsID(newsID))
			throw new Exception(); // TODO: Criar Exception custom por não existir Noticia no Publisher

		// Tentar apagar dos topicos
		if (!toPop.topic.deleteNewsID(newsID))
			throw new Exception(); // TODO: Criar Exception custom por não existir Noticia no Topico

		// Tenta apagar o ficheiro
		if (!toPop.delNewsFile())
			throw new Exception(); // TODO: Criar Exceoption custom por não conseguir eliminar ficheiro

		// Apagar do registo da HashMap
		NEWS_HASH_MAP.remove(newsID);

		return toPop;
    }
}