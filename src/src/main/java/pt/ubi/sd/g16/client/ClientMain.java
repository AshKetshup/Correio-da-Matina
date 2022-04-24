package pt.ubi.sd.g16.client;

import com.ashketshup.Landmark.Screens.Form;
import com.ashketshup.Landmark.Screens.Menu;
import com.ashketshup.Landmark.UIElements.Component;
import com.ashketshup.Landmark.UIElements.Option;
import com.ashketshup.Landmark.Navigation;
import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.TUI.Notifications;

import pt.ubi.sd.g16.server.ServerInterface;

import java.rmi.*;
import java.util.Arrays;

public class ClientMain extends java.rmi.server.UnicastRemoteObject implements ClientInterface {
    public ClientMain() throws RemoteException{
        super();
    }
    public static void main(String[] args) {
        ScreenManager sM = new ScreenManager();
        Navigation nav = new Navigation(sM, 5);

        declareScreens(sM);

        sM.bindScreen(sM.getMenu("welcome_menu"));

        System.setSecurityManager(new SecurityManager());
        try {
            ServerInterface si = (ServerInterface) Naming.lookup("CorreioDaMatina");
        } catch( java.rmi.NotBoundException | java.net.MalformedURLException | java.rmi.RemoteException e ) {
            System.out.println("Exception in client" + e.getMessage());

            System.exit(1);
        }

        nav.loop();
    }

    public static void declareScreens(ScreenManager sM) {
        sM.addForm(
            "login_form",
            new Form(
                "Login Form",
                Arrays.asList(
                    new Component("Username", false, true),
                    new Component("Password", true, true)
                ),
                components -> {
                    boolean isRequiredEmpty = components.stream().filter(Component::isRequired).allMatch(Component::isAnswerEmpty);
                    if (isRequiredEmpty) {
                        Notifications.createWarning("Required camps are not filled.");
                    } else {
                        /* TODO: onConfirmation */
                    }
                },
                sM
            )
        );

        sM.addForm(
            "register_form",
            new Form(
                "Register Form",
                Arrays.asList(
                    new Component("[Subscriber / Publisher]", false, true),
                    new Component("Username", false, true),
                    new Component("Password", true, true),
                    new Component("Confirm Password", true, true)
                ),
                components -> {
                    boolean isRequiredEmpty = components.stream().filter(Component::isRequired).allMatch(Component::isAnswerEmpty);
                    if (isRequiredEmpty) {
                        Notifications.createWarning("Required camps are not filled.");
                    } else {
                        /* TODO: onConfirmation */
                    }
                },
                sM
            )
        );

        sM.addMenu(
            "welcome_menu",
            new Menu(
                "Welcome to Correio da Matina",
                Arrays.asList(
                    
                ),
                sM
            )
        );
    }
}
