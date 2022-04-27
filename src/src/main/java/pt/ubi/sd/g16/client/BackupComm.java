package pt.ubi.sd.g16.client;

import pt.ubi.sd.g16.shared.News;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class BackupComm {

    public ArrayList<News> getNews() { // Devolve a lista de notícias no servidor de backup
        ArrayList<News> arrayNews = null;
        try {
            Socket s;
            s = new Socket("127.0.0.1", 1200);
            ObjectInputStream is = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
            os.writeObject(0); // <- ReceiveNews(n) método
            os.flush();
            arrayNews = (ArrayList<News>) is.readObject();
            is.close();
            s.close();
        }catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return arrayNews;
    }

    public ArrayList<String> seekTopics(){ // Devolve a lista de tópicos (IDs) no servidor de backup
        ArrayList<String> arrayTopics = null;
        try{
            Socket s;
            s = new Socket("127.0.0.1", 1200);
            ObjectInputStream is = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
            os.writeObject(1); // <- ReceiveNews(n) método
            os.flush();
            arrayTopics = (ArrayList<String>) is.readObject();
            is.close();
            s.close();
        }catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return arrayTopics;
    }

    public ArrayList<News> getNews_Topic(String idTopic){ // Recebe o id do tópico desejado, devolve uma lista de todas as notícias nesse tópico
        ArrayList<News> arrayNews = null;
        try{
            Socket s;
            s = new Socket("127.0.0.1", 1200);
            ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
            os.writeObject(2); // <- ReceiveNews(n) método
            os.flush();
            os.writeObject(idTopic); // <- String com id do tópico a enviar
            ObjectInputStream is = new ObjectInputStream(s.getInputStream());
            arrayNews = (ArrayList<News>) is.readObject();
            is.close();
            s.close();
        }catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return arrayNews;
    }
}
