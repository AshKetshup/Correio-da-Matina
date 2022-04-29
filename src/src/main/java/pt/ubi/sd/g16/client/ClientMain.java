package pt.ubi.sd.g16.client;

import com.ashketshup.Landmark.TUI.Input;
import com.ashketshup.Landmark.TUI.Output;
import com.ashketshup.Landmark.Navigation;
import com.ashketshup.Landmark.ScreenManager;
import pt.ubi.sd.g16.client.Control.*;
import pt.ubi.sd.g16.server.ServerInterface;

import java.net.InetAddress;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain extends java.rmi.server.UnicastRemoteObject {
    private static ServerInterface serverInterface;
    private static ScreenManager screenManager;
    private static String serverName = "";

    public ClientMain() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        screenManager = new ScreenManager();
        Navigation nav = new Navigation(screenManager, 10);

        if (args.length == 0) {
            try {
                serverName = Input.readString("Please insert server domain or IP (Empty for localhost):");
                if (serverName.isEmpty())
                    serverName = InetAddress.getLocalHost().getHostName();
            } catch (java.net.UnknownHostException e) {
                Output.writeln(e.getMessage());
            }
        } else
            serverName = args[0];

        System.setSecurityManager(new SecurityManager());

        try {
            Registry registry = LocateRegistry.getRegistry(serverName, 1099);
            serverInterface = (ServerInterface) registry.lookup("CorreioDaMatina");
        } catch(NotBoundException | RemoteException e ) {
            System.out.println("Exception in client" + e.getMessage());

            System.exit(1);
        }

        Thread notif = new Thread(new NotificationThread(serverInterface));
        if(!notif.isAlive())
            notif.start();

        declareScreens();

        screenManager.bindScreen(screenManager.getMenu("welcome_menu"));

        nav.loop();
    }

    public static void declareScreens() {
        // region login_form
        screenManager.addForm(
            "login_form",
            LoginForm.generate(screenManager, serverInterface)
        );
        // endregion

        // region register_form
        screenManager.addForm(
            "register_form",
            RegisterForm.generate(screenManager, serverInterface)
        );
        // endregion

        // region consult_last_from_topic_form
        screenManager.addForm(
            "consult_last_from_topic_form",
            ConsultLastFromTopicForm.generate(screenManager, serverInterface)
        );
        // endregion

        // region consult_news_form
        screenManager.addForm(
            "consult_news_form",
            ConsultNewsForm.generate(screenManager, serverInterface)
        );
        // endregion

        // region create_topic_form
        screenManager.addForm(
            "create_topic_form",
            CreateTopicForm.generate(screenManager, serverInterface)
        );
        // endregion

        // region create_topic_form
        screenManager.addForm(
            "create_news_form",
            CreateNewsForm.generate(screenManager, serverInterface)
        );
        // endregion

        // region subscribe_topic_form
        screenManager.addForm(
            "subscribe_topic_form",
            SubscribeTopicForm.generate(screenManager, serverInterface)
        );
        // endregion

        // region guest_menu
        screenManager.addMenu(
            "guest_menu",
            GuestMenu.generate(screenManager, serverInterface)
        );
        // endregion

        // region welcome_menu
        screenManager.addMenu(
            "welcome_menu",
            WelcomeMenu.generate(screenManager, serverInterface)
        );
        // endregion
    }
}
