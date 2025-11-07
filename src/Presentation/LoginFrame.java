package Presentation;

import java.awt.EventQueue;
import Metier.SingletonConnection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
/*
 * @author Balbali
 * @version 1.0
 */
public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtLogin;
	private JPasswordField txtmdp;
	private JButton btnValider=new JButton("Valider");

	/**
	 * Launch the application.
	 * Méthode Main
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	 public LoginFrame() {
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 setBounds(100, 100, 450, 300);
		 contentPane = new JPanel();
		 contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		 setContentPane(contentPane);
		 contentPane.setLayout(null);

		 JLabel lblNewLabel = new JLabel("Authetification");
		 lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
		 lblNewLabel.setBounds(90, 17, 261, 67);
		 contentPane.add(lblNewLabel);

		 JLabel lblNewLabel_1 = new JLabel("Login");
		 lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		 lblNewLabel_1.setBounds(78, 95, 70, 42);
		 contentPane.add(lblNewLabel_1);

		 JLabel lblNewLabel_2 = new JLabel("Mot de passe");
		 lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		 lblNewLabel_2.setBounds(78, 168, 109, 36);
		 contentPane.add(lblNewLabel_2);

		 txtLogin = new JTextField();
		 txtLogin.setBounds(197, 95, 134, 36);
		 contentPane.add(txtLogin);
		 txtLogin.setColumns(10);

		 txtmdp = new JPasswordField();
		 txtmdp.setBounds(197, 173, 134, 30);
		 contentPane.add(txtmdp);
		 btnValider.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		authenticateUser();
		 	}
		 });
		 btnValider.setFont(new Font("Tahoma", Font.BOLD, 16));
		 btnValider.setBounds(175, 214, 109, 38);
		 contentPane.add(btnValider);



		 JButton btnValider = new JButton("Valider");
		 btnValider.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 authenticateUser();
			 }
		 });





		 txtmdp.addActionListener(new ActionListener() {

			 @Override
			 public void actionPerformed(ActionEvent e) {
				 authenticateUser();

			 }
		 });

	 }
	/**
	 * Create the frame.
	 */private void authenticateUser() {
		 try {	String login=txtLogin.getText();
		 String mdp=txtmdp.getText();
		 Connection conn=SingletonConnection.getConnetion();
		 String query="select login, mdp from utilisateur where login=? and mdp=?";
		 PreparedStatement ps;

		 ps = conn.prepareStatement(query);
		 ps.setString(1, login);
		 ps.setString(2, mdp);
		 ResultSet rs=ps.executeQuery();
		 if(rs.next()) {
			 dispose();
			 CatalogueSwing cs=new CatalogueSwing();
			 cs.setTitle("Authentification");
			 cs.setVisible(true);
			 JOptionPane.showMessageDialog(btnValider,"Connexion réussie au catalogue!!!");

		 }else
		 {
			 JOptionPane.showMessageDialog(btnValider,"identifiat et/ou mot de passe erroné(s)!!!");
		 }

		 } catch (Exception e1) {
			 // TODO Auto-generated catch block
			 e1.printStackTrace();
		 } }


	

}

