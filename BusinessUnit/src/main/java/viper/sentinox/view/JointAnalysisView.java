package viper.sentinox.view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import viper.sentinox.control.MedicineDataMart;
import viper.sentinox.model.JointAnalysisResult;

import java.util.Objects;

public class JointAnalysisView {

    private final BorderPane root = new BorderPane();
    private final Stage stage;
    private final MedicineDataMart dataMart;
    private final String medicine;

    private Label resultLabel;

    public JointAnalysisView(Stage stage, MedicineDataMart dataMart, String medicine) {
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
        buildTop();
        buildContent();
        refreshAnalysis();
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
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false,
                        false,
                        true,
                        true
                )
        );

        root.setBackground(new Background(bg));
    }

    private void buildTop() {
        Button home = new Button("🏠");

        home.setOnAction(e -> {
            MainView main = new MainView(stage, dataMart);
            stage.setScene(new Scene(main.getRoot(), 1000, 650));
        });

        Label title = new Label("Joint Analysis for: " + medicine);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox top = new HBox(10, home, title);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(10));
        top.setStyle("-fx-background-color: rgba(0,0,0,0.6);");

        root.setTop(top);
    }

    private void buildContent() {
        resultLabel = new Label();
        resultLabel.setWrapText(true);
        resultLabel.setStyle("""
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-background-color: rgba(255,255,255,0.85);
                -fx-padding: 20;
                -fx-background-radius: 10;
                """);

        StackPane center = new StackPane(resultLabel);
        center.setPadding(new Insets(30));

        root.setCenter(center);
    }

    private void refreshAnalysis() {
        JointAnalysisResult result = dataMart.getMedicineJointAnalysis(medicine);

        String text = """
                Medicine: %s
                
                Matched reactions: %d
                Total known reactions: %d
                Agreement percentage: %.2f%%
                
                Conclusion:
                %s
                """.formatted(
                medicine,
                result.getMatchedReactions(),
                result.getTotalReactions(),
                result.getAgreementPercentage(),
                result.getConclusion()
        );

        resultLabel.setText(text);
    }

    private void startAutoRefresh() {
        Timeline refresher = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> refreshAnalysis())
        );

        refresher.setCycleCount(Animation.INDEFINITE);
        refresher.play();
    }
}