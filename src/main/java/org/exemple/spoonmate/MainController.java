package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() throws IOException {
        showFinance();
    }

    @FXML
    public void showFinance() throws IOException {
        loadPage("finance.fxml");
    }

    @FXML
    public void showDashboard() throws IOException {
        loadPage("dashboard.fxml");
    }

    private void loadPage(String fxml) throws IOException {
        Node page = FXMLLoader.load(getClass().getResource(fxml));
        contentArea.getChildren().setAll(page);
    }

    @FXML
    public void showLogin() throws IOException {
        loadPage("login-view.fxml");
    }

    @FXML
    public void showRegister() throws IOException {
        loadPage("register-view.fxml");
    }
}
