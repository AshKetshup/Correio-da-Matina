package pt.ubi.sd.g16.shared;

import com.google.gson.*;

public class Settings {
    private int limit_topics;

    public Settings(){
        this.limit_topics = 16;
    }

    public int getLimit_topics() {
        return limit_topics;
    }

    public void setLimit_topics(int limit_topics) {
        this.limit_topics = limit_topics;
    }

    public String serialize() {
        return new Gson().toJson(this);
    }

    public void deserialize(String GsonLine) {
        Settings aux = new Gson().fromJson(GsonLine,Settings.class);
        this.limit_topics = aux.limit_topics;
    }
}