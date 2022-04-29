package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Form;
import com.ashketshup.Landmark.TUI.Notifications;
import com.ashketshup.Landmark.UIElements.Command;
import com.ashketshup.Landmark.UIElements.Component;
import pt.ubi.sd.g16.client.Session;
import pt.ubi.sd.g16.server.ServerInterface;
import pt.ubi.sd.g16.shared.Subscriber;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

public class SubscribeTopicForm {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    private static void control(Collection<Component> components) {
        List<Component> componentList = (List<Component>) components;

        String topicID = componentList.get(0).getAnswer();
        try {
            Subscriber subscriber = (Subscriber) serverInterface.getAccountFromID(Session.getSessionAccount().getID());
            subscriber.subscribeTopic(topicID);

            Notifications.createValid("Topic '"+ topicID +"' subscribed ");
        } catch (RemoteException e) {
            Notifications.createCritical(e.getMessage());
        }
    }

    public static Form generate(ScreenManager screenManager, ServerInterface serverInterface) {
        return new Form(
            "Subscribe a Topic",
            List.of(new Component("TopicID", false, true)),
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
            SubscribeTopicForm::control,
            screenManager
        );
    }
}
