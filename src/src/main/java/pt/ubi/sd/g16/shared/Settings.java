package pt.ubi.sd.g16.shared;

import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import static pt.ubi.sd.g16.shared.FileManager.PATH_CONFIG;

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
        Settings aux = new Gson().fromJson(GsonLine, Settings.class);
        this.limit_topics = aux.limit_topics;
    }

    public void read(File file) throws IOException {
        FileReader fr = new FileReader(file); // Lê uma string Gson e transfere o seu conteúdo para variáveis
        char[] chars = new char[(int) file.length()];
        fr.read(chars);
        this.deserialize(Arrays.toString(chars));
    }

    public static Settings load() {
        File file = new File(PATH_CONFIG);
        Settings config = new Settings();
        try {
            if (!file.isFile()) {
                FileWriter fout = new FileWriter(file);
				config.setLimit_topics(16);
				fout.write(config.serialize()); // Cria um objecto Settings com as configurações pretendidas,
				fout.flush();                    // transforma-o numa string Gson e escreve para um ficheiro.
				fout.close();
            } else
                config.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }
}