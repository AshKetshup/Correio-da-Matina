package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Menu;
import com.ashketshup.Landmark.TUI.Notifications;
import com.ashketshup.Landmark.UIElements.Option;
import pt.ubi.sd.g16.client.Session;
import pt.ubi.sd.g16.server.ServerInterface;

import java.rmi.RemoteException;
import java.util.List;

public class PublisherMenu {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    private static void createTopic() {
        screenManager.bindScreen(screenManager.getForm("create_topic_form"));
    }

    private static void createNews() {
        screenManager.bindScreen(screenManager.getForm("create_news_form"));
    }

    private static void consultTopics() {
        try {
            screenManager.bindScreen(AllTopicsMenu.generate(screenManager, serverInterface));
        } catch (RemoteException e) {
            Notifications.createCritical(e.getMessage());
        }
    }

    private static void consultNews() {
        screenManager.bindScreen(screenManager.getForm("consult_news_form"));
    }

    public static Menu generate(ScreenManager screenManager, ServerInterface serverInterface) {
        PublisherMenu.screenManager = screenManager;
        PublisherMenu.serverInterface = serverInterface;

        return new Menu(
            "Welcome" + Session.getSessionAccount().getID() + "!",
            List.of(
                new Option("Create Topic", PublisherMenu::createTopic),
                new Option("Create News", PublisherMenu::createNews),
                new Option("Consult Topics", PublisherMenu::consultTopics),
                new Option("Consult News", PublisherMenu::consultNews )
            ),
            screenManager
        );
    }
}
