package viper.sentinox.view;

public class ConsoleView {

    public void show(String message) {
        System.out.println(message);
    }

    public void showError(String message, Exception e) {
        System.out.println(message);
    }
}