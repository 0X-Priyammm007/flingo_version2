package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Flashcard;

public class FlashcardDatabase {
    private static final String DB_URL = "jdbc:sqlite:flashcards.db";

    public FlashcardDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String createTable = "CREATE TABLE IF NOT EXISTS flashcards " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, answer TEXT)";
            conn.createStatement().execute(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addFlashcard(Flashcard flashcard) {
        String sql = "INSERT INTO flashcards(question, answer) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, flashcard.getQuestion());
            pstmt.setString(2, flashcard.getAnswer());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Flashcard> getAllFlashcards() {
        List<Flashcard> flashcards = new ArrayList<>();
        String sql = "SELECT * FROM flashcards";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                flashcards.add(new Flashcard(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("answer")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flashcards;
    }
}