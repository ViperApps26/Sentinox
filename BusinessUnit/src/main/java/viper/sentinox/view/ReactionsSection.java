package viper.sentinox.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import viper.sentinox.control.datamart.MedicineDataMart;

public class ReactionsSection {

    private final MedicineDataMart dataMart;

    private final VBox container = new VBox(10);
    private final VBox content = new VBox(10);

    private boolean expanded = false;

    private String currentMedicine;

    public ReactionsSection(MedicineDataMart dataMart) {

        this.dataMart = dataMart;

        content.setFillWidth(true);
    }

    public VBox getView(String medicine) {
        if (currentMedicine == null || !currentMedicine.equals(medicine)) {
            expanded = false;
        }
        currentMedicine = medicine;
        rebuildContent(medicine);
        Button toggle = buildToggleButton(medicine);

        container.getChildren().setAll(
                toggle,
                content
        );
        return container;
    }

    private void rebuildContent(String medicine) {
        content.getChildren().clear();

        for (String reaction : dataMart.getMedicineReactions(medicine)) {
            content.getChildren().add(
                    buildReactionCard(reaction)
            );
        }
        content.setVisible(expanded);
        content.setManaged(expanded);
    }

    private Button buildToggleButton(String medicine) {
        Button toggle = new Button(
                expanded
                        ? "▼ Reactions"
                        : "▶ Reactions"
        );
        toggle.setMaxWidth(Double.MAX_VALUE);

        toggle.setStyle("""
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-background-radius: 12;
                -fx-background-color: #ecf0f1;
        """);

        toggle.setOnAction(e -> {
            expanded = !expanded;
            getView(medicine);
        });
        return toggle;
    }

    private VBox buildReactionCard(String reaction) {
        Label label = getReactionLabel(reaction);
        return getReactionCard(label);
    }

    private static Label getReactionLabel(String reaction) {
        Label label = new Label("⚠ " + reaction);
        label.setWrapText(true);
        label.setStyle("""
                -fx-font-size: 14px;
                -fx-text-fill: #2c3e50;
        """);
        return label;
    }

    private static VBox getReactionCard(Label label) {
        VBox card = new VBox(label);
        card.setPadding(new Insets(15));
        card.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 15;
                -fx-border-radius: 15;
                -fx-border-color: rgba(0,0,0,0.08);
        """);
        return card;
    }
}
