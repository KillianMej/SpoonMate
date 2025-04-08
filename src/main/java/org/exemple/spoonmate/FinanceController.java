package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class FinanceController {

    @FXML
    private Label totalLabel;

    @FXML
    private VBox recetteBox;

    @FXML
    private VBox depenseBox;

    @FXML
    private TitledPane recettePane;

    @FXML
    private TitledPane depensePane;

    private List<Double> recettes = new ArrayList<>();
    private List<Double> depenses = new ArrayList<>();

    @FXML
    public void initialize() {
        recettes.add(1200.0);
        recettes.add(850.0);
        recettes.add(430.0);

        depenses.add(500.0);
        depenses.add(300.0);
        depenses.add(250.0);

        updateUI();
    }

    private void updateUI() {
        recetteBox.getChildren().clear();
        depenseBox.getChildren().clear();

        for (Double r : recettes) {
            recetteBox.getChildren().add(new Label(r + " €"));
        }

        for (Double d : depenses) {
            depenseBox.getChildren().add(new Label(d + " €"));
        }

        double totalRecettes = recettes.stream().mapToDouble(Double::doubleValue).sum();
        double totalDepenses = depenses.stream().mapToDouble(Double::doubleValue).sum();
        double benefice = totalRecettes - totalDepenses;

        totalLabel.setText("Bénéfice Total : " + benefice + " €");

        recettePane.setText("Recettes (" + totalRecettes + " €)");
        depensePane.setText("Dépenses (" + totalDepenses + " €)");
    }

    @FXML
    protected void onExportPDF() {
        System.out.println("Export PDF demandé");
    }
}
