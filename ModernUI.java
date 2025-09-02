package eeditura;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ModernUI {
    public static void initGlobalLookAndFeel() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI Semibold", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 14));

        UIManager.put("Panel.background", new ColorUIResource(245, 248, 255));
        UIManager.put("OptionPane.background", new ColorUIResource(250, 252, 255));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(25, 25, 28));
    }

    public static void applyModernStyle(JFrame frame) {
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder());
        frame.getRootPane().setDoubleBuffered(true);
        ConfettiOverlay.install(frame);
        List<JButton> buttons = new ArrayList<>();
        walkAndStyle(frame.getContentPane(), buttons);
        for (JButton button : buttons) {
            installConfettiTrigger(frame, button);
        }
    }

    public static Container wrapWithModernBackground(Container content) {
        ModernBackgroundPanel background = new ModernBackgroundPanel();
        background.setLayout(new GridBagLayout());
        if (content instanceof JComponent) {
            ((JComponent) content).setOpaque(false);
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1; gbc.weighty = 1; gbc.fill = GridBagConstraints.BOTH;
        background.add(content, gbc);
        return background;
    }

    private static void installConfettiTrigger(JFrame frame, JButton button) {
        ActionListener sparkle = new ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                ConfettiOverlay overlay = ConfettiOverlay.get(frame);
                if (overlay != null) {
                    Rectangle b = new Rectangle(0, 0, button.getWidth(), button.getHeight());
                    Rectangle onOverlay = SwingUtilities.convertRectangle(button, b, overlay);
                    overlay.sparkleOutline(onOverlay);
                }
            }
        };
        button.addActionListener(sparkle);
    }

    private static void walkAndStyle(Container container, List<JButton> buttons) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                styleButton(b);
                buttons.add(b);
            } else if (c instanceof JLabel) {
                c.setForeground(new Color(30, 35, 45));
            }
            if (c instanceof Container) {
                walkAndStyle((Container) c, buttons);
            }
        }
    }

    public static void styleButton(JButton button) {
        button.setUI(new ModernButtonUI());
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(98, 84, 243));
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    static class ModernBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            Color top = new Color(247, 250, 255);
            Color bottom = new Color(222, 228, 255);
            GradientPaint gp = new GradientPaint(0, 0, top, 0, h, bottom);
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
            RadialGradientPaint rg = new RadialGradientPaint(new Point(w / 3, h / 4), Math.max(w, h) / 2f,
                    new float[]{0f, 1f}, new Color[]{new Color(255, 255, 255, 120), new Color(255, 255, 255, 0)});
            g2.setPaint(rg);
            g2.fillRect(0, 0, w, h);
            g2.dispose();
        }
    }
}


