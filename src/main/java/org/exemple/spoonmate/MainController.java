package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button employe;
    @FXML
    private Button dashboard;
    @FXML
    private Button fianances;
    @FXML
    private Button tables;


    @FXML
    public void initialize() throws IOException {
        Database db = new Database();
        employe.setVisible(db.admin);
        employe.setManaged(db.admin);

        fianances.setVisible(db.admin);
        fianances.setManaged(db.admin);

        if (db.admin){
            showDashboard();
        }else{
            showTables();
        }
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
        loadPage("loginView.fxml");
    }

    @FXML
    public void showRegister() throws IOException {
        loadPage("registerView.fxml");
    }

    @FXML
    public void showCommandes() throws IOException {
        loadPage("commandes.fxml");
    }
    @FXML
    public void showEmploye() throws IOException {
        loadPage("employeView.fxml");
    }
    
    @FXML
    public void showTables() throws IOException {
        loadPage("manageTablesView.fxml");
    }
}
