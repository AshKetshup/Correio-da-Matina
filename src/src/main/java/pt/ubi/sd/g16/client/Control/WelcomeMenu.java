package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Menu;
import com.ashketshup.Landmark.UIElements.Option;
import pt.ubi.sd.g16.server.ServerInterface;

import java.util.List;

public class WelcomeMenu {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    private static void login() {
        screenManager.bindScreen(screenManager.getForm("login_form"));
    }

    private static void register() {
        screenManager.bindScreen(screenManager.getForm("register_form"));
    }

    private static void continueAsGuest() {
        screenManager.bindScreen(screenManager.getForm("guest_menu"));
    }

    public static Menu generate(ScreenManager screenManager, ServerInterface serverInterface) {
        WelcomeMenu.screenManager = screenManager;
        WelcomeMenu.serverInterface = serverInterface;

        return new Menu(
            "Welcome to Correio da Matina",
            List.of(
                new Option(
                    "Login",
                    WelcomeMenu::login
                ),
                new Option(
                    "Register",
                    WelcomeMenu::register
                ),
                new Option(
                    "Continue as Guest",
                    WelcomeMenu::continueAsGuest
                )
            ),
            screenManager
        );
    }
}
