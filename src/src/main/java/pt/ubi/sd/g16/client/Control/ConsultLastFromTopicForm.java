package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Form;
import com.ashketshup.Landmark.TUI.Notifications;
import com.ashketshup.Landmark.UIElements.Command;
import com.ashketshup.Landmark.UIElements.Component;
import pt.ubi.sd.g16.server.ServerInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConsultLastFromTopicForm {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    private static void control(Collection<Component> components) {
        if (components.stream().filter(Component::isRequired).allMatch(Component::isAnswerEmpty))
            Notifications.createWarning("Required camps are not filled.");
        else {
            Component component = new ArrayList<>(components).get(0);
            try {
                screenManager.bindScreen(LastNewsFromTopicArticle.generate(component.getAnswer(), screenManager, serverInterface));
            } catch (IOException e) {
                Notifications.createCritical(e.getMessage());
            }
        }
    }

    public static Form generate(ScreenManager screenManager, ServerInterface serverInterface) {
        ConsultLastFromTopicForm.screenManager = screenManager;
        ConsultLastFromTopicForm.serverInterface = serverInterface;

        return new Form(
            "Consult Last News from a Topic",
            List.of(
                // Input do ID do Topico
                new Component("TopicID", false, true)
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
            ConsultLastFromTopicForm::control,
            screenManager
        );
    }
}
