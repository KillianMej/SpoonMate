package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailInput;
    @FXML
    private PasswordField mdpInput;
    @FXML
    private Label error;


    @FXML
    public void TryConnect() throws IOException {
        Database db = new Database();
        Boolean connecter = db.tryConnectUtil(emailInput.getText(), mdpInput.getText());
        if (connecter){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = loader.load();  // Charger le contenu de finance.fxml


            Stage stage = (Stage) emailInput.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.show();

        }else {
            error.setText("Email ou Mot de passe incorrect");
        }
    }

    public void Inscrire() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("registerView.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) emailInput.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

}