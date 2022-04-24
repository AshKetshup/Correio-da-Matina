package pt.ubi.sd.g16.shared;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Topic implements Serializable {
    private static final HashMap<String, Topic> TOPIC_HASH_MAP = new HashMap<>();

    private final String id;
    private String title;
    private String description;
    private final ArrayList<UUID> newsIDList = new ArrayList<>();

    public static class TopicIDTakenException extends Exception {
        public TopicIDTakenException() {
            super("This TopicID is already taken.");
        }
    }

    public Topic(String id, String title, String description) throws TopicIDTakenException {
        if (Topic.isIDUsed(id))
			throw new TopicIDTakenException();

        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Topic(String jsonLine) {
        Topic x = new Gson().fromJson(jsonLine, Topic.class);

        this.id = x.getId();
        this.title = x.getTitle();
        this.description = x.getDescription();
        this.newsIDList.addAll(x.getNewsIDStock());
    }

    public static Topic getTopicFromID(String topicID) throws NullPointerException {
        Topic x = TOPIC_HASH_MAP.get(topicID);

        if (x == null)
            throw new NullPointerException();

        return x;
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

    public ArrayList<UUID> getNewsIDStock() {
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

    private static boolean isIDUsed(String username) {
		return TOPIC_HASH_MAP.containsKey(username);
	}

    public String serialize() {
        return new Gson().toJson(this);
    }
}