package Metier;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class ProduitModel extends AbstractTableModel {
	private String [] nomColonnes=new String [] {"ID","Désignation","Prix","Quantité","Catégorie"};
	private Vector<String[]>rows=new Vector<String[]>();
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return rows.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stubs
		return nomColonnes.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return rows.get(rowIndex)[columnIndex];
	}
	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return nomColonnes[column];
	}
	public void loadData(List<Produit> produits) {
		rows=new Vector<String[]>();
		for(Produit p:produits) {
			rows.add(new String[]
					{p.getIdProduit(),p.getNomProduit(),String.valueOf(p.getPrix()),String.valueOf(p.getQuantite()),p.getUneCategorie().getNomCategorie()});

		}
		fireTableChanged(null);

	}
}
