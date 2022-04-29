package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Article;
import com.ashketshup.Landmark.Screens.Menu;
import com.ashketshup.Landmark.TUI.StringStyler;
import com.ashketshup.Landmark.UIElements.Option;
import pt.ubi.sd.g16.server.ServerInterface;

import java.rmi.RemoteException;
import java.util.stream.Collectors;

public class AllTopicsArticle {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    public static Article generate(ScreenManager screenManager, ServerInterface serverInterface) throws RemoteException, IndexOutOfBoundsException {
        AllTopicsArticle.screenManager = screenManager;
        AllTopicsArticle.serverInterface = serverInterface;
        return new Article(
            "Check All Topics",
            serverInterface.getAllTopics().stream().map(
                x -> new StringStyler(x.getId() + "\t" + x.getTitle() + "\t" + x.getDescription())
            ).collect(Collectors.toList()),
            screenManager
        );
    }
}
