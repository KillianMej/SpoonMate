package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class AjouterCommandeController {

    @FXML
    private ComboBox<String> platComboBox;

    @FXML
    private ComboBox<String> tableComboBox;

    private CommandesController parentController;

    public void setParentController(CommandesController controller) {
        this.parentController = controller;
    }

    @FXML
    public void initialize() {
        platComboBox.getItems().addAll(
                "Pizza Margherita", "Burger Veggie", "Salade César", "Pâtes Carbonara"
        );
        tableComboBox.getItems().addAll(
                "Table 1", "Table 2", "Table 3", "Table 4", "Table 5"
        );
    }

    @FXML
    protected void onAjouter() {
        String plat = platComboBox.getValue();
        String table = tableComboBox.getValue();

        if (plat != null && table != null) {
            parentController.ajouterCommandeDepuisPopup(plat, table);
            ((Stage) platComboBox.getScene().getWindow()).close(); // Ferme la popup
        }
    }
}
