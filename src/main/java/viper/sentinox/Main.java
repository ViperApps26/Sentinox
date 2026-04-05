package viper.sentinox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        DatabaseManager.initializeDatabase();

        String token = "";
        Path tokenPath = Path.of("BlueskyToken.txt");

        if (Files.exists(tokenPath)) {
            token = Files.readString(tokenPath).trim();
        } else {
            System.out.println("Warning: BlueskyToken.txt not found. Bluesky commands will not work.");
        }

        Scanner scanner = new Scanner(System.in);
        String command = Command.askCommand(scanner);

        while (!command.equals("exit")) {
            Command.processCommand(command, token);
            command = Command.askCommand(scanner);
        }
    }
}





