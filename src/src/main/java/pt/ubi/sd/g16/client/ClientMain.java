package pt.ubi.sd.g16.client;

import com.ashketshup.Landmark.Screens.Article;
import com.ashketshup.Landmark.Screens.Form;
import com.ashketshup.Landmark.Screens.Menu;
import com.ashketshup.Landmark.TUI.StringStyler;
import com.ashketshup.Landmark.UIElements.Command;
import com.ashketshup.Landmark.UIElements.Component;
import com.ashketshup.Landmark.Navigation;
import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.TUI.Notifications;
import com.ashketshup.Landmark.UIElements.Option;
import pt.ubi.sd.g16.server.ServerInterface;
import pt.ubi.sd.g16.server.exceptions.NotFoundOnServerException;
import pt.ubi.sd.g16.shared.Exceptions.*;
import pt.ubi.sd.g16.shared.News;
import pt.ubi.sd.g16.shared.Publisher;
import pt.ubi.sd.g16.shared.Subscriber;

import java.io.IOException;
import java.rmi.*;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ClientMain extends java.rmi.server.UnicastRemoteObject {
    private static ServerInterface si;
    private static ScreenManager screenManager;
    static Session session;


    public ClientMain() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        screenManager = new ScreenManager();
        Navigation nav = new Navigation(screenManager, 10);

        declareScreens();

        screenManager.bindScreen(screenManager.getMenu("welcome_menu"));

        System.setSecurityManager(new SecurityManager());
        try {
            si = (ServerInterface) Naming.lookup("CorreioDaMatina");
        } catch( java.rmi.NotBoundException | java.net.MalformedURLException | java.rmi.RemoteException e ) {
            System.out.println("Exception in client" + e.getMessage());

            System.exit(1);
        }

        nav.loop();
    }

    public static void declareScreens() {
        // region login_form
        screenManager.addForm(
            "login_form",
            new Form(
                "Login",
                Arrays.asList(
                    new Component("Username", false, true),
                    new Component("Password", true, true)
                ),
                components -> {
                    List<Component> componentList = (List<Component>) components;

                    if (components.stream().filter(Component::isRequired).allMatch(Component::isAnswerEmpty)) {
                        Notifications.createWarning("Required camps are not filled.");
                    } else {
                        try {
                            session = new Session(
                                componentList.get(0).getAnswer(),
                                componentList.get(1).getAnswer(),
                                si
                            );
                        } catch (NoSuchAlgorithmException | RemoteException | WrongPasswordException e) {
                            Notifications.createCritical(e.getMessage());
                        }

                        String username = session.getSessionAccount().getID();
                        int rank = session.getSessionAccount().getRank();
                        switch (rank) {
                            case 0:
                                screenManager.bindScreen(generateSubscriberMenu(username));
                                break;
                            case 1:
                                screenManager.bindScreen(generatePublisherMenu(username));
                                break;
                        }
                    }
                },
                screenManager
            )
        );
        // endregion

        // region register_form
        screenManager.addForm(
            "register_form",
            new Form(
                "Register",
                Arrays.asList(
                    new Component("Subscriber / Publisher [0 / 1]", false, true),
                    new Component("Username", false, true),
                    new Component("Password", true, true),
                    new Component("Confirm Password", true, true)
                ),
                components -> {
                    List<Component> componentList = (List<Component>) components;

                    if (components.stream().filter(Component::isRequired).allMatch(Component::isAnswerEmpty)) {
                        Notifications.createWarning("Required camps are not filled.");
                    } else {
                        try {
                            si.createAccount(
                                componentList.get(1).getAnswer(),
                                componentList.get(1).getAnswer(),
                                componentList.get(1).getAnswer(),
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
                },
                screenManager
            )
        );
        // endregion

        // region consult_last_from_topic_form
        screenManager.addForm(
            "consult_last_from_topic_form",
            new Form(
                "Consult Last News from a Topic",
                List.of(
                    // Input do ID do Topico
                    new Component("TopicID", false, true)
                ),
                List.of(
                    // Commando para ver quais os topicos disponives
                    new Command(":ct", "Check Topics", () -> {
                        try {
                            screenManager.bindScreen(generateAllTopics());
                        } catch (RemoteException e) {
                            Notifications.createCritical(e.getMessage());
                        }
                    })
                ),
                components -> {
                    if (components.stream().filter(Component::isRequired).allMatch(Component::isAnswerEmpty))
                        Notifications.createWarning("Required camps are not filled.");
                    else {
                        Component component = new ArrayList<>(components).get(0);
                        try {
                            screenManager.bindScreen(generateLastNewsFromTopic(component.getAnswer()));
                        } catch (IOException e) {
                            Notifications.createCritical(e.getMessage());
                        }
                    }
                },
                screenManager
            )
        );
        // endregion

        // region consult_news_form
        screenManager.addForm(
            "consult_news_form",
            new Form(
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
                            screenManager.bindScreen(generateAllTopics());
                        } catch (RemoteException e) {
                            Notifications.createCritical(e.getMessage());
                        }
                    })
                ),
                components -> {
                    List<Component> componentList = (List<Component>) components;
                    try {
                        screenManager.bindScreen(
                            generateMenuNews(
                                componentList.get(0).getAnswer(),
                                componentList.get(1).getAnswer(),
                                componentList.get(2).getAnswer()
                            )
                        );
                    } catch (ParseException e) {
                        Notifications.createWarning("Given date doesn't correspond to the asked format [dd/MM/yyyy]");
                    } catch (NotFoundOnServerException e) {
                        Notifications.createWarning("Given topicID doesn't exist on the server");
                    } catch (RemoteException e) {
                        Notifications.createCritical(e.getMessage());
                    }
                },
                screenManager
            )
        );
        // endregion

        // region create_topic_form
        screenManager.addForm(
            "create_topic_form",
            new Form(
                "Create Topic",
                List.of(
                    new Component("ID", false, true),
                    new Component("Title", false, true),
                    new Component("Description", false, false)
                ),
                List.of(
                    // Commando para ver quais os topicos disponives
                    new Command(":ct", "Check Topics", () -> {
                        try {
                            screenManager.bindScreen(generateAllTopics());
                        } catch (RemoteException e) {
                            Notifications.createCritical(e.getMessage());
                        }
                    })
                ),
                components -> {
                    List<Component> componentList = (List<Component>) components;

                    if (components.stream().filter(Component::isRequired).allMatch(Component::isAnswerEmpty)) {
                        Notifications.createWarning("Required camps are not filled.");
                    } else {
                        try {
                            si.addTopic(
                                si.createTopic(
                                    componentList.get(0).getAnswer(),
                                    componentList.get(1).getAnswer(),
                                    componentList.get(2).getAnswer()
                                )
                            );

                            Notifications.createValid("Topic '"+ componentList.get(0).getAnswer() +"' created");
                        } catch (IOException e) {
                            Notifications.createCritical(e.getMessage());
                        } catch (TopicIDTakenException e) {
                            Notifications.createWarning(e.getMessage());
                        }
                    }
                },
                screenManager
            )
        );
        // endregion

        // region create_topic_form
        screenManager.addForm(
            "create_news_form",
            new Form(
                "Create News",
                List.of(
                    new Component("Title", false, true),
                    new Component("Content", false, true),
                    new Component("TopicID", false, false)
                ),
                List.of(
                    // Commando para ver quais os topicos disponives
                    new Command(":ct", "Check Topics", () -> {
                        try {
                            screenManager.bindScreen(generateAllTopics());
                        } catch (RemoteException e) {
                            Notifications.createCritical(e.getMessage());
                        }
                    })
                ),
                components -> {
                    List<Component> componentList = (List<Component>) components;

                    if (components.stream().filter(Component::isRequired).allMatch(Component::isAnswerEmpty)) {
                        Notifications.createWarning("Required camps are not filled.");
                    } else {
                        try {
                            si.addNews(
                                si.createNews(
                                    componentList.get(0).getAnswer(),
                                    componentList.get(1).getAnswer().toCharArray(),
                                    componentList.get(2).getAnswer(),
                                    (Publisher) session.getSessionAccount()
                                )
                            );

                            Notifications.createValid("News '"+ componentList.get(0).getAnswer() +"' created");
                        } catch (IOException | FailedDeleteException e) {
                            Notifications.createCritical(e.getMessage());
                        }
                    }
                },
                screenManager
            )
        );
        // endregion

        // region subscribe_topic_form
        screenManager.addForm(
            "subscribe_topic_form",
            new Form(
                "Subscribe a Topic",
                List.of(new Component("TopicID", false, true)),
                List.of(
                    // Commando para ver quais os topicos disponives
                    new Command(":ct", "Check Topics", () -> {
                        try {
                            screenManager.bindScreen(generateAllTopics());
                        } catch (RemoteException e) {
                            Notifications.createCritical(e.getMessage());
                        }
                    })
                ),
                components -> {
                    List<Component> componentList = (List<Component>) components;

                    String topicID = componentList.get(0).getAnswer();
                    try {
                        Subscriber subscriber = (Subscriber) si.getAccountFromID(session.getSessionAccount().getID());
                        subscriber.subscribeTopic(topicID);

                        Notifications.createValid("Topic '"+ topicID +"' subscribed ");
                    } catch (RemoteException e) {
                        Notifications.createCritical(e.getMessage());
                    }
                },
                screenManager
            )
        );
        // endregion

        // region guest_menu
        screenManager.addMenu(
            "guest_menu",
            new Menu(
                "Guest Menu",
                Arrays.asList(
                    new Option(
                        "Consult News",
                        () -> { screenManager.bindScreen(screenManager.getForm("consult_news_form")); }
                    ),
                    new Option(
                        "Consult Last News from a Topic",
                        () -> { screenManager.bindScreen(screenManager.getForm("consult_last_from_topic_form")); }
                    )
                ),
                screenManager
            )
        );
        // endregion

        // region welcome_menu
        screenManager.addMenu(
            "welcome_menu",
            new Menu(
                "Welcome to Correio da Matina",
                Arrays.asList(
                    new Option(
                        "Login",
                        () -> { screenManager.bindScreen(screenManager.getForm("login_form")); }
                    ),
                    new Option(
                        "Register",
                        () -> { screenManager.bindScreen(screenManager.getForm("register_form")); }
                    ),
                    new Option(
                        "Continue as Guest",
                        () -> { screenManager.bindScreen(screenManager.getForm("guest_menu")); }
                    )
                ),
                screenManager
            )
        );
        // endregion
    }

    private static Article generateLastNewsFromTopic(String topicID) throws IOException {
        News news = si.getLastNewsFromTopic(topicID);
        String[] lines = Arrays.toString(news.getContent()).split("\n");
        List<StringStyler> stylers = Arrays
            .stream(lines)
            .map(StringStyler::new)
            .collect(Collectors.toList());

        return new Article(news.getTitle(), stylers, screenManager);
    }

    private static Menu generateAllTopics() throws RemoteException {
        List<Option> topicList = si.getAllTopics().stream().map(
            x -> { return new Option(x.getId() + "\t" + x.getTitle() + "\t" + x.getDescription()); }
        ).collect(Collectors.toList());

        return new Menu(
            "Check All Topics",
            topicList,
            screenManager
        );
    }

    private static Menu generateMenuNews(String startDate, String endDate, String topicID) throws ParseException, RemoteException, NotFoundOnServerException {
        Date sDate, eDate;

        sDate = (!startDate.equals(""))
            ? new SimpleDateFormat("dd/MM/yyyy").parse(startDate)
            : Date.from(Instant.EPOCH);

        eDate = (!endDate.equals(""))
            ? new SimpleDateFormat("dd/MM/yyyy").parse(endDate)
            : Date.from(Instant.now());

        List<Option> optionList =
            ((!topicID.equals("")) ? si.getNewsInsideTimeInterval(topicID, sDate, eDate) : si.getAllNews())
                .stream().map(x -> {
                return new Option(
                    x.toString(),
                    () -> {
                        screenManager.bindScreen(new Article(
                            x.getTitle(),
                            Arrays.stream(Arrays.toString(x.getContent()).split("\n"))
                                .map(StringStyler::new)
                                .collect(Collectors.toList()),
                            screenManager
                        ));
                    }
                );
            }).collect(Collectors.toList());


        return new Menu(
            "Consult all Menus",
            optionList,
            screenManager
        );
    }

    private static Menu generatePublisherMenu(String username) {
        return new Menu(
            "Welcome" + username + "!",
            List.of(
                new Option("Create Topic", () -> {
                    screenManager.bindScreen(screenManager.getForm("create_topic_form"));
                }),
                new Option("Create News", () -> {
                    screenManager.bindScreen(screenManager.getForm("create_news_form"));
                }),
                new Option("Consult Topics", () -> {
                    try {
                        screenManager.bindScreen(generateAllTopics());
                    } catch (RemoteException e) {
                        Notifications.createCritical(e.getMessage());
                    }
                }),
                new Option("Consult News", () -> {
                    screenManager.bindScreen(screenManager.getForm("consult_news_form"));
                })
            ),
            screenManager
        );
    }

    private static Menu generateSubscriberMenu(String username) {
        return new Menu(
            "Welcome" + username + "!",
            List.of(
                new Option("Subscribe Topic", () -> {
                    screenManager.bindScreen(screenManager.getForm("subscribe_topic_form"));
                }),
                new Option("Consult News", () -> {
                    screenManager.bindScreen(screenManager.getForm("consult_news_form"));
                }),
                new Option("Consult Most Recent Topics", () -> {
                    screenManager.bindScreen(screenManager.getForm("consult_last_from_topic_form"));
                })
            ),
            screenManager
        );
    }
}
