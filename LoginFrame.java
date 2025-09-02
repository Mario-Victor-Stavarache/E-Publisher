package eeditura;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class LoginFrame extends JFrame {
    private final Catalog catalog;
    private JTextField tfUsername;
    private JPasswordField pfParola;
    private JButton btnLogin, btnInregistrare;

    private static final String USERS_FILE = "utilizatori.dat";

    public LoginFrame(Catalog catalog) {
        this.catalog = catalog;
        setTitle("Sign In");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        adaugaAdminImplicit();

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblUsername = new JLabel("Username:");
        tfUsername = new JTextField();
        JLabel lblParola = new JLabel("Password:");
        pfParola = new JPasswordField();

        btnLogin = new JButton("Login");
        btnInregistrare = new JButton("Register");

        panel.add(lblUsername); panel.add(tfUsername);
        panel.add(lblParola); panel.add(pfParola);
        panel.add(btnLogin); panel.add(btnInregistrare);

        Container wrapped = ModernUI.wrapWithModernBackground(panel);
        setContentPane(wrapped);
        ModernUI.applyModernStyle(this);

        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { autentificare(); }
        });
        btnInregistrare.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { inregistrare(); }
        });
    }

    private void autentificare() {
        String user = tfUsername.getText().trim();
        String pass = new String(pfParola.getPassword());

        HashMap<String, Client> utilizatori = incarcaUtilizatori();
        Client utilizator = utilizatori.get(user);

        if (utilizator != null && utilizator.getParola() != null && utilizator.getParola().equals(pass)) {
            JOptionPane.showMessageDialog(this, "Login successful!");

            if ("admin".equals(utilizator.getRol())) {
                JOptionPane.showMessageDialog(this, "Logged in as administrator.");
            }

            MainFrame mainFrame = new MainFrame(utilizator, catalog);
            mainFrame.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect username or password.");
        }
    }

    private void inregistrare() {
        JTextField tfNume = new JTextField();
        JTextField tfPrenume = new JTextField();
        JTextField tfAdresa = new JTextField();
        JTextField tfUser = new JTextField();
        JPasswordField pfPass = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("First name:")); panel.add(tfNume);
        panel.add(new JLabel("Last name:")); panel.add(tfPrenume);
        panel.add(new JLabel("Address:")); panel.add(tfAdresa);
        panel.add(new JLabel("Username:")); panel.add(tfUser);
        panel.add(new JLabel("Password:")); panel.add(pfPass);

        int ok = JOptionPane.showConfirmDialog(this, panel, "Register", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            String username = tfUser.getText().trim();
            String parola = new String(pfPass.getPassword());

            if (username.isEmpty() || parola.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid fields!");
                return;
            }

            HashMap<String, Client> utilizatori = incarcaUtilizatori();
            if (utilizatori.containsKey(username)) {
                JOptionPane.showMessageDialog(this, "Username already used!");
                return;
            }

            Client nou = new Client(tfNume.getText(), tfPrenume.getText(), tfAdresa.getText());
            nou.setUsername(username);
            nou.setParola(parola);
            nou.setRol("client");

            utilizatori.put(username, nou);
            salveazaUtilizatori(utilizatori);
            JOptionPane.showMessageDialog(this, "Account created successfully!");
        }
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Client> incarcaUtilizatori() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            return (HashMap<String, Client>) in.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private void salveazaUtilizatori(HashMap<String, Client> utilizatori) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            out.writeObject(utilizatori);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adaugaAdminImplicit() {
        HashMap<String, Client> utilizatori = incarcaUtilizatori();
        if (!utilizatori.containsKey("admin")) {
            Client admin = new Client("Admin", "Admin", "N/A");
            admin.setUsername("admin");
            admin.setParola("admin");
            admin.setRol("admin");
            utilizatori.put("admin", admin);
            salveazaUtilizatori(utilizatori);
        }
    }
}
