package Test;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import Metier.Categorie;
import Metier.MetierCatalogueImp;
import Metier.Produit;
import Metier.SingletonConnection;

public class App {
	public static void main(String[] args) {
	//je déclare et je crée une connection
     Connection conn=SingletonConnection.getConnetion();
//     MetierCatalogueImp metier=new MetierCatalogueImp();
   //  metier.addCategorie(new Categorie("Ecran"));
     //metier.addCategorie(new Categorie("Clavier"));
//     Scanner x=new Scanner(System.in);
//     System.out.println("Veuillez saisir le mot clé: ");
//     String mc=x.nextLine();
//     List<Produit> produits=metier.getProduitsParMotCle(mc);
//     for(Produit p:produits) {
//    	 System.out.println(p.getNomProduit());
//     }
//     System.out.println("\nLes noms des catégories \n");
//     List<Categorie> cats=metier.getAllCategorie();
//     for(Categorie c:cats) {
//    	 System.out.println(c.getNomCategorie());
//     }
//     
//     System.out.println(metier.getCategorie(1));
//	}
}
}
