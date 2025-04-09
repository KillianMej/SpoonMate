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
        launch();
    }
}
