package Presentation;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Metier.SingletonConnection;

public class LoginFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtLogin;
    private JPasswordField txtpwd;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LoginFrame() {
        initializeUI();
        setupLayout();
    }

    private void initializeUI() {
        setTitle("Authentification");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 0, 139)); // Bleu foncé
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);
    }

    private void setupLayout() {
        contentPane.setLayout(new GridBagLayout());
        Insets insets = new Insets(10, 10, 10, 10);

        // === Titre ===
        JLabel lblTitle = new JLabel("Authentification", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.insets = insets;
        gbcTitle.fill = GridBagConstraints.HORIZONTAL;
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        gbcTitle.gridwidth = 2;
        contentPane.add(lblTitle, gbcTitle);

        // === Login ===
        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setForeground(Color.WHITE);
        lblLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
        GridBagConstraints gbcLoginLabel = new GridBagConstraints();
        gbcLoginLabel.insets = insets;
        gbcLoginLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcLoginLabel.gridx = 0;
        gbcLoginLabel.gridy = 1;
        contentPane.add(lblLogin, gbcLoginLabel);

        txtLogin = new JTextField();
        txtLogin.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtLogin.setPreferredSize(new Dimension(200, 30));
        GridBagConstraints gbcLoginField = new GridBagConstraints();
        gbcLoginField.insets = insets;
        gbcLoginField.fill = GridBagConstraints.HORIZONTAL;
        gbcLoginField.gridx = 1;
        gbcLoginField.gridy = 1;
        gbcLoginField.weightx = 1.0;
        contentPane.add(txtLogin, gbcLoginField);

        // === Mot de passe ===
        JLabel lblPassword = new JLabel("Mot de passe:");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        GridBagConstraints gbcPasswordLabel = new GridBagConstraints();
        gbcPasswordLabel.insets = insets;
        gbcPasswordLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcPasswordLabel.gridx = 0;
        gbcPasswordLabel.gridy = 2;
        contentPane.add(lblPassword, gbcPasswordLabel);

        txtpwd = new JPasswordField();
        txtpwd.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtpwd.setPreferredSize(new Dimension(200, 30));
        GridBagConstraints gbcPasswordField = new GridBagConstraints();
        gbcPasswordField.insets = insets;
        gbcPasswordField.fill = GridBagConstraints.HORIZONTAL;
        gbcPasswordField.gridx = 1;
        gbcPasswordField.gridy = 2;
        gbcPasswordField.weightx = 1.0;
        contentPane.add(txtpwd, gbcPasswordField);

        // === Bouton Valider ===
        JButton btnValider = createValiderButton();
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.insets = insets;
        gbcButton.gridx = 0;
        gbcButton.gridy = 3;
        gbcButton.gridwidth = 2;
        gbcButton.anchor = GridBagConstraints.CENTER;
        contentPane.add(btnValider, gbcButton);

        // Listener pour Entrée
        setupEnterKeyListener();
    }

    private JButton createValiderButton() {
        JButton btnValider = new JButton("Valider");
        btnValider.setBackground(new Color(0, 191, 255)); // Bleu clair
        btnValider.setForeground(Color.BLACK);
        btnValider.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnValider.setPreferredSize(new Dimension(120, 35));
        btnValider.setFocusPainted(false);

        btnValider.addActionListener(e -> authenticateUser());
        return btnValider;
    }

    private void setupEnterKeyListener() {
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    authenticateUser();
                }
            }
        };
        txtLogin.addKeyListener(enterListener);
        txtpwd.addKeyListener(enterListener);
    }

    private void authenticateUser() {
        String login = txtLogin.getText().trim();
        String pwd = new String(txtpwd.getPassword()).trim();

        if (login.isEmpty() || pwd.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Veuillez remplir tous les champs!",
                "Champs manquants",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = SingletonConnection.getConnection()) {
            String query = "SELECT login, mdp FROM utilisateur WHERE login = ? AND mdp = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, login);
                ps.setString(2, pwd);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this,
                            "Connexion réussie au catalogue!!!",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        CatalogueSwing cs = new CatalogueSwing();
                        cs.setTitle("Catalogue");
                        cs.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Identifiant et/ou mot de passe erroné(s)!!!",
                            "Erreur d'authentification",
                            JOptionPane.ERROR_MESSAGE);
                        txtpwd.setText("");
                        txtLogin.requestFocus();
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Erreur de connexion à la base de données: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur inattendue: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
