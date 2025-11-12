////package Presentation;
////
////import java.awt.*;
////import java.awt.event.*;
////import java.sql.*;
////import java.util.List;
////import javax.swing.*;
////import javax.swing.border.EmptyBorder;
////import javax.swing.table.DefaultTableCellRenderer;
////
////import Metier.MetierCatalogueImp;
////import Metier.Produit;
////import Metier.ProduitModel;
////import Metier.SingletonConnection;
////import Metier.Categorie;
////
////public class CatalogueSwing extends JFrame {
////    private JLabel jlbMc = new JLabel("Mot clé:");
////    private JTextField txtMc = new JTextField(15);
////    private JButton btnOk = new JButton("Rechercher");
////    private JButton btnReset = new JButton("Réinitialiser");
////    private JTable jtable;
////    private ProduitModel produitModel;
////    private MetierCatalogueImp metier = new MetierCatalogueImp();
////    private JComboBox<String> jcombo = new JComboBox<>();
////    private JLabel lblCategorie = new JLabel("Catégorie:");
////    private JLabel lblIdCategorie = new JLabel("ID Catégorie:");
////    private JTextField txtid = new JTextField(5);
////    
////    // Composants CRUD
////    private JTextField txtIdProduit = new JTextField(10);
////    private JTextField txtNomProduit = new JTextField(20);
////    private JTextField txtPrix = new JTextField(10);
////    private JTextField txtQuantite = new JTextField(10);
////    private JComboBox<String> comboCategoriesCRUD = new JComboBox<>();
////    private JButton btnAjouter = new JButton("Ajouter");
////    private JButton btnModifier = new JButton("Modifier");
////    private JButton btnSupprimer = new JButton("Supprimer");
////    private JButton btnNouveau = new JButton("Nouveau");
////
////    private void remplirCombo() {
////        try {
////            Connection conn = SingletonConnection.getConnection();
////            String query = "SELECT * FROM categorie ORDER BY nomCategorie";
////            PreparedStatement ps = conn.prepareStatement(query);
////            ResultSet rs = ps.executeQuery();
////            
////            // Vider les combobox
////            jcombo.removeAllItems();
////            comboCategoriesCRUD.removeAllItems();
////            
////            // Ajouter l'option par défaut seulement s'il y a des catégories
////            jcombo.addItem("Toutes les catégories");
////            comboCategoriesCRUD.addItem("Sélectionner une catégorie");
////            
////            boolean hasCategories = false;
////            while (rs.next()) {
////                hasCategories = true;
////                String nomCategorie = rs.getString("nomCategorie");
////                jcombo.addItem(nomCategorie);
////                comboCategoriesCRUD.addItem(nomCategorie);
////            }
////            
////            rs.close();
////            ps.close();
////            
////            // Si pas de catégories, afficher un message
////            if (!hasCategories) {
////                System.out.println("Aucune catégorie trouvée dans la base de données");
////            }
////            
////        } catch (Exception e) {
////            JOptionPane.showMessageDialog(this, 
////                "Erreur lors du chargement des catégories: " + e.getMessage(),
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            e.printStackTrace();
////        }
////    }
////    
////    private int getIdCategorieFromName(String nomCategorie) {
////        try {
////            Connection conn = SingletonConnection.getConnection();
////            String query = "SELECT idCategorie FROM categorie WHERE nomCategorie = ?";
////            PreparedStatement ps = conn.prepareStatement(query);
////            ps.setString(1, nomCategorie);
////            ResultSet rs = ps.executeQuery();
////            
////            if (rs.next()) {
////                int id = rs.getInt("idCategorie");
////                rs.close();
////                ps.close();
////                return id;
////            }
////            
////            rs.close();
////            ps.close();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return -1;
////    }
////
////    public CatalogueSwing() {
////        initializeUI();
////        setupLayout();
////        // Remplir les combobox AVANT de setup les event listeners
////        remplirCombo();
////        setupEventListeners();
////        loadAllProducts();
////    }
////
////    private void initializeUI() {
////        setTitle("Catalogue des Produits - Gestion CRUD");
////        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////        setExtendedState(JFrame.MAXIMIZED_BOTH);
////        setMinimumSize(new Dimension(1200, 800));
////    }
////
////    private void setupLayout() {
////        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
////        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
////        setContentPane(mainPanel);
////
////        // Panel de recherche
////        JPanel searchPanel = createSearchPanel();
////        mainPanel.add(searchPanel, BorderLayout.NORTH);
////
////        // Table des produits
////        setupTable();
////        JScrollPane scrollPane = new JScrollPane(jtable);
////        mainPanel.add(scrollPane, BorderLayout.CENTER);
////
////        // Panel CRUD
////        JPanel crudPanel = createCrudPanel();
////        mainPanel.add(crudPanel, BorderLayout.SOUTH);
////    }
////
////    private JPanel createSearchPanel() {
////        JPanel searchPanel = new JPanel(new GridBagLayout());
////        searchPanel.setBorder(BorderFactory.createTitledBorder("Critères de recherche"));
////        searchPanel.setBackground(new Color(240, 240, 240));
////        
////        GridBagConstraints gbc = new GridBagConstraints();
////        gbc.insets = new Insets(5, 5, 5, 5);
////        gbc.fill = GridBagConstraints.HORIZONTAL;
////
////        // Ligne 1: Mot clé
////        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
////        searchPanel.add(jlbMc, gbc);
////
////        gbc.gridx = 1; gbc.weightx = 1.0;
////        txtMc.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        searchPanel.add(txtMc, gbc);
////
////        gbc.gridx = 2; gbc.weightx = 0;
////        styleButton(btnOk, new Color(70, 130, 180));
////        searchPanel.add(btnOk, gbc);
////
////        gbc.gridx = 3; gbc.weightx = 0;
////        styleButton(btnReset, new Color(220, 220, 220));
////        searchPanel.add(btnReset, gbc);
////
////        // Ligne 2: Catégorie
////        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
////        searchPanel.add(lblCategorie, gbc);
////
////        gbc.gridx = 1; gbc.weightx = 1.0;
////        jcombo.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        searchPanel.add(jcombo, gbc);
////
////        gbc.gridx = 2; gbc.weightx = 0;
////        searchPanel.add(lblIdCategorie, gbc);
////
////        gbc.gridx = 3; gbc.weightx = 0;
////        txtid.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        txtid.setEditable(false);
////        txtid.setBackground(new Color(240, 240, 240));
////        searchPanel.add(txtid, gbc);
////
////        return searchPanel;
////    }
////
////    private JPanel createCrudPanel() {
////        JPanel crudPanel = new JPanel(new GridBagLayout());
////        crudPanel.setBorder(BorderFactory.createTitledBorder("Gestion des Produits"));
////        crudPanel.setBackground(new Color(245, 245, 245));
////        
////        GridBagConstraints gbc = new GridBagConstraints();
////        gbc.insets = new Insets(5, 5, 5, 5);
////        gbc.fill = GridBagConstraints.HORIZONTAL;
////
////        // Ligne 1: Labels
////        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
////        crudPanel.add(new JLabel("ID Produit:"), gbc);
////
////        gbc.gridx = 1;
////        crudPanel.add(new JLabel("Nom Produit:"), gbc);
////
////        gbc.gridx = 2;
////        crudPanel.add(new JLabel("Prix:"), gbc);
////
////        gbc.gridx = 3;
////        crudPanel.add(new JLabel("Quantité:"), gbc);
////
////        gbc.gridx = 4;
////        crudPanel.add(new JLabel("Catégorie:"), gbc);
////
////        // Ligne 2: Champs de saisie
////        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.5;
////        txtIdProduit.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        crudPanel.add(txtIdProduit, gbc);
////
////        gbc.gridx = 1; gbc.weightx = 1.0;
////        txtNomProduit.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        crudPanel.add(txtNomProduit, gbc);
////
////        gbc.gridx = 2; gbc.weightx = 0.5;
////        txtPrix.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        crudPanel.add(txtPrix, gbc);
////
////        gbc.gridx = 3; gbc.weightx = 0.5;
////        txtQuantite.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        crudPanel.add(txtQuantite, gbc);
////
////        gbc.gridx = 4; gbc.weightx = 0.5;
////        comboCategoriesCRUD.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        crudPanel.add(comboCategoriesCRUD, gbc);
////
////        // Ligne 3: Boutons
////        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 5;
////        gbc.anchor = GridBagConstraints.CENTER;
////        
////        JPanel buttonPanel = new JPanel(new FlowLayout());
////        styleButton(btnAjouter, new Color(34, 139, 34)); // Vert
////        buttonPanel.add(btnAjouter);
////        
////        styleButton(btnModifier, new Color(255, 165, 0)); // Orange
////        buttonPanel.add(btnModifier);
////        
////        styleButton(btnSupprimer, new Color(220, 20, 60)); // Rouge
////        buttonPanel.add(btnSupprimer);
////        
////        styleButton(btnNouveau, new Color(70, 130, 180)); // Bleu
////        buttonPanel.add(btnNouveau);
////        
////        crudPanel.add(buttonPanel, gbc);
////
////        return crudPanel;
////    }
////
////    private void styleButton(JButton button, Color backgroundColor) {
////        button.setBackground(backgroundColor);
////        button.setForeground(Color.WHITE);
////        button.setFont(new Font("Tahoma", Font.BOLD, 12));
////        button.setFocusPainted(false);
////        button.setPreferredSize(new Dimension(100, 30));
////    }
////
////    private void setupTable() {
////        produitModel = new ProduitModel();
////        jtable = new JTable(produitModel);
////        
////        jtable.setFont(new Font("Tahoma", Font.PLAIN, 11));
////        jtable.setRowHeight(25);
////        jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
////        jtable.setAutoCreateRowSorter(true);
////        
////        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
////        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
////        jtable.setDefaultRenderer(Object.class, centerRenderer);
////        
////        jtable.getColumnModel().getColumn(0).setPreferredWidth(80);
////        jtable.getColumnModel().getColumn(1).setPreferredWidth(200);
////        jtable.getColumnModel().getColumn(2).setPreferredWidth(100);
////        jtable.getColumnModel().getColumn(3).setPreferredWidth(80);
////    }
////
////    private void setupEventListeners() {
////        // Recherche par mot clé
////        btnOk.addActionListener(e -> searchByKeyword());
////
////        // Réinitialisation
////        btnReset.addActionListener(e -> resetSearch());
////
////        // Recherche par catégorie
////        jcombo.addActionListener(e -> {
////            if (jcombo.getSelectedIndex() == 0) {
////                loadAllProducts();
////                txtid.setText("");
////            } else {
////                searchByCategory();
////            }
////        });
////
////        // Recherche avec la touche Entrée
////        txtMc.addActionListener(e -> searchByKeyword());
////
////        // Sélection dans le tableau
////        jtable.getSelectionModel().addListSelectionListener(e -> {
////            if (!e.getValueIsAdjusting() && jtable.getSelectedRow() != -1) {
////                int selectedRow = jtable.getSelectedRow();
////                int modelRow = jtable.convertRowIndexToModel(selectedRow);
////                
////                // Récupérer les données directement depuis le modèle de tableau
////                String idProduit = produitModel.getValueAt(modelRow, 0).toString();
////                String nomProduit = produitModel.getValueAt(modelRow, 1).toString();
////                double prix = Double.parseDouble(produitModel.getValueAt(modelRow, 2).toString());
////                int quantite = Integer.parseInt(produitModel.getValueAt(modelRow, 3).toString());
////                
////                // Créer un objet Produit temporaire avec les données
////                Produit produit = new Produit();
////                produit.setIdProduit(idProduit);
////                produit.setNomProduit(nomProduit);
////                produit.setPrix(prix);
////                produit.setQuantite(quantite);
////                
////                // Pour la catégorie, on va la chercher depuis la base de données
////                try {
////                    Produit produitComplet = metier.getProduitById(idProduit);
////                    if (produitComplet != null && produitComplet.getUneCategorie() != null) {
////                        produit.setUneCategorie(produitComplet.getUneCategorie());
////                    }
////                } catch (Exception ex) {
////                    ex.printStackTrace();
////                }
////                
////                fillFormWithProduct(produit);
////            }
////        });
////
////        // Boutons CRUD
////        btnAjouter.addActionListener(e -> ajouterProduit());
////        btnModifier.addActionListener(e -> modifierProduit());
////        btnSupprimer.addActionListener(e -> supprimerProduit());
////        btnNouveau.addActionListener(e -> clearForm());
////    }
////
////    private void fillFormWithProduct(Produit produit) {
////        txtIdProduit.setText(produit.getIdProduit());
////        txtNomProduit.setText(produit.getNomProduit());
////        txtPrix.setText(String.valueOf(produit.getPrix()));
////        txtQuantite.setText(String.valueOf(produit.getQuantite()));
////        
////        if (produit.getUneCategorie() != null) {
////            String nomCategorie = produit.getUneCategorie().getNomCategorie();
////            // Sélectionner la catégorie dans le combobox de manière sécurisée
////            setComboBoxValueSafely(comboCategoriesCRUD, nomCategorie);
////        }
////        
////        txtIdProduit.setEditable(false); // Empêcher la modification de l'ID
////    }
////
////    private void setComboBoxValueSafely(JComboBox<String> comboBox, String value) {
////        for (int i = 0; i < comboBox.getItemCount(); i++) {
////            if (comboBox.getItemAt(i).equals(value)) {
////                comboBox.setSelectedIndex(i);
////                return;
////            }
////        }
////        // Si la valeur n'est pas trouvée, sélectionner l'index 0 si disponible
////        if (comboBox.getItemCount() > 0) {
////            comboBox.setSelectedIndex(0);
////        }
////    }
////
////    private void clearForm() {
////        txtIdProduit.setText("");
////        txtNomProduit.setText("");
////        txtPrix.setText("");
////        txtQuantite.setText("");
////        
////        // Sélectionner l'index 0 seulement si le combobox n'est pas vide
////        if (comboCategoriesCRUD.getItemCount() > 0) {
////            comboCategoriesCRUD.setSelectedIndex(0);
////        }
////        
////        txtIdProduit.setEditable(true);
////        txtIdProduit.requestFocus();
////        
////        // Désélectionner la ligne dans le tableau
////        jtable.clearSelection();
////    }
////
////    private void ajouterProduit() {
////        if (validateForm()) {
////            try {
////                String idProduit = txtIdProduit.getText().trim();
////                String nomProduit = txtNomProduit.getText().trim();
////                double prix = Double.parseDouble(txtPrix.getText().trim());
////                int quantite = Integer.parseInt(txtQuantite.getText().trim());
////                String nomCategorie = (String) comboCategoriesCRUD.getSelectedItem();
////                
////                // Vérifier si une catégorie valide est sélectionnée
////                if (nomCategorie == null || nomCategorie.equals("Sélectionner une catégorie")) {
////                    JOptionPane.showMessageDialog(this, 
////                        "Veuillez sélectionner une catégorie valide!", 
////                        "Erreur", 
////                        JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                // Vérifier si le produit existe déjà
////                Produit existingProduct = metier.getProduitById(idProduit);
////                if (existingProduct != null) {
////                    JOptionPane.showMessageDialog(this, 
////                        "Un produit avec cet ID existe déjà!", 
////                        "Erreur", 
////                        JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                int idCategorie = getIdCategorieFromName(nomCategorie);
////                
////                if (idCategorie == -1) {
////                    JOptionPane.showMessageDialog(this, "Catégorie invalide!", "Erreur", JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                Produit nouveauProduit = new Produit();
////                nouveauProduit.setIdProduit(idProduit);
////                nouveauProduit.setNomProduit(nomProduit);
////                nouveauProduit.setPrix(prix);
////                nouveauProduit.setQuantite(quantite);
////                
////                metier.addProduit(nouveauProduit, idCategorie);
////                
////                JOptionPane.showMessageDialog(this, 
////                    "Produit ajouté avec succès!", 
////                    "Succès", 
////                    JOptionPane.INFORMATION_MESSAGE);
////                clearForm();
////                loadAllProducts();
////                
////            } catch (NumberFormatException ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Prix et quantité doivent être des nombres valides!", 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////            } catch (Exception ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Erreur lors de l'ajout: " + ex.getMessage(), 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                ex.printStackTrace();
////            }
////        }
////    }
////
////    private void modifierProduit() {
////        if (jtable.getSelectedRow() == -1) {
////            JOptionPane.showMessageDialog(this, 
////                "Veuillez sélectionner un produit à modifier!", 
////                "Avertissement", 
////                JOptionPane.WARNING_MESSAGE);
////            return;
////        }
////        
////        if (validateForm()) {
////            try {
////                String idProduit = txtIdProduit.getText().trim();
////                String nomProduit = txtNomProduit.getText().trim();
////                double prix = Double.parseDouble(txtPrix.getText().trim());
////                int quantite = Integer.parseInt(txtQuantite.getText().trim());
////                String nomCategorie = (String) comboCategoriesCRUD.getSelectedItem();
////                
////                // Vérifier si une catégorie valide est sélectionnée
////                if (nomCategorie == null || nomCategorie.equals("Sélectionner une catégorie")) {
////                    JOptionPane.showMessageDialog(this, 
////                        "Veuillez sélectionner une catégorie valide!", 
////                        "Erreur", 
////                        JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                int idCategorie = getIdCategorieFromName(nomCategorie);
////                
////                if (idCategorie == -1) {
////                    JOptionPane.showMessageDialog(this, "Catégorie invalide!", "Erreur", JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                // Récupérer la catégorie complète
////                Categorie categorie = metier.getCategorie(idCategorie);
////                
////                Produit produitModifie = new Produit();
////                produitModifie.setIdProduit(idProduit);
////                produitModifie.setNomProduit(nomProduit);
////                produitModifie.setPrix(prix);
////                produitModifie.setQuantite(quantite);
////                produitModifie.setUneCategorie(categorie);
////                
////                metier.updateProduit(produitModifie);
////                
////                JOptionPane.showMessageDialog(this, 
////                    "Produit modifié avec succès!", 
////                    "Succès", 
////                    JOptionPane.INFORMATION_MESSAGE);
////                clearForm();
////                loadAllProducts();
////                
////            } catch (NumberFormatException ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Prix et quantité doivent être des nombres valides!", 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////            } catch (Exception ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Erreur lors de la modification: " + ex.getMessage(), 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                ex.printStackTrace();
////            }
////        }
////    }
////
////    private void supprimerProduit() {
////        if (jtable.getSelectedRow() == -1) {
////            JOptionPane.showMessageDialog(this, 
////                "Veuillez sélectionner un produit à supprimer!", 
////                "Avertissement", 
////                JOptionPane.WARNING_MESSAGE);
////            return;
////        }
////        
////        String idProduit = txtIdProduit.getText().trim();
////        String nomProduit = txtNomProduit.getText().trim();
////        
////        int confirmation = JOptionPane.showConfirmDialog(this, 
////            "Êtes-vous sûr de vouloir supprimer le produit : " + nomProduit + " (ID: " + idProduit + ")?", 
////            "Confirmation de suppression", 
////            JOptionPane.YES_NO_OPTION,
////            JOptionPane.WARNING_MESSAGE);
////        
////        if (confirmation == JOptionPane.YES_OPTION) {
////            try {
////                metier.deleteProduit(idProduit);
////                
////                JOptionPane.showMessageDialog(this, 
////                    "Produit supprimé avec succès!", 
////                    "Succès", 
////                    JOptionPane.INFORMATION_MESSAGE);
////                clearForm();
////                loadAllProducts();
////                
////            } catch (Exception ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Erreur lors de la suppression: " + ex.getMessage(), 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                ex.printStackTrace();
////            }
////        }
////    }
////
////    private boolean validateForm() {
////        if (txtIdProduit.getText().trim().isEmpty()) {
////            JOptionPane.showMessageDialog(this, 
////                "L'ID produit est obligatoire!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtIdProduit.requestFocus();
////            return false;
////        }
////        if (txtNomProduit.getText().trim().isEmpty()) {
////            JOptionPane.showMessageDialog(this, 
////                "Le nom du produit est obligatoire!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtNomProduit.requestFocus();
////            return false;
////        }
////        if (txtPrix.getText().trim().isEmpty()) {
////            JOptionPane.showMessageDialog(this, 
////                "Le prix est obligatoire!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtPrix.requestFocus();
////            return false;
////        }
////        if (txtQuantite.getText().trim().isEmpty()) {
////            JOptionPane.showMessageDialog(this, 
////                "La quantité est obligatoire!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtQuantite.requestFocus();
////            return false;
////        }
////        
////        // Vérification sécurisée de la catégorie
////        if (comboCategoriesCRUD.getSelectedItem() == null || 
////            comboCategoriesCRUD.getSelectedItem().equals("Sélectionner une catégorie")) {
////            JOptionPane.showMessageDialog(this, 
////                "Veuillez sélectionner une catégorie!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            comboCategoriesCRUD.requestFocus();
////            return false;
////        }
////        
////        try {
////            double prix = Double.parseDouble(txtPrix.getText().trim());
////            if (prix < 0) {
////                JOptionPane.showMessageDialog(this, 
////                    "Le prix ne peut pas être négatif!", 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                txtPrix.requestFocus();
////                return false;
////            }
////        } catch (NumberFormatException e) {
////            JOptionPane.showMessageDialog(this, 
////                "Le prix doit être un nombre valide!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtPrix.requestFocus();
////            return false;
////        }
////        
////        try {
////            int quantite = Integer.parseInt(txtQuantite.getText().trim());
////            if (quantite < 0) {
////                JOptionPane.showMessageDialog(this, 
////                    "La quantité ne peut pas être négative!", 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                txtQuantite.requestFocus();
////                return false;
////            }
////        } catch (NumberFormatException e) {
////            JOptionPane.showMessageDialog(this, 
////                "La quantité doit être un nombre entier valide!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtQuantite.requestFocus();
////            return false;
////        }
////        
////        return true;
////    }
////
////    private void searchByKeyword() {
////        String mc = txtMc.getText().trim();
////        try {
////            List<Produit> produits;
////            if (mc.isEmpty()) {
////                produits = metier.getAllProduits();
////            } else {
////                produits = metier.getProduitsParMotCle(mc);
////            }
////            produitModel.loadData(produits);
////            
////            if (produits.isEmpty() && !mc.isEmpty()) {
////                JOptionPane.showMessageDialog(this, 
////                    "Aucun produit trouvé avec le mot clé: " + mc,
////                    "Information", 
////                    JOptionPane.INFORMATION_MESSAGE);
////            }
////        } catch (Exception ex) {
////            JOptionPane.showMessageDialog(this, 
////                "Erreur lors de la recherche: " + ex.getMessage(),
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            ex.printStackTrace();
////        }
////    }
////
////    private void searchByCategory() {
////        try {
////            String nomCat = (String) jcombo.getSelectedItem();
////            if (nomCat != null && !nomCat.equals("Toutes les catégories")) {
////                Connection conn = SingletonConnection.getConnection();
////                String query = "SELECT idCategorie FROM categorie WHERE nomCategorie = ?";
////                PreparedStatement ps = conn.prepareStatement(query);
////                ps.setString(1, nomCat);
////                ResultSet rs = ps.executeQuery();
////                
////                if (rs.next()) {
////                    int idCategorie = rs.getInt("idCategorie");
////                    List<Produit> produits = metier.getProduitsParIDCategorie(idCategorie);
////                    produitModel.loadData(produits);
////                    txtid.setText(String.valueOf(idCategorie));
////                    
////                    if (produits.isEmpty()) {
////                        JOptionPane.showMessageDialog(this, 
////                            "Aucun produit trouvé dans la catégorie: " + nomCat,
////                            "Information", 
////                            JOptionPane.INFORMATION_MESSAGE);
////                    }
////                }
////                
////                rs.close();
////                ps.close();
////            }
////        } catch (Exception e) {
////            JOptionPane.showMessageDialog(this, 
////                "Erreur lors de la recherche par catégorie: " + e.getMessage(),
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            e.printStackTrace();
////        }
////    }
////
////    private void resetSearch() {
////        txtMc.setText("");
////        
////        // Réinitialisation sécurisée des combobox
////        if (jcombo.getItemCount() > 0) {
////            jcombo.setSelectedIndex(0);
////        }
////        
////        txtid.setText("");
////        loadAllProducts();
////        txtMc.requestFocus();
////    }
////
////    private void loadAllProducts() {
////        try {
////            List<Produit> produits = metier.getAllProduits();
////            produitModel.loadData(produits);
////        } catch (Exception e) {
////            JOptionPane.showMessageDialog(this, 
////                "Erreur lors du chargement des produits: " + e.getMessage(),
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            e.printStackTrace();
////        }
////    }
////
////    public static void main(String[] args) {
////        // Solution pour getSystemLookAndFeel - utilisation d'un try-catch simple
////        try {
////            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
////        } catch (Exception e) {
////            // Si ça échoue, on utilise le look and feel par défaut
////            try {
////                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
////            } catch (Exception ex) {
////                ex.printStackTrace();
////            }
////        }
////        
////        // Création de l'interface dans l'EDT
////        java.awt.EventQueue.invokeLater(new Runnable() {
////            public void run() {
////                new CatalogueSwing().setVisible(true);
////            }
////        });
////    }
////}
////package Presentation;
////
////import java.awt.*;
////import java.awt.event.*;
////import java.sql.*;
////import java.util.List;
////import javax.swing.*;
////import javax.swing.border.EmptyBorder;
////import javax.swing.table.DefaultTableCellRenderer;
////
////import Metier.MetierCatalogueImp;
////import Metier.Produit;
////import Metier.ProduitModel;
////import Metier.SingletonConnection;
////import Metier.Categorie;
////
////public class CatalogueSwing extends JFrame {
////    private JLabel jlbMc = new JLabel("Mot clé:");
////    private JTextField txtMc = new JTextField(15);
////    private JButton btnOk = new JButton("Rechercher");
////    private JButton btnReset = new JButton("Réinitialiser");
////    private JButton btnDeconnexion = new JButton("Déconnexion");
////    private JTable jtable;
////    private ProduitModel produitModel;
////    private MetierCatalogueImp metier = new MetierCatalogueImp();
////    private JComboBox<String> jcombo = new JComboBox<>();
////    private JLabel lblCategorie = new JLabel("Catégorie:");
////    private JLabel lblIdCategorie = new JLabel("ID Catégorie:");
////    private JTextField txtid = new JTextField(5);
////    
////    // Composants CRUD
////    private JTextField txtIdProduit = new JTextField(10);
////    private JTextField txtNomProduit = new JTextField(20);
////    private JTextField txtPrix = new JTextField(10);
////    private JTextField txtQuantite = new JTextField(10);
////    private JComboBox<String> comboCategoriesCRUD = new JComboBox<>();
////    private JButton btnAjouter = new JButton("Ajouter");
////    private JButton btnModifier = new JButton("Modifier");
////    private JButton btnSupprimer = new JButton("Supprimer");
////    private JButton btnNouveau = new JButton("Nouveau");
////
////    private void remplirCombo() {
////        try {
////            Connection conn = SingletonConnection.getConnection();
////            String query = "SELECT * FROM categorie ORDER BY nomCategorie";
////            PreparedStatement ps = conn.prepareStatement(query);
////            ResultSet rs = ps.executeQuery();
////            
////            // Vider les combobox
////            jcombo.removeAllItems();
////            comboCategoriesCRUD.removeAllItems();
////            
////            // Ajouter l'option par défaut seulement s'il y a des catégories
////            jcombo.addItem("Toutes les catégories");
////            comboCategoriesCRUD.addItem("Sélectionner une catégorie");
////            
////            boolean hasCategories = false;
////            while (rs.next()) {
////                hasCategories = true;
////                String nomCategorie = rs.getString("nomCategorie");
////                jcombo.addItem(nomCategorie);
////                comboCategoriesCRUD.addItem(nomCategorie);
////            }
////            
////            rs.close();
////            ps.close();
////            
////            // Si pas de catégories, afficher un message
////            if (!hasCategories) {
////                System.out.println("Aucune catégorie trouvée dans la base de données");
////            }
////            
////        } catch (Exception e) {
////            JOptionPane.showMessageDialog(this, 
////                "Erreur lors du chargement des catégories: " + e.getMessage(),
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            e.printStackTrace();
////        }
////    }
////    
////    private int getIdCategorieFromName(String nomCategorie) {
////        try {
////            Connection conn = SingletonConnection.getConnection();
////            String query = "SELECT idCategorie FROM categorie WHERE nomCategorie = ?";
////            PreparedStatement ps = conn.prepareStatement(query);
////            ps.setString(1, nomCategorie);
////            ResultSet rs = ps.executeQuery();
////            
////            if (rs.next()) {
////                int id = rs.getInt("idCategorie");
////                rs.close();
////                ps.close();
////                return id;
////            }
////            
////            rs.close();
////            ps.close();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return -1;
////    }
////
////    public CatalogueSwing() {
////        initializeUI();
////        setupLayout();
////        // Remplir les combobox AVANT de setup les event listeners
////        remplirCombo();
////        setupEventListeners();
////        loadAllProducts();
////    }
////
////    private void initializeUI() {
////        setTitle("Catalogue des Produits - Gestion CRUD");
////        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////        setExtendedState(JFrame.MAXIMIZED_BOTH);
////        setMinimumSize(new Dimension(1200, 800));
////    }
////
////    private void setupLayout() {
////        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
////        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
////        setContentPane(mainPanel);
////
////        // Panel de recherche avec déconnexion
////        JPanel searchPanel = createSearchPanel();
////        mainPanel.add(searchPanel, BorderLayout.NORTH);
////
////        // Table des produits
////        setupTable();
////        JScrollPane scrollPane = new JScrollPane(jtable);
////        mainPanel.add(scrollPane, BorderLayout.CENTER);
////
////        // Panel CRUD
////        JPanel crudPanel = createCrudPanel();
////        mainPanel.add(crudPanel, BorderLayout.SOUTH);
////    }
////
////    private JPanel createSearchPanel() {
////        JPanel searchPanel = new JPanel(new GridBagLayout());
////        searchPanel.setBorder(BorderFactory.createTitledBorder("Critères de recherche"));
////        searchPanel.setBackground(new Color(240, 240, 240));
////        
////        GridBagConstraints gbc = new GridBagConstraints();
////        gbc.insets = new Insets(5, 5, 5, 5);
////        gbc.fill = GridBagConstraints.HORIZONTAL;
////
////        // Ligne 1: Mot clé et boutons
////        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
////        searchPanel.add(jlbMc, gbc);
////
////        gbc.gridx = 1; gbc.weightx = 1.0;
////        txtMc.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        searchPanel.add(txtMc, gbc);
////
////        gbc.gridx = 2; gbc.weightx = 0;
////        styleButton(btnOk, new Color(70, 130, 180));
////        searchPanel.add(btnOk, gbc);
////
////        gbc.gridx = 3; gbc.weightx = 0;
////        styleButton(btnReset, new Color(220, 220, 220));
////        searchPanel.add(btnReset, gbc);
////
////        gbc.gridx = 4; gbc.weightx = 0;
////        styleButton(btnDeconnexion, new Color(178, 34, 34)); // Rouge bordeaux
////        btnDeconnexion.setToolTipText("Se déconnecter et retourner à l'authentification");
////        searchPanel.add(btnDeconnexion, gbc);
////
////        // Ligne 2: Catégorie
////        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
////        searchPanel.add(lblCategorie, gbc);
////
////        gbc.gridx = 1; gbc.weightx = 1.0;
////        jcombo.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        searchPanel.add(jcombo, gbc);
////
////        gbc.gridx = 2; gbc.weightx = 0;
////        searchPanel.add(lblIdCategorie, gbc);
////
////        gbc.gridx = 3; gbc.weightx = 0;
////        txtid.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        txtid.setEditable(false);
////        txtid.setBackground(new Color(240, 240, 240));
////        searchPanel.add(txtid, gbc);
////
////        return searchPanel;
////    }
////
////    private JPanel createCrudPanel() {
////        JPanel crudPanel = new JPanel(new GridBagLayout());
////        crudPanel.setBorder(BorderFactory.createTitledBorder("Gestion des Produits"));
////        crudPanel.setBackground(new Color(245, 245, 245));
////        
////        GridBagConstraints gbc = new GridBagConstraints();
////        gbc.insets = new Insets(5, 5, 5, 5);
////        gbc.fill = GridBagConstraints.HORIZONTAL;
////
////        // Ligne 1: Labels
////        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
////        crudPanel.add(new JLabel("ID Produit:"), gbc);
////
////        gbc.gridx = 1;
////        crudPanel.add(new JLabel("Nom Produit:"), gbc);
////
////        gbc.gridx = 2;
////        crudPanel.add(new JLabel("Prix:"), gbc);
////
////        gbc.gridx = 3;
////        crudPanel.add(new JLabel("Quantité:"), gbc);
////
////        gbc.gridx = 4;
////        crudPanel.add(new JLabel("Catégorie:"), gbc);
////
////        // Ligne 2: Champs de saisie
////        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.5;
////        txtIdProduit.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        crudPanel.add(txtIdProduit, gbc);
////
////        gbc.gridx = 1; gbc.weightx = 1.0;
////        txtNomProduit.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        crudPanel.add(txtNomProduit, gbc);
////
////        gbc.gridx = 2; gbc.weightx = 0.5;
////        txtPrix.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        crudPanel.add(txtPrix, gbc);
////
////        gbc.gridx = 3; gbc.weightx = 0.5;
////        txtQuantite.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        crudPanel.add(txtQuantite, gbc);
////
////        gbc.gridx = 4; gbc.weightx = 0.5;
////        comboCategoriesCRUD.setFont(new Font("Tahoma", Font.PLAIN, 12));
////        crudPanel.add(comboCategoriesCRUD, gbc);
////
////        // Ligne 3: Boutons
////        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 5;
////        gbc.anchor = GridBagConstraints.CENTER;
////        
////        JPanel buttonPanel = new JPanel(new FlowLayout());
////        styleButton(btnAjouter, new Color(34, 139, 34)); // Vert
////        buttonPanel.add(btnAjouter);
////        
////        styleButton(btnModifier, new Color(255, 165, 0)); // Orange
////        buttonPanel.add(btnModifier);
////        
////        styleButton(btnSupprimer, new Color(220, 20, 60)); // Rouge
////        buttonPanel.add(btnSupprimer);
////        
////        styleButton(btnNouveau, new Color(70, 130, 180)); // Bleu
////        buttonPanel.add(btnNouveau);
////        
////        crudPanel.add(buttonPanel, gbc);
////
////        return crudPanel;
////    }
////
////    private void styleButton(JButton button, Color backgroundColor) {
////        button.setBackground(backgroundColor);
////        button.setForeground(Color.WHITE);
////        button.setFont(new Font("Tahoma", Font.BOLD, 12));
////        button.setFocusPainted(false);
////        button.setPreferredSize(new Dimension(100, 30));
////    }
////
////    private void setupTable() {
////        produitModel = new ProduitModel();
////        jtable = new JTable(produitModel);
////        
////        jtable.setFont(new Font("Tahoma", Font.PLAIN, 11));
////        jtable.setRowHeight(25);
////        jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
////        jtable.setAutoCreateRowSorter(true);
////        
////        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
////        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
////        jtable.setDefaultRenderer(Object.class, centerRenderer);
////        
////        jtable.getColumnModel().getColumn(0).setPreferredWidth(80);
////        jtable.getColumnModel().getColumn(1).setPreferredWidth(200);
////        jtable.getColumnModel().getColumn(2).setPreferredWidth(100);
////        jtable.getColumnModel().getColumn(3).setPreferredWidth(80);
////    }
////
////    private void setupEventListeners() {
////        // Recherche par mot clé
////        btnOk.addActionListener(e -> searchByKeyword());
////
////        // Réinitialisation
////        btnReset.addActionListener(e -> resetSearch());
////
////        // Déconnexion
////        btnDeconnexion.addActionListener(e -> deconnecter());
////
////        // Recherche par catégorie
////        jcombo.addActionListener(e -> {
////            if (jcombo.getSelectedIndex() == 0) {
////                loadAllProducts();
////                txtid.setText("");
////            } else {
////                searchByCategory();
////            }
////        });
////
////        // Recherche avec la touche Entrée
////        txtMc.addActionListener(e -> searchByKeyword());
////
////        // Sélection dans le tableau
////        jtable.getSelectionModel().addListSelectionListener(e -> {
////            if (!e.getValueIsAdjusting() && jtable.getSelectedRow() != -1) {
////                int selectedRow = jtable.getSelectedRow();
////                int modelRow = jtable.convertRowIndexToModel(selectedRow);
////                
////                // Récupérer les données directement depuis le modèle de tableau
////                String idProduit = produitModel.getValueAt(modelRow, 0).toString();
////                String nomProduit = produitModel.getValueAt(modelRow, 1).toString();
////                double prix = Double.parseDouble(produitModel.getValueAt(modelRow, 2).toString());
////                int quantite = Integer.parseInt(produitModel.getValueAt(modelRow, 3).toString());
////                
////                // Créer un objet Produit temporaire avec les données
////                Produit produit = new Produit();
////                produit.setIdProduit(idProduit);
////                produit.setNomProduit(nomProduit);
////                produit.setPrix(prix);
////                produit.setQuantite(quantite);
////                
////                // Pour la catégorie, on va la chercher depuis la base de données
////                try {
////                    Produit produitComplet = metier.getProduitById(idProduit);
////                    if (produitComplet != null && produitComplet.getUneCategorie() != null) {
////                        produit.setUneCategorie(produitComplet.getUneCategorie());
////                    }
////                } catch (Exception ex) {
////                    ex.printStackTrace();
////                }
////                
////                fillFormWithProduct(produit);
////            }
////        });
////
////        // Boutons CRUD
////        btnAjouter.addActionListener(e -> ajouterProduit());
////        btnModifier.addActionListener(e -> modifierProduit());
////        btnSupprimer.addActionListener(e -> supprimerProduit());
////        btnNouveau.addActionListener(e -> clearForm());
////    }
////
////    private void deconnecter() {
////        int confirmation = JOptionPane.showConfirmDialog(this,
////            "Êtes-vous sûr de vouloir vous déconnecter?",
////            "Confirmation de déconnexion",
////            JOptionPane.YES_NO_OPTION,
////            JOptionPane.QUESTION_MESSAGE);
////
////        if (confirmation == JOptionPane.YES_OPTION) {
////            // Fermer la fenêtre courante
////            this.dispose();
////            
////            // Rouvrir la fenêtre d'authentification
////            java.awt.EventQueue.invokeLater(new Runnable() {
////                public void run() {
////                    new LoginFrame().setVisible(true);
////                }
////            });
////        }
////    }
////
////    private void fillFormWithProduct(Produit produit) {
////        txtIdProduit.setText(produit.getIdProduit());
////        txtNomProduit.setText(produit.getNomProduit());
////        txtPrix.setText(String.valueOf(produit.getPrix()));
////        txtQuantite.setText(String.valueOf(produit.getQuantite()));
////        
////        if (produit.getUneCategorie() != null) {
////            String nomCategorie = produit.getUneCategorie().getNomCategorie();
////            // Sélectionner la catégorie dans le combobox de manière sécurisée
////            setComboBoxValueSafely(comboCategoriesCRUD, nomCategorie);
////        }
////        
////        txtIdProduit.setEditable(false); // Empêcher la modification de l'ID
////    }
////
////    private void setComboBoxValueSafely(JComboBox<String> comboBox, String value) {
////        for (int i = 0; i < comboBox.getItemCount(); i++) {
////            if (comboBox.getItemAt(i).equals(value)) {
////                comboBox.setSelectedIndex(i);
////                return;
////            }
////        }
////        // Si la valeur n'est pas trouvée, sélectionner l'index 0 si disponible
////        if (comboBox.getItemCount() > 0) {
////            comboBox.setSelectedIndex(0);
////        }
////    }
////
////    private void clearForm() {
////        txtIdProduit.setText("");
////        txtNomProduit.setText("");
////        txtPrix.setText("");
////        txtQuantite.setText("");
////        
////        // Sélectionner l'index 0 seulement si le combobox n'est pas vide
////        if (comboCategoriesCRUD.getItemCount() > 0) {
////            comboCategoriesCRUD.setSelectedIndex(0);
////        }
////        
////        txtIdProduit.setEditable(true);
////        txtIdProduit.requestFocus();
////        
////        // Désélectionner la ligne dans le tableau
////        jtable.clearSelection();
////    }
////
////    private void ajouterProduit() {
////        if (validateForm()) {
////            try {
////                String idProduit = txtIdProduit.getText().trim();
////                String nomProduit = txtNomProduit.getText().trim();
////                double prix = Double.parseDouble(txtPrix.getText().trim());
////                int quantite = Integer.parseInt(txtQuantite.getText().trim());
////                String nomCategorie = (String) comboCategoriesCRUD.getSelectedItem();
////                
////                // Vérifier si une catégorie valide est sélectionnée
////                if (nomCategorie == null || nomCategorie.equals("Sélectionner une catégorie")) {
////                    JOptionPane.showMessageDialog(this, 
////                        "Veuillez sélectionner une catégorie valide!", 
////                        "Erreur", 
////                        JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                // Vérifier si le produit existe déjà
////                Produit existingProduct = metier.getProduitById(idProduit);
////                if (existingProduct != null) {
////                    JOptionPane.showMessageDialog(this, 
////                        "Un produit avec cet ID existe déjà!", 
////                        "Erreur", 
////                        JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                int idCategorie = getIdCategorieFromName(nomCategorie);
////                
////                if (idCategorie == -1) {
////                    JOptionPane.showMessageDialog(this, "Catégorie invalide!", "Erreur", JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                Produit nouveauProduit = new Produit();
////                nouveauProduit.setIdProduit(idProduit);
////                nouveauProduit.setNomProduit(nomProduit);
////                nouveauProduit.setPrix(prix);
////                nouveauProduit.setQuantite(quantite);
////                
////                metier.addProduit(nouveauProduit, idCategorie);
////                
////                JOptionPane.showMessageDialog(this, 
////                    "Produit ajouté avec succès!", 
////                    "Succès", 
////                    JOptionPane.INFORMATION_MESSAGE);
////                clearForm();
////                loadAllProducts();
////                
////            } catch (NumberFormatException ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Prix et quantité doivent être des nombres valides!", 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////            } catch (Exception ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Erreur lors de l'ajout: " + ex.getMessage(), 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                ex.printStackTrace();
////            }
////        }
////    }
////
////    private void modifierProduit() {
////        if (jtable.getSelectedRow() == -1) {
////            JOptionPane.showMessageDialog(this, 
////                "Veuillez sélectionner un produit à modifier!", 
////                "Avertissement", 
////                JOptionPane.WARNING_MESSAGE);
////            return;
////        }
////        
////        if (validateForm()) {
////            try {
////                String idProduit = txtIdProduit.getText().trim();
////                String nomProduit = txtNomProduit.getText().trim();
////                double prix = Double.parseDouble(txtPrix.getText().trim());
////                int quantite = Integer.parseInt(txtQuantite.getText().trim());
////                String nomCategorie = (String) comboCategoriesCRUD.getSelectedItem();
////                
////                // Vérifier si une catégorie valide est sélectionnée
////                if (nomCategorie == null || nomCategorie.equals("Sélectionner une catégorie")) {
////                    JOptionPane.showMessageDialog(this, 
////                        "Veuillez sélectionner une catégorie valide!", 
////                        "Erreur", 
////                        JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                int idCategorie = getIdCategorieFromName(nomCategorie);
////                
////                if (idCategorie == -1) {
////                    JOptionPane.showMessageDialog(this, "Catégorie invalide!", "Erreur", JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                // Récupérer la catégorie complète
////                Categorie categorie = metier.getCategorie(idCategorie);
////                
////                Produit produitModifie = new Produit();
////                produitModifie.setIdProduit(idProduit);
////                produitModifie.setNomProduit(nomProduit);
////                produitModifie.setPrix(prix);
////                produitModifie.setQuantite(quantite);
////                produitModifie.setUneCategorie(categorie);
////                
////                metier.updateProduit(produitModifie);
////                
////                JOptionPane.showMessageDialog(this, 
////                    "Produit modifié avec succès!", 
////                    "Succès", 
////                    JOptionPane.INFORMATION_MESSAGE);
////                clearForm();
////                loadAllProducts();
////                
////            } catch (NumberFormatException ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Prix et quantité doivent être des nombres valides!", 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////            } catch (Exception ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Erreur lors de la modification: " + ex.getMessage(), 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                ex.printStackTrace();
////            }
////        }
////    }
////
////    private void supprimerProduit() {
////        if (jtable.getSelectedRow() == -1) {
////            JOptionPane.showMessageDialog(this, 
////                "Veuillez sélectionner un produit à supprimer!", 
////                "Avertissement", 
////                JOptionPane.WARNING_MESSAGE);
////            return;
////        }
////        
////        String idProduit = txtIdProduit.getText().trim();
////        String nomProduit = txtNomProduit.getText().trim();
////        
////        int confirmation = JOptionPane.showConfirmDialog(this, 
////            "Êtes-vous sûr de vouloir supprimer le produit : " + nomProduit + " (ID: " + idProduit + ")?", 
////            "Confirmation de suppression", 
////            JOptionPane.YES_NO_OPTION,
////            JOptionPane.WARNING_MESSAGE);
////        
////        if (confirmation == JOptionPane.YES_OPTION) {
////            try {
////                metier.deleteProduit(idProduit);
////                
////                JOptionPane.showMessageDialog(this, 
////                    "Produit supprimé avec succès!", 
////                    "Succès", 
////                    JOptionPane.INFORMATION_MESSAGE);
////                clearForm();
////                loadAllProducts();
////                
////            } catch (Exception ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Erreur lors de la suppression: " + ex.getMessage(), 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                ex.printStackTrace();
////            }
////        }
////    }
////
////    private boolean validateForm() {
////        if (txtIdProduit.getText().trim().isEmpty()) {
////            JOptionPane.showMessageDialog(this, 
////                "L'ID produit est obligatoire!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtIdProduit.requestFocus();
////            return false;
////        }
////        if (txtNomProduit.getText().trim().isEmpty()) {
////            JOptionPane.showMessageDialog(this, 
////                "Le nom du produit est obligatoire!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtNomProduit.requestFocus();
////            return false;
////        }
////        if (txtPrix.getText().trim().isEmpty()) {
////            JOptionPane.showMessageDialog(this, 
////                "Le prix est obligatoire!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtPrix.requestFocus();
////            return false;
////        }
////        if (txtQuantite.getText().trim().isEmpty()) {
////            JOptionPane.showMessageDialog(this, 
////                "La quantité est obligatoire!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtQuantite.requestFocus();
////            return false;
////        }
////        
////        // Vérification sécurisée de la catégorie
////        if (comboCategoriesCRUD.getSelectedItem() == null || 
////            comboCategoriesCRUD.getSelectedItem().equals("Sélectionner une catégorie")) {
////            JOptionPane.showMessageDialog(this, 
////                "Veuillez sélectionner une catégorie!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            comboCategoriesCRUD.requestFocus();
////            return false;
////        }
////        
////        try {
////            double prix = Double.parseDouble(txtPrix.getText().trim());
////            if (prix < 0) {
////                JOptionPane.showMessageDialog(this, 
////                    "Le prix ne peut pas être négatif!", 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                txtPrix.requestFocus();
////                return false;
////            }
////        } catch (NumberFormatException e) {
////            JOptionPane.showMessageDialog(this, 
////                "Le prix doit être un nombre valide!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtPrix.requestFocus();
////            return false;
////        }
////        
////        try {
////            int quantite = Integer.parseInt(txtQuantite.getText().trim());
////            if (quantite < 0) {
////                JOptionPane.showMessageDialog(this, 
////                    "La quantité ne peut pas être négative!", 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                txtQuantite.requestFocus();
////                return false;
////            }
////        } catch (NumberFormatException e) {
////            JOptionPane.showMessageDialog(this, 
////                "La quantité doit être un nombre entier valide!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtQuantite.requestFocus();
////            return false;
////        }
////        
////        return true;
////    }
////
////    private void searchByKeyword() {
////        String mc = txtMc.getText().trim();
////        try {
////            List<Produit> produits;
////            if (mc.isEmpty()) {
////                produits = metier.getAllProduits();
////            } else {
////                produits = metier.getProduitsParMotCle(mc);
////            }
////            produitModel.loadData(produits);
////            
////            if (produits.isEmpty() && !mc.isEmpty()) {
////                JOptionPane.showMessageDialog(this, 
////                    "Aucun produit trouvé avec le mot clé: " + mc,
////                    "Information", 
////                    JOptionPane.INFORMATION_MESSAGE);
////            }
////        } catch (Exception ex) {
////            JOptionPane.showMessageDialog(this, 
////                "Erreur lors de la recherche: " + ex.getMessage(),
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            ex.printStackTrace();
////        }
////    }
////
////    private void searchByCategory() {
////        try {
////            String nomCat = (String) jcombo.getSelectedItem();
////            if (nomCat != null && !nomCat.equals("Toutes les catégories")) {
////                Connection conn = SingletonConnection.getConnection();
////                String query = "SELECT idCategorie FROM categorie WHERE nomCategorie = ?";
////                PreparedStatement ps = conn.prepareStatement(query);
////                ps.setString(1, nomCat);
////                ResultSet rs = ps.executeQuery();
////                
////                if (rs.next()) {
////                    int idCategorie = rs.getInt("idCategorie");
////                    List<Produit> produits = metier.getProduitsParIDCategorie(idCategorie);
////                    produitModel.loadData(produits);
////                    txtid.setText(String.valueOf(idCategorie));
////                    
////                    if (produits.isEmpty()) {
////                        JOptionPane.showMessageDialog(this, 
////                            "Aucun produit trouvé dans la catégorie: " + nomCat,
////                            "Information", 
////                            JOptionPane.INFORMATION_MESSAGE);
////                    }
////                }
////                
////                rs.close();
////                ps.close();
////            }
////        } catch (Exception e) {
////            JOptionPane.showMessageDialog(this, 
////                "Erreur lors de la recherche par catégorie: " + e.getMessage(),
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            e.printStackTrace();
////        }
////    }
////
////    private void resetSearch() {
////        txtMc.setText("");
////        
////        // Réinitialisation sécurisée des combobox
////        if (jcombo.getItemCount() > 0) {
////            jcombo.setSelectedIndex(0);
////        }
////        
////        txtid.setText("");
////        loadAllProducts();
////        txtMc.requestFocus();
////    }
////
////    private void loadAllProducts() {
////        try {
////            List<Produit> produits = metier.getAllProduits();
////            produitModel.loadData(produits);
////        } catch (Exception e) {
////            JOptionPane.showMessageDialog(this, 
////                "Erreur lors du chargement des produits: " + e.getMessage(),
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            e.printStackTrace();
////        }
////    }
////
////    public static void main(String[] args) {
////        // Solution pour getSystemLookAndFeel - utilisation d'un try-catch simple
////        try {
////            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
////        } catch (Exception e) {
////            // Si ça échoue, on utilise le look and feel par défaut
////            try {
////                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
////            } catch (Exception ex) {
////                ex.printStackTrace();
////            }
////        }
////        
////        // Création de l'interface dans l'EDT
////        java.awt.EventQueue.invokeLater(new Runnable() {
////            public void run() {
////                new CatalogueSwing().setVisible(true);
////            }
////        });
////    }
////}
//// Déclaration du package - organisation logique des classes
////package Presentation;
////
////// Import iText pour la génération PDF
////import com.itextpdf.text.Font; // For PDF generation
////
////// Import des classes Java standard pour l'interface graphique
////import java.awt.*;
////import java.awt.event.*;
////import java.sql.*;
////import java.util.List;
////import java.io.FileOutputStream;
////import javax.swing.*;
////import javax.swing.border.EmptyBorder;
////import javax.swing.table.DefaultTableCellRenderer;
////
////// Import des classes métier de l'application
////import Metier.MetierCatalogueImp;
////import Metier.Produit;
////import Metier.ProduitModel;
////import Metier.SingletonConnection;
////import Metier.Categorie;
////
////// Import iText pour la génération PDF - classes spécifiques
////import com.itextpdf.text.Document;          // Document PDF principal
////import com.itextpdf.text.Paragraph;         // Paragraphe dans le PDF
////import com.itextpdf.text.Phrase;            // Phrase dans le PDF
////import com.itextpdf.text.Font;              // Police PDF
////import com.itextpdf.text.FontFactory;       // Factory pour les polices PDF
////import com.itextpdf.text.pdf.PdfPCell;      // Cellule de tableau PDF
////import com.itextpdf.text.pdf.PdfPTable;     // Tableau PDF
////import com.itextpdf.text.pdf.PdfWriter;     // Writer pour générer le PDF
////import com.itextpdf.text.BaseColor;         // Couleurs PDF
////
/////**
//// * Classe principale de l'interface Swing pour la gestion du catalogue de produits
//// * Cette classe implémente une interface CRUD complète avec génération de rapports PDF
//// */
////public class CatalogueSwing extends JFrame {
////    
////    // Déclaration des composants de l'interface - Section Recherche
////    private JLabel jlbMc = new JLabel("Mot clé:");                    // Label pour le mot clé
////    private JTextField txtMc = new JTextField(15);                    // Champ de saisie pour la recherche
////    private JButton btnOk = new JButton("Rechercher");                // Bouton de recherche
////    private JButton btnReset = new JButton("Réinitialiser");          // Bouton de réinitialisation
////    private JButton btnDeconnexion = new JButton("Déconnexion");      // Bouton de déconnexion
////    private JButton btnGenererPDF = new JButton("Générer PDF");       // Bouton pour générer PDF
////    
////    // Composants pour l'affichage des données
////    private JTable jtable;                                            // Tableau pour afficher les produits
////    private ProduitModel produitModel;                                // Modèle de données pour le tableau
////    private MetierCatalogueImp metier = new MetierCatalogueImp();     // Couche métier pour les opérations CRUD
////    
////    // Composants pour le filtrage par catégorie
////    private JComboBox<String> jcombo = new JComboBox<>();             // Combo box pour sélectionner une catégorie
////    private JLabel lblCategorie = new JLabel("Catégorie:");           // Label pour la catégorie
////    private JLabel lblIdCategorie = new JLabel("ID Catégorie:");      // Label pour l'ID catégorie
////    private JTextField txtid = new JTextField(5);                     // Champ affichant l'ID catégorie
////    
////    // Composants CRUD - Gestion des produits
////    private JTextField txtIdProduit = new JTextField(10);             // Champ pour l'ID du produit
////    private JTextField txtNomProduit = new JTextField(20);            // Champ pour le nom du produit
////    private JTextField txtPrix = new JTextField(10);                  // Champ pour le prix
////    private JTextField txtQuantite = new JTextField(10);              // Champ pour la quantité
////    private JComboBox<String> comboCategoriesCRUD = new JComboBox<>(); // Combo box pour choisir une catégorie CRUD
////    
////    // Boutons CRUD
////    private JButton btnAjouter = new JButton("Ajouter");              // Bouton pour ajouter un produit
////    private JButton btnModifier = new JButton("Modifier");            // Bouton pour modifier un produit
////    private JButton btnSupprimer = new JButton("Supprimer");          // Bouton pour supprimer un produit
////    private JButton btnNouveau = new JButton("Nouveau");              // Bouton pour réinitialiser le formulaire
////
////    /**
////     * Méthode pour remplir les ComboBox avec les catégories depuis la base de données
////     * Cette méthode charge toutes les catégories et les ajoute aux listes déroulantes
////     */
////    private void remplirCombo() {
////        try {
////            // Connexion à la base de données via le singleton
////            Connection conn = SingletonConnection.getConnection();
////            
////            // Requête SQL pour récupérer toutes les catégories triées par nom
////            String query = "SELECT * FROM categorie ORDER BY nomCategorie";
////            PreparedStatement ps = conn.prepareStatement(query);
////            ResultSet rs = ps.executeQuery();
////            
////            // Vider les combobox avant de les remplir
////            jcombo.removeAllItems();
////            comboCategoriesCRUD.removeAllItems();
////            
////            // Ajouter l'option par défaut seulement s'il y a des catégories
////            jcombo.addItem("Toutes les catégories");
////            comboCategoriesCRUD.addItem("Sélectionner une catégorie");
////            
////            // Variable pour vérifier si des catégories existent
////            boolean hasCategories = false;
////            
////            // Parcourir le ResultSet et ajouter chaque catégorie aux ComboBox
////            while (rs.next()) {
////                hasCategories = true;
////                String nomCategorie = rs.getString("nomCategorie");
////                jcombo.addItem(nomCategorie);
////                comboCategoriesCRUD.addItem(nomCategorie);
////            }
////            
////            // Fermeture des ressources
////            rs.close();
////            ps.close();
////            
////            // Si pas de catégories, afficher un message dans la console
////            if (!hasCategories) {
////                System.out.println("Aucune catégorie trouvée dans la base de données");
////            }
////            
////        } catch (Exception e) {
////            // Gestion des erreurs avec message à l'utilisateur
////            JOptionPane.showMessageDialog(this, 
////                "Erreur lors du chargement des catégories: " + e.getMessage(),
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            e.printStackTrace();
////        }
////    }
////    
////    /**
////     * Méthode utilitaire pour obtenir l'ID d'une catégorie à partir de son nom
////     * @param nomCategorie le nom de la catégorie
////     * @return l'ID de la catégorie ou -1 si non trouvée
////     */
////    private int getIdCategorieFromName(String nomCategorie) {
////        try {
////            Connection conn = SingletonConnection.getConnection();
////            
////            // Requête paramétrée pour éviter les injections SQL
////            String query = "SELECT idCategorie FROM categorie WHERE nomCategorie = ?";
////            PreparedStatement ps = conn.prepareStatement(query);
////            ps.setString(1, nomCategorie);
////            ResultSet rs = ps.executeQuery();
////            
////            // Si une catégorie est trouvée, retourner son ID
////            if (rs.next()) {
////                int id = rs.getInt("idCategorie");
////                rs.close();
////                ps.close();
////                return id;
////            }
////            
////            // Fermeture des ressources
////            rs.close();
////            ps.close();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return -1; // Retourne -1 si la catégorie n'est pas trouvée
////    }
////
////    /**
////     * Constructeur principal - initialise l'interface utilisateur
////     */
////    public CatalogueSwing() {
////        initializeUI();         // Initialisation de la fenêtre
////        setupLayout();          // Configuration du layout
////        remplirCombo();         // Remplissage des ComboBox AVANT les listeners
////        setupEventListeners();  // Configuration des événements
////        loadAllProducts();      // Chargement initial des produits
////    }
////
////    /**
////     * Initialisation des propriétés de base de la fenêtre
////     */
////    private void initializeUI() {
////        setTitle("Catalogue des Produits - Gestion CRUD");  // Titre de la fenêtre
////        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     // Comportement à la fermeture
////        setExtendedState(JFrame.MAXIMIZED_BOTH);            // Fenêtre en plein écran
////        setMinimumSize(new Dimension(1200, 800));           // Taille minimale
////    }
////
////    /**
////     * Configuration du layout principal de l'application
////     */
////    private void setupLayout() {
////        // Panel principal avec BorderLayout et marges
////        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
////        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
////        setContentPane(mainPanel);
////
////        // Panel de recherche avec déconnexion - positionné en haut
////        JPanel searchPanel = createSearchPanel();
////        mainPanel.add(searchPanel, BorderLayout.NORTH);
////
////        // Table des produits - positionnée au centre
////        setupTable();
////        JScrollPane scrollPane = new JScrollPane(jtable);
////        mainPanel.add(scrollPane, BorderLayout.CENTER);
////
////        // Panel CRUD - positionné en bas
////        JPanel crudPanel = createCrudPanel();
////        mainPanel.add(crudPanel, BorderLayout.SOUTH);
////    }
////
////    /**
////     * Création du panel de recherche avec tous ses composants
////     * @return JPanel configuré pour la recherche
////     */
////    private JPanel createSearchPanel() {
////        // Panel avec GridBagLayout pour un contrôle précis du positionnement
////        JPanel searchPanel = new JPanel(new GridBagLayout());
////        searchPanel.setBorder(BorderFactory.createTitledBorder("Critères de recherche"));
////        searchPanel.setBackground(new Color(240, 240, 240));
////        
////        // Configuration des contraintes pour le GridBagLayout
////        GridBagConstraints gbc = new GridBagConstraints();
////        gbc.insets = new Insets(5, 5, 5, 5);  // Marges internes
////        gbc.fill = GridBagConstraints.HORIZONTAL; // Remplissage horizontal
////
////        // Ligne 1: Mot clé et boutons
////        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
////        searchPanel.add(jlbMc, gbc);  // Ajout du label "Mot clé"
////
////        gbc.gridx = 1; gbc.weightx = 1.0;
////        // Utilisation de java.awt.Font pour éviter les conflits avec iText
////        java.awt.Font swingFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
////        txtMc.setFont(swingFont);
////        searchPanel.add(txtMc, gbc);  // Ajout du champ de texte
////
////        gbc.gridx = 2; gbc.weightx = 0;
////        styleButton(btnOk, new Color(70, 130, 180)); // Bouton bleu
////        searchPanel.add(btnOk, gbc);
////
////        gbc.gridx = 3; gbc.weightx = 0;
////        styleButton(btnReset, new Color(220, 220, 220)); // Bouton gris
////        searchPanel.add(btnReset, gbc);
////
////        gbc.gridx = 4; gbc.weightx = 0;
////        styleButton(btnGenererPDF, new Color(75, 0, 130)); // Bouton violet
////        btnGenererPDF.setToolTipText("Générer un rapport PDF des produits par catégorie");
////        searchPanel.add(btnGenererPDF, gbc);
////
////        gbc.gridx = 5; gbc.weightx = 0;
////        styleButton(btnDeconnexion, new Color(178, 34, 34)); // Bouton rouge bordeaux
////        btnDeconnexion.setToolTipText("Se déconnecter et retourner à l'authentification");
////        searchPanel.add(btnDeconnexion, gbc);
////
////        // Ligne 2: Catégorie
////        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
////        searchPanel.add(lblCategorie, gbc);
////
////        gbc.gridx = 1; gbc.weightx = 1.0;
////        java.awt.Font swingFont2 = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
////        jcombo.setFont(swingFont2);
////        searchPanel.add(jcombo, gbc);
////
////        gbc.gridx = 2; gbc.weightx = 0;
////        searchPanel.add(lblIdCategorie, gbc);
////
////        gbc.gridx = 3; gbc.weightx = 0;
////        java.awt.Font swingFont3 = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
////        txtid.setFont(swingFont3);
////        txtid.setEditable(false);  // Champ non modifiable
////        txtid.setBackground(new Color(240, 240, 240)); // Fond gris pour indiquer lecture seule
////        searchPanel.add(txtid, gbc);
////
////        return searchPanel;
////    }
////
////    /**
////     * Création du panel CRUD pour la gestion des produits
////     * @return JPanel configuré pour les opérations CRUD
////     */
////    private JPanel createCrudPanel() {
////        JPanel crudPanel = new JPanel(new GridBagLayout());
////        crudPanel.setBorder(BorderFactory.createTitledBorder("Gestion des Produits"));
////        crudPanel.setBackground(new Color(245, 245, 245));
////        
////        GridBagConstraints gbc = new GridBagConstraints();
////        gbc.insets = new Insets(5, 5, 5, 5);
////        gbc.fill = GridBagConstraints.HORIZONTAL;
////
////        // Ligne 1: Labels des champs
////        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
////        crudPanel.add(new JLabel("ID Produit:"), gbc);
////
////        gbc.gridx = 1;
////        crudPanel.add(new JLabel("Nom Produit:"), gbc);
////
////        gbc.gridx = 2;
////        crudPanel.add(new JLabel("Prix:"), gbc);
////
////        gbc.gridx = 3;
////        crudPanel.add(new JLabel("Quantité:"), gbc);
////
////        gbc.gridx = 4;
////        crudPanel.add(new JLabel("Catégorie:"), gbc);
////
////        // Ligne 2: Champs de saisie
////        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.5;
////        java.awt.Font swingFont4 = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
////        txtIdProduit.setFont(swingFont4);
////        crudPanel.add(txtIdProduit, gbc);
////
////        gbc.gridx = 1; gbc.weightx = 1.0;
////        java.awt.Font swingFont5 = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
////        txtNomProduit.setFont(swingFont5);
////        crudPanel.add(txtNomProduit, gbc);
////
////        gbc.gridx = 2; gbc.weightx = 0.5;
////        java.awt.Font swingFont6 = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
////        txtPrix.setFont(swingFont6);
////        crudPanel.add(txtPrix, gbc);
////
////        gbc.gridx = 3; gbc.weightx = 0.5;
////        java.awt.Font swingFont7 = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
////        txtQuantite.setFont(swingFont7);
////        crudPanel.add(txtQuantite, gbc);
////
////        gbc.gridx = 4; gbc.weightx = 0.5;
////        java.awt.Font swingFont8 = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
////        comboCategoriesCRUD.setFont(swingFont8);
////        crudPanel.add(comboCategoriesCRUD, gbc);
////
////        // Ligne 3: Boutons CRUD
////        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 5;
////        gbc.anchor = GridBagConstraints.CENTER;
////        
////        JPanel buttonPanel = new JPanel(new FlowLayout());
////        styleButton(btnAjouter, new Color(34, 139, 34)); // Vert
////        buttonPanel.add(btnAjouter);
////        
////        styleButton(btnModifier, new Color(255, 165, 0)); // Orange
////        buttonPanel.add(btnModifier);
////        
////        styleButton(btnSupprimer, new Color(220, 20, 60)); // Rouge
////        buttonPanel.add(btnSupprimer);
////        
////        styleButton(btnNouveau, new Color(70, 130, 180)); // Bleu
////        buttonPanel.add(btnNouveau);
////        
////        crudPanel.add(buttonPanel, gbc);
////
////        return crudPanel;
////    }
////
////    /**
////     * Méthode utilitaire pour styliser les boutons
////     * @param button le bouton à styliser
////     * @param backgroundColor la couleur de fond
////     */
////    private void styleButton(JButton button, Color backgroundColor) {
////        button.setBackground(backgroundColor);
////        button.setForeground(Color.WHITE);
////        java.awt.Font swingFont9 = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
////        button.setFont(swingFont9);
////        button.setFocusPainted(false);  // Désactive l'effet de focus
////        button.setPreferredSize(new Dimension(100, 30)); // Taille uniforme
////    }
////
////    /**
////     * Configuration du tableau des produits
////     */
////    private void setupTable() {
////        produitModel = new ProduitModel();  // Création du modèle de données
////        jtable = new JTable(produitModel);  // Création du tableau avec le modèle
////        
////        // Configuration de l'apparence du tableau
////        java.awt.Font swingFont10 = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
////        jtable.setFont(swingFont10);
////        jtable.setRowHeight(25);                           // Hauteur des lignes
////        jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Sélection unique
////        jtable.setAutoCreateRowSorter(true);               // Tri automatique des colonnes
////        
////        // Renderer pour centrer le contenu des cellules
////        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
////        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
////        jtable.setDefaultRenderer(Object.class, centerRenderer);
////        
////        // Configuration des largeurs de colonnes
////        jtable.getColumnModel().getColumn(0).setPreferredWidth(80);   // ID Produit
////        jtable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Nom Produit
////        jtable.getColumnModel().getColumn(2).setPreferredWidth(100);  // Prix
////        jtable.getColumnModel().getColumn(3).setPreferredWidth(80);   // Quantité
////    }
////
////    /**
////     * Configuration de tous les écouteurs d'événements
////     */
////    private void setupEventListeners() {
////        // Recherche par mot clé
////        btnOk.addActionListener(e -> searchByKeyword());
////
////        // Réinitialisation
////        btnReset.addActionListener(e -> resetSearch());
////
////        // Déconnexion
////        btnDeconnexion.addActionListener(e -> deconnecter());
////
////        // Génération PDF
////        btnGenererPDF.addActionListener(e -> genererRapportPDF());
////
////        // Recherche par catégorie
////        jcombo.addActionListener(e -> {
////            if (jcombo.getSelectedIndex() == 0) {
////                loadAllProducts();  // Charger tous les produits si "Toutes les catégories"
////                txtid.setText("");
////            } else {
////                searchByCategory(); // Rechercher par catégorie spécifique
////            }
////        });
////
////        // Recherche avec la touche Entrée
////        txtMc.addActionListener(e -> searchByKeyword());
////
////        // Sélection dans le tableau - remplissage automatique du formulaire
////        jtable.getSelectionModel().addListSelectionListener(e -> {
////            if (!e.getValueIsAdjusting() && jtable.getSelectedRow() != -1) {
////                int selectedRow = jtable.getSelectedRow();
////                int modelRow = jtable.convertRowIndexToModel(selectedRow);
////                
////                // Récupérer les données directement depuis le modèle de tableau
////                String idProduit = produitModel.getValueAt(modelRow, 0).toString();
////                String nomProduit = produitModel.getValueAt(modelRow, 1).toString();
////                double prix = Double.parseDouble(produitModel.getValueAt(modelRow, 2).toString());
////                int quantite = Integer.parseInt(produitModel.getValueAt(modelRow, 3).toString());
////                
////                // Créer un objet Produit temporaire avec les données
////                Produit produit = new Produit();
////                produit.setIdProduit(idProduit);
////                produit.setNomProduit(nomProduit);
////                produit.setPrix(prix);
////                produit.setQuantite(quantite);
////                
////                // Pour la catégorie, on va la chercher depuis la base de données
////                try {
////                    Produit produitComplet = metier.getProduitById(idProduit);
////                    if (produitComplet != null && produitComplet.getUneCategorie() != null) {
////                        produit.setUneCategorie(produitComplet.getUneCategorie());
////                    }
////                } catch (Exception ex) {
////                    ex.printStackTrace();
////                }
////                
////                fillFormWithProduct(produit); // Remplir le formulaire avec les données
////            }
////        });
////
////        // Boutons CRUD
////        btnAjouter.addActionListener(e -> ajouterProduit());
////        btnModifier.addActionListener(e -> modifierProduit());
////        btnSupprimer.addActionListener(e -> supprimerProduit());
////        btnNouveau.addActionListener(e -> clearForm());
////    }
////
////    /**
////     * Génère un rapport PDF des produits par catégorie
////     * Cette méthode crée un document PDF structuré avec des tableaux et des statistiques
////     */
////    private void genererRapportPDF() {
////        // Création d'un sélecteur de fichier pour choisir l'emplacement de sauvegarde
////        JFileChooser fileChooser = new JFileChooser();
////        fileChooser.setDialogTitle("Enregistrer le rapport PDF");
////        fileChooser.setSelectedFile(new java.io.File("rapport_produits_par_categories.pdf"));
////        
////        int userSelection = fileChooser.showSaveDialog(this);
////        
////        if (userSelection == JFileChooser.APPROVE_OPTION) {
////            FileOutputStream file = null;
////            try {
////                java.io.File selectedFile = fileChooser.getSelectedFile();
////                // S'assurer que le fichier a l'extension .pdf
////                if (!selectedFile.getName().toLowerCase().endsWith(".pdf")) {
////                    selectedFile = new java.io.File(selectedFile.getAbsolutePath() + ".pdf");
////                }
////                
////                file = new FileOutputStream(selectedFile);
////                
////                // Création du document PDF
////                Document document = new Document();
////                PdfWriter.getInstance(document, file);
////                document.open();
////                
////                // Définition des polices pour le document
////                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
////                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
////                Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
////                Font categoryFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
////                
////                // Titre du rapport
////                Paragraph title = new Paragraph("RAPPORT DES PRODUITS PAR CATÉGORIES", titleFont);
////                title.setAlignment(Paragraph.ALIGN_CENTER);
////                title.setSpacingAfter(20);
////                document.add(title);
////                
////                // Date de génération
////                Paragraph date = new Paragraph("Généré le : " + new java.util.Date(), normalFont);
////                date.setAlignment(Paragraph.ALIGN_RIGHT);
////                date.setSpacingAfter(20);
////                document.add(date);
////                
////                // Récupérer toutes les catégories depuis la base de données
////                List<Categorie> categories = metier.getAllCategorie();
////                
////                if (categories.isEmpty()) {
////                    document.add(new Paragraph("Aucune catégorie trouvée.", normalFont));
////                } else {
////                    // Pour chaque catégorie, afficher les produits
////                    for (Categorie categorie : categories) {
////                        // Titre de la catégorie
////                        Paragraph catTitle = new Paragraph("Catégorie : " + categorie.getNomCategorie(), categoryFont);
////                        catTitle.setSpacingBefore(15);
////                        catTitle.setSpacingAfter(10);
////                        document.add(catTitle);
////                        
////                        // Récupérer les produits de cette catégorie
////                        List<Produit> produits = metier.getProduitsParIDCategorie(categorie.getIdCategorie());
////                        
////                        if (produits.isEmpty()) {
////                            document.add(new Paragraph("   Aucun produit dans cette catégorie.", normalFont));
////                            document.add(new Paragraph(" ")); // Ligne vide
////                        } else {
////                            // Créer un tableau pour les produits
////                            PdfPTable table = new PdfPTable(4);
////                            table.setWidthPercentage(100);
////                            table.setSpacingBefore(10);
////                            table.setSpacingAfter(15);
////                            
////                            // En-têtes du tableau
////                            String[] headers = {"ID Produit", "Nom Produit", "Prix (€)", "Quantité"};
////                            for (String header : headers) {
////                                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
////                                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
////                                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
////                                table.addCell(cell);
////                            }
////                            
////                            // Remplir le tableau avec les produits
////                            double totalCategorie = 0;
////                            for (Produit produit : produits) {
////                                table.addCell(new PdfPCell(new Phrase(produit.getIdProduit(), normalFont)));
////                                table.addCell(new PdfPCell(new Phrase(produit.getNomProduit(), normalFont)));
////                                table.addCell(new PdfPCell(new Phrase(String.format("%.2f", produit.getPrix()), normalFont)));
////                                table.addCell(new PdfPCell(new Phrase(String.valueOf(produit.getQuantite()), normalFont)));
////                                
////                                totalCategorie += produit.getPrix() * produit.getQuantite();
////                            }
////                            
////                            document.add(table);
////                            
////                            // Total pour la catégorie
////                            Paragraph total = new Paragraph(
////                                "Total de la catégorie : " + String.format("%.2f", totalCategorie) + " €", 
////                                headerFont
////                            );
////                            total.setSpacingAfter(10);
////                            document.add(total);
////                        }
////                    }
////                }
////                
////                // Statistiques générales
////                document.add(new Paragraph(" "));
////                Paragraph stats = new Paragraph("STATISTIQUES GÉNÉRALES", headerFont);
////                stats.setSpacingAfter(10);
////                document.add(stats);
////                
////                int totalProduits = 0;
////                double valeurTotale = 0;
////                
////                // Calcul des statistiques globales
////                for (Categorie categorie : categories) {
////                    List<Produit> produits = metier.getProduitsParIDCategorie(categorie.getIdCategorie());
////                    totalProduits += produits.size();
////                    for (Produit produit : produits) {
////                        valeurTotale += produit.getPrix() * produit.getQuantite();
////                    }
////                }
////                
////                document.add(new Paragraph("Nombre total de catégories : " + categories.size(), normalFont));
////                document.add(new Paragraph("Nombre total de produits : " + totalProduits, normalFont));
////                document.add(new Paragraph("Valeur totale du stock : " + String.format("%.2f", valeurTotale) + " €", normalFont));
////                
////                document.close();
////                
////                JOptionPane.showMessageDialog(this,
////                    "Rapport PDF généré avec succès!\nFichier : " + selectedFile.getAbsolutePath(),
////                    "Rapport généré",
////                    JOptionPane.INFORMATION_MESSAGE);
////                    
////            } catch (Exception ex) {
////                JOptionPane.showMessageDialog(this,
////                    "Erreur lors de la génération du PDF : " + ex.getMessage(),
////                    "Erreur",
////                    JOptionPane.ERROR_MESSAGE);
////                ex.printStackTrace();
////            } finally {
////                // Fermeture sécurisée du flux de fichier
////                if (file != null) {
////                    try {
////                        file.close();
////                    } catch (Exception ex) {
////                        ex.printStackTrace();
////                    }
////                }
////            }
////        }
////    }
////
////    /**
////     * Méthode de déconnexion - ferme la fenêtre courante et rouvre l'authentification
////     */
////    private void deconnecter() {
////        int confirmation = JOptionPane.showConfirmDialog(this,
////            "Êtes-vous sûr de vouloir vous déconnecter?",
////            "Confirmation de déconnexion",
////            JOptionPane.YES_NO_OPTION,
////            JOptionPane.QUESTION_MESSAGE);
////
////        if (confirmation == JOptionPane.YES_OPTION) {
////            // Fermer la fenêtre courante
////            this.dispose();
////            
////            // Rouvrir la fenêtre d'authentification dans l'EDT
////            java.awt.EventQueue.invokeLater(new Runnable() {
////                public void run() {
////                    new LoginFrame().setVisible(true);
////                }
////            });
////        }
////    }
////
////    /**
////     * Remplit le formulaire avec les données d'un produit sélectionné
////     * @param produit le produit à afficher dans le formulaire
////     */
////    private void fillFormWithProduct(Produit produit) {
////        txtIdProduit.setText(produit.getIdProduit());
////        txtNomProduit.setText(produit.getNomProduit());
////        txtPrix.setText(String.valueOf(produit.getPrix()));
////        txtQuantite.setText(String.valueOf(produit.getQuantite()));
////        
////        if (produit.getUneCategorie() != null) {
////            String nomCategorie = produit.getUneCategorie().getNomCategorie();
////            // Sélectionner la catégorie dans le combobox de manière sécurisée
////            setComboBoxValueSafely(comboCategoriesCRUD, nomCategorie);
////        }
////        
////        txtIdProduit.setEditable(false); // Empêcher la modification de l'ID
////    }
////
////    /**
////     * Méthode utilitaire pour sélectionner une valeur dans un JComboBox de manière sécurisée
////     * @param comboBox le ComboBox à modifier
////     * @param value la valeur à sélectionner
////     */
////    private void setComboBoxValueSafely(JComboBox<String> comboBox, String value) {
////        for (int i = 0; i < comboBox.getItemCount(); i++) {
////            if (comboBox.getItemAt(i).equals(value)) {
////                comboBox.setSelectedIndex(i);
////                return;
////            }
////        }
////        // Si la valeur n'est pas trouvée, sélectionner l'index 0 si disponible
////        if (comboBox.getItemCount() > 0) {
////            comboBox.setSelectedIndex(0);
////        }
////    }
////
////    /**
////     * Réinitialise le formulaire CRUD à son état initial
////     */
////    private void clearForm() {
////        txtIdProduit.setText("");
////        txtNomProduit.setText("");
////        txtPrix.setText("");
////        txtQuantite.setText("");
////        
////        // Sélectionner l'index 0 seulement si le combobox n'est pas vide
////        if (comboCategoriesCRUD.getItemCount() > 0) {
////            comboCategoriesCRUD.setSelectedIndex(0);
////        }
////        
////        txtIdProduit.setEditable(true);  // Réactiver l'édition de l'ID
////        txtIdProduit.requestFocus();     // Donner le focus au champ ID
////        
////        // Désélectionner la ligne dans le tableau
////        jtable.clearSelection();
////    }
////
////    /**
////     * Ajoute un nouveau produit à la base de données
////     */
////    private void ajouterProduit() {
////        if (validateForm()) {  // Validation des données avant ajout
////            try {
////                // Récupération des données du formulaire
////                String idProduit = txtIdProduit.getText().trim();
////                String nomProduit = txtNomProduit.getText().trim();
////                double prix = Double.parseDouble(txtPrix.getText().trim());
////                int quantite = Integer.parseInt(txtQuantite.getText().trim());
////                String nomCategorie = (String) comboCategoriesCRUD.getSelectedItem();
////                
////                // Vérifier si une catégorie valide est sélectionnée
////                if (nomCategorie == null || nomCategorie.equals("Sélectionner une catégorie")) {
////                    JOptionPane.showMessageDialog(this, 
////                        "Veuillez sélectionner une catégorie valide!", 
////                        "Erreur", 
////                        JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                // Vérifier si le produit existe déjà
////                Produit existingProduct = metier.getProduitById(idProduit);
////                if (existingProduct != null) {
////                    JOptionPane.showMessageDialog(this, 
////                        "Un produit avec cet ID existe déjà!", 
////                        "Erreur", 
////                        JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                int idCategorie = getIdCategorieFromName(nomCategorie);
////                
////                if (idCategorie == -1) {
////                    JOptionPane.showMessageDialog(this, "Catégorie invalide!", "Erreur", JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                // Création du nouveau produit
////                Produit nouveauProduit = new Produit();
////                nouveauProduit.setIdProduit(idProduit);
////                nouveauProduit.setNomProduit(nomProduit);
////                nouveauProduit.setPrix(prix);
////                nouveauProduit.setQuantite(quantite);
////                
////                // Appel à la couche métier pour l'ajout
////                metier.addProduit(nouveauProduit, idCategorie);
////                
////                JOptionPane.showMessageDialog(this, 
////                    "Produit ajouté avec succès!", 
////                    "Succès", 
////                    JOptionPane.INFORMATION_MESSAGE);
////                clearForm();
////                loadAllProducts();  // Recharger les données
////                
////            } catch (NumberFormatException ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Prix et quantité doivent être des nombres valides!", 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////            } catch (Exception ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Erreur lors de l'ajout: " + ex.getMessage(), 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                ex.printStackTrace();
////            }
////        }
////    }
////
////    /**
////     * Modifie un produit existant dans la base de données
////     */
////    private void modifierProduit() {
////        if (jtable.getSelectedRow() == -1) {
////            JOptionPane.showMessageDialog(this, 
////                "Veuillez sélectionner un produit à modifier!", 
////                "Avertissement", 
////                JOptionPane.WARNING_MESSAGE);
////            return;
////        }
////        
////        if (validateForm()) {
////            try {
////                String idProduit = txtIdProduit.getText().trim();
////                String nomProduit = txtNomProduit.getText().trim();
////                double prix = Double.parseDouble(txtPrix.getText().trim());
////                int quantite = Integer.parseInt(txtQuantite.getText().trim());
////                String nomCategorie = (String) comboCategoriesCRUD.getSelectedItem();
////                
////                // Vérifier si une catégorie valide est sélectionnée
////                if (nomCategorie == null || nomCategorie.equals("Sélectionner une catégorie")) {
////                    JOptionPane.showMessageDialog(this, 
////                        "Veuillez sélectionner une catégorie valide!", 
////                        "Erreur", 
////                        JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                int idCategorie = getIdCategorieFromName(nomCategorie);
////                
////                if (idCategorie == -1) {
////                    JOptionPane.showMessageDialog(this, "Catégorie invalide!", "Erreur", JOptionPane.ERROR_MESSAGE);
////                    return;
////                }
////                
////                // Récupérer la catégorie complète
////                Categorie categorie = metier.getCategorie(idCategorie);
////                
////                // Création du produit modifié
////                Produit produitModifie = new Produit();
////                produitModifie.setIdProduit(idProduit);
////                produitModifie.setNomProduit(nomProduit);
////                produitModifie.setPrix(prix);
////                produitModifie.setQuantite(quantite);
////                produitModifie.setUneCategorie(categorie);
////                
////                // Appel à la couche métier pour la modification
////                metier.updateProduit(produitModifie);
////                
////                JOptionPane.showMessageDialog(this, 
////                    "Produit modifié avec succès!", 
////                    "Succès", 
////                    JOptionPane.INFORMATION_MESSAGE);
////                clearForm();
////                loadAllProducts();
////                
////            } catch (NumberFormatException ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Prix et quantité doivent être des nombres valides!", 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////            } catch (Exception ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Erreur lors de la modification: " + ex.getMessage(), 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                ex.printStackTrace();
////            }
////        }
////    }
////
////    /**
////     * Supprime un produit de la base de données après confirmation
////     */
////    private void supprimerProduit() {
////        if (jtable.getSelectedRow() == -1) {
////            JOptionPane.showMessageDialog(this, 
////                "Veuillez sélectionner un produit à supprimer!", 
////                "Avertissement", 
////                JOptionPane.WARNING_MESSAGE);
////            return;
////        }
////        
////        String idProduit = txtIdProduit.getText().trim();
////        String nomProduit = txtNomProduit.getText().trim();
////        
////        // Demande de confirmation avant suppression
////        int confirmation = JOptionPane.showConfirmDialog(this, 
////            "Êtes-vous sûr de vouloir supprimer le produit : " + nomProduit + " (ID: " + idProduit + ")?", 
////            "Confirmation de suppression", 
////            JOptionPane.YES_NO_OPTION,
////            JOptionPane.WARNING_MESSAGE);
////        
////        if (confirmation == JOptionPane.YES_OPTION) {
////            try {
////                metier.deleteProduit(idProduit);
////                
////                JOptionPane.showMessageDialog(this, 
////                    "Produit supprimé avec succès!", 
////                    "Succès", 
////                    JOptionPane.INFORMATION_MESSAGE);
////                clearForm();
////                loadAllProducts();
////                
////            } catch (Exception ex) {
////                JOptionPane.showMessageDialog(this, 
////                    "Erreur lors de la suppression: " + ex.getMessage(), 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                ex.printStackTrace();
////            }
////        }
////    }
////
////    /**
////     * Valide les données du formulaire CRUD
////     * @return true si le formulaire est valide, false sinon
////     */
////    private boolean validateForm() {
////        // Validation de l'ID produit
////        if (txtIdProduit.getText().trim().isEmpty()) {
////            JOptionPane.showMessageDialog(this, 
////                "L'ID produit est obligatoire!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtIdProduit.requestFocus();
////            return false;
////        }
////        // Validation du nom produit
////        if (txtNomProduit.getText().trim().isEmpty()) {
////            JOptionPane.showMessageDialog(this, 
////                "Le nom du produit est obligatoire!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtNomProduit.requestFocus();
////            return false;
////        }
////        // Validation du prix
////        if (txtPrix.getText().trim().isEmpty()) {
////            JOptionPane.showMessageDialog(this, 
////                "Le prix est obligatoire!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtPrix.requestFocus();
////            return false;
////        }
////        // Validation de la quantité
////        if (txtQuantite.getText().trim().isEmpty()) {
////            JOptionPane.showMessageDialog(this, 
////                "La quantité est obligatoire!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtQuantite.requestFocus();
////            return false;
////        }
////        
////        // Vérification sécurisée de la catégorie
////        if (comboCategoriesCRUD.getSelectedItem() == null || 
////            comboCategoriesCRUD.getSelectedItem().equals("Sélectionner une catégorie")) {
////            JOptionPane.showMessageDialog(this, 
////                "Veuillez sélectionner une catégorie!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            comboCategoriesCRUD.requestFocus();
////            return false;
////        }
////        
////        // Validation du format et de la valeur du prix
////        try {
////            double prix = Double.parseDouble(txtPrix.getText().trim());
////            if (prix < 0) {
////                JOptionPane.showMessageDialog(this, 
////                    "Le prix ne peut pas être négatif!", 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                txtPrix.requestFocus();
////                return false;
////            }
////        } catch (NumberFormatException e) {
////            JOptionPane.showMessageDialog(this, 
////                "Le prix doit être un nombre valide!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtPrix.requestFocus();
////            return false;
////        }
////        
////        // Validation du format et de la valeur de la quantité
////        try {
////            int quantite = Integer.parseInt(txtQuantite.getText().trim());
////            if (quantite < 0) {
////                JOptionPane.showMessageDialog(this, 
////                    "La quantité ne peut pas être négative!", 
////                    "Erreur", 
////                    JOptionPane.ERROR_MESSAGE);
////                txtQuantite.requestFocus();
////                return false;
////            }
////        } catch (NumberFormatException e) {
////            JOptionPane.showMessageDialog(this, 
////                "La quantité doit être un nombre entier valide!", 
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            txtQuantite.requestFocus();
////            return false;
////        }
////        
////        return true; // Toutes les validations sont passées
////    }
////
////    /**
////     * Recherche des produits par mot clé
////     */
////    private void searchByKeyword() {
////        String mc = txtMc.getText().trim();
////        try {
////            List<Produit> produits;
////            if (mc.isEmpty()) {
////                produits = metier.getAllProduits();  // Tous les produits si champ vide
////            } else {
////                produits = metier.getProduitsParMotCle(mc);  // Recherche par mot clé
////            }
////            produitModel.loadData(produits);  // Mise à jour du modèle de tableau
////            
////            // Message d'information si aucun résultat
////            if (produits.isEmpty() && !mc.isEmpty()) {
////                JOptionPane.showMessageDialog(this, 
////                    "Aucun produit trouvé avec le mot clé: " + mc,
////                    "Information", 
////                    JOptionPane.INFORMATION_MESSAGE);
////            }
////        } catch (Exception ex) {
////            JOptionPane.showMessageDialog(this, 
////                "Erreur lors de la recherche: " + ex.getMessage(),
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            ex.printStackTrace();
////        }
////    }
////
////    /**
////     * Recherche des produits par catégorie
////     */
////    private void searchByCategory() {
////        try {
////            String nomCat = (String) jcombo.getSelectedItem();
////            if (nomCat != null && !nomCat.equals("Toutes les catégories")) {
////                Connection conn = SingletonConnection.getConnection();
////                String query = "SELECT idCategorie FROM categorie WHERE nomCategorie = ?";
////                PreparedStatement ps = conn.prepareStatement(query);
////                ps.setString(1, nomCat);
////                ResultSet rs = ps.executeQuery();
////                
////                if (rs.next()) {
////                    int idCategorie = rs.getInt("idCategorie");
////                    List<Produit> produits = metier.getProduitsParIDCategorie(idCategorie);
////                    produitModel.loadData(produits);
////                    txtid.setText(String.valueOf(idCategorie));  // Affichage de l'ID catégorie
////                    
////                    if (produits.isEmpty()) {
////                        JOptionPane.showMessageDialog(this, 
////                            "Aucun produit trouvé dans la catégorie: " + nomCat,
////                            "Information", 
////                            JOptionPane.INFORMATION_MESSAGE);
////                    }
////                }
////                
////                rs.close();
////                ps.close();
////            }
////        } catch (Exception e) {
////            JOptionPane.showMessageDialog(this, 
////                "Erreur lors de la recherche par catégorie: " + e.getMessage(),
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            e.printStackTrace();
////        }
////    }
////
////    /**
////     * Réinitialise les critères de recherche et recharge tous les produits
////     */
////    private void resetSearch() {
////        txtMc.setText("");
////        
////        // Réinitialisation sécurisée des combobox
////        if (jcombo.getItemCount() > 0) {
////            jcombo.setSelectedIndex(0);
////        }
////        
////        txtid.setText("");
////        loadAllProducts();  // Rechargement de tous les produits
////        txtMc.requestFocus();  // Focus sur le champ de recherche
////    }
////
////    /**
////     * Charge tous les produits depuis la base de données
////     */
////    private void loadAllProducts() {
////        try {
////            List<Produit> produits = metier.getAllProduits();
////            produitModel.loadData(produits);  // Mise à jour du modèle de tableau
////        } catch (Exception e) {
////            JOptionPane.showMessageDialog(this, 
////                "Erreur lors du chargement des produits: " + e.getMessage(),
////                "Erreur", 
////                JOptionPane.ERROR_MESSAGE);
////            e.printStackTrace();
////        }
////    }
////
////    /**
////     * Méthode main - point d'entrée de l'application
////     * @param args arguments de la ligne de commande
////     */
////    public static void main(String[] args) {
////        // Configuration du look and feel du système
////        try {
////            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
////        } catch (Exception e) {
////            // Fallback sur le look and feel par défaut en cas d'erreur
////            try {
////                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
////            } catch (Exception ex) {
////                ex.printStackTrace();
////            }
////        }
////        
////        // Création de l'interface dans l'EDT (Event Dispatch Thread)
////        java.awt.EventQueue.invokeLater(new Runnable() {
////            public void run() {
////                new CatalogueSwing().setVisible(true);  // Création et affichage de la fenêtre
////            }
////        });
////    }
////}
//package Presentation;
//
//import java.awt.*;
//import java.awt.event.*;
//import java.sql.*;
//import java.util.List;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.table.DefaultTableCellRenderer;
//
//import Metier.MetierCatalogueImp;
//import Metier.Produit;
//import Metier.ProduitModel;
//import Metier.SingletonConnection;
//import Metier.Categorie;
//
//// Import iTextPDF pour la génération de rapports
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Phrase;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.text.BaseColor;
//import java.io.FileOutputStream;
//import java.io.File;
//
//public class CatalogueSwing extends JFrame {
//    private JLabel jlbMc = new JLabel("Mot clé:");
//    private JTextField txtMc = new JTextField(15);
//    private JButton btnOk = new JButton("Rechercher");
//    private JButton btnReset = new JButton("Réinitialiser");
//    private JButton btnDeconnexion = new JButton("Déconnexion");
//    private JButton btnGenererPDF = new JButton("Générer PDF"); // NOUVEAU BOUTON
//    private JTable jtable;
//    private ProduitModel produitModel;
//    private MetierCatalogueImp metier = new MetierCatalogueImp();
//    private JComboBox<String> jcombo = new JComboBox<>();
//    private JLabel lblCategorie = new JLabel("Catégorie:");
//    private JLabel lblIdCategorie = new JLabel("ID Catégorie:");
//    private JTextField txtid = new JTextField(5);
//    
//    // Composants CRUD
//    private JTextField txtIdProduit = new JTextField(10);
//    private JTextField txtNomProduit = new JTextField(20);
//    private JTextField txtPrix = new JTextField(10);
//    private JTextField txtQuantite = new JTextField(10);
//    private JComboBox<String> comboCategoriesCRUD = new JComboBox<>();
//    private JButton btnAjouter = new JButton("Ajouter");
//    private JButton btnModifier = new JButton("Modifier");
//    private JButton btnSupprimer = new JButton("Supprimer");
//    private JButton btnNouveau = new JButton("Nouveau");
//
//    /**
//     * Test de connexion au démarrage
//     */
//    private boolean testerConnexionInitiale() {
//        try {
//            if (!SingletonConnection.testConnection()) {
//                JOptionPane.showMessageDialog(this,
//                    "❌ Impossible de se connecter à la base de données.\n" +
//                    "Veuillez vérifier que:\n" +
//                    "• MySQL est démarré\n" +
//                    "• La base 'catalogue1' existe\n" +
//                    "• Les identifiants sont corrects\n\n" +
//                    "L'application va se fermer.",
//                    "Erreur de Connexion",
//                    JOptionPane.ERROR_MESSAGE);
//                return false;
//            }
//            return true;
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this,
//                "❌ Erreur critique: " + e.getMessage(),
//                "Erreur de Connexion",
//                JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
//    }
//
//    private void remplirCombo() {
//        try {
//            Connection conn = SingletonConnection.getConnection();
//            String query = "SELECT * FROM categorie ORDER BY nomCategorie";
//            PreparedStatement ps = conn.prepareStatement(query);
//            ResultSet rs = ps.executeQuery();
//            
//            // Vider les combobox
//            jcombo.removeAllItems();
//            comboCategoriesCRUD.removeAllItems();
//            
//            // Ajouter l'option par défaut seulement s'il y a des catégories
//            jcombo.addItem("Toutes les catégories");
//            comboCategoriesCRUD.addItem("Sélectionner une catégorie");
//            
//            boolean hasCategories = false;
//            while (rs.next()) {
//                hasCategories = true;
//                String nomCategorie = rs.getString("nomCategorie");
//                jcombo.addItem(nomCategorie);
//                comboCategoriesCRUD.addItem(nomCategorie);
//            }
//            
//            rs.close();
//            ps.close();
//            
//            // Si pas de catégories, afficher un message
//            if (!hasCategories) {
//                System.out.println("Aucune catégorie trouvée dans la base de données");
//            }
//            
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, 
//                "Erreur lors du chargement des catégories: " + e.getMessage(),
//                "Erreur", 
//                JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
//    }
//    
//    private int getIdCategorieFromName(String nomCategorie) {
//        try {
//            Connection conn = SingletonConnection.getConnection();
//            String query = "SELECT idCategorie FROM categorie WHERE nomCategorie = ?";
//            PreparedStatement ps = conn.prepareStatement(query);
//            ps.setString(1, nomCategorie);
//            ResultSet rs = ps.executeQuery();
//            
//            if (rs.next()) {
//                int id = rs.getInt("idCategorie");
//                rs.close();
//                ps.close();
//                return id;
//            }
//            
//            rs.close();
//            ps.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//
//    public CatalogueSwing() {
//        // Tester la connexion avant tout
//        if (!testerConnexionInitiale()) {
//            System.exit(1);
//        }
//        
//        initializeUI();
//        setupLayout();
//        // Remplir les combobox AVANT de setup les event listeners
//        remplirCombo();
//        setupEventListeners();
//        loadAllProducts();
//    }
//
//    private void initializeUI() {
//        setTitle("Catalogue des Produits - Gestion CRUD");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setMinimumSize(new Dimension(1200, 800));
//    }
//
//    private void setupLayout() {
//        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
//        setContentPane(mainPanel);
//
//        // Panel de recherche avec déconnexion
//        JPanel searchPanel = createSearchPanel();
//        mainPanel.add(searchPanel, BorderLayout.NORTH);
//
//        // Table des produits
//        setupTable();
//        JScrollPane scrollPane = new JScrollPane(jtable);
//        mainPanel.add(scrollPane, BorderLayout.CENTER);
//
//        // Panel CRUD
//        JPanel crudPanel = createCrudPanel();
//        mainPanel.add(crudPanel, BorderLayout.SOUTH);
//    }
//
//    private JPanel createSearchPanel() {
//    	java.awt.Font swingFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
//        JPanel searchPanel = new JPanel(new GridBagLayout());
//        searchPanel.setBorder(BorderFactory.createTitledBorder("Critères de recherche"));
//        searchPanel.setBackground(new Color(240, 240, 240));
//        
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 5, 5, 5);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        // Ligne 1: Mot clé et boutons
//        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
//        searchPanel.add(jlbMc, gbc);
//
//        gbc.gridx = 1; gbc.weightx = 1.0;
//        txtMc.setFont(swingFont);
//        searchPanel.add(txtMc, gbc);
//
//        gbc.gridx = 2; gbc.weightx = 0;
//        styleButton(btnOk, new Color(70, 130, 180));
//        searchPanel.add(btnOk, gbc);
//
//        gbc.gridx = 3; gbc.weightx = 0;
//        styleButton(btnReset, new Color(220, 220, 220));
//        searchPanel.add(btnReset, gbc);
//
//        // BOUTON PDF
//        gbc.gridx = 4; gbc.weightx = 0;
//        styleButton(btnGenererPDF, new Color(75, 0, 130)); // Violet
//        btnGenererPDF.setToolTipText("Générer un rapport PDF des produits");
//        searchPanel.add(btnGenererPDF, gbc);
//
//        gbc.gridx = 5; gbc.weightx = 0;
//        styleButton(btnDeconnexion, new Color(178, 34, 34)); // Rouge bordeaux
//        btnDeconnexion.setToolTipText("Se déconnecter et retourner à l'authentification");
//        searchPanel.add(btnDeconnexion, gbc);
//
//        // Ligne 2: Catégorie
//        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
//        searchPanel.add(lblCategorie, gbc);
//
//        gbc.gridx = 1; gbc.weightx = 1.0;
//       // jcombo.setFont(new Font("Tahoma", Font.PLAIN, 12));
//      //  java.awt.Font swingFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
//        jcombo.setFont(swingFont);
//        searchPanel.add(jcombo, gbc);
//        /*
//         *  java.awt.Font swingFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
////        txtMc.setFont(swingFont);
//         */
//
//        gbc.gridx = 2; gbc.weightx = 0;
//        searchPanel.add(lblIdCategorie, gbc);
//
//        gbc.gridx = 3; gbc.weightx = 0;
//        txtid.setFont(swingFont);
//        txtid.setEditable(false);
//        txtid.setBackground(new Color(240, 240, 240));
//        searchPanel.add(txtid, gbc);
//
//        return searchPanel;
//    }
//
//    private JPanel createCrudPanel() {
//    	java.awt.Font swingFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
//        JPanel crudPanel = new JPanel(new GridBagLayout());
//        crudPanel.setBorder(BorderFactory.createTitledBorder("Gestion des Produits"));
//        crudPanel.setBackground(new Color(245, 245, 245));
//        
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 5, 5, 5);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        // Ligne 1: Labels
//        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
//        crudPanel.add(new JLabel("ID Produit:"), gbc);
//
//        gbc.gridx = 1;
//        crudPanel.add(new JLabel("Nom Produit:"), gbc);
//
//        gbc.gridx = 2;
//        crudPanel.add(new JLabel("Prix:"), gbc);
//
//        gbc.gridx = 3;
//        crudPanel.add(new JLabel("Quantité:"), gbc);
//
//        gbc.gridx = 4;
//        crudPanel.add(new JLabel("Catégorie:"), gbc);
//
//        // Ligne 2: Champs de saisie
//        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.5;
//        txtIdProduit.setFont(swingFont);
//        crudPanel.add(txtIdProduit, gbc);
//
//        gbc.gridx = 1; gbc.weightx = 1.0;
//        txtNomProduit.setFont(swingFont);
//        crudPanel.add(txtNomProduit, gbc);
//
//        gbc.gridx = 2; gbc.weightx = 0.5;
//        txtPrix.setFont(swingFont);
//        crudPanel.add(txtPrix, gbc);
//
//        gbc.gridx = 3; gbc.weightx = 0.5;
//        txtQuantite.setFont(swingFont);
//        crudPanel.add(txtQuantite, gbc);
//
//        gbc.gridx = 4; gbc.weightx = 0.5;
//        comboCategoriesCRUD.setFont(swingFont);
//        crudPanel.add(comboCategoriesCRUD, gbc);
//
//        // Ligne 3: Boutons
//        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 5;
//        gbc.anchor = GridBagConstraints.CENTER;
//        
//        JPanel buttonPanel = new JPanel(new FlowLayout());
//        styleButton(btnAjouter, new Color(34, 139, 34)); // Vert
//        buttonPanel.add(btnAjouter);
//        
//        styleButton(btnModifier, new Color(255, 165, 0)); // Orange
//        buttonPanel.add(btnModifier);
//        
//        styleButton(btnSupprimer, new Color(220, 20, 60)); // Rouge
//        buttonPanel.add(btnSupprimer);
//        
//        styleButton(btnNouveau, new Color(70, 130, 180)); // Bleu
//        buttonPanel.add(btnNouveau);
//        
//        crudPanel.add(buttonPanel, gbc);
//
//        return crudPanel;
//    }
//
//    private void styleButton(JButton button, Color backgroundColor) {
//        button.setBackground(backgroundColor);
//        java.awt.Font swingFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
//        button.setForeground(Color.WHITE);
//        button.setFont(swingFont);
//        button.setFocusPainted(false);
//        button.setPreferredSize(new Dimension(100, 30));
//    }
//
//    private void setupTable() {
//    	java.awt.Font swingFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
//        produitModel = new ProduitModel();
//        jtable = new JTable(produitModel);
//        
//        jtable.setFont(swingFont);
//        jtable.setRowHeight(25);
//        jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        jtable.setAutoCreateRowSorter(true);
//        
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//        jtable.setDefaultRenderer(Object.class, centerRenderer);
//        
//        jtable.getColumnModel().getColumn(0).setPreferredWidth(80);
//        jtable.getColumnModel().getColumn(1).setPreferredWidth(200);
//        jtable.getColumnModel().getColumn(2).setPreferredWidth(100);
//        jtable.getColumnModel().getColumn(3).setPreferredWidth(80);
//    }
//
//    private void setupEventListeners() {
//        // Recherche par mot clé
//        btnOk.addActionListener(e -> searchByKeyword());
//
//        // Réinitialisation
//        btnReset.addActionListener(e -> resetSearch());
//
//        // Déconnexion
//        btnDeconnexion.addActionListener(e -> deconnecter());
//
//        // Génération PDF
//        btnGenererPDF.addActionListener(e -> genererRapportPDF());
//
//        // Recherche par catégorie
//        jcombo.addActionListener(e -> {
//            if (jcombo.getSelectedIndex() == 0) {
//                loadAllProducts();
//                txtid.setText("");
//            } else {
//                searchByCategory();
//            }
//        });
//
//        // Recherche avec la touche Entrée
//        txtMc.addActionListener(e -> searchByKeyword());
//
//        // Sélection dans le tableau - VERSION CORRIGÉE
//        jtable.getSelectionModel().addListSelectionListener(e -> {
//            if (!e.getValueIsAdjusting() && jtable.getSelectedRow() != -1) {
//                int selectedRow = jtable.getSelectedRow();
//                int modelRow = jtable.convertRowIndexToModel(selectedRow);
//                
//                try {
//                    // Récupérer les données directement depuis le modèle de tableau
//                    String idProduit = produitModel.getValueAt(modelRow, 0).toString();
//                    String nomProduit = produitModel.getValueAt(modelRow, 1).toString();
//                    double prix = Double.parseDouble(produitModel.getValueAt(modelRow, 2).toString());
//                    int quantite = Integer.parseInt(produitModel.getValueAt(modelRow, 3).toString());
//                    
//                    // Créer un objet Produit temporaire avec les données
//                    Produit produit = new Produit();
//                    produit.setIdProduit(idProduit);
//                    produit.setNomProduit(nomProduit);
//                    produit.setPrix(prix);
//                    produit.setQuantite(quantite);
//                    
//                    // Essayer de récupérer la catégorie avec gestion d'erreur améliorée
//                    try {
//                        Produit produitComplet = metier.getProduitById(idProduit);
//                        if (produitComplet != null && produitComplet.getUneCategorie() != null) {
//                            produit.setUneCategorie(produitComplet.getUneCategorie());
//                        }
//                    } catch (Exception ex) {
//                        System.err.println("⚠️ Impossible de récupérer la catégorie: " + ex.getMessage());
//                        // Continuer sans la catégorie plutôt que de planter
//                    }
//                    
//                    fillFormWithProduct(produit);
//                    
//                } catch (Exception ex) {
//                    System.err.println("❌ Erreur lors de la sélection: " + ex.getMessage());
//                    JOptionPane.showMessageDialog(this,
//                        "Erreur lors de la récupération des données: " + ex.getMessage(),
//                        "Erreur",
//                        JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//
//        // Boutons CRUD
//        btnAjouter.addActionListener(e -> ajouterProduit());
//        btnModifier.addActionListener(e -> modifierProduit());
//        btnSupprimer.addActionListener(e -> supprimerProduit());
//        btnNouveau.addActionListener(e -> clearForm());
//    }
//
//    /**
//     * Génère un rapport PDF des produits
//     */
//    private void genererRapportPDF() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle("Enregistrer le rapport PDF");
//        fileChooser.setSelectedFile(new File("rapport_produits.pdf"));
//        
//        int userSelection = fileChooser.showSaveDialog(this);
//        
//        if (userSelection == JFileChooser.APPROVE_OPTION) {
//            FileOutputStream file = null;
//            try {
//                File selectedFile = fileChooser.getSelectedFile();
//                // S'assurer que le fichier a l'extension .pdf
//                if (!selectedFile.getName().toLowerCase().endsWith(".pdf")) {
//                    selectedFile = new File(selectedFile.getAbsolutePath() + ".pdf");
//                }
//                
//                file = new FileOutputStream(selectedFile);
//                
//                // Création du document PDF
//                Document document = new Document();
//                PdfWriter.getInstance(document, file);
//                document.open();
//                
//                // Configuration des polices
//                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
//                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
//                Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
//                Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
//                
//                // Titre du rapport
//                Paragraph title = new Paragraph("RAPPORT DU CATALOGUE DES PRODUITS", titleFont);
//                title.setAlignment(Paragraph.ALIGN_CENTER);
//                title.setSpacingAfter(20);
//                document.add(title);
//                
//                // Date de génération
//                Paragraph date = new Paragraph("Généré le : " + new java.util.Date(), normalFont);
//                date.setAlignment(Paragraph.ALIGN_RIGHT);
//                date.setSpacingAfter(20);
//                document.add(date);
//                
//                // Récupérer tous les produits
//                List<Produit> produits = metier.getAllProduits();
//                
//                if (produits.isEmpty()) {
//                    document.add(new Paragraph("Aucun produit trouvé dans le catalogue.", normalFont));
//                } else {
//                    // Création du tableau principal
//                    PdfPTable table = new PdfPTable(5);
//                    table.setWidthPercentage(100);
//                    table.setSpacingBefore(10);
//                    table.setSpacingAfter(15);
//                    
//                    // En-têtes du tableau
//                    String[] headers = {"ID Produit", "Nom Produit", "Catégorie", "Prix (€)", "Quantité"};
//                    for (String header : headers) {
//                        PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
//                        cell.setBackgroundColor(BaseColor.DARK_GRAY);
//                        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//                        cell.setPadding(5);
//                        table.addCell(cell);
//                    }
//                    
//                    // Remplissage du tableau avec les produits
//                    double valeurTotaleStock = 0;
//                    int totalProduits = 0;
//                    
//                    for (Produit produit : produits) {
//                        table.addCell(new PdfPCell(new Phrase(produit.getIdProduit(), normalFont)));
//                        table.addCell(new PdfPCell(new Phrase(produit.getNomProduit(), normalFont)));
//                        
//                        String nomCategorie = "Non catégorisé";
//                        if (produit.getUneCategorie() != null) {
//                            nomCategorie = produit.getUneCategorie().getNomCategorie();
//                        }
//                        table.addCell(new PdfPCell(new Phrase(nomCategorie, normalFont)));
//                        
//                        table.addCell(new PdfPCell(new Phrase(String.format("%.2f", produit.getPrix()), normalFont)));
//                        table.addCell(new PdfPCell(new Phrase(String.valueOf(produit.getQuantite()), normalFont)));
//                        
//                        // Calcul des statistiques
//                        valeurTotaleStock += produit.getPrix() * produit.getQuantite();
//                        totalProduits++;
//                    }
//                    
//                    document.add(table);
//                    
//                    // Section des statistiques
//                    Paragraph statsTitle = new Paragraph("STATISTIQUES", boldFont);
//                    statsTitle.setSpacingBefore(20);
//                    statsTitle.setSpacingAfter(10);
//                    document.add(statsTitle);
//                    
//                    // Tableau des statistiques
//                    PdfPTable statsTable = new PdfPTable(2);
//                    statsTable.setWidthPercentage(50);
//                    statsTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
//                    
//                    addStatRow(statsTable, "Nombre total de produits:", String.valueOf(totalProduits), boldFont, normalFont);
//                    addStatRow(statsTable, "Valeur totale du stock:", String.format("%.2f €", valeurTotaleStock), boldFont, normalFont);
//                    
//                    // Calcul du produit le plus cher
//                    if (!produits.isEmpty()) {
//                        Produit produitPlusCher = produits.stream()
//                            .max((p1, p2) -> Double.compare(p1.getPrix(), p2.getPrix()))
//                            .orElse(null);
//                        if (produitPlusCher != null) {
//                            addStatRow(statsTable, "Produit le plus cher:", 
//                                produitPlusCher.getNomProduit() + " (" + String.format("%.2f €", produitPlusCher.getPrix()) + ")", 
//                                boldFont, normalFont);
//                        }
//                    }
//                    
//                    document.add(statsTable);
//                }
//                
//                document.close();
//                
//                JOptionPane.showMessageDialog(this,
//                    "✅ Rapport PDF généré avec succès!\nFichier : " + selectedFile.getAbsolutePath(),
//                    "Rapport généré",
//                    JOptionPane.INFORMATION_MESSAGE);
//                    
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this,
//                    "❌ Erreur lors de la génération du PDF : " + ex.getMessage(),
//                    "Erreur",
//                    JOptionPane.ERROR_MESSAGE);
//                ex.printStackTrace();
//            } finally {
//                // Fermeture sécurisée du flux de fichier
//                if (file != null) {
//                    try {
//                        file.close();
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * Méthode utilitaire pour ajouter une ligne de statistique au tableau PDF
//     */
//    private void addStatRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
//        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
//        labelCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//        labelCell.setBorder(PdfPCell.NO_BORDER);
//        labelCell.setPadding(5);
//        
//        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
//        valueCell.setBorder(PdfPCell.NO_BORDER);
//        valueCell.setPadding(5);
//        
//        table.addCell(labelCell);
//        table.addCell(valueCell);
//    }
//
//    private void deconnecter() {
//        int confirmation = JOptionPane.showConfirmDialog(this,
//            "Êtes-vous sûr de vouloir vous déconnecter?",
//            "Confirmation de déconnexion",
//            JOptionPane.YES_NO_OPTION,
//            JOptionPane.QUESTION_MESSAGE);
//
//        if (confirmation == JOptionPane.YES_OPTION) {
//            // Fermer proprement la connexion
//            SingletonConnection.closeConnection();
//            
//            // Fermer la fenêtre courante
//            this.dispose();
//            
//            // Rouvrir la fenêtre d'authentification
//            java.awt.EventQueue.invokeLater(new Runnable() {
//                public void run() {
//                    // new LoginFrame().setVisible(true); // Décommentez si vous avez une fenêtre de login
//                    System.out.println("✅ Déconnexion réussie");
//                }
//            });
//        }
//    }
//
//    private void fillFormWithProduct(Produit produit) {
//        txtIdProduit.setText(produit.getIdProduit());
//        txtNomProduit.setText(produit.getNomProduit());
//        txtPrix.setText(String.valueOf(produit.getPrix()));
//        txtQuantite.setText(String.valueOf(produit.getQuantite()));
//        
//        if (produit.getUneCategorie() != null) {
//            String nomCategorie = produit.getUneCategorie().getNomCategorie();
//            // Sélectionner la catégorie dans le combobox de manière sécurisée
//            setComboBoxValueSafely(comboCategoriesCRUD, nomCategorie);
//        }
//        
//        txtIdProduit.setEditable(false); // Empêcher la modification de l'ID
//    }
//
//    private void setComboBoxValueSafely(JComboBox<String> comboBox, String value) {
//        for (int i = 0; i < comboBox.getItemCount(); i++) {
//            if (comboBox.getItemAt(i).equals(value)) {
//                comboBox.setSelectedIndex(i);
//                return;
//            }
//        }
//        // Si la valeur n'est pas trouvée, sélectionner l'index 0 si disponible
//        if (comboBox.getItemCount() > 0) {
//            comboBox.setSelectedIndex(0);
//        }
//    }
//
//    private void clearForm() {
//        txtIdProduit.setText("");
//        txtNomProduit.setText("");
//        txtPrix.setText("");
//        txtQuantite.setText("");
//        
//        // Sélectionner l'index 0 seulement si le combobox n'est pas vide
//        if (comboCategoriesCRUD.getItemCount() > 0) {
//            comboCategoriesCRUD.setSelectedIndex(0);
//        }
//        
//        txtIdProduit.setEditable(true);
//        txtIdProduit.requestFocus();
//        
//        // Désélectionner la ligne dans le tableau
//        jtable.clearSelection();
//    }
//
//    private void ajouterProduit() {
//        if (validateForm()) {
//            try {
//                String idProduit = txtIdProduit.getText().trim();
//                String nomProduit = txtNomProduit.getText().trim();
//                double prix = Double.parseDouble(txtPrix.getText().trim());
//                int quantite = Integer.parseInt(txtQuantite.getText().trim());
//                String nomCategorie = (String) comboCategoriesCRUD.getSelectedItem();
//                
//                // Vérifier si une catégorie valide est sélectionnée
//                if (nomCategorie == null || nomCategorie.equals("Sélectionner une catégorie")) {
//                    JOptionPane.showMessageDialog(this, 
//                        "Veuillez sélectionner une catégorie valide!", 
//                        "Erreur", 
//                        JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                
//                // Vérifier si le produit existe déjà
//                Produit existingProduct = metier.getProduitById(idProduit);
//                if (existingProduct != null) {
//                    JOptionPane.showMessageDialog(this, 
//                        "Un produit avec cet ID existe déjà!", 
//                        "Erreur", 
//                        JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                
//                int idCategorie = getIdCategorieFromName(nomCategorie);
//                
//                if (idCategorie == -1) {
//                    JOptionPane.showMessageDialog(this, "Catégorie invalide!", "Erreur", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                
//                Produit nouveauProduit = new Produit();
//                nouveauProduit.setIdProduit(idProduit);
//                nouveauProduit.setNomProduit(nomProduit);
//                nouveauProduit.setPrix(prix);
//                nouveauProduit.setQuantite(quantite);
//                
//                metier.addProduit(nouveauProduit, idCategorie);
//                
//                JOptionPane.showMessageDialog(this, 
//                    "✅ Produit ajouté avec succès!", 
//                    "Succès", 
//                    JOptionPane.INFORMATION_MESSAGE);
//                clearForm();
//                loadAllProducts();
//                
//            } catch (NumberFormatException ex) {
//                JOptionPane.showMessageDialog(this, 
//                    "Prix et quantité doivent être des nombres valides!", 
//                    "Erreur", 
//                    JOptionPane.ERROR_MESSAGE);
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, 
//                    "Erreur lors de l'ajout: " + ex.getMessage(), 
//                    "Erreur", 
//                    JOptionPane.ERROR_MESSAGE);
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    private void modifierProduit() {
//        if (jtable.getSelectedRow() == -1) {
//            JOptionPane.showMessageDialog(this, 
//                "Veuillez sélectionner un produit à modifier!", 
//                "Avertissement", 
//                JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        
//        if (validateForm()) {
//            try {
//                String idProduit = txtIdProduit.getText().trim();
//                String nomProduit = txtNomProduit.getText().trim();
//                double prix = Double.parseDouble(txtPrix.getText().trim());
//                int quantite = Integer.parseInt(txtQuantite.getText().trim());
//                String nomCategorie = (String) comboCategoriesCRUD.getSelectedItem();
//                
//                // Vérifier si une catégorie valide est sélectionnée
//                if (nomCategorie == null || nomCategorie.equals("Sélectionner une catégorie")) {
//                    JOptionPane.showMessageDialog(this, 
//                        "Veuillez sélectionner une catégorie valide!", 
//                        "Erreur", 
//                        JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                
//                int idCategorie = getIdCategorieFromName(nomCategorie);
//                
//                if (idCategorie == -1) {
//                    JOptionPane.showMessageDialog(this, "Catégorie invalide!", "Erreur", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                
//                // Récupérer la catégorie complète
//                Categorie categorie = metier.getCategorie(idCategorie);
//                
//                Produit produitModifie = new Produit();
//                produitModifie.setIdProduit(idProduit);
//                produitModifie.setNomProduit(nomProduit);
//                produitModifie.setPrix(prix);
//                produitModifie.setQuantite(quantite);
//                produitModifie.setUneCategorie(categorie);
//                
//                metier.updateProduit(produitModifie);
//                
//                JOptionPane.showMessageDialog(this, 
//                    "✅ Produit modifié avec succès!", 
//                    "Succès", 
//                    JOptionPane.INFORMATION_MESSAGE);
//                clearForm();
//                loadAllProducts();
//                
//            } catch (NumberFormatException ex) {
//                JOptionPane.showMessageDialog(this, 
//                    "Prix et quantité doivent être des nombres valides!", 
//                    "Erreur", 
//                    JOptionPane.ERROR_MESSAGE);
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, 
//                    "Erreur lors de la modification: " + ex.getMessage(), 
//                    "Erreur", 
//                    JOptionPane.ERROR_MESSAGE);
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    private void supprimerProduit() {
//        if (jtable.getSelectedRow() == -1) {
//            JOptionPane.showMessageDialog(this, 
//                "Veuillez sélectionner un produit à supprimer!", 
//                "Avertissement", 
//                JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        
//        String idProduit = txtIdProduit.getText().trim();
//        String nomProduit = txtNomProduit.getText().trim();
//        
//        int confirmation = JOptionPane.showConfirmDialog(this, 
//            "Êtes-vous sûr de vouloir supprimer le produit : " + nomProduit + " (ID: " + idProduit + ")?", 
//            "Confirmation de suppression", 
//            JOptionPane.YES_NO_OPTION,
//            JOptionPane.WARNING_MESSAGE);
//        
//        if (confirmation == JOptionPane.YES_OPTION) {
//            try {
//                metier.deleteProduit(idProduit);
//                
//                JOptionPane.showMessageDialog(this, 
//                    "✅ Produit supprimé avec succès!", 
//                    "Succès", 
//                    JOptionPane.INFORMATION_MESSAGE);
//                clearForm();
//                loadAllProducts();
//                
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, 
//                    "Erreur lors de la suppression: " + ex.getMessage(), 
//                    "Erreur", 
//                    JOptionPane.ERROR_MESSAGE);
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    private boolean validateForm() {
//        if (txtIdProduit.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, 
//                "L'ID produit est obligatoire!", 
//                "Erreur", 
//                JOptionPane.ERROR_MESSAGE);
//            txtIdProduit.requestFocus();
//            return false;
//        }
//        if (txtNomProduit.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, 
//                "Le nom du produit est obligatoire!", 
//                "Erreur", 
//                JOptionPane.ERROR_MESSAGE);
//            txtNomProduit.requestFocus();
//            return false;
//        }
//        if (txtPrix.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, 
//                "Le prix est obligatoire!", 
//                "Erreur", 
//                JOptionPane.ERROR_MESSAGE);
//            txtPrix.requestFocus();
//            return false;
//        }
//        if (txtQuantite.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, 
//                "La quantité est obligatoire!", 
//                "Erreur", 
//                JOptionPane.ERROR_MESSAGE);
//            txtQuantite.requestFocus();
//            return false;
//        }
//        
//        // Vérification sécurisée de la catégorie
//        if (comboCategoriesCRUD.getSelectedItem() == null || 
//            comboCategoriesCRUD.getSelectedItem().equals("Sélectionner une catégorie")) {
//            JOptionPane.showMessageDialog(this, 
//                "Veuillez sélectionner une catégorie!", 
//                "Erreur", 
//                JOptionPane.ERROR_MESSAGE);
//            comboCategoriesCRUD.requestFocus();
//            return false;
//        }
//        
//        try {
//            double prix = Double.parseDouble(txtPrix.getText().trim());
//            if (prix < 0) {
//                JOptionPane.showMessageDialog(this, 
//                    "Le prix ne peut pas être négatif!", 
//                    "Erreur", 
//                    JOptionPane.ERROR_MESSAGE);
//                txtPrix.requestFocus();
//                return false;
//            }
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, 
//                "Le prix doit être un nombre valide!", 
//                "Erreur", 
//                JOptionPane.ERROR_MESSAGE);
//            txtPrix.requestFocus();
//            return false;
//        }
//        
//        try {
//            int quantite = Integer.parseInt(txtQuantite.getText().trim());
//            if (quantite < 0) {
//                JOptionPane.showMessageDialog(this, 
//                    "La quantité ne peut pas être négative!", 
//                    "Erreur", 
//                    JOptionPane.ERROR_MESSAGE);
//                txtQuantite.requestFocus();
//                return false;
//            }
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, 
//                "La quantité doit être un nombre entier valide!", 
//                "Erreur", 
//                JOptionPane.ERROR_MESSAGE);
//            txtQuantite.requestFocus();
//            return false;
//        }
//        
//        return true;
//    }
//
//    private void searchByKeyword() {
//        String mc = txtMc.getText().trim();
//        try {
//            List<Produit> produits;
//            if (mc.isEmpty()) {
//                produits = metier.getAllProduits();
//            } else {
//                produits = metier.getProduitsParMotCle(mc);
//            }
//            produitModel.loadData(produits);
//            
//            if (produits.isEmpty() && !mc.isEmpty()) {
//                JOptionPane.showMessageDialog(this, 
//                    "Aucun produit trouvé avec le mot clé: " + mc,
//                    "Information", 
//                    JOptionPane.INFORMATION_MESSAGE);
//            }
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, 
//                "Erreur lors de la recherche: " + ex.getMessage(),
//                "Erreur", 
//                JOptionPane.ERROR_MESSAGE);
//            ex.printStackTrace();
//        }
//    }
//
//    private void searchByCategory() {
//        try {
//            String nomCat = (String) jcombo.getSelectedItem();
//            if (nomCat != null && !nomCat.equals("Toutes les catégories")) {
//                Connection conn = SingletonConnection.getConnection();
//                String query = "SELECT idCategorie FROM categorie WHERE nomCategorie = ?";
//                PreparedStatement ps = conn.prepareStatement(query);
//                ps.setString(1, nomCat);
//                ResultSet rs = ps.executeQuery();
//                
//                if (rs.next()) {
//                    int idCategorie = rs.getInt("idCategorie");
//                    List<Produit> produits = metier.getProduitsParIDCategorie(idCategorie);
//                    produitModel.loadData(produits);
//                    txtid.setText(String.valueOf(idCategorie));
//                    
//                    if (produits.isEmpty()) {
//                        JOptionPane.showMessageDialog(this, 
//                            "Aucun produit trouvé dans la catégorie: " + nomCat,
//                            "Information", 
//                            JOptionPane.INFORMATION_MESSAGE);
//                    }
//                }
//                
//                rs.close();
//                ps.close();
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, 
//                "Erreur lors de la recherche par catégorie: " + e.getMessage(),
//                "Erreur", 
//                JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
//    }
//
//    private void resetSearch() {
//        txtMc.setText("");
//        
//        // Réinitialisation sécurisée des combobox
//        if (jcombo.getItemCount() > 0) {
//            jcombo.setSelectedIndex(0);
//        }
//        
//        txtid.setText("");
//        loadAllProducts();
//        txtMc.requestFocus();
//    }
//
//    private void loadAllProducts() {
//        try {
//            List<Produit> produits = metier.getAllProduits();
//            produitModel.loadData(produits);
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, 
//                "Erreur lors du chargement des produits: " + e.getMessage(),
//                "Erreur", 
//                JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Au moment de fermer l'application
//     */
//    @Override
//    public void dispose() {
//        // Fermer proprement la connexion
//        SingletonConnection.closeConnection();
//        super.dispose();
//    }
//
//    public static void main(String[] args) {
//        // Configuration du look and feel
//        try {
//            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
//        } catch (Exception e) {
//            try {
//                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        
//        // Création de l'interface dans l'EDT
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new CatalogueSwing().setVisible(true);
//            }
//        });
//    }
//}
package Presentation;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import Metier.MetierCatalogueImp;
import Metier.Produit;
import Metier.ProduitModel;
import Metier.SingletonConnection;
import Metier.Categorie;

// Import iTextPDF pour la génération de rapports
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;

// Import pour la gestion mémoire des PDF
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CatalogueSwing extends JFrame {
    private JLabel jlbMc = new JLabel("Mot clé:");
    private JTextField txtMc = new JTextField(15);
    private JButton btnOk = new JButton("Rechercher");
    private JButton btnReset = new JButton("Réinitialiser");
    private JButton btnDeconnexion = new JButton("Déconnexion");
    private JButton btnGenererPDF = new JButton("Générer PDF Catalogue");
    private JButton btnPDFProduits = new JButton("PDF Liste Produits");
    private JButton btnPDFCategories = new JButton("PDF Liste Catégories");
    private JTable jtable;
    private ProduitModel produitModel;
    private MetierCatalogueImp metier = new MetierCatalogueImp();
    private JComboBox<String> jcombo = new JComboBox<>();
    private JLabel lblCategorie = new JLabel("Catégorie:");
    private JLabel lblIdCategorie = new JLabel("ID Catégorie:");
    private JTextField txtid = new JTextField(5);
    
    // Composants CRUD
    private JTextField txtIdProduit = new JTextField(10);
    private JTextField txtNomProduit = new JTextField(20);
    private JTextField txtPrix = new JTextField(10);
    private JTextField txtQuantite = new JTextField(10);
    private JComboBox<String> comboCategoriesCRUD = new JComboBox<>();
    private JButton btnAjouter = new JButton("Ajouter");
    private JButton btnModifier = new JButton("Modifier");
    private JButton btnSupprimer = new JButton("Supprimer");
    private JButton btnNouveau = new JButton("Nouveau");

    /**
     * Test de connexion au démarrage
     */
    private boolean testerConnexionInitiale() {
        try {
            if (!SingletonConnection.testConnection()) {
                JOptionPane.showMessageDialog(this,
                    "❌ Impossible de se connecter à la base de données.\n" +
                    "Veuillez vérifier que:\n" +
                    "• MySQL est démarré\n" +
                    "• La base 'catalogue1' existe\n" +
                    "• Les identifiants sont corrects\n\n" +
                    "L'application va se fermer.",
                    "Erreur de Connexion",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ Erreur critique: " + e.getMessage(),
                "Erreur de Connexion",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void remplirCombo() {
        try {
            Connection conn = SingletonConnection.getConnection();
            String query = "SELECT * FROM categorie ORDER BY nomCategorie";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            // Vider les combobox
            jcombo.removeAllItems();
            comboCategoriesCRUD.removeAllItems();
            
            // Ajouter l'option par défaut seulement s'il y a des catégories
            jcombo.addItem("Toutes les catégories");
            comboCategoriesCRUD.addItem("Sélectionner une catégorie");
            
            boolean hasCategories = false;
            while (rs.next()) {
                hasCategories = true;
                String nomCategorie = rs.getString("nomCategorie");
                jcombo.addItem(nomCategorie);
                comboCategoriesCRUD.addItem(nomCategorie);
            }
            
            rs.close();
            ps.close();
            
            // Si pas de catégories, afficher un message
            if (!hasCategories) {
                System.out.println("Aucune catégorie trouvée dans la base de données");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement des catégories: " + e.getMessage(),
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private int getIdCategorieFromName(String nomCategorie) {
        try {
            Connection conn = SingletonConnection.getConnection();
            String query = "SELECT idCategorie FROM categorie WHERE nomCategorie = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, nomCategorie);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("idCategorie");
                rs.close();
                ps.close();
                return id;
            }
            
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public CatalogueSwing() {
        // Tester la connexion avant tout
        if (!testerConnexionInitiale()) {
            System.exit(1);
        }
        
        initializeUI();
        setupLayout();
        // Remplir les combobox AVANT de setup les event listeners
        remplirCombo();
        setupEventListeners();
        loadAllProducts();
    }

    private void initializeUI() {
        setTitle("Catalogue des Produits - Gestion CRUD");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 800));
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // Panel de recherche avec déconnexion
        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Table des produits
        setupTable();
        JScrollPane scrollPane = new JScrollPane(jtable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel CRUD
        JPanel crudPanel = createCrudPanel();
        mainPanel.add(crudPanel, BorderLayout.SOUTH);
    }

    private JPanel createSearchPanel() {
    	java.awt.Font swingFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Critères de recherche"));
        searchPanel.setBackground(new Color(240, 240, 240));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ligne 1: Mot clé et boutons
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        searchPanel.add(jlbMc, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        
        txtMc.setFont(swingFont);
        searchPanel.add(txtMc, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        styleButton(btnOk, new Color(70, 130, 180));
        searchPanel.add(btnOk, gbc);

        gbc.gridx = 3; gbc.weightx = 0;
        styleButton(btnReset, new Color(220, 220, 220));
        searchPanel.add(btnReset, gbc);

        // BOUTONS PDF
        gbc.gridx = 4; gbc.weightx = 0;
        styleButton(btnPDFProduits, new Color(46, 204, 113)); // Vert
        btnPDFProduits.setToolTipText("Afficher la liste complète des produits en PDF");
        searchPanel.add(btnPDFProduits, gbc);

        gbc.gridx = 5; gbc.weightx = 0;
        styleButton(btnPDFCategories, new Color(155, 89, 182)); // Violet
        btnPDFCategories.setToolTipText("Afficher la liste complète des catégories en PDF");
        searchPanel.add(btnPDFCategories, gbc);

        gbc.gridx = 6; gbc.weightx = 0;
        styleButton(btnGenererPDF, new Color(52, 152, 219)); // Bleu
        btnGenererPDF.setToolTipText("Afficher le rapport PDF complet du catalogue");
        searchPanel.add(btnGenererPDF, gbc);

        gbc.gridx = 7; gbc.weightx = 0;
        styleButton(btnDeconnexion, new Color(231, 76, 60)); // Rouge
        btnDeconnexion.setToolTipText("Se déconnecter et retourner à l'authentification");
        searchPanel.add(btnDeconnexion, gbc);

        // Ligne 2: Catégorie
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        searchPanel.add(lblCategorie, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        jcombo.setFont(swingFont);
        searchPanel.add(jcombo, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        searchPanel.add(lblIdCategorie, gbc);

        gbc.gridx = 3; gbc.weightx = 0;
        txtid.setFont(swingFont);
        txtid.setEditable(false);
        txtid.setBackground(new Color(240, 240, 240));
        searchPanel.add(txtid, gbc);

        return searchPanel;
    }

    private JPanel createCrudPanel() {
    	java.awt.Font swingFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
        JPanel crudPanel = new JPanel(new GridBagLayout());
        crudPanel.setBorder(BorderFactory.createTitledBorder("Gestion des Produits"));
        crudPanel.setBackground(new Color(245, 245, 245));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ligne 1: Labels
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        crudPanel.add(new JLabel("ID Produit:"), gbc);

        gbc.gridx = 1;
        crudPanel.add(new JLabel("Nom Produit:"), gbc);

        gbc.gridx = 2;
        crudPanel.add(new JLabel("Prix:"), gbc);

        gbc.gridx = 3;
        crudPanel.add(new JLabel("Quantité:"), gbc);

        gbc.gridx = 4;
        crudPanel.add(new JLabel("Catégorie:"), gbc);

        // Ligne 2: Champs de saisie
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.5;
        txtIdProduit.setFont(swingFont);
        crudPanel.add(txtIdProduit, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNomProduit.setFont(swingFont);
        crudPanel.add(txtNomProduit, gbc);

        gbc.gridx = 2; gbc.weightx = 0.5;
        txtPrix.setFont(swingFont);
        crudPanel.add(txtPrix, gbc);

        gbc.gridx = 3; gbc.weightx = 0.5;
        txtQuantite.setFont(swingFont);
        crudPanel.add(txtQuantite, gbc);

        gbc.gridx = 4; gbc.weightx = 0.5;
        comboCategoriesCRUD.setFont(swingFont);
        crudPanel.add(comboCategoriesCRUD, gbc);

        // Ligne 3: Boutons
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        styleButton(btnAjouter, new Color(34, 139, 34)); // Vert
        buttonPanel.add(btnAjouter);
        
        styleButton(btnModifier, new Color(255, 165, 0)); // Orange
        buttonPanel.add(btnModifier);
        
        styleButton(btnSupprimer, new Color(220, 20, 60)); // Rouge
        buttonPanel.add(btnSupprimer);
        
        styleButton(btnNouveau, new Color(70, 130, 180)); // Bleu
        buttonPanel.add(btnNouveau);
        
        crudPanel.add(buttonPanel, gbc);

        return crudPanel;
    }

    private void styleButton(JButton button, Color backgroundColor) {
    	java.awt.Font swingFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
    	button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(swingFont);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
    }

    private void setupTable() {
    	java.awt.Font swingFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
        produitModel = new ProduitModel();
        jtable = new JTable(produitModel);
        
        jtable.setFont(swingFont);
        jtable.setRowHeight(25);
        jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtable.setAutoCreateRowSorter(true);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        jtable.setDefaultRenderer(Object.class, centerRenderer);
        
        jtable.getColumnModel().getColumn(0).setPreferredWidth(80);
        jtable.getColumnModel().getColumn(1).setPreferredWidth(200);
        jtable.getColumnModel().getColumn(2).setPreferredWidth(100);
        jtable.getColumnModel().getColumn(3).setPreferredWidth(80);
    }

    private void setupEventListeners() {
        // Recherche par mot clé
        btnOk.addActionListener(e -> searchByKeyword());

        // Réinitialisation
        btnReset.addActionListener(e -> resetSearch());

        // Déconnexion
        btnDeconnexion.addActionListener(e -> deconnecter());

        // Génération PDF - AFFICHAGE DANS FENÊTRES
        btnGenererPDF.addActionListener(e -> afficherPDFComplet());
        btnPDFProduits.addActionListener(e -> afficherPDFProduits());
        btnPDFCategories.addActionListener(e -> afficherPDFCategories());

        // Recherche par catégorie
        jcombo.addActionListener(e -> {
            if (jcombo.getSelectedIndex() == 0) {
                loadAllProducts();
                txtid.setText("");
            } else {
                searchByCategory();
            }
        });

        // Recherche avec la touche Entrée
        txtMc.addActionListener(e -> searchByKeyword());

        // Sélection dans le tableau
        jtable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jtable.getSelectedRow() != -1) {
                int selectedRow = jtable.getSelectedRow();
                int modelRow = jtable.convertRowIndexToModel(selectedRow);
                
                try {
                    // Récupérer les données directement depuis le modèle de tableau
                    String idProduit = produitModel.getValueAt(modelRow, 0).toString();
                    String nomProduit = produitModel.getValueAt(modelRow, 1).toString();
                    double prix = Double.parseDouble(produitModel.getValueAt(modelRow, 2).toString());
                    int quantite = Integer.parseInt(produitModel.getValueAt(modelRow, 3).toString());
                    
                    // Créer un objet Produit temporaire avec les données
                    Produit produit = new Produit();
                    produit.setIdProduit(idProduit);
                    produit.setNomProduit(nomProduit);
                    produit.setPrix(prix);
                    produit.setQuantite(quantite);
                    
                    // Essayer de récupérer la catégorie avec gestion d'erreur améliorée
                    try {
                        Produit produitComplet = metier.getProduitById(idProduit);
                        if (produitComplet != null && produitComplet.getUneCategorie() != null) {
                            produit.setUneCategorie(produitComplet.getUneCategorie());
                        }
                    } catch (Exception ex) {
                        System.err.println("⚠️ Impossible de récupérer la catégorie: " + ex.getMessage());
                    }
                    
                    fillFormWithProduct(produit);
                    
                } catch (Exception ex) {
                    System.err.println("❌ Erreur lors de la sélection: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this,
                        "Erreur lors de la récupération des données: " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Boutons CRUD
        btnAjouter.addActionListener(e -> ajouterProduit());
        btnModifier.addActionListener(e -> modifierProduit());
        btnSupprimer.addActionListener(e -> supprimerProduit());
        btnNouveau.addActionListener(e -> clearForm());
    }

    // =========================================================================
    // NOUVELLES FONCTIONNALITÉS POUR AFFICHAGE PDF DANS FENÊTRES
    // =========================================================================

    /**
     * Affiche le PDF complet dans une nouvelle fenêtre
     */
    private void afficherPDFComplet() {
        try {
            byte[] pdfData = genererPDFCompletEnMemoire();
            afficherPDFDansFenetre(pdfData, "Rapport Complet du Catalogue", "catalogue_complet.pdf");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "❌ Erreur lors de la génération du PDF : " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Affiche le PDF des produits dans une nouvelle fenêtre
     */
    private void afficherPDFProduits() {
        try {
            byte[] pdfData = genererPDFProduitsEnMemoire();
            afficherPDFDansFenetre(pdfData, "Liste Complète des Produits", "liste_produits.pdf");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "❌ Erreur lors de la génération du PDF : " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Affiche le PDF des catégories dans une nouvelle fenêtre
     */
    private void afficherPDFCategories() {
        try {
            byte[] pdfData = genererPDFCategoriesEnMemoire();
            afficherPDFDansFenetre(pdfData, "Liste Complète des Catégories", "liste_categories.pdf");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "❌ Erreur lors de la génération du PDF : " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Affiche un PDF dans une nouvelle fenêtre Swing avec options de téléchargement
     */
    private void afficherPDFDansFenetre(byte[] pdfData, String titre, String nomFichier) {
        JFrame pdfFrame = new JFrame(titre);
        pdfFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pdfFrame.setSize(900, 700);
        pdfFrame.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Barre d'outils avec boutons
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(Color.WHITE);
        
        JButton btnTelecharger = new JButton("💾 Télécharger PDF");
        JButton btnImprimer = new JButton("🖨️ Imprimer");
        JButton btnFermer = new JButton("❌ Fermer");
        
        // Style des boutons
        styleButtonInToolbar(btnTelecharger, new Color(46, 204, 113));
        styleButtonInToolbar(btnImprimer, new Color(52, 152, 219));
        styleButtonInToolbar(btnFermer, new Color(231, 76, 60));
        
        toolBar.add(btnTelecharger);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(btnImprimer);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(btnFermer);
        
        // Zone d'information du document
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informations du document"));
        
        JLabel infoLabel = new JLabel(
            "<html><b>Document généré le :</b> " + new java.util.Date() + 
            " | <b>Pages :</b> 1 | <b>Taille :</b> " + (pdfData.length / 1024) + " Ko</html>"
        );
        infoPanel.add(infoLabel);
        
        // Zone d'aperçu du contenu
        JTextArea contenuArea = new JTextArea();
        java.awt.Font swingFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
        contenuArea.setFont(swingFont);
        contenuArea.setEditable(false);
        contenuArea.setBackground(Color.WHITE);
        contenuArea.setLineWrap(true);
        contenuArea.setWrapStyleWord(true);
        
        // Remplir le contenu avec les données formatées
        String contenuFormate = formaterContenuPDF(pdfData, titre);
        contenuArea.setText(contenuFormate);
        
        JScrollPane scrollPane = new JScrollPane(contenuArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Aperçu du document - " + titre));
        scrollPane.setPreferredSize(new Dimension(800, 500));
        
        mainPanel.add(toolBar, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        // Listeners des boutons
        btnFermer.addActionListener(e -> pdfFrame.dispose());
        
        btnTelecharger.addActionListener(e -> {
            telechargerPDF(pdfData, nomFichier, pdfFrame);
        });
        
        btnImprimer.addActionListener(e -> {
            imprimerContenu(contenuFormate, titre, pdfFrame);
        });
        
        pdfFrame.setContentPane(mainPanel);
        pdfFrame.setVisible(true);
    }

    /**
     * Style pour les boutons de la toolbar
     */
    private void styleButtonInToolbar(JButton button, Color backgroundColor) {
    	java.awt.Font swingFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
    	button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(swingFont);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    /**
     * Formate le contenu du PDF pour l'affichage texte
     */
    private String formaterContenuPDF(byte[] pdfData, String titre) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("╔══════════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                             ").append(titre.toUpperCase()).append("                             ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════════════════════╣\n");
        sb.append("║ Généré le : ").append(new java.util.Date()).append(String.format("%" + (70 - new java.util.Date().toString().length()) + "s", "║\n"));
        sb.append("║ Taille du document : ").append(pdfData.length).append(" octets").append(String.format("%" + (55 - String.valueOf(pdfData.length).length()) + "s", "║\n"));
        sb.append("╚══════════════════════════════════════════════════════════════════════════════╝\n\n");
        
        // Contenu spécifique selon le type de document
        if (titre.contains("Produits")) {
            sb.append(formaterContenuProduits());
        } else if (titre.contains("Catégories")) {
            sb.append(formaterContenuCategories());
        } else if (titre.contains("Catalogue")) {
            sb.append(formaterContenuCatalogueComplet());
        }
        
        sb.append("\n╔══════════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                             FIN DU DOCUMENT                                ║\n");
        sb.append("╚══════════════════════════════════════════════════════════════════════════════╝\n");
        
        return sb.toString();
    }

    /**
     * Formate le contenu des produits pour l'affichage
     */
    private String formaterContenuProduits() {
        StringBuilder sb = new StringBuilder();
        List<Produit> produits = metier.getAllProduits();
        
        sb.append("LISTE DES PRODUITS (").append(produits.size()).append(" produits)\n");
        sb.append("────────────────────────────────────────────────────────────────────────────────\n\n");
        
        if (produits.isEmpty()) {
            sb.append("Aucun produit trouvé dans le catalogue.\n");
        } else {
            // En-tête du tableau
            sb.append(String.format("%-12s %-30s %-10s %-8s %-15s%n", 
                "ID Produit", "Nom Produit", "Prix (€)", "Quantité", "Catégorie"));
            sb.append("──────────── ────────────────────────────── ────────── ──────── ───────────────\n");
            
            // Données des produits
            for (Produit produit : produits) {
                String nomCategorie = produit.getUneCategorie() != null ? 
                    produit.getUneCategorie().getNomCategorie() : "Non catégorisé";
                
                sb.append(String.format("%-12s %-30s %-10.2f %-8d %-15s%n",
                    produit.getIdProduit(),
                    produit.getNomProduit().length() > 30 ? 
                        produit.getNomProduit().substring(0, 27) + "..." : produit.getNomProduit(),
                    produit.getPrix(),
                    produit.getQuantite(),
                    nomCategorie.length() > 15 ? 
                        nomCategorie.substring(0, 12) + "..." : nomCategorie));
            }
            
            // Statistiques
            sb.append("\n────────────────────────────────────────────────────────────────────────────────\n");
            double valeurTotale = produits.stream().mapToDouble(p -> p.getPrix() * p.getQuantite()).sum();
            sb.append(String.format("Valeur totale du stock : %.2f €%n", valeurTotale));
        }
        
        return sb.toString();
    }

    /**
     * Formate le contenu des catégories pour l'affichage
     */
    private String formaterContenuCategories() {
        StringBuilder sb = new StringBuilder();
        List<Categorie> categories = metier.getAllCategorie();
        
        sb.append("LISTE DES CATÉGORIES (").append(categories.size()).append(" catégories)\n");
        sb.append("────────────────────────────────────────────────────────────────────────────────\n\n");
        
        if (categories.isEmpty()) {
            sb.append("Aucune catégorie trouvée dans la base de données.\n");
        } else {
            // En-tête du tableau
            sb.append(String.format("%-4s %-30s %-20s%n", 
                "ID", "Nom Catégorie", "Nombre de Produits"));
            sb.append("──── ────────────────────────────── ────────────────────\n");
            
            // Données des catégories
            for (Categorie categorie : categories) {
                List<Produit> produits = metier.getProduitsParIDCategorie(categorie.getIdCategorie());
                sb.append(String.format("%-4d %-30s %-20d%n",
                    categorie.getIdCategorie(),
                    categorie.getNomCategorie().length() > 30 ? 
                        categorie.getNomCategorie().substring(0, 27) + "..." : categorie.getNomCategorie(),
                    produits.size()));
            }
            
            // Statistiques
            sb.append("\n────────────────────────────────────────────────────────────────────────────────\n");
            int totalProduits = categories.stream()
                .mapToInt(c -> metier.getProduitsParIDCategorie(c.getIdCategorie()).size())
                .sum();
            sb.append(String.format("Total des produits dans toutes les catégories : %d%n", totalProduits));
        }
        
        return sb.toString();
    }

    /**
     * Formate le contenu du catalogue complet pour l'affichage
     */
    private String formaterContenuCatalogueComplet() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("RAPPORT COMPLET DU CATALOGUE\n");
        sb.append("────────────────────────────────────────────────────────────────────────────────\n\n");
        
        // Section Produits
        sb.append(formaterContenuProduits()).append("\n\n");
        
        // Section Catégories
        sb.append(formaterContenuCategories());
        
        return sb.toString();
    }

    /**
     * Télécharge le PDF sur le disque
     */
    private void telechargerPDF(byte[] pdfData, String nomFichier, JFrame parentFrame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le PDF");
        fileChooser.setSelectedFile(new java.io.File(nomFichier));
        
        if (fileChooser.showSaveDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().toLowerCase().endsWith(".pdf")) {
                    selectedFile = new java.io.File(selectedFile.getAbsolutePath() + ".pdf");
                }
                
                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(selectedFile)) {
                    fos.write(pdfData);
                }
                
                JOptionPane.showMessageDialog(parentFrame,
                    "✅ PDF téléchargé avec succès!\nFichier : " + selectedFile.getAbsolutePath(),
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame,
                    "❌ Erreur lors du téléchargement : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Imprime le contenu texte
     */
    private void imprimerContenu(String contenu, String titre, JFrame parentFrame) {
        try {
            javax.swing.text.JTextComponent printableText = new javax.swing.JTextArea(contenu);
            java.awt.Font swingFont = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12);
            printableText.setFont(swingFont);
            
            java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
            job.setJobName(titre);
            //exemple  2
            if (job.printDialog()) {
                job.print();
                JOptionPane.showMessageDialog(parentFrame,
                    "✅ Document envoyé à l'imprimante",
                    "Impression",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame,
                "❌ Erreur lors de l'impression : " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // MÉTHODES DE GÉNÉRATION PDF EN MÉMOIRE
    // =========================================================================

    /**
     * Génère le PDF complet en mémoire
     */
    private byte[] genererPDFCompletEnMemoire() throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        
        // Configuration des polices
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        
        // Titre du rapport
        Paragraph title = new Paragraph("RAPPORT COMPLET DU CATALOGUE", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // Date de génération
        Paragraph date = new Paragraph("Généré le : " + new java.util.Date(), normalFont);
        date.setAlignment(Paragraph.ALIGN_RIGHT);
        date.setSpacingAfter(20);
        document.add(date);
        
        // Section 1: Liste des produits
        Paragraph sectionProduits = new Paragraph("LISTE DES PRODUITS", boldFont);
        sectionProduits.setSpacingAfter(10);
        document.add(sectionProduits);
        
        List<Produit> produits = metier.getAllProduits();
        
        if (produits.isEmpty()) {
            document.add(new Paragraph("Aucun produit trouvé dans le catalogue.", normalFont));
        } else {
            PdfPTable tableProduits = new PdfPTable(5);
            tableProduits.setWidthPercentage(100);
            tableProduits.setSpacingBefore(10);
            tableProduits.setSpacingAfter(15);
            
            String[] headers = {"ID Produit", "Nom Produit", "Catégorie", "Prix (€)", "Quantité"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(BaseColor.DARK_GRAY);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setPadding(5);
                tableProduits.addCell(cell);
            }
            
            double valeurTotaleStock = 0;
            int totalProduits = 0;
            
            for (Produit produit : produits) {
                tableProduits.addCell(new PdfPCell(new Phrase(produit.getIdProduit(), normalFont)));
                tableProduits.addCell(new PdfPCell(new Phrase(produit.getNomProduit(), normalFont)));
                
                String nomCategorie = "Non catégorisé";
                if (produit.getUneCategorie() != null) {
                    nomCategorie = produit.getUneCategorie().getNomCategorie();
                }
                tableProduits.addCell(new PdfPCell(new Phrase(nomCategorie, normalFont)));
                
                tableProduits.addCell(new PdfPCell(new Phrase(String.format("%.2f", produit.getPrix()), normalFont)));
                tableProduits.addCell(new PdfPCell(new Phrase(String.valueOf(produit.getQuantite()), normalFont)));
                
                valeurTotaleStock += produit.getPrix() * produit.getQuantite();
                totalProduits++;
            }
            
            document.add(tableProduits);
            
            // Section 2: Statistiques
            Paragraph statsTitle = new Paragraph("STATISTIQUES GLOBALES", boldFont);
            statsTitle.setSpacingBefore(20);
            statsTitle.setSpacingAfter(10);
            document.add(statsTitle);
            
            PdfPTable statsTable = new PdfPTable(2);
            statsTable.setWidthPercentage(50);
            statsTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
            
            addStatRow(statsTable, "Nombre total de produits:", String.valueOf(totalProduits), boldFont, normalFont);
            addStatRow(statsTable, "Valeur totale du stock:", String.format("%.2f €", valeurTotaleStock), boldFont, normalFont);
            
            document.add(statsTable);
        }
        
        document.close();
        return baos.toByteArray();
    }

    /**
     * Génère le PDF des produits en mémoire
     */
    private byte[] genererPDFProduitsEnMemoire() throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        
        // Configuration des polices
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLUE);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        
        // Titre
        Paragraph title = new Paragraph("LISTE COMPLÈTE DES PRODUITS", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(15);
        document.add(title);
        
        // Date
        Paragraph date = new Paragraph("Généré le : " + new java.util.Date(), normalFont);
        date.setAlignment(Paragraph.ALIGN_RIGHT);
        date.setSpacingAfter(15);
        document.add(date);
        
        List<Produit> produits = metier.getAllProduits();
        
        if (produits.isEmpty()) {
            document.add(new Paragraph("Aucun produit trouvé dans le catalogue.", normalFont));
        } else {
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            
            String[] headers = {"ID Produit", "Nom du Produit", "Prix (€)", "Quantité"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(BaseColor.DARK_GRAY);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setPadding(4);
                table.addCell(cell);
            }
            
            for (Produit produit : produits) {
                table.addCell(new PdfPCell(new Phrase(produit.getIdProduit(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(produit.getNomProduit(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.format("%.2f", produit.getPrix()), normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(produit.getQuantite()), normalFont)));
            }
            
            document.add(table);
            
            // Résumé
            Paragraph resume = new Paragraph(
                "Total des produits : " + produits.size(), 
                normalFont
            );
            resume.setSpacingBefore(10);
            document.add(resume);
        }
        
        document.close();
        return baos.toByteArray();
    }

    /**
     * Génère le PDF des catégories en mémoire
     */
    private byte[] genererPDFCategoriesEnMemoire() throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        
        // Configuration des polices
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new BaseColor(155, 89, 182));
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        
        // Titre
        Paragraph title = new Paragraph("LISTE COMPLÈTE DES CATÉGORIES", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(15);
        document.add(title);
        
        // Date
        Paragraph date = new Paragraph("Généré le : " + new java.util.Date(), normalFont);
        date.setAlignment(Paragraph.ALIGN_RIGHT);
        date.setSpacingAfter(15);
        document.add(date);
        
        List<Categorie> categories = metier.getAllCategorie();
        
        if (categories.isEmpty()) {
            document.add(new Paragraph("Aucune catégorie trouvée dans la base de données.", normalFont));
        } else {
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(80);
            table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            table.setSpacingBefore(10);
            
            String[] headers = {"ID Catégorie", "Nom de la Catégorie", "Nombre de Produits"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(new BaseColor(155, 89, 182));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setPadding(4);
                table.addCell(cell);
            }
            
            for (Categorie categorie : categories) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(categorie.getIdCategorie()), normalFont)));
                table.addCell(new PdfPCell(new Phrase(categorie.getNomCategorie(), normalFont)));
                
                List<Produit> produitsCategorie = metier.getProduitsParIDCategorie(categorie.getIdCategorie());
                table.addCell(new PdfPCell(new Phrase(String.valueOf(produitsCategorie.size()), normalFont)));
            }
            
            document.add(table);
            
            // Résumé
            Paragraph resume = new Paragraph(
                "Total des catégories : " + categories.size(), 
                boldFont
            );
            resume.setSpacingBefore(10);
            document.add(resume);
        }
        
        document.close();
        return baos.toByteArray();
    }

    /**
     * Méthode utilitaire pour ajouter une ligne de statistique au tableau PDF
     */
    private void addStatRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        labelCell.setBorder(PdfPCell.NO_BORDER);
        labelCell.setPadding(5);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(PdfPCell.NO_BORDER);
        valueCell.setPadding(5);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    // =========================================================================
    // MÉTHODES EXISTANTES (CRUD et autres fonctionnalités)
    // =========================================================================

    private void deconnecter() {
        int confirmation = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir vous déconnecter?",
            "Confirmation de déconnexion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            SingletonConnection.closeConnection();
            this.dispose();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    // new LoginFrame().setVisible(true);
                    System.out.println("✅ Déconnexion réussie");
                }
            });
        }
    }

    private void fillFormWithProduct(Produit produit) {
        txtIdProduit.setText(produit.getIdProduit());
        txtNomProduit.setText(produit.getNomProduit());
        txtPrix.setText(String.valueOf(produit.getPrix()));
        txtQuantite.setText(String.valueOf(produit.getQuantite()));
        
        if (produit.getUneCategorie() != null) {
            String nomCategorie = produit.getUneCategorie().getNomCategorie();
            setComboBoxValueSafely(comboCategoriesCRUD, nomCategorie);
        }
        
        txtIdProduit.setEditable(false);
    }

    private void setComboBoxValueSafely(JComboBox<String> comboBox, String value) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals(value)) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        if (comboBox.getItemCount() > 0) {
            comboBox.setSelectedIndex(0);
        }
    }

    private void clearForm() {
        txtIdProduit.setText("");
        txtNomProduit.setText("");
        txtPrix.setText("");
        txtQuantite.setText("");
        
        if (comboCategoriesCRUD.getItemCount() > 0) {
            comboCategoriesCRUD.setSelectedIndex(0);
        }
        
        txtIdProduit.setEditable(true);
        txtIdProduit.requestFocus();
        jtable.clearSelection();
    }

    private void ajouterProduit() {
        if (validateForm()) {
            try {
                String idProduit = txtIdProduit.getText().trim();
                String nomProduit = txtNomProduit.getText().trim();
                double prix = Double.parseDouble(txtPrix.getText().trim());
                int quantite = Integer.parseInt(txtQuantite.getText().trim());
                String nomCategorie = (String) comboCategoriesCRUD.getSelectedItem();
                
                if (nomCategorie == null || nomCategorie.equals("Sélectionner une catégorie")) {
                    JOptionPane.showMessageDialog(this, 
                        "Veuillez sélectionner une catégorie valide!", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Produit existingProduct = metier.getProduitById(idProduit);
                if (existingProduct != null) {
                    JOptionPane.showMessageDialog(this, 
                        "Un produit avec cet ID existe déjà!", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int idCategorie = getIdCategorieFromName(nomCategorie);
                if (idCategorie == -1) {
                    JOptionPane.showMessageDialog(this, "Catégorie invalide!", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Produit nouveauProduit = new Produit();
                nouveauProduit.setIdProduit(idProduit);
                nouveauProduit.setNomProduit(nomProduit);
                nouveauProduit.setPrix(prix);
                nouveauProduit.setQuantite(quantite);
                
                metier.addProduit(nouveauProduit, idCategorie);
                
                JOptionPane.showMessageDialog(this, 
                    "✅ Produit ajouté avec succès!", 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadAllProducts();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Prix et quantité doivent être des nombres valides!", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de l'ajout: " + ex.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void modifierProduit() {
        if (jtable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un produit à modifier!", 
                "Avertissement", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (validateForm()) {
            try {
                String idProduit = txtIdProduit.getText().trim();
                String nomProduit = txtNomProduit.getText().trim();
                double prix = Double.parseDouble(txtPrix.getText().trim());
                int quantite = Integer.parseInt(txtQuantite.getText().trim());
                String nomCategorie = (String) comboCategoriesCRUD.getSelectedItem();
                
                if (nomCategorie == null || nomCategorie.equals("Sélectionner une catégorie")) {
                    JOptionPane.showMessageDialog(this, 
                        "Veuillez sélectionner une catégorie valide!", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int idCategorie = getIdCategorieFromName(nomCategorie);
                if (idCategorie == -1) {
                    JOptionPane.showMessageDialog(this, "Catégorie invalide!", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Categorie categorie = metier.getCategorie(idCategorie);
                Produit produitModifie = new Produit();
                produitModifie.setIdProduit(idProduit);
                produitModifie.setNomProduit(nomProduit);
                produitModifie.setPrix(prix);
                produitModifie.setQuantite(quantite);
                produitModifie.setUneCategorie(categorie);
                
                metier.updateProduit(produitModifie);
                
                JOptionPane.showMessageDialog(this, 
                    "✅ Produit modifié avec succès!", 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadAllProducts();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Prix et quantité doivent être des nombres valides!", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de la modification: " + ex.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void supprimerProduit() {
        if (jtable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un produit à supprimer!", 
                "Avertissement", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idProduit = txtIdProduit.getText().trim();
        String nomProduit = txtNomProduit.getText().trim();
        
        int confirmation = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir supprimer le produit : " + nomProduit + " (ID: " + idProduit + ")?", 
            "Confirmation de suppression", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                metier.deleteProduit(idProduit);
                
                JOptionPane.showMessageDialog(this, 
                    "✅ Produit supprimé avec succès!", 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadAllProducts();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de la suppression: " + ex.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private boolean validateForm() {
        if (txtIdProduit.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "L'ID produit est obligatoire!", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            txtIdProduit.requestFocus();
            return false;
        }
        if (txtNomProduit.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Le nom du produit est obligatoire!", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            txtNomProduit.requestFocus();
            return false;
        }
        if (txtPrix.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Le prix est obligatoire!", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            txtPrix.requestFocus();
            return false;
        }
        if (txtQuantite.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "La quantité est obligatoire!", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            txtQuantite.requestFocus();
            return false;
        }
        
        if (comboCategoriesCRUD.getSelectedItem() == null || 
            comboCategoriesCRUD.getSelectedItem().equals("Sélectionner une catégorie")) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner une catégorie!", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            comboCategoriesCRUD.requestFocus();
            return false;
        }
        
        try {
            double prix = Double.parseDouble(txtPrix.getText().trim());
            if (prix < 0) {
                JOptionPane.showMessageDialog(this, 
                    "Le prix ne peut pas être négatif!", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                txtPrix.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Le prix doit être un nombre valide!", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            txtPrix.requestFocus();
            return false;
        }
        
        try {
            int quantite = Integer.parseInt(txtQuantite.getText().trim());
            if (quantite < 0) {
                JOptionPane.showMessageDialog(this, 
                    "La quantité ne peut pas être négative!", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                txtQuantite.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "La quantité doit être un nombre entier valide!", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            txtQuantite.requestFocus();
            return false;
        }
        
        return true;
    }

    private void searchByKeyword() {
        String mc = txtMc.getText().trim();
        try {
            List<Produit> produits;
            if (mc.isEmpty()) {
                produits = metier.getAllProduits();
            } else {
                produits = metier.getProduitsParMotCle(mc);
            }
            produitModel.loadData(produits);
            
            if (produits.isEmpty() && !mc.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Aucun produit trouvé avec le mot clé: " + mc,
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de la recherche: " + ex.getMessage(),
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void searchByCategory() {
        try {
            String nomCat = (String) jcombo.getSelectedItem();
            if (nomCat != null && !nomCat.equals("Toutes les catégories")) {
                Connection conn = SingletonConnection.getConnection();
                String query = "SELECT idCategorie FROM categorie WHERE nomCategorie = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, nomCat);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    int idCategorie = rs.getInt("idCategorie");
                    List<Produit> produits = metier.getProduitsParIDCategorie(idCategorie);
                    produitModel.loadData(produits);
                    txtid.setText(String.valueOf(idCategorie));
                    
                    if (produits.isEmpty()) {
                        JOptionPane.showMessageDialog(this, 
                            "Aucun produit trouvé dans la catégorie: " + nomCat,
                            "Information", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                
                rs.close();
                ps.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de la recherche par catégorie: " + e.getMessage(),
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void resetSearch() {
        txtMc.setText("");
        
        if (jcombo.getItemCount() > 0) {
            jcombo.setSelectedIndex(0);
        }
        
        txtid.setText("");
        loadAllProducts();
        txtMc.requestFocus();
    }

    private void loadAllProducts() {
        try {
            List<Produit> produits = metier.getAllProduits();
            produitModel.loadData(produits);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement des produits: " + e.getMessage(),
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        SingletonConnection.closeConnection();
        super.dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CatalogueSwing().setVisible(true);
            }
        });
    }
}