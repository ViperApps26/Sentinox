package viper.sentinox.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import viper.sentinox.control.datamart.MedicineDataMart;
import javafx.scene.image.Image;

import java.util.Objects;

public class ViperApp extends Application {

    private static MedicineDataMart dataMart;

    public static void setDataMart(MedicineDataMart dataMart) {
        ViperApp.dataMart = dataMart;
    }

    @Override
    public void start(Stage stage) {
        MainView mainView = new MainView(dataMart);
        Scene scene = new Scene(mainView.getRoot(), 1000, 650);
        stage.setTitle("ViperApps");
        stage.getIcons().add(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ViperApps window.png")))
        );
        stage.setScene(scene);
        stage.show();
    }
}

