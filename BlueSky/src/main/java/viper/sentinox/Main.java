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
        BlueskyPrint blueskyPrint = new BlueskyPrint(blueskyGet);

        BlueskyControl blueskyControl = new BlueskyControl(blueskyGetToken, blueskyConnect, blueskyPrint);


        //DatabaseCreator.createDatabases(databaseURL);

        Scanner scanner = new Scanner(System.in);
        String command = blueskyControl.askCommand(scanner);

        while (!command.equals("exit")) {
            blueskyControl.processCommand(command, token, password, databaseURL);
            command = blueskyControl.askCommand(scanner);
        }
        Files.delete(Path.of("BlueskyToken.txt"));
    }
}
