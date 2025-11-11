package Presentation;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login4 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtLogin;
	private JPasswordField txtpwd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login4 frame = new Login4();
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
	public Login4() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLUE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitre = new JLabel("Authentification");
		lblTitre.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblTitre.setForeground(Color.WHITE);
		lblTitre.setBounds(121, 10, 194, 35);
		contentPane.add(lblTitre);
		
		JLabel lbLogin = new JLabel("Login:");
		lbLogin.setFont(new Font("Tahoma", Font.BOLD, 17));
		lbLogin.setForeground(Color.WHITE);
		lbLogin.setBounds(41, 66, 99, 29);
		contentPane.add(lbLogin);
		
		txtLogin = new JTextField();
		txtLogin.setBounds(170, 66, 232, 27);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);
		
		JLabel lblMotDePasse = new JLabel("Mot de passe:");
		lblMotDePasse.setForeground(Color.WHITE);
		lblMotDePasse.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblMotDePasse.setBounds(10, 123, 149, 29);
		contentPane.add(lblMotDePasse);
		
		txtpwd = new JPasswordField();
		txtpwd.setBounds(170, 123, 232, 27);
		contentPane.add(txtpwd);
		
		JButton btnValier = new JButton("Valider");
		btnValier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnValier.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnValier.setForeground(Color.BLUE);
		btnValier.setBounds(197, 179, 104, 35);
		contentPane.add(btnValier);
	}

}
