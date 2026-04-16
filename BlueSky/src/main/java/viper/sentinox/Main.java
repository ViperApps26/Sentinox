package viper.sentinox;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        String token = args[0];
        String password = args[1];
        String databaseURL = args[2];

        BlueskyGetToken blueskyGetToken = new BlueskyGetToken();
        BlueskyConnect blueskyConnect = new BlueskyConnect();
        BlueskyGet blueskyGet = new BlueskyGet(blueskyConnect);

        BlueskyDatabaseCreator blueskyDatabaseCreator = new BlueskyDatabaseCreator();
        BlueskyInsert blueskyInsert = new BlueskyInsert(blueskyConnect, blueskyGet);
        BlueskyDataViewer blueskyDataViewer = new BlueskyDataViewer();
        BlueskyFeeder blueskyFeeder = new BlueskyFeeder(blueskyGetToken, blueskyInsert);

        BlueskyControl blueskyControl = new BlueskyControl(
                blueskyConnect,
                blueskyFeeder,
                blueskyDataViewer
        );

        blueskyDatabaseCreator.createDatabase(databaseURL);

        Scanner scanner = new Scanner(System.in);
        String command = blueskyControl.askCommand(scanner);

        while (!command.equals("exit")) {
            blueskyControl.processCommand(command, token, password, databaseURL);
            command = blueskyControl.askCommand(scanner);
        }

        scanner.close();
        Files.deleteIfExists(Path.of("BlueskyToken.txt"));
    }
}