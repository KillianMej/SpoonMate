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
    private Label resultLabel;

    @FXML
    protected void openAssignTablesModal() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AssignTablesModal-view.fxml"));

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
}