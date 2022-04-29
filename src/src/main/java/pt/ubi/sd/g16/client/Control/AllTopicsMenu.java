package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Menu;
import com.ashketshup.Landmark.UIElements.Option;
import pt.ubi.sd.g16.server.ServerInterface;

import java.rmi.RemoteException;
import java.util.stream.Collectors;

public class AllTopicsMenu {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    public static Menu generate(ScreenManager screenManager, ServerInterface serverInterface) throws RemoteException {
        AllTopicsMenu.screenManager = screenManager;
        AllTopicsMenu.serverInterface = serverInterface;

        return new Menu(
            "Check All Topics",
            serverInterface.getAllTopics().stream().map(
                x -> new Option(x.getId() + "\t" + x.getTitle() + "\t" + x.getDescription())
            ).collect(Collectors.toList()),
            screenManager
        );
    }
}
