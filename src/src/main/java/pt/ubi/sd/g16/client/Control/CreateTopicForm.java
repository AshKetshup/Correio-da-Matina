package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Form;
import com.ashketshup.Landmark.TUI.Notifications;
import com.ashketshup.Landmark.UIElements.Command;
import com.ashketshup.Landmark.UIElements.Component;
import pt.ubi.sd.g16.server.ServerInterface;
import pt.ubi.sd.g16.shared.Exceptions.TopicIDTakenException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

public class CreateTopicForm {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    private static void control(Collection<Component> components) {
        List<Component> componentList = (List<Component>) components;

        if (components.stream().filter(Component::isRequired).allMatch(Component::isAnswerEmpty))
            Notifications.createWarning("Required camps are not filled.");
        else {
            try {
                serverInterface.addTopic(
                    serverInterface.createTopic(
                        componentList.get(0).getAnswer(),
                        componentList.get(1).getAnswer(),
                        componentList.get(2).getAnswer()
                    )
                );

                Notifications.createValid("Topic '"+ componentList.get(0).getAnswer() +"' created");
            } catch (IOException e) {
                Notifications.createCritical(e.getMessage());
            } catch (TopicIDTakenException e) {
                Notifications.createWarning(e.getMessage());
            }
        }
    }


    public static Form generate(ScreenManager screenManager, ServerInterface serverInterface) {
        CreateTopicForm.screenManager = screenManager;
        CreateTopicForm.serverInterface = serverInterface;

        return new Form(
            "Create Topic",
            List.of(
                new Component("ID", false, true),
                new Component("Title", false, true),
                new Component("Description", false, false)
            ),
            List.of(
                // Commando para ver quais os topicos disponives
                new Command(":ct", "Check Topics", () -> {
                    try {
                        screenManager.bindScreen(AllTopicsArticle.generate(screenManager, serverInterface));
                    } catch (RemoteException e) {
                        Notifications.createCritical(e.getMessage());
                    }
                })
            ),
            CreateTopicForm::control,
            screenManager
        );
    }
}
