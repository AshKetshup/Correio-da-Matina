package pt.ubi.sd.g16.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Topic implements Serializable {
    private static final HashMap<String, Topic> topicHashMap = new HashMap<>();

    private final String id;
    private String title;
    private String description;
    private final ArrayList<UUID> newsIDList = new ArrayList<>();
    private final ArrayList<UUID> newsIDStock = new ArrayList<>();

    public Topic(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public static Topic getTopicFromID(String topicID) throws NullPointerException {
        Topic x = topicHashMap.get(topicID);

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

    public ArrayList<UUID> getNewsIDList() {
        return newsIDList;
    }
    
    public ArrayList<UUID> getNewsIDStock() {
        return newsIDStock;
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
}
