package pt.ubi.sd.g16.client.Control;

import com.ashketshup.Landmark.ScreenManager;
import com.ashketshup.Landmark.Screens.Article;
import com.ashketshup.Landmark.Screens.Menu;
import com.ashketshup.Landmark.TUI.Notifications;
import com.ashketshup.Landmark.TUI.StringStyler;
import com.ashketshup.Landmark.UIElements.Option;
import pt.ubi.sd.g16.client.BackupComm;
import pt.ubi.sd.g16.server.ServerInterface;
import pt.ubi.sd.g16.server.exceptions.NotFoundOnServerException;
import pt.ubi.sd.g16.shared.News;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class NewsMenu {
    private static ScreenManager screenManager;
    private static ServerInterface serverInterface;

    private static void control(News news) {
        String[] lines = new String(news.getContent()).split("\n");

        screenManager.bindScreen(
            new Article(
                news.getTitle(),
                Arrays.stream(lines)
                    .map(StringStyler::new)
                    .collect(Collectors.toList()),
                screenManager
            )
        );
    }

    public static Menu generate(String startDate, String endDate, String topicID, ScreenManager screenManager, ServerInterface serverInterface) throws ParseException, RemoteException, NotFoundOnServerException {
        NewsMenu.screenManager = screenManager;
        NewsMenu.serverInterface = serverInterface;
        Date sDate, eDate;

        sDate = (!startDate.equals(""))
            ? new SimpleDateFormat("dd/MM/yyyy").parse(startDate)
            : Date.from(Instant.EPOCH);

        eDate = (!endDate.equals(""))
            ? new SimpleDateFormat("dd/MM/yyyy").parse(endDate)
            : Date.from(Instant.now());

        List<Option> optionList =
            ((!topicID.equals("")) ? serverInterface.getNewsInsideTimeInterval(topicID, sDate, eDate) : serverInterface.getAllNews())
                .stream().map(
                    x -> new Option(
                        x.toString(),
                        () -> control(x)
                    )
                )
            .collect(Collectors.toList());


        return new Menu(
            "Consult all Menus",
            optionList,
            screenManager
        );
    }

    public static Menu generate(String startDate, String endDate, String topicID, String ip, ScreenManager screenManager, ServerInterface serverInterface) throws ParseException, IOException, ClassNotFoundException, NotFoundOnServerException {
        NewsMenu.screenManager = screenManager;
        NewsMenu.serverInterface = serverInterface;
        Date sDate, eDate;

        sDate = (!startDate.equals(""))
            ? new SimpleDateFormat("dd/MM/yyyy").parse(startDate)
            : Date.from(Instant.EPOCH);

        eDate = (!endDate.equals(""))
            ? new SimpleDateFormat("dd/MM/yyyy").parse(endDate)
            : Date.from(Instant.now());

        List<News> newsList = ((!topicID.equals("")) ? BackupComm.getNewsTopic(ip, topicID) : BackupComm.getNews(ip))
            .stream().filter(x -> x.getDate().after(sDate) && x.getDate().before(eDate)).collect(Collectors.toList());

        if (newsList.isEmpty()) {
            throw new NotFoundOnServerException();
        }

        List<Option> optionList =
            newsList.stream().map(
                    x -> new Option(
                        x.toString(),
                        () -> control(x)
                    )
                )
            .collect(Collectors.toList());

        return new Menu(
            "Consult all Menus",
            optionList,
            screenManager
        );
    }

}
