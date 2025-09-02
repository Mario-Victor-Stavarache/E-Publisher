package eeditura;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class AdaugaCarteFrame extends JFrame {
    public AdaugaCarteFrame(Catalog catalog) {
        setTitle("Add new book");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField tfTitlu = new JTextField();
        JTextField tfAutori = new JTextField();
        JTextField tfAn = new JTextField();
        JTextField tfPagini = new JTextField();
        JTextField tfPret = new JTextField();
        JTextField tfLivrare = new JTextField();
        JTextField tfRecenzii = new JTextField();

        JButton btnAdauga = new JButton("Add book");

        panel.add(new JLabel("Title:")); panel.add(tfTitlu);
        panel.add(new JLabel("Authors (comma):")); panel.add(tfAutori);
        panel.add(new JLabel("Publication year:")); panel.add(tfAn);
        panel.add(new JLabel("Page count:")); panel.add(tfPagini);
        panel.add(new JLabel("Price (USD):")); panel.add(tfPret);
        panel.add(new JLabel("Delivery time (days):")); panel.add(tfLivrare);
        panel.add(new JLabel("Reviews (comma):")); panel.add(tfRecenzii);
        panel.add(new JLabel("")); panel.add(btnAdauga);

        Container wrapped = ModernUI.wrapWithModernBackground(panel);
        setContentPane(wrapped);
        ModernUI.applyModernStyle(this);

        btnAdauga.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    String titlu = tfTitlu.getText().trim();
                    java.util.List<String> autori = Arrays.asList(tfAutori.getText().trim().split("\\s*,\\s*"));
                    int an = Integer.parseInt(tfAn.getText().trim());
                    int pagini = Integer.parseInt(tfPagini.getText().trim());
                    double pret = Double.parseDouble(tfPret.getText().trim());
                    int livrare = Integer.parseInt(tfLivrare.getText().trim());

                    List<String> recenzii = new ArrayList<>();
                    if (!tfRecenzii.getText().trim().isEmpty()) {
                        recenzii = Arrays.asList(tfRecenzii.getText().trim().split("\\s*,\\s*"));
                    }

                    Carte carte = new Carte(titlu, autori, an, pagini, pret, livrare, recenzii);
                    catalog.adaugaCarte(carte);

                    JOptionPane.showMessageDialog(AdaugaCarteFrame.this, "Book added successfully!");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AdaugaCarteFrame.this, "Error: Please check all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}