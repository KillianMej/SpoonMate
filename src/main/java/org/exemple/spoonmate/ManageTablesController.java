package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.exemple.spoonmate.Database.url;

public class ManageTablesController {
    //@FXML
    @FXML
    private TextField assignTableId;

    @FXML
    private TextField freeTableId;

    @FXML
    private TextField locationTableId;

    @FXML
    private TextField seatsAmountId;

    @FXML
    private ListView<String> freeTableListing;

    @FXML
    private Label resultLabel;

    @FXML
    private Button displayTableSubmitBtn;

    public List<Table> getAllTables() {
        List<Table> tables = new ArrayList<>();
        String sql = "SELECT restau_id, numero, taille, libre FROM `Table`";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int restauNumber = rs.getInt("restau_id");
                int tableNumber = rs.getInt("numero");
                int tableSize = rs.getInt("taille");
                boolean isFree = rs.getInt("libre") == 1;
                tables.add(new Table(restauNumber, tableNumber, tableSize, isFree));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public void testDatabaseConnection() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("✅ Connexion à la base réussie !");
            } else {
                System.out.println("❌ Connexion échouée !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur de connexion à la base !");
            e.printStackTrace();
        }
    }

    @FXML
    protected void openAssignTablesModal() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("assignTablesModalView.fxml"));

            Stage modale = new Stage();
            modale.initModality(Modality.APPLICATION_MODAL);
            modale.setTitle("Gestion des tables");
            modale.setScene(new Scene(root, 400, 300));
            modale.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void openFreeTablesModal() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("freeTablesModalView.fxml"));

            Stage modale = new Stage();
            modale.initModality(Modality.APPLICATION_MODAL);
            modale.setTitle("Gestion des tables");
            modale.setScene(new Scene(root, 400, 300));
            modale.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void openCreateTablesModal() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("createTablesModalView.fxml"));

            Stage modale = new Stage();
            modale.initModality(Modality.APPLICATION_MODAL);
            modale.setTitle("Gestion des tables");
            modale.setScene(new Scene(root, 400, 300));
            modale.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void openDisplayTablesModal() {
        try {
            System.out.println("Chargement du fichier FXML : " + "displayTablesModalView.fxml");
            Parent root = FXMLLoader.load(getClass().getResource("displayTablesModalView.fxml"));
            System.out.println("FXML chargé avec succès.");

            Stage modale = new Stage();
            modale.initModality(Modality.APPLICATION_MODAL);
            modale.setTitle("Gestion des tables");
            modale.setScene(new Scene(root, 400, 300));
            modale.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void assignTableSubmit() {
        String tableName = assignTableId.getText();
        System.out.println("Valeur du champ est " + tableName);
        resultLabel.setText("Table assignée : " + tableName + " !");
    }

    @FXML
    protected void freeTableSubmit() {
        String tableName = freeTableId.getText();
        System.out.println("Valeur du champ est " + tableName);
        resultLabel.setText("Table libérée : " + tableName + " !");
    }

    @FXML
    protected void createTableSubmit() {
        String locationName = locationTableId.getText();
        String sizeTable = seatsAmountId.getText();
        int size = Integer.parseInt(sizeTable);
        int location = Integer.parseInt(locationName);

        Database db = new Database();
        // Obtenir le dernier numéro de table et incrémenter
        int lastTableNumber = db.getLastTableNumber(); // à implémenter dans ta classe Database
        int newTableNumber = lastTableNumber + 1;

        db.insertTable(1, newTableNumber, size, location, true);
        System.out.println("Valeur du champ est " + locationName);
        resultLabel.setText("Table à créer : numéro-secteur-places " + newTableNumber + " et " + locationName + " et " + sizeTable + " places." );
    }

    @FXML
    protected void testConnectionSubmit() {
        testDatabaseConnection();
    }

    @FXML
    public void testGetAllTables() {
        List<Table> tables = getAllTables();

        if (tables.isEmpty()) {
            System.out.println("❌ Aucune table récupérée !");
        } else {
            System.out.println("✅ Tables récupérées :");
            for (Table t : tables) {
                System.out.println("→ Table #" + t.getNumero() +
                        ", Taille: " + t.getTaille() +
                        ", Libre: " + t.isLibre() +
                        ", Restau ID: " + t.getRestauId());
            }
        }
    }

    @FXML
    protected void displayTableSubmit() {
        System.out.println("Bouton cliqué !");
        System.out.println("Méthode displayTableSubmit appelée !");
        List<Table> tables = getAllTables();

        // On efface les anciennes données dans la ListView
        freeTableListing.getItems().clear();

        freeTableListing.getItems().add("Test initial manuel !");

        System.out.println("Tables récupérées : " + tables.size());

        // On ajoute chaque table libre dans la ListView
        //for (Table t : tables) {
            //if (t.isLibre()) {
                /* String item = "Table #" + t.getNumero() +
                        " | Taille: " + t.getTaille() +
                        " | Restaurant ID: " + t.getRestauId(); */

            //String item = "Table" + t.getNumero();
            /*String item = String.format("Table #%d | Taille: %d | Secteur: %d",
                    t.getNumero(), t.getTaille(), t.getRestauId());

            freeTableListing.getItems().add("Test manuel !");
            resultLabel.setText("Table ajoutée !");
            freeTableListing.getItems().add(item);
            //}
                    */
            // Affiche les tables récupérées

        for (Table t : tables) {
            String item = "Table #" + t.getNumero(); // Formater l'affichage de la table
            freeTableListing.getItems().add(item);
        }

        // Mettre à jour l'étiquette
        resultLabel.setText("Table ajoutée !");
        System.out.println("Tables ajoutées à la ListView.");

        resultLabel.setText("Tables disponibles listées !");
    }

}