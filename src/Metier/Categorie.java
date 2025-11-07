package Metier;

import java.util.ArrayList;
import java.util.List;
/*
 * @version 1
 * @author Balbali
 */
public class Categorie {
	private int idCategorie;
	private String nomCategorie;
	private List<Produit> lesProduits;
	public Categorie() {
		super();
	}
	/*
	 * Le constructeur
	 * @param nomCategorie
	 */
	public Categorie(String nomCategorie) {
		this.nomCategorie = nomCategorie;
		this.lesProduits = lesProduits != null ? lesProduits: new ArrayList<Produit>();
	}
	/*
	 * Fonction getNomCategorie 
	 * @return nomCategorie
	 */
	public String getNomCategorie() {
		return nomCategorie;
	}
	public void setNomCategorie(String nomCategorie) {
		this.nomCategorie = nomCategorie;
	}
	public List<Produit> getLesProduits() {
		return lesProduits;
	}
	public void setLesProduits(List<Produit> lesProduits) {
		this.lesProduits = lesProduits;
	}
	
	public int getIdCategorie() {
		return idCategorie;
	}
	public void setIdCategorie(int idCategorie) {
		this.idCategorie = idCategorie;
	}
	@Override
	public String toString() {
		return "Categorie [idCategorie=" + idCategorie + ", nomCategorie=" + nomCategorie + ", lesProduits="
				+ lesProduits + "]";
	}
	
	

}
