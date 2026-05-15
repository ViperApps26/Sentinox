package viper.sentinox.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class WelcomeSection {

    public VBox getView() {
        VBox box = new VBox(20);

        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(30));

        ImageView logo = getLogoView();

        Label title = getWelcomeTitle();

        Label subtitle = getWelcomeSubtitle();

        box.getChildren().addAll(
                logo,
                title,
                subtitle
        );

        return box;
    }

    private ImageView getLogoView() {
        Image logoImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(
                                "/images/ViperApps logo.png"
                        )
                )
        );

        ImageView logoView = new ImageView(logoImage);

        logoView.setFitHeight(180);
        logoView.setPreserveRatio(true);

        return logoView;
    }

    private Label getWelcomeTitle() {
        Label title = new Label("Welcome to ViperApps");

        title.setStyle("""
                        -fx-font-size: 26px;
                        -fx-font-weight: bold;
                        -fx-text-fill: #145a32;
                """);

        return title;
    }

    private Label getWelcomeSubtitle() {
        Label subtitle = new Label("""
                        Search for a medicine on the left panel to view:
                
                        • General statistics
                        • Joint analysis
                        • User comments
                        • Known reactions
                
                        Your guide to safer treatments 💚
                """);

        subtitle.setWrapText(true);

        subtitle.setStyle("""
                        -fx-font-size: 15px;
                        -fx-text-fill: #2c3e50;
                        -fx-alignment: center;
                """);

        return subtitle;
    }
}
