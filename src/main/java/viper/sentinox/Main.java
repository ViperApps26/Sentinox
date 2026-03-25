package viper.sentinox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> tokens = getTokens();

        Scanner scanner = new Scanner(System.in);
        String command = Command.askCommand(scanner);

        while (!command.equals("exit")) {
            Command.processCommand(command, tokens);
            command = Command.askCommand(scanner);
        }
    }

    private static List<String> getTokens() throws IOException {
        return new ArrayList<>(
                List.of(
                        Files.readString(Path.of("OpenFDAToken.txt")),
                        Files.readString(Path.of("BlueskyToken.txt"))
                )
        );
    }
}