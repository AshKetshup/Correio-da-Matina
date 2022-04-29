package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Article;
import com.ashketshup.Landmark.TUI.StringStyler;
import pt.ubi.sd.g16.server.ServerInterface;
import pt.ubi.sd.g16.shared.News;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LastNewsFromTopicArticle {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    public static Article generate(String topicID, ScreenManager screenManager, ServerInterface serverInterface) throws IOException {
        LastNewsFromTopicArticle.screenManager = screenManager;
        LastNewsFromTopicArticle.serverInterface = serverInterface;

        News news = serverInterface.getLastNewsFromTopic(topicID);
        String[] lines = new String(news.getContent()).split("\n");

        List<StringStyler> stylers = Arrays
            .stream(lines)
            .map(StringStyler::new)
            .collect(Collectors.toList());

        return new Article(news.getTitle(), stylers, screenManager);
    }
}
