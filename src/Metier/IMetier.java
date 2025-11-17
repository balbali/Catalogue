package Metier;

import java.util.List;

public interface IMetier {
    // ajouter une nouvelle catégorie
    public void addCategorie(Categorie c);
    // Ajouter un nouveau produit appartenant à une catégorie
    public void addProduit(Produit p, int idCategorie);
    //Liste de produits en fonction d'un mot clé
    public List<Produit> getProduitsParMotCle(String mc);
    //Lister toutes les catégories
    public List<Categorie> getAllCategorie();
    // Lister les produits en fonction de l'idCategorie
    public List<Produit> getProduitsParIDCategorie(int idCategorie);
    //récupérer une catégorie sachant son id
    public Categorie getCategorie(int idCategorie);
    
    // NOUVELLES MÉTHODES AJOUTÉES
    public void updateProduit(Produit p);
    public void deleteProduit(String idProduit);
    public List<Produit> getAllProduits();
    public Produit getProduitById(String idProduit);
    public Produit getProduitBycat(String nomCat, int idProduit);
}