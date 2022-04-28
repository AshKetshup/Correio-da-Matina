package pt.ubi.sd.g16.shared;

import com.google.gson.Gson;
import pt.ubi.sd.g16.shared.Exceptions.TopicIDTakenException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static pt.ubi.sd.g16.shared.FileManager.PATH_TOPICS;

public class Topic implements Serializable {
    private static final HashMap<String, Topic> TOPIC_HASH_MAP = new HashMap<>();

    private final String id;
    private String title;
    private String description;
    private final ArrayList<UUID> newsIDList = new ArrayList<>();

    public Topic(String id, String title, String description) throws TopicIDTakenException {
        if (Topic.isIDUsed(id))
			throw new TopicIDTakenException();

        this.id = id;
        this.title = title;
        this.description = description;

        TOPIC_HASH_MAP.put(id, this);
    }

    public Topic(String jsonLine) {
        Topic x = new Gson().fromJson(jsonLine, Topic.class);

        this.id = x.getId();
        this.title = x.getTitle();
        this.description = x.getDescription();
        this.newsIDList.addAll(x.getNewsIDList());

        TOPIC_HASH_MAP.put(id, this);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<UUID> getNewsIDList() {
        return newsIDList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addNews(News news) {
        newsIDList.add(news.getId());
    }

    public void addNews(UUID newsID) {
        newsIDList.add(newsID);
    }

    public boolean deleteNewsID(UUID newsID) {
		return newsIDList.remove(newsID);
	}

    public String serialize() {
        return new Gson().toJson(this);
    }

	// region "Read, Load and Save"
	public static void read(File file) throws IOException {
		FileReader fileReader = new FileReader(file);
		char[] content = new char[(int) file.length()];
		fileReader.read(content);

		new Topic(Arrays.toString(content));
	}

	public static boolean load() throws FileNotFoundException {
		File folder = new File(PATH_TOPICS);
		File[] listOfFiles = folder.listFiles();

		if (listOfFiles == null)
			throw new FileNotFoundException();

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
		File fileDir = new File(PATH_TOPICS);
		String fileName = this.id + ".json";
		File file = new File(fileDir, fileName);

		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(this.serialize());
		fileWriter.flush();
		fileWriter.close();
	}
	// endregion

    public static HashMap<String, Topic> getTopicHashMap() {
        return TOPIC_HASH_MAP;
    }

    public static Topic getTopicFromID(String topicID) throws NullPointerException {
        Topic x = TOPIC_HASH_MAP.get(topicID);

        if (x == null)
            throw new NullPointerException();

        return x;
    }

    private static boolean isIDUsed(String username) {
		return TOPIC_HASH_MAP.containsKey(username);
	}
}
