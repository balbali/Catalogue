package Metier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetierCatalogueImp implements IMetier {

	@Override
	public void addCategorie(Categorie c)  {
		Connection conn=SingletonConnection.getConnetion();		


		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO CATEGORIE (nomCategorie) values (?)");
			ps.setString(1, c.getNomCategorie());
			ps.executeUpdate();
			ps.close();


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void addProduit(Produit p, int idCategorie) {
		Connection conn=SingletonConnection.getConnetion();		
		try {
			PreparedStatement ps=conn.prepareStatement("insert into produit values (?,?,?,?,?)");
			ps.setString(1, p.getIdProduit());
			ps.setString(2, p.getNomProduit());
			ps.setDouble(3, p.getPrix());
			ps.setInt(4, p.getQuantite());
			ps.setInt(5, idCategorie);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public List<Produit> getProduitsParMotCle(String mc) {
		List<Produit> lesProduits=new ArrayList<Produit>();
		Connection conn=SingletonConnection.getConnetion();

		try {
			PreparedStatement ps=conn.prepareStatement("Select * from produit where designationProduit like ? ");
			ps.setString(1, "%"+mc+"%");
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				Produit p=new Produit();
				p.setIdProduit(rs.getString("idProduit"));
				p.setNomProduit(rs.getString("designationProduit"));
				p.setPrix(rs.getDouble("prix"));
				p.setQuantite(rs.getInt("quantite"));
				int idCategorie=rs.getInt("idCategorie");
				PreparedStatement ps2=conn.prepareStatement("select * from categorie where idCategorie =?");
				ps2.setInt(1, idCategorie);
				ResultSet rs2=ps2.executeQuery();
				if(rs2.next()) {
					Categorie cat=new Categorie();
					cat.setIdCategorie(rs2.getInt("idCategorie"));
					cat.setNomCategorie(rs2.getString("nomCategorie"));
					p.setUneCategorie(cat);

				}
				lesProduits.add(p);
				ps2.close();
			}
			ps.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lesProduits;

	}

	@Override
	public List<Categorie> getAllCategorie() {
		Connection conn=SingletonConnection.getConnetion();	
		List<Categorie> cats=new ArrayList<Categorie>();

		try {
			PreparedStatement ps=conn.prepareStatement("select * from categorie");
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				Categorie c=new Categorie();
				c.setIdCategorie(rs.getInt("idCategorie"));
				c.setNomCategorie(rs.getString("nomCategorie"));
				cats.add(c);

			}
			ps.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cats;
	}

	@Override
	public List<Produit> getProduitsParIDCategorie(int idCategorie) {
		List<Produit> lesProduits=new ArrayList<Produit>();
		Connection conn=SingletonConnection.getConnetion();

		try {
			PreparedStatement ps=conn.prepareStatement("Select * from produit where idCategorie = ? ");
			ps.setInt(1, idCategorie);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				Produit p=new Produit();
				p.setIdProduit(rs.getString("idProduit"));
				p.setNomProduit(rs.getString("designationProduit"));
				p.setPrix(rs.getDouble("prix"));
				p.setQuantite(rs.getInt("quantite"));
				idCategorie=rs.getInt("idCategorie");
				PreparedStatement ps2=conn.prepareStatement("select * from categorie where idCategorie =?");
				ps2.setInt(1, idCategorie);
				ResultSet rs2=ps2.executeQuery();
				if(rs2.next()) {
					Categorie cat=new Categorie();
					cat.setIdCategorie(rs2.getInt("idCategorie"));
					cat.setNomCategorie(rs2.getString("nomCategorie"));
					p.setUneCategorie(cat);

				}
				lesProduits.add(p);
				ps2.close();
			}
			ps.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lesProduits;

	}
	@Override
	public Categorie getCategorie(int idCategorie) {
		Connection conn=SingletonConnection.getConnetion();		
		Categorie cat = null;
		try {
			PreparedStatement ps=conn.prepareStatement("select * from categorie where idCategorie=?");
			ps.setInt(1, idCategorie);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				cat=new Categorie();
				cat.setIdCategorie(rs.getInt("idCategorie"));
				cat.setNomCategorie(rs.getString("nomCategorie"));
				List<Produit> produits=this.getProduitsParIDCategorie(idCategorie);
				cat.setLesProduits(produits);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cat;
	}
}




