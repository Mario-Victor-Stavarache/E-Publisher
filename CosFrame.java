package eeditura;

import javax.swing.*;
import java.awt.*;

public class CosFrame extends JFrame {
    public CosFrame() {
        setTitle("Shopping cart");
        setSize(500, 300);
        setLocationRelativeTo(null);

        JTextArea taCos = new JTextArea();
        taCos.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taCos);
        add(scrollPane, BorderLayout.CENTER);

        CosCumparaturi cos = new CosCumparaturi();

        StringBuilder sb = new StringBuilder("Books in cart:\n\n");
        for (Carte c : cos.getCarti()) {
            sb.append(c).append("\n\n");
        }
        sb.append("Total: $").append(String.format("%.2f", cos.getTotal()));

        taCos.setText(sb.toString());
    }
}
