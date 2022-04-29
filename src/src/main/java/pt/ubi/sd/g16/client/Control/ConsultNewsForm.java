package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Form;
import com.ashketshup.Landmark.TUI.Notifications;
import com.ashketshup.Landmark.UIElements.Command;
import com.ashketshup.Landmark.UIElements.Component;
import pt.ubi.sd.g16.server.ServerInterface;
import pt.ubi.sd.g16.server.exceptions.NotFoundOnServerException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

public class ConsultNewsForm {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    private static void control(Collection<Component> components) {
        List<Component> componentList = (List<Component>) components;

        try {
            screenManager.bindScreen(
                NewsMenu.generate(
                    componentList.get(0).getAnswer(),
                    componentList.get(1).getAnswer(),
                    componentList.get(2).getAnswer(),
                    screenManager,
                    serverInterface
                )
            );
        } catch (ParseException e) {
            Notifications.createWarning("Given date doesn't correspond to the asked format [dd/MM/yyyy]");
        } catch (NotFoundOnServerException e) {
            Notifications.createTip(
                "Weren't found any News from this time period on our main server. So we tried on the Backup."
            );

            try {
                screenManager.bindScreen(
                    NewsMenu.generate(
                        componentList.get(0).getAnswer(),
                        componentList.get(1).getAnswer(),
                        componentList.get(2).getAnswer(),
                        e.getIp(),
                        screenManager,
                        serverInterface
                    )
                );
            } catch (ParseException ex) {
                Notifications.createWarning("Given date doesn't correspond to the asked format [dd/MM/yyyy]");
            } catch (NotFoundOnServerException ex) {
                Notifications.createWarning("There are no News from the given Date interval");
            } catch (IOException | ClassNotFoundException ex) {
                Notifications.createCritical(ex.getMessage());
            }
        } catch (RemoteException e) {
            Notifications.createCritical(e.getMessage());
        }
    }

    public static Form generate(ScreenManager screenManager, ServerInterface serverInterface) {
        ConsultNewsForm.screenManager = screenManager;
        ConsultNewsForm.serverInterface = serverInterface;

        return new Form(
            "Consult News",
            List.of(
                new Component("Start Date [dd/MM/yyyy]", false, false),
                new Component("End Date [dd/MM/yyyy]", false, false),
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
            ConsultNewsForm::control,
            screenManager
        );
    }
}
