package org.exemple.spoonmate;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        loadDataFromDatabase();
        updateUI();
    }

    private void loadDataFromDatabase() {
        String url = "jdbc:sqlite:database.db";
        String sqlRecettes = "SELECT montant FROM Depenses_Recettes WHERE type = 1";
        String sqlDepenses = "SELECT montant FROM Depenses_Recettes WHERE type = 0";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            ResultSet rsRecettes = stmt.executeQuery(sqlRecettes);
            while (rsRecettes.next()) {
                recettes.add(rsRecettes.getDouble("montant"));
            }

            ResultSet rsDepenses = stmt.executeQuery(sqlDepenses);
            while (rsDepenses.next()) {
                depenses.add(rsDepenses.getDouble("montant"));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
        String filePath = "finance_data.pdf";
        double totalRecettes = recettes.stream().mapToDouble(Double::doubleValue).sum();
        double totalDepenses = depenses.stream().mapToDouble(Double::doubleValue).sum();
        double benefice = totalRecettes - totalDepenses;

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("=== Données Financières ==="));
            document.add(new Paragraph(" "));
            
            // Section Recettes
            document.add(new Paragraph("Recettes :"));
            for (Double recette : recettes) {
                document.add(new Paragraph("  - " + recette + " €"));
            }
            document.add(new Paragraph("Total Recettes : " + totalRecettes + " €"));
            document.add(new Paragraph(" "));
            
            document.add(new Paragraph("Dépenses :"));
            for (Double depense : depenses) {
                document.add(new Paragraph("  - " + depense + " €"));
            }
            document.add(new Paragraph("Total Dépenses : " + totalDepenses + " €"));
            document.add(new Paragraph(" "));
            
            document.add(new Paragraph("Bénéfice Total : " + benefice + " €"));
            
            document.close();
            System.out.println("Export PDF réussi ! Fichier sauvegardé sous : " + filePath);
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }
}
