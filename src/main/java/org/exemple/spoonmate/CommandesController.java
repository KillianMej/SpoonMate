package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandesController {

    @FXML
    private VBox commandesBox;

    private static class Commande {
        String plat;
        String table;
        boolean livree;

        Commande(String plat, String table) {
            this.plat = plat;
            this.table = table;
            this.livree = false;
        }
    }

    private final List<Commande> commandes = new ArrayList<>();

    @FXML
    public void initialize() {
        commandes.add(new Commande("Pizza Margherita", "Table 1"));
        commandes.add(new Commande("Burger Veggie", "Table 2"));
        commandes.add(new Commande("Salade César", "Table 3"));

        updateUI();
    }

    private void updateUI() {
        commandesBox.getChildren().clear();

        for (Commande commande : commandes) {
            String texte = "Commande - " + commande.plat + " (" + commande.table + ")";
            if (commande.livree) texte += " ✅";

            Label label = new Label(texte);
            Button livreeBtn = new Button("Marquer comme livrée");

            livreeBtn.setOnAction(e -> {
                commande.livree = true;
                updateUI();
            });

            VBox commandeItem = new VBox(5, label, livreeBtn);
            commandeItem.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-radius: 5;");
            commandesBox.getChildren().add(commandeItem);
        }
    }

    @FXML
    protected void onAjouterCommande() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajouterCommande.fxml"));
            Stage popup = new Stage();
            popup.setTitle("Ajouter une commande");
            popup.setScene(new Scene(loader.load()));

            AjouterCommandeController controller = loader.getController();
            controller.setParentController(this);

            popup.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ajouterCommandeDepuisPopup(String plat, String table) {
        commandes.add(new Commande(plat, table));
        updateUI();
    }
}
