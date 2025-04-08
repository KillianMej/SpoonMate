package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ManageTablesController {
    //@FXML


    @FXML
    protected void openAssignTablesModal() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AssignTablesModal-view.fxml"));

            Stage modale = new Stage();
            modale.initModality(Modality.APPLICATION_MODAL);
            modale.setTitle("Ma Grande Modale");
            modale.setScene(new Scene(root, 400, 300));
            modale.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}