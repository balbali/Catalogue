package Presentation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import Metier.MetierCatalogueImp;
import Metier.Produit;
import Metier.ProduitModel;
import Metier.SingletonConnection;

public class CatalogueSwing extends JFrame {
	private JLabel jLabelMc=new JLabel("Mot clé:");
	private JTextField txtMc=new JTextField(12);
	private JTextField txtID=new JTextField(2);
	private JButton btnOK=new JButton("OK");
	private JButton btnDeconnexion=new JButton("Se déconnecter");
	private JTable jTable=new JTable();
	private ProduitModel produitModel;
	JComboBox jCombo=new JComboBox();
	private MetierCatalogueImp metier=new MetierCatalogueImp();
	private void remplirCombo() {
		try {
			String Monsql="select * from categorie";
		    Connection conn=SingletonConnection.getConnetion();
			PreparedStatement ps=conn.prepareStatement(Monsql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				jCombo.addItem(rs.getString("nomCategorie"));
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	public CatalogueSwing() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		remplirCombo();
		this.setLayout(new BorderLayout());
		JPanel jPannelN=new JPanel();
		jPannelN.setLayout(new FlowLayout());
		jPannelN.add(jLabelMc);jPannelN.add(txtMc);jPannelN.add(btnOK); jPannelN.add(jCombo);jPannelN.add(txtID); jPannelN.add(btnDeconnexion);
		this.add(jPannelN,BorderLayout.NORTH);
		produitModel=new ProduitModel();
		jTable=new JTable(produitModel);
		JScrollPane jScrol=new JScrollPane(jTable);
		this.add(jScrol,BorderLayout.CENTER);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setVisible(true);
		
		btnOK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String mc=txtMc.getText();
				List<Produit>produits=metier.getProduitsParMotCle(mc);
				produitModel.loadData(produits);
				
			}
		});
		btnDeconnexion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				LoginFrame lf=new LoginFrame();
				lf.setTitle("Authentification");
				lf.setVisible(true);
				
			}
		});
		jCombo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			  String query="select * from categorie where nomCategorie like ?";
			  Connection conn=SingletonConnection.getConnetion();
				try {
					PreparedStatement ps=conn.prepareStatement(query);
					String nomCat=(String) jCombo.getSelectedItem();
					ps.setString(1, nomCat);
					ResultSet rs=ps.executeQuery();
					while(rs.next()) {
						int idCategorie=rs.getInt("idCategorie");
						List<Produit> lesProduits=metier.getProduitsParIDCategorie(idCategorie);
						produitModel.loadData(lesProduits);
						txtID.setText(String.valueOf(rs.getInt("idCategorie")));
						txtID.setEditable(false);
					}
					remplirCombo();
					
					
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			  
				
			}
		});
		
	}
//	public static void main(String[] args) {
//		new CatalogueSwing();
//	}
	

	// Méthode pour récupérer la liste des produits (à adapter)

}
