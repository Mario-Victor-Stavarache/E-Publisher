package eeditura;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PlasareComandaFrame extends JFrame {
    public PlasareComandaFrame(Client client, Catalog catalog, CosCumparaturi cos) {
        setTitle("Place Order");
        setSize(500, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(10, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Selectie carte
        JLabel lblCarte = new JLabel("Select book:");
        List<Carte> carti = catalog.getCarti();
        String[] titluri = carti.stream().map(Carte::getTitlu).toArray(String[]::new);
        JComboBox<String> cbCarti = new JComboBox<>(titluri);
        JButton btnAdaugaInCos = new JButton("Add to cart");
        JLabel lblTotal = new JLabel("Current total: $" + String.format("%.2f", cos.getTotal()));

        JLabel lblLivrare = new JLabel("Delivery type:");
        String[] optiuniLivrare = {"Standard mail", "Express mail (24h)"};
        JComboBox<String> cbLivrare = new JComboBox<>(optiuniLivrare);

        JLabel lblTipPlata = new JLabel("Payment type:");
        String[] tipuriPlata = {"Credit card", "Bank account"};
        JComboBox<String> cbTipPlata = new JComboBox<>(tipuriPlata);

        JLabel lbl1 = new JLabel("Detail 1:");
        JTextField tf1 = new JTextField();
        JLabel lbl2 = new JLabel("Detail 2:");
        JTextField tf2 = new JTextField();
        JLabel lbl3 = new JLabel("Detail 3:");
        JTextField tf3 = new JTextField();

        JButton btnPlaseaza = new JButton("Place order");

        panel.add(lblCarte); panel.add(cbCarti);
        panel.add(new JLabel()); panel.add(btnAdaugaInCos);
        panel.add(new JLabel()); panel.add(lblTotal);
        panel.add(lblLivrare); panel.add(cbLivrare);
        panel.add(lblTipPlata); panel.add(cbTipPlata);
        panel.add(lbl1); panel.add(tf1);
        panel.add(lbl2); panel.add(tf2);
        panel.add(lbl3); panel.add(tf3);
        panel.add(new JLabel()); panel.add(btnPlaseaza);

        Container wrapped = ModernUI.wrapWithModernBackground(panel);
        setContentPane(wrapped);
        ModernUI.applyModernStyle(this);

        btnAdaugaInCos.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                int idx = cbCarti.getSelectedIndex();
                if (idx >= 0 && idx < carti.size()) {
                    cos.adaugaCarte(carti.get(idx));
                    lblTotal.setText("Current total: $" + String.format("%.2f", cos.getTotal()));
                    JOptionPane.showMessageDialog(PlasareComandaFrame.this, "Book added to cart.");
                } else {
                    JOptionPane.showMessageDialog(PlasareComandaFrame.this, "Please select a valid book!");
                }
            }
        });

        cbTipPlata.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                String tip = (String) cbTipPlata.getSelectedItem();
                if ("Credit card".equals(tip)) {
                    lbl1.setText("Card type:");
                    lbl2.setText("Card number:");
                    lbl3.setText("Expiry (MM/YY):");
                    tf3.setEditable(true);
                } else {
                    lbl1.setText("Bank:");
                    lbl2.setText("Account number:");
                    lbl3.setText("");
                    tf3.setText("");
                    tf3.setEditable(false);
                }
            }
        });

        btnPlaseaza.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                String tipLivrare = (String) cbLivrare.getSelectedItem();
                String tipPlata = (String) cbTipPlata.getSelectedItem();

                Plata plata = null;

                if ("Credit card".equals(tipPlata)) {
                    plata = new PlataCardCredit(tf1.getText(), tf2.getText(), tf3.getText());
                } else if ("Bank account".equals(tipPlata)) {
                    plata = new PlataContBancar(tf1.getText(), tf2.getText());
                }

                if (plata == null) {
                    JOptionPane.showMessageDialog(PlasareComandaFrame.this, "Selectati o metoda valida de plata!");
                    return;
                }

                Comanda comanda = new Comanda(client, cos, tipLivrare, plata);
                if (comanda.proceseaza()) {
                    JOptionPane.showMessageDialog(PlasareComandaFrame.this, "Order processed successfully!\n" + comanda);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(PlasareComandaFrame.this, "Error processing payment.");
                }
            }
        });
    }
}
