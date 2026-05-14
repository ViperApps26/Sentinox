package viper.sentinox.view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import viper.sentinox.control.MedicineDataMart;

import java.util.Objects;

public class ReactionsView {

    private final BorderPane root = new BorderPane();
    private final Stage stage;
    private final MedicineDataMart dataMart;
    private final String medicine;
    private ListView<String> list;

    public ReactionsView(Stage stage, MedicineDataMart dataMart, String medicine) {
        this.stage = stage;
        this.dataMart = dataMart;
        this.medicine = medicine;

        configureLayout();
        startAutoRefresh();
    }

    public Parent getRoot() {
        return root;
    }

    private void configureLayout() {
        buildBackground();
        Button home = getHomeButton();
        Label title = getTitleLabel();
        buildTop(home, title);
        buildReactionsList();
    }

    private void buildBackground() {
        Image bgImage = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/images/Medicines Background.png"))
        );
        BackgroundImage bg = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        root.setBackground(new Background(bg));
    }

    private Button getHomeButton() {
        Button home = new Button("🏠");
        home.setOnAction(e -> {
            MainView main = new MainView(stage, dataMart);
            stage.setScene(new Scene(main.getRoot(), 1000, 650));
        });
        return home;
    }

    private Label getTitleLabel() {
        Label title = new Label("Reactions for: " + medicine);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        return title;
    }

    private void buildTop(Button home, Label title) {
        HBox top = new HBox(10, home, title);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(10));
        top.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        root.setTop(top);
    }

    private void buildReactionsList() {
        getStringListView();
        list.setStyle("-fx-background-color: rgba(255,255,255,0.85);");

        root.setCenter(list);
        BorderPane.setMargin(list, new Insets(10));
    }

    private void getStringListView() {
        list = new ListView<>();
        list.setItems(FXCollections.observableArrayList(dataMart.getMedicineReactions(medicine)));
        list.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String reaction, boolean empty) {
                super.updateItem(reaction, empty);
                if (empty || reaction == null) {
                    buildReaction(null);
                    return;
                }
                Label textLabel = getReaction(reaction);
                buildReaction(textLabel);
            }

            private void buildReaction(Label textLabel) {
                setGraphic(textLabel);
                setText(null);
            }
        });
    }

    private static Label getReaction(String reaction) {
        Label textLabel = new Label(reaction);
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(800);
        textLabel.setStyle("-fx-font-size: 14px;");
        return textLabel;
    }

    private void startAutoRefresh() {
        Timeline refresher = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> refreshList())
        );
        refresher.setCycleCount(Animation.INDEFINITE);
        refresher.play();
    }

    private void refreshList() {
        list.setItems(FXCollections.observableArrayList(
                dataMart.getMedicineReactions(medicine)
        ));
    }
}

