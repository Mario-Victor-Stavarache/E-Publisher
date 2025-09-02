package eeditura;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ConfettiOverlay extends JComponent {
    private static final float GRAVITY = 0.22f;
    private static final float FRICTION = 0.985f;
    private static final int MAX_LIFE = 55;

    private final List<Particle> particles = new ArrayList<>();
    private final Timer timer;
    private final Random random = new Random();

    private ConfettiOverlay() {
        setOpaque(false);
        timer = new Timer(16, new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { tick(); }
        });
        timer.setRepeats(true);
        timer.start();
    }

    public static void install(JFrame frame) {
        JRootPane root = frame.getRootPane();
        if (get(frame) == null) {
            ConfettiOverlay overlay = new ConfettiOverlay();
            root.setGlassPane(overlay);
            overlay.setVisible(true);
            root.revalidate();
            root.repaint();
        }
    }

    public static ConfettiOverlay get(JFrame frame) {
        Component gp = frame.getRootPane().getGlassPane();
        if (gp instanceof ConfettiOverlay) {
            return (ConfettiOverlay) gp;
        }
        return null;
    }

    public void sparkleOutline(Rectangle r) {
        int step = 10;
        for (int x = r.x; x <= r.x + r.width; x += step) {
            emitEdgeParticle(x, r.y, 0, -1);
            emitEdgeParticle(x, r.y + r.height, 0, 1);
        }
        for (int y = r.y; y <= r.y + r.height; y += step) {
            emitEdgeParticle(r.x, y, -1, 0);
            emitEdgeParticle(r.x + r.width, y, 1, 0);
        }
        repaint();
    }

    private void emitEdgeParticle(int x, int y, int nx, int ny) {
        float speed = 2.0f + random.nextFloat() * 1.5f;
        float jitterX = (random.nextFloat() - 0.5f) * 0.6f;
        float jitterY = (random.nextFloat() - 0.5f) * 0.6f;
        float vx = nx * speed + jitterX;
        float vy = ny * speed + jitterY - 0.6f;
        particles.add(new Particle(x, y, vx, vy));
    }

    private void tick() {
        if (particles.isEmpty()) return;
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.vx *= FRICTION;
            p.vy = p.vy * FRICTION + GRAVITY;
            p.x += p.vx;
            p.y += p.vy;
            p.life++;
            if (p.life > MAX_LIFE || p.y > getHeight() + 40) {
                it.remove();
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (particles.isEmpty()) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Particle p : particles) {
            int size = 5;
            int alpha = Math.max(0, 200 - (int) (p.life * (200f / MAX_LIFE)));
            g2.setColor(new Color(255, 214, 10, alpha));
            g2.fillRect((int) p.x, (int) p.y, size, size);
        }
        g2.dispose();
    }

    private static class Particle {
        float x, y;
        float vx, vy;
        int life;
        
        Particle(float x, float y, float vx, float vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.life = 0;
        }
    }
}


