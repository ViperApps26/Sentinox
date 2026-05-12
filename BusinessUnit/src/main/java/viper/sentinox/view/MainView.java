package viper.sentinox.view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import viper.sentinox.control.MedicineDataMart;

import java.util.Objects;

public class MainView {

    private final BorderPane root = new BorderPane();

    private final TextField searchField = new TextField();
    private final ListView<String> medicineList = new ListView<>();
    private final Label summaryLabel = new Label();

    private final Button commentsBtn = new Button("Comments");
    private final Button reactionsBtn = new Button("Reactions");
    private final Button jointBtn = new Button("Joint Analysis");

    private final Stage stage;
    private final MedicineDataMart dataMart;
    private final ObservableList<String> allMedicines;

    private static final String ACTIVE_BUTTON_STYLE = "-fx-background-color: rgba(0,128,0); -fx-text-fill: white;";
    private static final String INACTIVE_BUTTON_STYLE = "-fx-background-color: rgba(0,128,0,0.5); -fx-text-fill: white;";

    public MainView(Stage stage, MedicineDataMart dataMart) {
        this.stage = stage;
        this.dataMart = dataMart;
        this.allMedicines = FXCollections.observableArrayList(dataMart.getAllStats().keySet());

        configureLayout();
        configureBehavior();
        startAutoRefresh();
    }

    public Parent getRoot() {
        return root;
    }

    private void configureLayout() {
        buildBackground();
        ImageView logoView = getLogoView();
        HBox searchBox = getSearchBarView(logoView);
        setMedicinesList();
        VBox summaryBox = getSummaryBox();
        SplitPane center = getSplitPane(summaryBox);
        setInfoButtonsState(false);
        HBox topButtons = getInfoButtons();
        placeInfoButtonsAboveSearchBar(topButtons, searchBox, center);
    }

    private HBox getSearchBarView(ImageView logoView) {
        searchField.setPromptText("Search medicine by full name...");
        HBox searchBox = new HBox(10, logoView, searchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(10));
        searchBox.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 10;");
        searchField.setPrefWidth(200);
        return searchBox;
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

    private ImageView getLogoView() {
        Image logoImage = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/images/ViperApps logo.png"))
        );
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(80);
        logoView.setPreserveRatio(true);
        return logoView;
    }

    private void setMedicinesList() {
        medicineList.setItems(allMedicines);
        medicineList.setStyle("-fx-background-color: rgba(255,255,255,0.8);");
    }

    private VBox getSummaryBox() {
        summaryLabel.setWrapText(true);
        summaryLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox summaryBox = new VBox(10, summaryLabel);
        summaryBox.setPadding(new Insets(10));
        summaryBox.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 10;");
        return summaryBox;
    }

    private SplitPane getSplitPane(VBox summaryBox) {
        SplitPane center = new SplitPane(medicineList, summaryBox);
        center.setDividerPositions(0.3);
        return center;
    }

    private void setInfoButtonsState(boolean hasSelection) {
        commentsBtn.setDisable(!hasSelection);
        reactionsBtn.setDisable(!hasSelection);
        jointBtn.setDisable(!hasSelection);
        setInfoButtonsColor(hasSelection);
    }

    private void setInfoButtonsColor(boolean hasSelection) {
        if (!hasSelection) {
            colorize(commentsBtn, false);
            colorize(reactionsBtn, false);
            colorize(jointBtn, false);
            return;
        }
        colorize(commentsBtn, medicineHasComments());
        colorize(reactionsBtn, medicineHasReactions());
        colorize(jointBtn, medicineHasComments() && medicineHasReactions());
    }

    private void colorize(Button btn, boolean active) {
        btn.setStyle(active ? ACTIVE_BUTTON_STYLE : INACTIVE_BUTTON_STYLE);
    }

    private boolean medicineHasComments() {
        return !dataMart.getMedicineComments(medicineList.getSelectionModel().getSelectedItem()).isEmpty();
    }

    private boolean medicineHasReactions() {
        return !dataMart.getMedicineReactions(medicineList.getSelectionModel().getSelectedItem()).isEmpty();
    }

    private HBox getInfoButtons() {
        HBox topButtons = new HBox(10, commentsBtn, reactionsBtn, jointBtn);
        topButtons.setAlignment(Pos.CENTER);
        topButtons.setPadding(new Insets(5));
        return topButtons;
    }

    private void placeInfoButtonsAboveSearchBar(HBox topButtons, HBox searchBox, SplitPane center) {
        VBox top = new VBox(topButtons, searchBox);

        root.setTop(top);
        root.setCenter(center);
        BorderPane.setMargin(center, new Insets(10));
    }


    private void configureBehavior() {
        selectMedicine();
        writeInSearchBar();
        pressCommentsButton();
        pressReactionsButton();
        pressJointAnalysisButton();
    }

    private void selectMedicine() {
        medicineList.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> {
                    updateSummary(selected);
                    boolean hasSelection = selected != null;
                    setInfoButtonsState(hasSelection);
                }
        );
    }

    private void writeInSearchBar() {
        searchField.textProperty().addListener((obs, old, text)
                -> applyFilter()
        );
    }

    private void pressCommentsButton() {
        commentsBtn.setOnAction(e -> {
            String med = medicineList.getSelectionModel().getSelectedItem();
            if (med != null && medicineHasComments()) {
                CommentsView view = new CommentsView(stage, dataMart, med);
                stage.setScene(new Scene(view.getRoot(), 1000, 650));
            }
        });
    }

    private void pressReactionsButton() {
        reactionsBtn.setOnAction(e -> {
            String med = medicineList.getSelectionModel().getSelectedItem();
            if (med != null && medicineHasReactions()) {
                ReactionsView view = new ReactionsView(stage, dataMart, med);
                stage.setScene(new Scene(view.getRoot(), 1000, 650));
            }
        });
    }

    private void pressJointAnalysisButton() {
        jointBtn.setOnAction(e -> {
            String med = medicineList.getSelectionModel().getSelectedItem();

            if (med != null && medicineHasComments() && medicineHasReactions()) {
                JointAnalysisView view = new JointAnalysisView(stage, dataMart, med);
                stage.setScene(new Scene(view.getRoot(), 1000, 650));
            }
        });
    }


    private void updateSummary(String medicine) {
        if (medicine == null) {
            summaryLabel.setText("");
            return;
        }
        String text = "%s Data:\n%d reactions - %d comments"
                .formatted(
                        medicine,
                        dataMart.getMedicineReactions(medicine).size(),
                        dataMart.getMedicineComments(medicine).size()
                );
        summaryLabel.setText(text);
    }

    private void startAutoRefresh() {
        Timeline refresher = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> refreshList())
        );
        refresher.setCycleCount(Animation.INDEFINITE);
        refresher.play();
    }

    private void refreshList() {
        applyFilter();
    }

    private void applyFilter() {
        String search = searchField.getText().toLowerCase().trim();

        var all = dataMart.getAllStats().keySet();

        if (search.isEmpty()) {
            medicineList.setItems(FXCollections.observableArrayList(all));
        } else {
            var filtered = all.stream()
                    .filter(medicine -> medicine.contains(search))
                    .toList();

            medicineList.setItems(FXCollections.observableArrayList(filtered));
        }
    }
}

