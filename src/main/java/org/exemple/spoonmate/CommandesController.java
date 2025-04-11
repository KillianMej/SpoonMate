package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandesController {
    Database db = new Database();

    int utilid = db.util_id;

    int restau = db.admin ? utilid : db.getRestauByEmployeId(utilid);

    @FXML
    private FlowPane commandesBox;

    public static class Commande {
        int id;
        int restau;
        String plat;
        String table;
        boolean livree;

        public Commande(int id, int restau, String plat, String table, boolean livree) {
            this.id = id;
            this.restau = restau;
            this.plat = plat;
            this.table = table;
            this.livree = livree;
        }

        public Commande(int id, int restau, String plat, String table) {
            this(id, restau, plat, table, false);
        }
    }

    private final List<Commande> commandes = new ArrayList<>();

    @FXML
    public void initialize() {
        Database db = new Database();
        List<Commande> dbCommandes = db.getAllCommandesByRestau(restau);

        for (Commande commande : dbCommandes) {
            int id = commande.id;
            String plat = "Plat " + commande.plat;
            String table = commande.table;
            Boolean livree = commande.livree;
            commandes.add(new Commande(id, restau, plat, table, livree));
        }


        updateUI();
    }

    private void updateUI() {
        commandesBox.getChildren().clear();

        for (Commande commande : commandes) {
            String texte = "Commande - " + commande.plat + " (" + commande.table + ")";
            if (commande.livree) texte += " ✅";

            Label label = new Label(texte);
            Button livreeBtn = new Button("Marquer comme livrée");
            Button supprimerBtn = new Button("Supprimer");

            livreeBtn.setOnAction(e -> {
                commande.livree = !commande.livree;
                Database db = new Database();
                db.updateCommandeStatus(commande.id,commande.livree);
                updateUI();
            });

            supprimerBtn.setOnAction(e -> {
                Database db = new Database();
                db.deleteCommande(commande.id);
                commandes.remove(commande);
                updateUI();
            });

            HBox boutonsBox = new HBox(10, livreeBtn, supprimerBtn);
            VBox commandeItem = new VBox(5, label, boutonsBox);
            commandeItem.setPrefWidth(275);
            commandeItem.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
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

    public void ajouterCommandeDepuisPopup(Commande commande) {
        Database db = new Database();
        commandes.add(commande);
        updateUI();
    }
}
