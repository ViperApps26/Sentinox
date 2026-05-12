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
import viper.sentinox.model.Comment;

import java.util.Objects;

public class CommentsView {

    private final BorderPane root = new BorderPane();
    private final Stage stage;
    private final MedicineDataMart dataMart;
    private final String medicine;
    private ListView<Comment> list;


    public CommentsView(Stage stage, MedicineDataMart dataMart, String medicine) {
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
        buildCommentsList();
    }

    private void buildTop(Button home, Label title) {
        HBox top = new HBox(10, home, title);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(10));
        top.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        root.setTop(top);
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
        Label title = new Label("Comments for: " + medicine);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        return title;
    }

    private void buildCommentsList() {
        getStringListView();
        list.setStyle("-fx-background-color: rgba(255,255,255,0.85);");

        root.setCenter(list);
        BorderPane.setMargin(list, new Insets(10));
    }

    private void getStringListView() {
        list = new ListView<>();
        list.setItems(FXCollections.observableArrayList(dataMart.getMedicineComments(medicine)));
        list.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Comment comment, boolean empty) {
                super.updateItem(comment, empty);
                if (empty || comment == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Label iconLabel = getLabel(comment);
                VBox textBox = getComment(comment);
                buildComment(iconLabel, textBox);
            }

            private void buildComment(Label iconLabel, VBox textBox) {
                HBox.setHgrow(textBox, Priority.ALWAYS);
                HBox row = new HBox(10, iconLabel, textBox);
                row.setAlignment(Pos.TOP_LEFT);

                setGraphic(row);
                setText(null);
            }
        });
    }

    private Label getLabel(Comment comment) {
        Label iconLabel = new Label(sentimentIcon(comment));
        iconLabel.setStyle("-fx-font-size: 20px;");
        iconLabel.setMinWidth(30);
        iconLabel.setPrefWidth(30);
        iconLabel.setMaxWidth(30);
        iconLabel.setAlignment(Pos.TOP_CENTER);
        return iconLabel;
    }

    private static VBox getComment(Comment comment) {
        Label textLabel = new Label(comment.getText());
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(800);

        Label dateLabel = new Label(comment.getDate());
        dateLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");

        VBox textBox = new VBox(
                new Label(comment.getAuthor()),
                dateLabel,
                textLabel
        );
        textBox.setSpacing(2);
        return textBox;
    }

    private String sentimentIcon(Comment comment) {
        switch (comment.getSentiment()) {
            case "Positive" -> {
                return "😊";
            }
            case "Negative" -> {
                return "😞";
            }
            default -> {
                return "😐";
            }
        }
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
                dataMart.getMedicineComments(medicine)
        ));
    }
}

