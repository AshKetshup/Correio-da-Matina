package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Form;
import com.ashketshup.Landmark.TUI.Notifications;
import com.ashketshup.Landmark.UIElements.Component;
import pt.ubi.sd.g16.client.Session;
import pt.ubi.sd.g16.server.ServerInterface;
import pt.ubi.sd.g16.shared.Exceptions.WrongPasswordException;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LoginForm {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    private static void control(Collection<Component> components) {
        List<Component> componentList = (List<Component>) components;

        if (components.stream().filter(Component::isRequired).allMatch(Component::isAnswerEmpty)) {
            Notifications.createWarning("Required camps are not filled.");
        } else {
            try {
                Session.setSessionAccount(
                    componentList.get(0).getAnswer(),
                    componentList.get(1).getAnswer(),
                    serverInterface
                );
            } catch (NoSuchAlgorithmException | RemoteException | WrongPasswordException e) {
                Notifications.createCritical(e.getMessage());
            }

            String username = Session.getSessionAccount().getID();
            int rank = Session.getSessionAccount().getRank();
            switch (rank) {
                case 0:
                    screenManager.bindScreen(SubscriberMenu.generate(screenManager, serverInterface));
                    break;
                case 1:
                    screenManager.bindScreen(PublisherMenu.generate(screenManager, serverInterface));
                    break;
            }
        }
    }

    public static Form generate(ScreenManager screenManager, ServerInterface serverInterface) {
        LoginForm.screenManager = screenManager;
        LoginForm.serverInterface = serverInterface;

        return new Form(
            "Login",
            Arrays.asList(
                new Component("Username", false, true),
                new Component("Password", true, true)
            ),
            LoginForm::control,
            screenManager
        );
    }
}
