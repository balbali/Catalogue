package Metier;

public class Produit {
	private String idProduit;
	private String nomProduit;
	private double prix;
	private int quantite;
	private Categorie uneCategorie;
	public Produit(String idProduit, String nomProduit, double prix, int quantite) {
		super();
		this.idProduit = idProduit;
		this.nomProduit = nomProduit;
		this.prix = prix;
		this.quantite = quantite;
	}
	public Produit() {
		
	}
	public String getIdProduit() {
		return idProduit;
	}
	public void setIdProduit(String idProduit) {
		this.idProduit = idProduit;
	}
	public String getNomProduit() {
		return nomProduit;
	}
	public void setNomProduit(String nomProduit) {
		this.nomProduit = nomProduit;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public int getQuantite() {
		return quantite;
	}
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	public Categorie getUneCategorie() {
		return uneCategorie;
	}
	public void setUneCategorie(Categorie uneCategorie) {
		this.uneCategorie = uneCategorie;
	}
	@Override
	public String toString() {
		return "Produit [idProduit=" + idProduit + ", nomProduit=" + nomProduit + ", prix=" + prix + ", quantite="
				+ quantite + ", uneCategorie=" + uneCategorie + "]";
	}
	
	
  
}
