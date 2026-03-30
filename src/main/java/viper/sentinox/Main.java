package viper.sentinox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String token = Files.readString(Path.of("BlueskyToken.txt"));

        Scanner scanner = new Scanner(System.in);
        String command = Command.askCommand(scanner);

        while (!command.equals("exit")) {
            Command.processCommand(command, token);
            command = Command.askCommand(scanner);
        }
    }
}