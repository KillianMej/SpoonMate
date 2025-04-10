package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class EmployeController {
    @FXML
    private TextField InputNom;
    @FXML
    private TextField InputEmail;
    @FXML
    private PasswordField InputMdp;
    @FXML
    private TextField InputPoste;
    @FXML
    private DatePicker DateNaissance;
    @FXML
    private ListView<Employe> listEmploye;
    @FXML
    private ListView<Pointage> listPoitage;

    @FXML
    public void initialize(){
        refreshEmployee();
        refreshPointage();
    }

    public void refreshEmployee(){
        listEmploye.getItems().clear();
        Database db = new Database();

        List<String> employee = db.GetEmployes();
        if(employee != null){
            employee.stream()
                    .forEach(employe -> {
                        String[] employeInfo = employe.split(":");
                        listEmploye.getItems().add(new Employe(Integer.parseInt(employeInfo[0]), employeInfo[1], employeInfo[2]));
                    });

        }
    }

    public void refreshPointage(){
        listPoitage.getItems().clear();
        Database db = new Database();
        List<String> employee = db.GetEmployes();
        List<String> pointages = db.GetEmployesPointage();
        if (employee != null){
            employee.stream().forEach(employe ->{
                String[] employeInfo = employe.split(":");
                Employe employeActuel = new Employe(Integer.parseInt(employeInfo[0]), employeInfo[1], employeInfo[2]);
                AtomicBoolean insidePointageTable = new AtomicBoolean(false);
                pointages.stream().forEach(pointage ->{
                    String[] pointageInfo = pointage.split("#:");
                    Pointage pointageActuel = new Pointage(Integer.parseInt(pointageInfo[0]), Integer.parseInt(pointageInfo[1]), pointageInfo[2], pointageInfo[3], pointageInfo[4]);
                    if (employeActuel.id == pointageActuel.util_id){
                        insidePointageTable.set(true);
                        listPoitage.getItems().add(new Pointage(pointageActuel.id, pointageActuel.util_id, pointageActuel.nom, pointageActuel.debut, pointageActuel.fin));
                    }
                });
                if (!insidePointageTable.get()){
                    listPoitage.getItems().add(new Pointage(0, employeActuel.id, employeActuel.nom, "0.0", "0.0"));
                }
            });
        }
    }

    public void ajouterEmployee(){
        if(!Objects.equals(InputNom.getText(), "") && !Objects.equals(InputEmail.getText(), "") && !Objects.equals(InputMdp.getText(), "") && !Objects.equals(InputPoste.getText(), "") && DateNaissance.getValue() != null){
            Database db = new Database();
            db.AjouterEmploye(InputNom.getText(), InputEmail.getText(),InputMdp.getText(), InputPoste.getText(), DateNaissance.getValue().toString());
            InputNom.setText("");
            InputEmail.setText("");
            InputMdp.setText("");
            InputPoste.setText("");
            DateNaissance.setValue(null);

            refreshEmployee();
            refreshPointage();
        }
    }

    public void deleteEmploye(){
        Employe selectedEmploye = listEmploye.getSelectionModel().getSelectedItem();
        Database db = new Database();
        db.DeleteEmploye(selectedEmploye.id);
        refreshEmployee();
        refreshPointage();
    }

    public void pointer(){
        Pointage selectedPointage = listPoitage.getSelectionModel().getSelectedItem();
        if(Objects.equals(selectedPointage.fin, "0.0")){
            Database db = new Database();
            db.Pointer(selectedPointage.id, selectedPointage.util_id);
            refreshPointage();

        }
    }




    public static class Employe{
        public int id;
        public String nom;
        public String email;

        public Employe(int id, String nom, String email) {
            this.id = id;
            this.nom = nom;
            this.email = email;
        }

        @Override
        public String toString() {
            return "nom : " + nom + " - email : " + email;
        }
    }

    public static class Pointage{
        public int id;
        public int util_id;
        public String nom;
        public String debut;
        public String fin;

        public Pointage(int id, int util_id, String nom, String debut, String fin) {
            this.id = id;
            this.util_id = util_id;
            this.nom = nom;
            this.debut = debut;
            this.fin = fin;
        }

        @Override
        public String toString() {
            return "nom : " + nom + " - debut : " + debut + " - fin : " + fin;
        }
    }
}
