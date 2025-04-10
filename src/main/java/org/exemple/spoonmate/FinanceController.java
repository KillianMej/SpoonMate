package org.exemple.spoonmate;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

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
        recettes.add(1200.0);
        recettes.add(850.0);
        recettes.add(430.0);
        recettes.add(1200.0);
        recettes.add(850.0);
        recettes.add(430.0);
        recettes.add(1200.0);
        recettes.add(850.0);
        recettes.add(430.0);
        recettes.add(1200.0);
        recettes.add(850.0);
        recettes.add(430.0);
        recettes.add(1200.0);
        recettes.add(850.0);
        recettes.add(430.0);
        recettes.add(1200.0);
        recettes.add(850.0);
        recettes.add(430.0);
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
