package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Menu;
import com.ashketshup.Landmark.TUI.Notifications;
import com.ashketshup.Landmark.UIElements.Option;
import pt.ubi.sd.g16.client.NotificationThread;
import pt.ubi.sd.g16.client.Session;
import pt.ubi.sd.g16.server.ServerInterface;

import java.rmi.RemoteException;
import java.util.List;

public class SubscriberMenu {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    private static void subscribeTopic() {
        screenManager.bindScreen(screenManager.getForm("subscribe_topic_form"));
    }

    private static void consultNews() {
        screenManager.bindScreen(screenManager.getForm("consult_news_form"));
    }

    private static void consultMostRecentFromTopics() {
        screenManager.bindScreen(screenManager.getForm("consult_last_from_topic_form"));
    }

    public static Menu generate(ScreenManager screenManager, ServerInterface serverInterface) {

        SubscriberMenu.screenManager = screenManager;
        SubscriberMenu.serverInterface = serverInterface;

        return new Menu(
            "Welcome " + Session.getSessionAccount().getID() + "!",
            List.of(
                new Option("Subscribe Topic", SubscriberMenu::subscribeTopic),
                new Option("Consult News", SubscriberMenu::consultNews),
                new Option("Consult Most Recent News From Topic", SubscriberMenu::consultMostRecentFromTopics)
            ),
            screenManager
        );
    }
}
