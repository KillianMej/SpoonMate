package org.exemple.spoonmate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Finance extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Finance.class.getResource("main.fxml"));
        Scene scene = new Scene(loader.load(), 1280, 800);
        stage.setTitle("Application de Gestion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Database db = new Database();
        db.creationDb();
        db.creationUtil("jean", "pierre","eds");

        db.insertPlat(1, "Burger", "Steak, pain, salade", 10, "burger.png");
        db.insertPlat(1, "Pizza", "Tomate, fromage, jambon", 12, "pizza.png");
        db.insertPlat(1, "Salade César", "Salade, poulet, sauce", 9, "cesar.png");
        db.insertPlat(1, "Soupe du Jour", "Carottes, poireau, pommes de terre", 5, "soupe.png");

        db.insertTable(1, 1, 4, true);
        db.insertTable(1, 2, 2, false);
        db.insertTable(1, 3, 6, true);


        db.insertCommande(1, 1, 1);
        db.insertCommande(1, 1, 2);
        db.insertCommande(1, 2, 3);

        db.ajouterRecette(1, "Vente de plat", 1200.0, "2023-10-01");
        db.ajouterRecette(1, "Vente de boisson", 850.0, "2023-10-02");
        db.ajouterRecette(1, "Vente de dessert", 430.0, "2023-10-03");

        db.ajouterDepense(1, "Achat de matières premières", 500.0, "2023-10-01");
        db.ajouterDepense(1, "Salaire employé", 300.0, "2023-10-02");
        db.ajouterDepense(1, "Loyer", 250.0, "2023-10-03");
        launch();
    }
}
