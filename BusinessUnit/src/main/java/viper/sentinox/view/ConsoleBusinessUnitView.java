package viper.sentinox.view;

public class ConsoleBusinessUnitView implements BusinessUnitView {

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showError(String message, Exception e) {
        System.out.println(message);
        e.printStackTrace();
    }
}