package viper.sentinox;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        String token = args[0];
        String password = args[1];
        String databaseURL = args[2];

        DatabaseCreator.createDatabases(databaseURL);

        Scanner scanner = new Scanner(System.in);
        String command = Command.askCommand(scanner);

        while (!command.equals("exit")) {
            Command.processCommand(command, token, password, databaseURL);
            command = Command.askCommand(scanner);
        }
        Files.delete(Path.of("BlueskyToken.txt"));
    }
}





