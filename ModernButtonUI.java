package eeditura;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernButtonUI extends BasicButtonUI {
    private static final int RADIUS = 12;

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(false);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = c.getWidth();
        int h = c.getHeight();
        Color base = b.getBackground() != null ? b.getBackground() : new Color(98, 84, 243);
        boolean pressed = b.getModel().isArmed() && b.getModel().isPressed();
        boolean rollover = b.getModel().isRollover();
        Color top = adjustBrightness(base, pressed ? -0.08f : rollover ? 0.04f : 0.0f);
        Color bottom = adjustBrightness(base, pressed ? -0.18f : -0.08f);
        RoundRectangle2D shape = new RoundRectangle2D.Float(0, 0, w, h, RADIUS, RADIUS);
        g2.setPaint(new GradientPaint(0, 0, top, 0, h, bottom));
        g2.fill(shape);
        g2.setPaint(new Color(255, 255, 255, pressed ? 60 : 90));
        g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1f, h - 1f, RADIUS, RADIUS));
        g2.setPaint(new Color(0, 0, 0, 35));
        g2.draw(new RoundRectangle2D.Float(1.5f, 1.5f, w - 3f, h - 3f, RADIUS, RADIUS));
        FontMetrics fm = g2.getFontMetrics(b.getFont());
        String text = b.getText();
        int textX = (w - fm.stringWidth(text)) / 2;
        int textY = (h + fm.getAscent() - fm.getDescent()) / 2;
        g2.setColor(b.getForeground() != null ? b.getForeground() : Color.WHITE);
        g2.drawString(text, textX, textY);
        g2.dispose();
    }

    private static Color adjustBrightness(Color c, float delta) {
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        float b = Math.max(0f, Math.min(1f, hsb[2] + delta));
        int rgb = Color.HSBtoRGB(hsb[0], hsb[1], b);
        return new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
    }
}



