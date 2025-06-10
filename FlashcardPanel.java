import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FlashcardPanel extends JPanel {
    private String front, back;
    private boolean showingFront = true;
    private double animationProgress = 0.0;
    private Timer flipTimer;
    private static final int FLIP_DURATION = 400; // ms
    private static final int FPS = 60;

    public FlashcardPanel(String front, String back) {
        this.front = front;
        this.back = back;
        setOpaque(false);
        setPreferredSize(new Dimension(300, 180));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (flipTimer == null || !flipTimer.isRunning()) {
                    startFlip();
                }
            }
        });
    }

    private void startFlip() {
        final long startTime = System.currentTimeMillis();
        final boolean wasFront = showingFront;
        flipTimer = new Timer(1000 / FPS, null);
        flipTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            animationProgress = Math.min(1.0, elapsed / (double) FLIP_DURATION);
            if (animationProgress >= 1.0) {
                showingFront = !wasFront;
                flipTimer.stop();
                animationProgress = 0.0;
            }
            repaint();
        });
        flipTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        double progress = animationProgress;
        boolean flipping = flipTimer != null && flipTimer.isRunning();

        double angle;
        boolean showFrontNow;
        if (flipping) {
            if (progress < 0.5) {
                angle = Math.PI * progress;
                showFrontNow = showingFront;
            } else {
                angle = Math.PI * progress;
                showFrontNow = !showingFront;
            }
        } else {
            angle = 0;
            showFrontNow = showingFront;
        }

        int arc = 24;
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillRoundRect(8, 12, w - 16, h - 16, arc, arc);

        g2.translate(w / 2, h / 2);

        // Simulate 3D flip: scale X from 1 to 0 to -1
        double scaleX = Math.cos(angle);
        g2.scale(scaleX, 1);

        g2.setColor(showFrontNow ? new Color(255, 255, 240) : new Color(204, 229, 255));
        g2.fillRoundRect(-w / 2 + 4, -h / 2 + 4, w - 8, h - 8, arc, arc);

        g2.setColor(new Color(150, 180, 220, 90));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(-w / 2 + 4, -h / 2 + 4, w - 8, h - 8, arc, arc);

        g2.setColor(new Color(60, 60, 80));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
        String text = showFrontNow ? front : back;
        drawCenteredString(g2, text, new Rectangle(-w/2 + 16, -h/2 + 16, w-32, h-32), g2.getFont());

        g2.dispose();
    }

    private void drawCenteredString(Graphics2D g2, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g2.getFontMetrics(font);
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        for (String line : text.split("\n")) {
            int x = rect.x + (rect.width - metrics.stringWidth(line)) / 2;
            g2.drawString(line, x, y);
            y += metrics.getHeight();
        }
    }
}