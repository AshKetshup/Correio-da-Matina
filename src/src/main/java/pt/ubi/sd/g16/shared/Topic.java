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
    private final ArrayList<UUID> newsIDStock = new ArrayList<>();
    private boolean archivedNews;

    public Topic(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.archivedNews = false;

        TOPIC_HASH_MAP.put(id, this);
    }

    public Topic(String jsonLine) {
        Topic x = new Gson().fromJson(jsonLine, Topic.class);

        this.id = x.getId();
        this.title = x.getTitle();
        this.description = x.getDescription();
        this.newsIDStock.addAll(x.getNewsIDStock());
        this.archivedNews = x.isArchivedNews();

        TOPIC_HASH_MAP.put(id, this);
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

    public ArrayList<UUID> getNewsIDList() {
        return newsIDList;
    }

    public ArrayList<UUID> getNewsIDStock() {
        return newsIDStock;
    }

    public boolean isArchivedNews() {
        return archivedNews;
    }

    public void setArchivedNews(boolean archivedNews) {
        this.archivedNews = archivedNews;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addNews(News news) {
        newsIDStock.add(news.getId());
    }

    public boolean delNewsStock(UUID newsID){ return newsIDList.remove(newsID);}

    public void addNews(UUID newsID) {
        newsIDStock.add(newsID);
    }

    public String serialize() {
        return new Gson().toJson(this);
    }


    }
