package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Form;
import com.ashketshup.Landmark.TUI.Notifications;
import com.ashketshup.Landmark.UIElements.Component;
import pt.ubi.sd.g16.server.ServerInterface;
import pt.ubi.sd.g16.shared.Exceptions.PasswordNotMatchingException;
import pt.ubi.sd.g16.shared.Exceptions.UsernameTakenException;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RegisterForm {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    private static void control(Collection<Component> components) {
        List<Component> componentList = (List<Component>) components;

        if (components.stream().filter(Component::isRequired).allMatch(Component::isAnswerEmpty))
            Notifications.createWarning("Required camps are not filled.");
        else {
            try {
                serverInterface.createAccount(
                    componentList.get(1).getAnswer(),
                    componentList.get(2).getAnswer(),
                    componentList.get(3).getAnswer(),
                    Integer.parseInt(
                        componentList.get(0).getAnswer()
                    )
                );

                Notifications.createValid("Account '"+ componentList.get(0).getAnswer() +"' created");
                screenManager.unbindLastScreen();
            } catch (PasswordNotMatchingException | UsernameTakenException e) {
                Notifications.createWarning(e.getMessage());
            } catch (NoSuchAlgorithmException | RemoteException e) {
                Notifications.createCritical(e.getMessage());
            }
        }
    }

    public static Form generate(ScreenManager screenManager, ServerInterface serverInterface) {
        RegisterForm.screenManager = screenManager;
        RegisterForm.serverInterface = serverInterface;

        return new Form(
            "Register",
            Arrays.asList(
                new Component("Subscriber / Publisher [0 / 1]", false, true),
                new Component("Username", false, true),
                new Component("Password", true, true),
                new Component("Confirm Password", true, true)
            ),
            RegisterForm::control,
            screenManager
        );
    }
}
