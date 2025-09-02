package eeditura;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    @SuppressWarnings("unused")
    private final Catalog catalog;
    private final CosCumparaturi cos;

    public MainFrame(Client client, Catalog catalog) {
        this.cos = new CosCumparaturi();
        this.catalog = catalog;

        setTitle("E-Publisher - Welcome, " + client.getUsername());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnVizualizareCatalog = new JButton("View Catalog");
        JButton btnPlaseazaComanda = new JButton("Place Order");
        JButton btnLogout = new JButton("Log out");

        panel.add(btnVizualizareCatalog);
        panel.add(btnPlaseazaComanda);

        if ("admin".equals(client.getRol())) {
            JButton btnAdaugaCarte = new JButton("Add Book to Catalog");
            btnAdaugaCarte.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new AdaugaCarteFrame(catalog).setVisible(true);
                }
            });
            panel.add(btnAdaugaCarte);
        }

        panel.add(btnLogout);

        Container wrapped = ModernUI.wrapWithModernBackground(panel);
        setContentPane(wrapped);
        ModernUI.applyModernStyle(this);

        btnVizualizareCatalog.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this, catalog.toString());
            }
        });

        btnPlaseazaComanda.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new PlasareComandaFrame(client, catalog, cos).setVisible(true);
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame(catalog).setVisible(true);
            }
        });
    }

    public void adaugaCarteInCos(Carte carte) {
        cos.adaugaCarte(carte);
    }
}
