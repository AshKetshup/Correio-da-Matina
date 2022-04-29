package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Menu;
import com.ashketshup.Landmark.UIElements.Option;
import pt.ubi.sd.g16.server.ServerInterface;

import java.util.List;

public class GuestMenu {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    private static void consultNews() {
        screenManager.bindScreen(screenManager.getForm("consult_news_form"));
    }

    private static void consultLastNewsFromTopic() {
        screenManager.bindScreen(screenManager.getForm("consult_last_from_topic_form"));
    }

    public static Menu generate(ScreenManager screenManager, ServerInterface serverInterface) {
        GuestMenu.screenManager = screenManager;
        GuestMenu.serverInterface = serverInterface;

        return new Menu(
            "Guest Menu",
            List.of(
                new Option(
                    "Consult News",
                    GuestMenu::consultNews
                ),
                new Option(
                    "Consult Last News from a Topic",
                    GuestMenu::consultLastNewsFromTopic
                )
            ),
            screenManager
        );
    }
}
