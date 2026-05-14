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
import viper.sentinox.control.datamart.MedicineDataMart;
import viper.sentinox.model.Comment;
import viper.sentinox.model.JointAnalysis;

import java.util.Objects;

public class MainView {

    private final BorderPane root = new BorderPane();

    private final TextField searchField = new TextField();
    private final ListView<String> medicineList = new ListView<>();
    private final VBox summaryContent = new VBox(15);
    private ListView<Comment> commentsListView;
    private ListView<String> reactionsListView;
    private String currentMedicine = null;

    private boolean commentsExpanded = false;
    private boolean reactionsExpanded = false;

    private final Button commentsBtn = new Button("Recommend Medicine");

    private final Stage stage;
    private final MedicineDataMart dataMart;
    private final ObservableList<String> allMedicines;

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
        ScrollPane summaryBox = getSummaryBox();
        SplitPane center = getSplitPane(summaryBox);
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
        medicineList.setStyle("""
                    -fx-background-color: rgba(255,255,255,0.92);
                    -fx-background-radius: 20;
                    -fx-border-radius: 20;
                    -fx-padding: 10;
                    -fx-font-size: 14px;
                """);
    }

    private ScrollPane getSummaryBox() {
        summaryContent.setPadding(new Insets(20));
        summaryContent.setSpacing(15);

        VBox container = getSummaryStyle();

        return getScrollPane(container);
    }

    private VBox getSummaryStyle() {
        VBox container = new VBox(summaryContent);
        container.setPadding(new Insets(15));
        container.setStyle("""
                    -fx-background-color: rgba(255,255,255,0.92);
                    -fx-background-radius: 20;
                    -fx-border-radius: 20;
                    -fx-border-color: rgba(0,0,0,0.08);
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 5);
                """);
        return container;
    }

    private static ScrollPane getScrollPane(VBox container) {
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("""
                    -fx-background: transparent;
                    -fx-background-color: transparent;
                """);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }

    private SplitPane getSplitPane(ScrollPane summaryBox) {
        SplitPane center = new SplitPane(medicineList, summaryBox);
        center.setDividerPositions(0.3);
        return center;
    }

    private HBox getInfoButtons() {
        commentsBtn.setStyle("""
                    -fx-background-color: linear-gradient(to right, #1e8449, #27ae60);
                    -fx-text-fill: white;
                    -fx-font-weight: bold;
                    -fx-background-radius: 12;
                    -fx-cursor: hand;
                """);
        HBox topButtons = new HBox(10, commentsBtn);
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
    }

    private void selectMedicine() {
        medicineList.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> {
                    currentMedicine = selected;
                    updateSummary(selected);
                }
        );
    }

    private void writeInSearchBar() {
        searchField.textProperty().addListener((obs, old, text)
                -> refreshList()
        );
    }

    private void pressCommentsButton() {
        commentsBtn.setOnAction(e -> {
            String med = medicineList.getSelectionModel().getSelectedItem();
            CommentsView view = new CommentsView(stage, dataMart, med);
            stage.setScene(new Scene(view.getRoot(), 1000, 650));
        });  // TODO: Poner la función extra
    }


    private void updateSummary(String medicine) {
        summaryContent.getChildren().clear();
        if (medicine == null) {
            return;
        }
        JointAnalysis result = dataMart.getMedicineJointAnalysis(medicine);
        Label title = getTitleLabel(medicine);
        VBox statsBox = getStatsBox(medicine);
        VBox analysisBox = getJointAnalysisBox(result);
        TitledPane commentsPane = getCommentsPane(medicine);
        TitledPane reactionsPane = getReactionsPane(medicine);

        summaryContent.getChildren().addAll(
                title,
                statsBox,
                analysisBox,
                commentsPane,
                reactionsPane
        );
    }

    private TitledPane getCommentsPane(String medicine) {
        TitledPane commentsPane = buildCommentsPane(medicine);
        commentsPane.setExpanded(commentsExpanded);
        commentsPane.expandedProperty().addListener((obs, oldVal, newVal)
                -> commentsExpanded = newVal
        );
        return commentsPane;
    }

    private TitledPane getReactionsPane(String medicine) {
        TitledPane reactionsPane = buildReactionsPane(medicine);
        reactionsPane.setExpanded(reactionsExpanded);
        reactionsPane.expandedProperty().addListener((obs, oldVal, newVal)
                -> reactionsExpanded = newVal
        );
        return reactionsPane;
    }

    private static Label getTitleLabel(String medicine) {
        Label title = new Label("💊 " + medicine);
        title.setStyle("""
                    -fx-font-size: 24px;
                    -fx-font-weight: bold;
                    -fx-text-fill: #145a32;
                """);
        return title;
    }

    private VBox getStatsBox(String medicine) {
        return createCard("""
                📊 General Statistics
                
                • Reactions detected: %d
                • User comments: %d
                """.formatted(
                dataMart.getMedicineReactions(medicine).size(),
                dataMart.getMedicineComments(medicine).size()
        ));
    }

    private VBox getJointAnalysisBox(JointAnalysis result) {
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
        Label label = new Label(text);
        label.setWrapText(true);
        label.setStyle("""
                    -fx-font-size: 14px;
                    -fx-text-fill: #2c3e50;
                    -fx-line-spacing: 4px;
                """);

        VBox box = new VBox(label);
        box.setPadding(new Insets(15));
        box.setStyle("""
                    -fx-background-color: white;
                    -fx-background-radius: 15;
                    -fx-border-radius: 15;
                    -fx-border-color: rgba(0,0,0,0.05);
                """);
        return box;
    }

    private TitledPane buildCommentsPane(String medicine) {
        commentsListView = new ListView<>();

        commentsListView.setItems(FXCollections.observableArrayList(
                dataMart.getMedicineComments(medicine)
        ));
        commentsListView.setPrefHeight(250);
        commentsListView.setCellFactory(lv -> createCommentCell());

        return createTitledPane("💬 Comments", commentsListView);
    }

    private ListCell<Comment> createCommentCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Comment comment, boolean empty) {
                super.updateItem(comment, empty);

                if (empty || comment == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(buildCommentRow(comment));
            }
        };
    }

    private HBox buildCommentRow(Comment comment) {
        Label icon = createSentimentLabel(comment);

        VBox textBox = new VBox(
                createAuthorLabel(comment),
                createDateLabel(comment),
                createCommentText(comment)
        );
        textBox.setSpacing(4);

        HBox row = new HBox(10, icon, textBox);
        row.setAlignment(Pos.TOP_LEFT);
        return row;
    }

    private Label createSentimentLabel(Comment comment) {
        Label label = new Label(sentimentIcon(comment));
        label.setStyle("-fx-font-size: 20px;");
        return label;
    }

    private Label createAuthorLabel(Comment comment) {
        Label label = new Label(comment.getAuthor());
        label.setStyle("-fx-font-weight: bold;");
        return label;
    }

    private Label createDateLabel(Comment comment) {
        Label label = new Label(comment.getDate());
        label.setStyle("-fx-text-fill: gray; -fx-font-size: 11px;");
        return label;
    }

    private Label createCommentText(Comment comment) {
        Label label = new Label(comment.getText());
        label.setWrapText(true);
        return label;
    }

    private TitledPane createTitledPane(String title, Region content) {
        TitledPane pane = new TitledPane(title, content);
        pane.setExpanded(false);
        return pane;
    }

    private String sentimentIcon(Comment comment) {
        return switch (comment.getSentiment()) {
            case "Positive" -> "😊";
            case "Negative" -> "😞";
            default -> "😐";
        };
    }


    private TitledPane buildReactionsPane(String medicine) {
        reactionsListView = new ListView<>();

        reactionsListView.setItems(FXCollections.observableArrayList(
                dataMart.getMedicineReactions(medicine)
        ));
        reactionsListView.setPrefHeight(250);
        reactionsListView.setCellFactory(lv -> createReactionCell());

        return createTitledPane("⚠ Reactions", reactionsListView);
    }

    private ListCell<String> createReactionCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(String reaction, boolean empty) {
                super.updateItem(reaction, empty);

                if (empty || reaction == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(createReactionLabel(reaction));
            }
        };
    }

    private Label createReactionLabel(String reaction) {
        Label label = new Label("⚠ " + reaction);
        label.setWrapText(true);
        label.setStyle("""
            -fx-font-size: 14px;
            -fx-padding: 8;
        """);
        return label;
    }

    private void startAutoRefresh() {
        Timeline refresher = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> refreshList())
        );
        refresher.setCycleCount(Animation.INDEFINITE);
        refresher.play();
    }

    private void refreshList() {
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
        if (currentMedicine != null) {  // TODO: Hacer que no se reinicie la barra desplegable

            if (commentsListView != null) {
                commentsListView.setItems(FXCollections.observableArrayList(
                        dataMart.getMedicineComments(currentMedicine)
                ));
            }

            if (reactionsListView != null) {
                reactionsListView.setItems(FXCollections.observableArrayList(
                        dataMart.getMedicineReactions(currentMedicine)
                ));  // TODO: Refactorizar
            }
        }
    }
}

