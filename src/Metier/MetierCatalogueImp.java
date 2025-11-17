package Metier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetierCatalogueImp implements IMetier {

    @Override
    public void addCategorie(Categorie c) {
           try {
        	   Connection conn = SingletonConnection.getConnection();
    
            PreparedStatement ps = conn.prepareStatement("INSERT INTO CATEGORIE (nomCategorie) values (?)");
            ps.setString(1, c.getNomCategorie());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addProduit(Produit p, int idCategorie) {
       
        try { Connection conn = SingletonConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO produit (idProduit, designationProduit, prix, quantite, idCategorie) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, p.getIdProduit());
            ps.setString(2, p.getNomProduit());
            ps.setDouble(3, p.getPrix());
            ps.setInt(4, p.getQuantite());
            ps.setInt(5, idCategorie);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Produit> getProduitsParMotCle(String mc) {
        
List<Produit> lesProduits = new ArrayList<Produit>();
        try {
        Connection conn = SingletonConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM produit WHERE designationProduit LIKE ?");
            ps.setString(1, "%" + mc + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Produit p = createProduitFromResultSet(rs);
                lesProduits.add(p);
            }
            rs.close();
            ps.close();
            
        } catch (Exception   e) {
            e.printStackTrace();
        }
      return lesProduits;
    }

    @Override
    public List<Categorie> getAllCategorie() {
       List<Categorie> cats = new ArrayList<Categorie>();

        try { 
        	Connection conn = SingletonConnection.getConnection();
       
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM categorie ORDER BY nomCategorie");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categorie c = new Categorie();
                c.setIdCategorie(rs.getInt("idCategorie"));
                c.setNomCategorie(rs.getString("nomCategorie"));
                cats.add(c);
            }
            rs.close();
            ps.close(); 
           
        } catch (Exception e) {
            e.printStackTrace();
        }
     return cats;
    }

    @Override
    public List<Produit> getProduitsParIDCategorie(int idCategorie) {
       List<Produit> lesProduits = new ArrayList<Produit>();
       try {
    	  
    	   Connection conn = SingletonConnection.getConnection();
           PreparedStatement ps = conn.prepareStatement("SELECT * FROM produit WHERE idCategorie = ?");
            ps.setInt(1, idCategorie);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Produit p = createProduitFromResultSet(rs);
                lesProduits.add(p);
            }
            rs.close();
            ps.close();
        
        } catch (Exception e) {
            e.printStackTrace();
        }

	return lesProduits;
        
		
    }

    @Override
    public Categorie getCategorie(int idCategorie) {
        Categorie cat = null;
        try {
        	Connection conn = SingletonConnection.getConnection();
        
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM categorie WHERE idCategorie = ?");
            ps.setInt(1, idCategorie);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cat = new Categorie();
                cat.setIdCategorie(rs.getInt("idCategorie"));
                cat.setNomCategorie(rs.getString("nomCategorie"));
                List<Produit> produits = this.getProduitsParIDCategorie(idCategorie);
                cat.setLesProduits(produits);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cat;
    }

    // NOUVELLES MÉTHODES IMPLÉMENTÉES

    @Override
    public void updateProduit(Produit p) {
       
        try { Connection conn = SingletonConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE produit SET designationProduit = ?, prix = ?, quantite = ?, idCategorie = ? WHERE idProduit = ?");
            
            ps.setString(1, p.getNomProduit());
            ps.setDouble(2, p.getPrix());
            ps.setInt(3, p.getQuantite());
            
            // Récupérer l'ID de la catégorie à partir de l'objet Categorie
            if (p.getUneCategorie() != null) {
                ps.setInt(4, p.getUneCategorie().getIdCategorie());
            } else {
                // Si pas de catégorie, garder l'ancienne valeur
                PreparedStatement psSelect = conn.prepareStatement("SELECT idCategorie FROM produit WHERE idProduit = ?");
                psSelect.setString(1, p.getIdProduit());
                ResultSet rs = psSelect.executeQuery();
                if (rs.next()) {
                    ps.setInt(4, rs.getInt("idCategorie"));
                } else {
                    ps.setNull(4, java.sql.Types.INTEGER);
                }
                rs.close();
                psSelect.close();
            }
            
            ps.setString(5, p.getIdProduit());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Aucun produit trouvé avec l'ID: " + p.getIdProduit());
            }
            
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour du produit: " + e.getMessage());
        }
    }

    @Override
    public void deleteProduit(String idProduit) {
       
        try { Connection conn = SingletonConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM produit WHERE idProduit = ?");
            ps.setString(1, idProduit);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Aucun produit trouvé avec l'ID: " + idProduit);
            }
            
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression du produit: " + e.getMessage());
        }
    }

    @Override
    public List<Produit> getAllProduits() {
        List<Produit> lesProduits = new ArrayList<Produit>();
        

        try {Connection conn = SingletonConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM produit ORDER BY idProduit");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Produit p = createProduitFromResultSet(rs);
                lesProduits.add(p);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lesProduits;
    }

    @Override
    public Produit getProduitById(String idProduit) {
        
        Produit produit = null;
        
        try {Connection conn = SingletonConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM produit WHERE idProduit = ?");
            ps.setString(1, idProduit);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                produit = createProduitFromResultSet(rs);
            }
            
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produit;
    }

    // MÉTHODE UTILITAIRE POUR ÉVITER LA DUPLICATION DE CODE
    private Produit createProduitFromResultSet(ResultSet rs) throws SQLException {
        Produit p = new Produit();
        p.setIdProduit(rs.getString("idProduit"));
        p.setNomProduit(rs.getString("designationProduit"));
        p.setPrix(rs.getDouble("prix"));
        p.setQuantite(rs.getInt("quantite"));
        
        int idCategorie = rs.getInt("idCategorie");
        
        // Récupérer les informations de la catégorie
        Connection conn = SingletonConnection.getConnection();
        PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM categorie WHERE idCategorie = ?");
        ps2.setInt(1, idCategorie);
        ResultSet rs2 = ps2.executeQuery();
        
        if (rs2.next()) {
            Categorie cat = new Categorie();
            cat.setIdCategorie(rs2.getInt("idCategorie"));
            cat.setNomCategorie(rs2.getString("nomCategorie"));
            p.setUneCategorie(cat);
        }
        
        rs2.close();
        ps2.close();
        
        return p;
    }

	public Produit getProduitBycat(String nomCat, int idProduit) {
		// TODO Auto-generated method stub
		return null;
	}
}