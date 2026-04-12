package viper.sentinox;

import java.io.IOException;
import java.util.Scanner;

public class Control {

    private final BlueskyGetToken blueskyGetToken;
    private final BlueskyConnect blueskyConnect;
    private final BlueskyPrint blueskyPrint;

    public Control(BlueskyGetToken blueskyGetToken,
                          BlueskyConnect blueskyConnect,
                          BlueskyPrint blueskyPrint) {
        this.blueskyGetToken = blueskyGetToken;
        this.blueskyConnect = blueskyConnect;
        this.blueskyPrint = blueskyPrint;
    }

    public void processCommand(String command, String token, String password, String databaseURL) throws IOException, InterruptedException {
        String[] parts = command.split(" ");

        switch (parts[0]) {
            case "bluesky_set_query" -> {
                if (validate(parts, 2, 2,-1)) {
                    blueskyConnect.setQuery(parts[1]);
                }
            }
            case "bluesky_set_limit" -> {
                if (validate(parts, 2, 2,1)) {
                    blueskyConnect.setLimit(Integer.parseInt(parts[1]));
                }
            }
            case "bluesky_set_start" -> {
                if (validate(parts, 2, 2,-1)) {
                    blueskyConnect.setStartDate(parts[1]);
                }
            }
            case "bluesky_set_end" -> {
                if (validate(parts, 2, 2,-1)) {
                    blueskyConnect.setFinalDate(parts[1]);
                }
            }

            case "bluesky_print_posts" -> {
                String blueskyToken = blueskyGetToken.getAccessToken(token, password);
                blueskyPrint.printPosts(blueskyToken);
            }
            /*
            case "bluesky_save_posts" -> {
                String blueskyToken = blueskyGetToken.getAccessToken(token, password);
                DatabaseInsert.saveBlueskyPosts(blueskyToken, databaseURL);
            }

            case "bluesky_show_db" -> DataViewer.showBlueskyPosts(databaseURL);
            */

            case "help" -> help();
            default -> System.out.println("Command not found");
        }
    }

    public boolean validate(String[] parts, int minLength, int maxLength, int positionToCheck) {
        if (parts.length < minLength | parts.length > maxLength) {
            System.out.println("Incorrect command length");
            return false;
        } else if (positionToCheck >= 0 && !parts[positionToCheck].matches("\\d+")) {
            System.out.println("Incorrect attribute type");
            return false;
        }

        return true;
    }

    public String askCommand(Scanner scanner) {
        System.out.println("Write a command\n(Write \"help\" if you don't know any command)");
        return scanner.nextLine().trim();
    }

    public void help() {
        System.out.println("""
            - HELP - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            command: description | params
            - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            GENERAL
            exit: Exit the program
            help: Show this help menu
            - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            BLUESKY
            bluesky_set_query: Set a search message | search
            bluesky_set_limit: Set a limit of results | number
            bluesky_set_start: Set a start date to search | date("YYYY-MM-DD")
            bluesky_set_end: Set an end date to search | date("YYYY-MM-DD")

            bluesky_print_posts: Show all messages from posts
            
            bluesky_save_posts: Save posts from Bluesky into the database
            
            bluesky_show_db: Show all stored Bluesky posts
            - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            """);
    }
}