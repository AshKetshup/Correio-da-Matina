package pt.ubi.sd.g16.shared;

import com.google.gson.*;

public class Settings {
    private int limit_topics;
    private int news_size;
    private int topics_size;

    public Settings(){
        this.limit_topics = 0;
        this.news_size = 0;
    }

    public int getTopics_size() {
        return topics_size;
    }

    public void setTopics_size(int topics_size) {
        this.topics_size = topics_size;
    }

    public int getLimit_topics() {
        return limit_topics;
    }

    public void setLimit_topics(int limit_topics) {
        this.limit_topics = limit_topics;
    }

    public int getNews_size() {
        return news_size;
    }

    public void setNews_size(int news_size) {
        this.news_size = news_size;
    }

    public String serialize() {
        return new Gson().toJson(this);
    }

    public void deserialize(String GsonLine) {
        Settings aux = new Gson().fromJson(GsonLine,Settings.class);
        this.limit_topics = aux.limit_topics;
        this.news_size = aux.news_size;
        this.topics_size = aux.topics_size;
    }
}