package ui;

import db.FlashcardDatabase;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Flashcard;

import java.io.File;
import java.util.List;

public class FlashcardApp extends Application {
    private FlashcardDatabase db = new FlashcardDatabase();
    private List<Flashcard> flashcards;
    private int currentIndex = 0;
    private Label questionLabel;
    private Label answerLabel;
    private Scene scene;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        flashcards = db.getAllFlashcards();
        questionLabel = new Label();
        answerLabel = new Label();

        Button showAnswerBtn = new Button("Show Answer");
        showAnswerBtn.setOnAction(e -> answerLabel.setVisible(true));

        Button nextBtn = new Button("Next");
        nextBtn.setOnAction(e -> showNextCard());

        Button addBtn = new Button("Add Flashcard");
        addBtn.setOnAction(e -> showAddDialog());

        ComboBox<String> themeSelector = new ComboBox<>();
        themeSelector.getItems().addAll("Light", "Dark", "Dark Red/Yellow");
        themeSelector.setValue("Light");
        themeSelector.setOnAction(e -> setTheme(themeSelector.getValue()));

        VBox vbox = new VBox(20, themeSelector, questionLabel, answerLabel, showAnswerBtn, nextBtn, addBtn);
        vbox.setPadding(new Insets(40));
        vbox.setStyle("-fx-alignment: center;");

        scene = new Scene(vbox, 420, 340);
        setTheme("Light"); // Default theme

        updateFlashcardView();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Flingo Flashcards");
        primaryStage.show();
    }

    private void setTheme(String theme) {
        scene.getStylesheets().clear();
        String themeFile;
        if ("Dark Red/Yellow".equals(theme)) {
            themeFile = "src/ui/themes/dark-red-yellow-theme.css";
        } else if ("Dark".equals(theme)) {
            themeFile = "src/ui/themes/dark-theme.css";
        } else {
            themeFile = "src/ui/themes/light-theme.css";
        }
        File f = new File(themeFile);
        scene.getStylesheets().add(f.toURI().toString());
    }

    private void updateFlashcardView() {
        if (flashcards.isEmpty()) {
            questionLabel.setText("No flashcards available.");
            answerLabel.setText("");
            answerLabel.setVisible(false);
            return;
        }
        Flashcard fc = flashcards.get(currentIndex);
        questionLabel.setText("Q: " + fc.getQuestion());
        answerLabel.setText("A: " + fc.getAnswer());
        answerLabel.setVisible(false);
    }

    private void showNextCard() {
        if (flashcards.isEmpty()) return;
        currentIndex = (currentIndex + 1) % flashcards.size();
        updateFlashcardView();
    }

    private void showAddDialog() {
        Dialog<Flashcard> dialog = new Dialog<>();
        dialog.setTitle("Add Flashcard");

        Label qLabel = new Label("Question:");
        TextField qField = new TextField();
        Label aLabel = new Label("Answer:");
        TextField aField = new TextField();

        VBox vbox = new VBox(10, qLabel, qField, aLabel, aField);
        dialog.getDialogPane().setContent(vbox);

        ButtonType addBtnType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtnType, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == addBtnType) {
                return new Flashcard(qField.getText(), aField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(fc -> {
            db.addFlashcard(fc);
            flashcards = db.getAllFlashcards();
            currentIndex = flashcards.size() - 1;
            updateFlashcardView();
        });
    }
}