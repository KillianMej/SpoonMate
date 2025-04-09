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

public class RegisterController {

    @FXML
    private TextField nameRegisterField;

    @FXML
    private TextField emailRegisterField;

    @FXML
    private PasswordField pwdRegisterField;

    @FXML
    private Label error;

    public void onRegisterButtonClick() throws IOException {
        Database db = new Database();
        db.creationUtil(nameRegisterField.getText(),emailRegisterField.getText(),pwdRegisterField.getText());
        Boolean connecter = db.tryConnectUtil(emailRegisterField.getText(), pwdRegisterField.getText());
        if (connecter){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) emailRegisterField.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.show();

        }else {
            error.setText("Erreur de creation du compte");
        }
    }

    public void seConnecter() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginView.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) emailRegisterField.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }
}
