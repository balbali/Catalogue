package Presentation;

import java.awt.EventQueue;

import javax.swing.border.EmptyBorder;

import Metier.SingletonConnection;

import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Login3 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtLogin;
	private JPasswordField txtmdp;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login3 frame = new Login3();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login3() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLUE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Authentification");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 23));
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(115, 10, 209, 83);
		contentPane.add(lblNewLabel);
		
		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblLogin.setForeground(Color.WHITE);
		lblLogin.setBounds(49, 99, 134, 21);
		contentPane.add(lblLogin);
		
		txtLogin = new JTextField();
		txtLogin.setBounds(153, 95, 262, 36);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);
		
		JLabel lblMotDePasse = new JLabel("Mot de passe:");
		lblMotDePasse.setForeground(Color.WHITE);
		lblMotDePasse.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblMotDePasse.setBounds(10, 164, 134, 21);
		contentPane.add(lblMotDePasse);
		
		txtmdp = new JPasswordField();
		txtmdp.setBounds(153, 160, 262, 36);
		contentPane.add(txtmdp);
		
		JButton btnValider = new JButton("Valider");
		btnValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				authentificateUser();
			}
		});
		
	
		btnValider.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnValider.setForeground(Color.BLUE);
		btnValider.setBounds(185, 216, 122, 37);
		contentPane.add(btnValider);
	}
	private void authentificateUser() {
		String login = txtLogin.getText().trim();
        String pwd = new String(txtmdp.getPassword()).trim();

        // Validation des champs
        if (login.isEmpty() || pwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez remplir tous les champs!", 
                "Champs manquants", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
			Connection conn=SingletonConnection.getConnection();
			String query="Select login, mdp from utilisateur where login=? and mdp=?";
			PreparedStatement ps=conn.prepareStatement(query);
			ps.setString(1, login);
			ps.setString(2,pwd);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				//Authentification réussie
				JOptionPane.showMessageDialog(this, 
						"Connexion réussie au catalogue!!!",
						 "succès",JOptionPane.WARNING_MESSAGE);
				dispose();
				CatalogueSwing cs=new CatalogueSwing();
				cs.setTitle("Catalogue");
				cs.setVisible(true);


				
				
				
			} else {
				JOptionPane.showMessageDialog(this, 
		                "Identifiant et/ou mot de passe erroné(s)!!!", 
		                "Erreur d'authentification", 
		                JOptionPane.ERROR_MESSAGE);
				txtmdp.setText("");
				txtLogin.requestFocus();
			}
		} catch (Exception e) {
			  JOptionPane.showMessageDialog(this, 
		                "Erreur inattendue de connexion: " + e.getMessage(), 
		                "Erreur", 
		                JOptionPane.ERROR_MESSAGE);
		            e.printStackTrace();
			
		}
        
	}

}
