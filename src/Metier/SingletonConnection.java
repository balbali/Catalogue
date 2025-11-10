package Metier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SingletonConnection {
    private static Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/catalogue1";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("✅ Driver MySQL chargé avec succès");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver MySQL non trouvé");
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        // Si pas de connexion ou connexion fermée, en créer une nouvelle
        if (connection == null || connection.isClosed()) {
            createNewConnection();
        }
        
        // Vérifier si la connexion est toujours valide
        if (!isConnectionValid()) {
            System.out.println("🔄 Connexion invalide, création d'une nouvelle connexion...");
            createNewConnection();
        }
        
        return connection;
    }
    
    /**
     * Crée une nouvelle connexion
     */
    private static void createNewConnection() throws SQLException {
        // Fermer l'ancienne connexion si elle existe
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("⚠️ Erreur lors de la fermeture de l'ancienne connexion: " + e.getMessage());
            }
        }
        
        // Créer une nouvelle connexion
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(true); // ✅ Configuration correcte
            System.out.println("✅ Nouvelle connexion MySQL établie");
        } catch (SQLException e) {
            System.err.println("❌ Échec de la création de la connexion: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Vérifie si la connexion est valide en exécutant une requête simple
     */
    private static boolean isConnectionValid() {
        if (connection == null) return false;
        
        try (Statement stmt = connection.createStatement()) {
            // Test simple pour vérifier la connexion
            stmt.executeQuery("SELECT 1");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Connexion invalide détectée: " + e.getMessage());
            return false;
        }
    }
    
    // Les autres méthodes (testConnection, closeConnection, etc.) restent les mêmes
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("❌ Test de connexion échoué: " + e.getMessage());
            return false;
        }
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Connexion fermée");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la fermeture: " + e.getMessage());
        } finally {
            connection = null;
        }
    }
}