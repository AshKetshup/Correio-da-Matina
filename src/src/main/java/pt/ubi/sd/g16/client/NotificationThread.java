package pt.ubi.sd.g16.client;

import com.ashketshup.Landmark.TUI.Notifications;
import pt.ubi.sd.g16.server.ServerInterface;

import java.io.IOException;
import java.util.ArrayList;

public class NotificationThread implements Runnable {
    ServerInterface serverInterface;

    public NotificationThread(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (Session.getSessionAccount() != null && Session.getSessionAccount().getRank() == 0) {
                    ArrayList<String> notifications = serverInterface.checkForNotifications(Session.getSessionAccount().getID());
                    for (String notification : notifications)
                        Notifications.createTip(notification);
                }

                Thread.sleep(15000);
            }
        } catch (IOException | InterruptedException e) {
            Notifications.createCritical(e.getMessage());
        }
    }
}
