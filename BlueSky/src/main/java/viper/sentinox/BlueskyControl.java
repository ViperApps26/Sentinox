package viper.sentinox;

import java.io.IOException;

public class BlueskyCommand {

    private final BlueskyGetToken tokenService;
    private final BlueskyConnect blueskyConnect;
    private final BlueskyPrint blueskyPrint;

    public BlueskyCommand(BlueskyGetToken tokenService,
                          BlueskyConnect blueskyConnect,
                          BlueskyPrint blueskyPrint) {
        this.tokenService = tokenService;
        this.blueskyConnect = blueskyConnect;
        this.blueskyPrint = blueskyPrint;
    }

    public void execute(String[] parts, String token, String password, String databaseURL)
            throws IOException, InterruptedException {

        switch (parts[0]) {

            case "bluesky_set_query" -> {
                if (validate(parts, 2)) {
                    blueskyConnect.setQuery(parts[1]);
                }
            }

            case "bluesky_set_limit" -> {
                if (validate(parts, 2)) {
                    blueskyConnect.setLimit(Integer.parseInt(parts[1]));
                }
            }

            case "bluesky_set_start" -> {
                if (validate(parts, 2)) {
                    blueskyConnect.setStartDate(parts[1]);
                }
            }

            case "bluesky_set_end" -> {
                if (validate(parts, 2)) {
                    blueskyConnect.setFinalDate(parts[1]);
                }
            }

            case "bluesky_print_posts" -> {
                String accessToken = tokenService.getAccessToken(token, password);
                blueskyPrint.printPosts(accessToken);
            }

            case "bluesky_save_posts" -> {
                String accessToken = tokenService.getAccessToken(token, password);
                DatabaseInsert.saveBlueskyPosts(accessToken, databaseURL);
            }

            case "bluesky_show_db" -> DataViewer.showBlueskyPosts(databaseURL);

            case "bluesky_help" -> help();
        }
    }

    public void help() {
        System.out.println("""
            ------------------------- BLUESKY ----------------------

            bluesky_set_query <search>: Set the search query for Bluesky
            bluesky_set_limit <number>: Set the maximum number of posts
            bluesky_set_start <YYYY-MM-DD>: Set the start date
            bluesky_set_end <YYYY-MM-DD>: Set the end date
            bluesky_print_posts: Print posts from Bluesky
            bluesky_save_posts: Save posts into the database
            bluesky_show_db: Show stored Bluesky posts

            -------------------------------------------------------
            """);
    }

    private boolean validate(String[] parts, int expectedLength) {
        if (parts.length != expectedLength) {
            System.out.println("Incorrect command length");
            return false;
        }
        return true;
    }
}
