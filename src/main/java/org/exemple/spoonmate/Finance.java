package org.exemple.spoonmate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Finance extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Finance.class.getResource("main.fxml"));
        Scene scene = new Scene(loader.load(), 900, 600);
        stage.setTitle("Application de Gestion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Database db = new Database();
        db.creationDb();
        db.creationUtil("jean", "pierre","eds");

        db.ajouterRecette(1, "Vente de plat", 1200.0, "2023-10-01");
        db.ajouterRecette(1, "Vente de boisson", 850.0, "2023-10-02");
        db.ajouterRecette(1, "Vente de dessert", 430.0, "2023-10-03");

        db.ajouterDepense(1, "Achat de matières premières", 500.0, "2023-10-01");
        db.ajouterDepense(1, "Salaire employé", 300.0, "2023-10-02");
        db.ajouterDepense(1, "Loyer", 250.0, "2023-10-03");

        launch();
    }
}
