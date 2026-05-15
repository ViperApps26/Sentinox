package viper.sentinox.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import viper.sentinox.control.datamart.MedicineDataMart;
import viper.sentinox.model.JointAnalysis;

public class MedicineInfoSection {

    private final MedicineDataMart dataMart;

    public MedicineInfoSection(MedicineDataMart dataMart) {
        this.dataMart = dataMart;
    }

    public VBox getView(String medicine) {
        JointAnalysis result = dataMart.getMedicineJointAnalysis(medicine);

        VBox content = new VBox(15);

        content.getChildren().addAll(
                buildTitle(medicine),
                buildStatsCard(medicine)
        );
        if (dataMart.medicineHasComments(medicine) && dataMart.medicineHasReactions(medicine)) {
            content.getChildren().add(buildAnalysisCard(result));
        }
        return content;
    }

    private Label buildTitle(String medicine) {
        Label title = new Label("💊 " + medicine);
        title.setStyle("""
                        -fx-font-size: 24px;
                        -fx-font-weight: bold;
                        -fx-text-fill: #145a32;
                """);
        return title;
    }

    private VBox buildStatsCard(String medicine) {
        return createCard("""
                📊 General Statistics
                
                • Reactions detected: %d
                • User comments: %d
                """.formatted(
                dataMart.getMedicineReactions(medicine).size(),
                dataMart.getMedicineComments(medicine).size()
        ));
    }

    private VBox buildAnalysisCard(JointAnalysis result) {
        return createCard("""
                🔬 Joint Analysis
                
                ✔ Matched reactions: %d
                📈 Total known reactions: %d
                🤝 Agreement level: %.2f%%
                
                🔍 Conclusion:
                %s
                """.formatted(
                result.getMatchedReactions(),
                result.getTotalReactions(),
                result.getAgreementPercentage(),
                result.getConclusion()
        ));
    }

    private VBox createCard(String text) {
        Label label = getCardLabel(text);
        return getCardBox(label);
    }

    private static Label getCardLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setStyle("""
                        -fx-font-size: 14px;
                        -fx-text-fill: #2c3e50;
                """);
        return label;
    }

    private static VBox getCardBox(Label label) {
        VBox box = new VBox(label);
        box.setPadding(new Insets(15));
        box.setAlignment(Pos.CENTER_LEFT);

        box.setStyle("""
                        -fx-background-color: white;
                        -fx-background-radius: 15;
                        -fx-border-radius: 15;
                        -fx-border-color: rgba(0,0,0,0.05);
                """);

        return box;
    }
}