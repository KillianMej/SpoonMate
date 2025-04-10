package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private Label resultLabel;

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
            Parent root = FXMLLoader.load(getClass().getResource("displayTablesModalView.fxml"));

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
    protected void displayTableSubmit() {
        System.out.println("Voici la liste des tables : ");
        resultLabel.setText("Voici la liste des tables disponibles:" );
    }
}