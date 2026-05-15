package viper.sentinox.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import viper.sentinox.control.datamart.MedicineDataMart;
import viper.sentinox.model.Comment;
import javafx.scene.layout.Priority;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class CommentsSection {

    private final MedicineDataMart dataMart;

    private final VBox container = new VBox(10);
    private final VBox content = new VBox(10);

    private boolean expanded = false;

    public CommentsSection(MedicineDataMart dataMart) {
        this.dataMart = dataMart;
        content.setFillWidth(true);
    }

    public VBox getView(String medicine) {
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

        setTextContent(medicine);
        content.setVisible(expanded);
        content.setManaged(expanded);
    }

    private void setTextContent(String medicine) {
        dataMart.getMedicineComments(medicine)
                .stream()
                .sorted(getDateOrder())
                .forEach(comment ->
                        content.getChildren().add(
                                buildCommentCard(comment)
                        )
                );
    }

    private static Comparator<Comment> getDateOrder() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "dd MMM yyyy, HH:mm"
        );

        return Comparator.comparing(
                (Comment comment) ->
                        LocalDateTime.parse(
                                comment.getDate(),
                                formatter
                        )
        ).reversed();
    }

    private Button buildToggleButton(String medicine) {
        Button toggle = new Button(
                expanded
                        ? "▼ Comments"
                        : "▶ Comments"
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

    private VBox buildCommentCard(Comment comment) {
        Label icon = getSentimentLabel(comment);

        Label author = getAuthorLabel(comment);
        Label date = getDateLabel(comment);
        Label text = getTextLabel(comment);
        VBox textBox = getCommentBox(author, date, text);

        HBox row = getCommentRow(icon, textBox);
        return getCommentCard(row);
    }

    private Label getSentimentLabel(Comment comment) {
        Label icon = new Label(getSentimentIcon(comment));
        icon.setStyle("-fx-font-size: 20px;");

        icon.setMinWidth(35);
        icon.setAlignment(Pos.TOP_CENTER);
        return icon;
    }

    private static Label getAuthorLabel(Comment comment) {
        Label author = new Label(comment.getAuthor());
        author.setStyle("""
                -fx-font-weight: bold;
                -fx-font-size: 14px;
        """);
        return author;
    }

    private static Label getDateLabel(Comment comment) {
        Label date = new Label(comment.getDate());
        date.setStyle("""
                -fx-font-size: 11px;
                -fx-text-fill: gray;
        """);
        return date;
    }

    private static Label getTextLabel(Comment comment) {
        Label text = new Label(comment.getText());
        text.setWrapText(true);
        return text;
    }

    private static VBox getCommentBox(Label author, Label date, Label text) {
        VBox textBox = new VBox(
                author,
                date,
                text
        );
        textBox.setSpacing(5);
        return textBox;
    }

    private static HBox getCommentRow(Label icon, VBox textBox) {
        HBox.setHgrow(textBox, Priority.ALWAYS);
        HBox row = new HBox(
                12,
                icon,
                textBox
        );
        row.setAlignment(Pos.TOP_LEFT);
        return row;
    }

    private static VBox getCommentCard(HBox row) {
        VBox card = new VBox(row);
        card.setPadding(new Insets(15));
        card.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 15;
                -fx-border-radius: 15;
                -fx-border-color: rgba(0,0,0,0.08);
        """);
        return card;
    }

    private String getSentimentIcon(Comment comment) {
        return switch (comment.getSentiment()) {
            case "Positive" -> "😊";
            case "Negative" -> "😞";
            default -> "😐";
        };
    }
}