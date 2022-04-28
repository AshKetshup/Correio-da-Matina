package pt.ubi.sd.g16.client;

import pt.ubi.sd.g16.shared.News;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class BackupComm {
    // Devolve a lista de notícias no servidor de backup
    public static ArrayList<News> getNews(String ip) throws IOException, ClassNotFoundException {
        String[] ip_split = ip.split(":");

        Socket s = new Socket(ip_split[0], Integer.parseInt(ip_split[1]));
        ObjectInputStream is = new ObjectInputStream(s.getInputStream());
        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
        os.writeObject(0); // <- ReceiveNews(n) método
        os.flush();
        ArrayList<News> arrayNews = (ArrayList<News>) is.readObject();
        is.close();
        s.close();
        return arrayNews;
    }

    // Devolve a lista de tópicos (IDs) no servidor de backup
    public static ArrayList<String> seekTopics(String ip) throws IOException, ClassNotFoundException {
        String[] ip_split = ip.split(":");

        Socket s = new Socket(ip_split[0], Integer.parseInt(ip_split[1]));
        ObjectInputStream is = new ObjectInputStream(s.getInputStream());
        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
        os.writeObject(1); // <- ReceiveNews(n) método
        os.flush();
        ArrayList<String> arrayTopics = (ArrayList<String>) is.readObject();
        is.close();
        s.close();

        return arrayTopics;
    }

    // Recebe o id do tópico desejado, devolve uma lista de todas as notícias nesse tópico
    public static ArrayList<News> getNewsTopic(String ip, String idTopic) throws IOException, ClassNotFoundException {
        String[] ip_split = ip.split(":");
        Socket s = new Socket(ip_split[0], Integer.parseInt(ip_split[1]));
        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
        os.writeObject(2); // <- ReceiveNews(n) método
        os.flush();
        os.writeObject(idTopic); // <- String com id do tópico a enviar
        ObjectInputStream is = new ObjectInputStream(s.getInputStream());
        ArrayList<News> arrayNews = (ArrayList<News>) is.readObject();
        is.close();
        s.close();

        return arrayNews;
    }
}
