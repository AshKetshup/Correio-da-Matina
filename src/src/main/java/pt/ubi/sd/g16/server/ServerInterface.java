package pt.ubi.sd.g16.server;

import pt.ubi.sd.g16.shared.News;
import pt.ubi.sd.g16.shared.Topic;

import java.io.IOException;
import java.util.ArrayList;

public interface ServerInterface extends java.rmi.Remote{

    void loadConfig() throws IOException;

    void updateConfig() throws IOException;

     void delNewsFile(News n) throws IOException;

     void backupNews(News n) throws java.rmi.RemoteException;

     void updateTopic(Topic t) throws IOException;

     void updateNews(News n) throws IOException;

     News readNews(String filename) throws IOException;

     Topic readTopic(String filename) throws IOException;

     boolean loadTopic() throws IOException;

     boolean loadNews() throws IOException;

     void addTopic(String id_topic, String title_topic, String desc_topic) throws IOException, Topic.TopicIDTakenException;

     ArrayList<Topic> getTopics() throws java.rmi.RemoteException;

     void addNews(News n) throws IOException;

     ArrayList<News> getNews() throws java.rmi.RemoteException;
}