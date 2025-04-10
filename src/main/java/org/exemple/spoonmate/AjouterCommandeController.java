package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.List;

public class AjouterCommandeController {

    @FXML
    private ComboBox<String> platComboBox;

    @FXML
    private ComboBox<String> tableComboBox;

    private CommandesController parentController;

    public void setParentController(CommandesController controller) {
        this.parentController = controller;
    }

    public static class Plat {
        int id;
        String nom;
        String desc;
        int prix;
        String image;

        public Plat(int id, String nom, String desc, int prix, String image) {
            this.id = id;
            this.nom = nom;
            this.desc = desc;
            this.prix = prix;
            this.image = image;
        }

        public Plat(int id, String nom, String desc, int prix) {
            this(id, nom, desc, prix,"vide");
        }
    }

    public static class Table {
        int id;
        int numero;
        int taille;
        boolean libre;

        public Table(int id, int numero, int taille, boolean libre) {
            this.id = id;
            this.numero = numero;
            this.taille = taille;
            this.libre = libre;
        }

        public Table(int id, int numero, int taille) {
            this(id, numero, taille, false);
        }
    }

    @FXML
    public void initialize() {
        Database db = new Database();
        int restauId = 1; // Remplace ça avec le bon ID (récupéré via login/session par exemple)

        // Récupération des plats
        List<Plat> plats = db.getAllPlatByRestau(restauId);
        for (AjouterCommandeController.Plat plat : plats) {
            platComboBox.getItems().add(plat.nom);
        }

        // Récupération des tables
        List<Table> tables = db.getAllTableByRestau(restauId);
        for (Table table : tables) {
            tableComboBox.getItems().add("Table " + table.numero); // Ou juste table si toString()
        }
    }


    @FXML
    protected void onAjouter() {
        String platNom = platComboBox.getValue();  // Plat sélectionné
        String tableNom = tableComboBox.getValue();  // Table sélectionnée

        if (platNom != null && tableNom != null) {
            // Obtenir l'ID du plat
            Database db = new Database();
            List<AjouterCommandeController.Plat> plats = db.getAllPlatByRestau(1); // Utilisez le bon ID du restaurant ici
            int platId = -1;
            for (AjouterCommandeController.Plat plat : plats) {
                if (plat.nom.equals(platNom)) {
                    platId = plat.id;  // Récupérer l'ID du plat
                    break;
                }
            }

            // Obtenir l'ID de la table
            List<AjouterCommandeController.Table> tables = db.getAllTableByRestau(1); // Utilisez le bon ID du restaurant ici
            int tableId = -1;
            for (AjouterCommandeController.Table table : tables) {
                if (("Table " + table.numero).equals(tableNom)) {
                    tableId = table.id;  // Récupérer l'ID de la table
                    break;
                }
            }

            // Vérifiez que les IDs ont été trouvés
            if (platId != -1 && tableId != -1) {
                parentController.ajouterCommandeDepuisPopup(db.ajouterCommande(1, tableId,tableNom, platId, platNom));
                ((Stage) platComboBox.getScene().getWindow()).close(); // Ferme la popup
            } else {
                System.out.println("Erreur : Plat ou table non trouvée.");
            }
        }
    }
}
