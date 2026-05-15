package viper.sentinox.view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import viper.sentinox.control.datamart.MedicineDataMart;

import java.util.List;
import java.util.Objects;

public class MainView {

    private final BorderPane root = new BorderPane();

    private final TextField searchField = new TextField();
    private final ListView<String> medicineList = new ListView<>();
    private final VBox summaryContent = new VBox(15);

    private final MedicineDataMart dataMart;

    private final ObservableList<String> allMedicines;

    private final WelcomeSection welcomeSection;
    private final MedicineInfoSection medicineInfoSection;
    private final CommentsSection commentsSection;
    private final ReactionsSection reactionsSection;

    private String currentMedicine;

    public MainView(MedicineDataMart dataMart) {
        this.dataMart = dataMart;
        this.allMedicines = FXCollections.observableArrayList(
                dataMart.getAllMedicinesSorted()
        );
        this.welcomeSection = new WelcomeSection();
        this.medicineInfoSection = new MedicineInfoSection(dataMart);
        this.commentsSection = new CommentsSection(dataMart);
        this.reactionsSection = new ReactionsSection(dataMart);

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
        configureMedicineList();
        ScrollPane summaryBox = buildSummaryBox();
        SplitPane center = buildSplitPane(summaryBox);

        root.setTop(searchBox);
        root.setCenter(center);

        BorderPane.setMargin(center, new Insets(10));
    }

    private void buildBackground() {
        Image bgImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(
                                "/images/Medicines Background.png"
                        )
                )
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

    private ImageView getLogoView() {
        Image logoImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(
                                "/images/ViperApps logo.png"
                        )
                )
        );
        ImageView logoView = new ImageView(logoImage);

        logoView.setFitHeight(80);
        logoView.setPreserveRatio(true);

        return logoView;
    }

    private HBox getSearchBarView(ImageView logoView) {
        searchField.setPromptText("Search medicine by full name...");
        searchField.setPrefWidth(250);

        return getSearchBox(logoView);
    }

    private HBox getSearchBox(ImageView logoView) {
        HBox searchBox = new HBox(10, logoView, searchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(10));
        searchBox.setStyle("""
                        -fx-background-color: rgba(255,255,255,0.85);
                        -fx-background-radius: 15;
                """);
        return searchBox;
    }

    private void configureMedicineList() {
        medicineList.setItems(allMedicines);
        medicineList.setStyle("""
                        -fx-background-color: rgba(255,255,255,0.92);
                        -fx-background-radius: 20;
                        -fx-border-radius: 20;
                        -fx-padding: 10;
                        -fx-font-size: 14px;
                """);
    }

    private ScrollPane buildSummaryBox() {
        summaryContent.getChildren().add(welcomeSection.getView());
        VBox container = getContainer();

        return getScrollPane(container);
    }

    private VBox getContainer() {
        VBox container = new VBox(summaryContent);
        container.setPadding(new Insets(15));
        container.setStyle("""
                        -fx-background-color: rgba(255,255,255,0.92);
                        -fx-background-radius: 20;
                        -fx-border-radius: 20;
                        -fx-border-color: rgba(0,0,0,0.08);
                """);
        return container;
    }

    private static ScrollPane getScrollPane(VBox container) {
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );
        return scrollPane;
    }

    private SplitPane buildSplitPane(ScrollPane summaryBox) {
        SplitPane center = new SplitPane(
                medicineList,
                summaryBox
        );
        center.setDividerPositions(0.3);

        return center;
    }

    private void configureBehavior() {
        medicineList.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, selected) -> {
                    currentMedicine = selected;

                    updateSummary(selected);
                });
        searchField.textProperty().addListener(
                (obs, old, text) -> refreshList()
        );
    }

    private void updateSummary(String medicine) {
        summaryContent.getChildren().clear();

        if (medicine == null) {
            summaryContent.getChildren().add(welcomeSection.getView());
            return;
        }

        summaryContent.getChildren().add(medicineInfoSection.getView(medicine));
        if (dataMart.medicineHasComments(medicine)) {
            summaryContent.getChildren().add(commentsSection.getView(medicine));
        }
        if (dataMart.medicineHasReactions(medicine)) {
            summaryContent.getChildren().add(reactionsSection.getView(medicine));
        }
    }

    private void startAutoRefresh() {
        Timeline refresher = new Timeline(
                new KeyFrame(
                        Duration.seconds(5),
                        e -> refreshList()
                )
        );
        refresher.setCycleCount(Animation.INDEFINITE);

        refresher.play();
    }

    private void refreshList() {
        String search = searchField.getText().toLowerCase().trim();
        var all = dataMart.getAllMedicinesSorted();

        filterMedicinesList(search, all);
        if (currentMedicine != null) {
            updateSummary(currentMedicine);
        }
    }

    private void filterMedicinesList(String search, List<String> all) {
        if (search.isEmpty()) {
            medicineList.setItems(
                    FXCollections.observableArrayList(all)
            );
        } else {
            var filtered = all.stream()
                    .filter(m -> m.contains(search))
                    .toList();
            medicineList.setItems(
                    FXCollections.observableArrayList(filtered)
            );
        }
    }
}