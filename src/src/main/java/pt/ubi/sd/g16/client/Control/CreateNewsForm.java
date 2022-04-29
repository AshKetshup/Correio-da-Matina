package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Form;
import com.ashketshup.Landmark.TUI.Notifications;
import com.ashketshup.Landmark.UIElements.Command;
import com.ashketshup.Landmark.UIElements.Component;
import pt.ubi.sd.g16.client.Session;
import pt.ubi.sd.g16.server.ServerInterface;
import pt.ubi.sd.g16.shared.Exceptions.FailedDeleteException;
import pt.ubi.sd.g16.shared.Publisher;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

public class CreateNewsForm {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    private static void control(Collection<Component> components) {
        List<Component> componentList = (List<Component>) components;

        if (components.stream().filter(Component::isRequired).allMatch(Component::isAnswerEmpty)) {
            Notifications.createWarning("Required camps are not filled.");
        } else {
            try {
                serverInterface.addNews(
                    serverInterface.createNews(
                        componentList.get(0).getAnswer(),
                        componentList.get(1).getAnswer().toCharArray(),
                        componentList.get(2).getAnswer(),
                        (Publisher) Session.getSessionAccount()
                    )
                );

                Notifications.createValid("News '"+ componentList.get(0).getAnswer() +"' created");
            } catch (IOException | FailedDeleteException e) {
                Notifications.createCritical(e.getMessage());
            }
        }
    }

    public static Form generate(ScreenManager screenManager, ServerInterface serverInterface) {
        CreateNewsForm.screenManager = screenManager;
        CreateNewsForm.serverInterface = serverInterface;

        return new Form(
            "Create News",
            List.of(
                new Component("Title", false, true),
                new Component("Content", false, true),
                new Component("TopicID", false, false)
            ),
            List.of(
                // Commando para ver quais os topicos disponives
                new Command(":ct", "Check Topics", () -> {
                    try {
                        screenManager.bindScreen(AllTopicsMenu.generate(screenManager, serverInterface));
                    } catch (RemoteException e) {
                        Notifications.createCritical(e.getMessage());
                    }
                })
            ),
            CreateNewsForm::control,
            screenManager
        );
    }

}
