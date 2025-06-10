import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ModernDashboardFrame extends JFrame {
    User user;
    JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 32, 32));

    public ModernDashboardFrame(User user) {
        this.user = user;
        setTitle("Flingo - Modern Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardPanel.setBackground(new Color(245, 249, 255));
        JScrollPane scroll = new JScrollPane(cardPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        topBar.setBackground(new Color(60, 90, 180));
        topBar.add(new JLabel("<html><span style='color:white;font-size:22px;font-family:Segoe UI;'>Welcome, " + user.username + "</span></html>"));

        JButton addBtn = new JButton("Add Card");
        styleButton(addBtn);
        addBtn.addActionListener(e -> addOrEditCard(null));
        topBar.add(addBtn);

        JButton refreshBtn = new JButton("Refresh");
        styleButton(refreshBtn);
        refreshBtn.addActionListener(e -> loadCards());
        topBar.add(refreshBtn);

        add(topBar, BorderLayout.NORTH);

        loadCards();
        setVisible(true);
    }

    private void styleButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(240, 245, 255));
        btn.setForeground(new Color(50, 70, 120));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void loadCards() {
        cardPanel.removeAll();
        try (Connection con = DB.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT id, front_text, back_text, language FROM flashcards WHERE user_id=?");
            ps.setInt(1, user.id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int fid = rs.getInt(1);
                String front = rs.getString(2);
                String back = rs.getString(3);
                String lang = rs.getString(4);

                JPanel wrapper = new JPanel();
                wrapper.setLayout(new BorderLayout());
                wrapper.setOpaque(false);
                FlashcardPanel fpanel = new FlashcardPanel(front, back);
                wrapper.add(fpanel, BorderLayout.CENTER);

                JPanel btns = new JPanel();
                btns.setOpaque(false);

                JButton edit = new JButton("Edit");
                styleButton(edit);
                edit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                edit.addActionListener(e -> addOrEditCard(new Flashcard(fid, front, back, lang)));

                JButton del = new JButton("Delete");
                styleButton(del);
                del.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                del.addActionListener(e -> deleteCard(fid));

                btns.add(edit);
                btns.add(del);

                wrapper.add(btns, BorderLayout.SOUTH);
                cardPanel.add(wrapper);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private void addOrEditCard(Flashcard card) {
        JTextField front = new JTextField(card != null ? card.front : "");
        JTextField back = new JTextField(card != null ? card.back : "");
        JTextField lang = new JTextField(card != null ? card.language : "");
        Object[] fields = {
                "Front:", front,
                "Back:", back,
                "Language:", lang
        };
        int res = JOptionPane.showConfirmDialog(this, fields, card == null ? "Add Flashcard" : "Edit Flashcard", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try (Connection con = DB.getConnection()) {
                if (card == null) {
                    PreparedStatement ps = con.prepareStatement("INSERT INTO flashcards (user_id, front_text, back_text, language) VALUES (?, ?, ?, ?)");
                    ps.setInt(1, user.id);
                    ps.setString(2, front.getText());
                    ps.setString(3, back.getText());
                    ps.setString(4, lang.getText());
                    ps.executeUpdate();
                } else {
                    PreparedStatement ps = con.prepareStatement("UPDATE flashcards SET front_text=?, back_text=?, language=? WHERE id=? AND user_id=?");
                    ps.setString(1, front.getText());
                    ps.setString(2, back.getText());
                    ps.setString(3, lang.getText());
                    ps.setInt(4, card.id);
                    ps.setInt(5, user.id);
                    ps.executeUpdate();
                }
                loadCards();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deleteCard(int cardId) {
        int res = JOptionPane.showConfirmDialog(this, "Delete this card?", "Delete", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            try (Connection con = DB.getConnection()) {
                PreparedStatement ps = con.prepareStatement("DELETE FROM flashcards WHERE id=? AND user_id=?");
                ps.setInt(1, cardId);
                ps.setInt(2, user.id);
                ps.executeUpdate();
                loadCards();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}