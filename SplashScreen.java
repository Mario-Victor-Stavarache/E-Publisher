package eeditura;

import javax.swing.*;
import java.awt.*;

class SplashScreen extends JWindow {
    private float alpha = 0f;
    private int tick = 0;
    private final Timer timer;
    private final Color accent;

    SplashScreen() {
        Color[] palette = new Color[]{
                new Color(98, 84, 243),   // indigo
                new Color(69, 170, 242),  // sky blue
                new Color(255, 180, 0),   // amber
                new Color(48, 209, 88)    // green
        };
        accent = palette[(int) (Math.random() * palette.length)];

        setSize(480, 300);
        setLocationRelativeTo(null);

        timer = new Timer(16, new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                tick++;
                // 0-30: fade in, 30-110: hold, 110-150: fade out
                if (tick <= 30) {
                    alpha = tick / 30f;
                } else if (tick <= 110) {
                    alpha = 1f;
                } else if (tick <= 150) {
                    alpha = Math.max(0f, 1f - (tick - 110) / 40f);
                } else {
                    timer.stop();
                    dispose();
                    onFinished();
                }
                repaint();
            }
        });
    }

    void start() {
        setVisible(true);
        timer.start();
    }

    protected void onFinished() {}

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Background gradient
        Color top = new Color(247, 250, 255);
        Color bottom = new Color(222, 228, 255);
        g2.setPaint(new GradientPaint(0, 0, withAlpha(top, alpha), 0, h, withAlpha(bottom, alpha)));
        g2.fillRect(0, 0, w, h);

        // Abstract logo: overlapping circles and a ring
        int cx = w / 2;
        int cy = h / 2 - 10;
        int r1 = 48;
        int r2 = 28;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, alpha - 0.05f)));
        g2.setColor(new Color(255, 214, 10)); // yellow accent echo (matches confetti)
        g2.fillOval(cx - r1, cy - r1, r1 * 2, r1 * 2);
        g2.setColor(withAlpha(accent, alpha));
        g2.fillOval(cx - r2, cy - r2, r2 * 2, r2 * 2);

        // Outer ring
        g2.setStroke(new BasicStroke(4f));
        g2.setColor(withAlpha(new Color(30, 35, 45), alpha * 0.9f));
        g2.drawOval(cx - r1 - 16, cy - r1 - 16, (r1 + 16) * 2, (r1 + 16) * 2);

        // App name
        g2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 26));
        String title = "E-Publisher";
        int tw = g2.getFontMetrics().stringWidth(title);
        g2.setColor(withAlpha(new Color(30, 35, 45), alpha));
        g2.drawString(title, cx - tw / 2, cy + r1 + 40);

        g2.dispose();
    }

    private static Color withAlpha(Color c, float a) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.min(255, Math.max(0, (int) (a * 255))));
    }
}



