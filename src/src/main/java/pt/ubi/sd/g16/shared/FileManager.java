package pt.ubi.sd.g16.shared;

import java.io.File;

public class FileManager {
    public static final String PATH_CONFIG = "config.json";
	public static final String PATH_DATA   = System.getProperty("user.dir") + File.separator + "data";
	public static final String PATH_NEWS   = PATH_DATA + File.separator + "news";
	public static final String PATH_TOPICS = PATH_DATA + File.separator + "topics";
	public static final String PATH_USERS  = PATH_DATA + File.separator + "users";
}
