package viper.sentinox.view;

public interface BusinessUnitView {
    void showMessage(String message);
    void showError(String message, Exception e);
}