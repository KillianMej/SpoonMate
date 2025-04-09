package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    //@FXML

    @FXML
    private Label registerLabel;
    @FXML
    private TextField nameRegisterField;

    @FXML
    private TextField emailRegisterField;

    @FXML
    private PasswordField pwdRegisterField;

    private void handleSubmit() {
        String name = nameRegisterField.getText();
        registerLabel.setText("Bonjour, " + name + " !");
    }

    @FXML
    protected void onRegisterButtonClick() {
        String name = nameRegisterField.getText();
        registerLabel.setText("Bonjour, " + name + " !");
    }
}
