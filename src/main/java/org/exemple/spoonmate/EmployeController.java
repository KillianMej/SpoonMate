package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.Objects;

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
    public void initialize(){
        refreshEmployee();
    }

    public void refreshEmployee(){
        listEmploye.getItems().clear();
        Database db = new Database();

        List<String> employee = db.GetEmployes();
        if (employee != null){

            for (String employe : employee) {
                String[] employeInfo = employe.split(":");
                System.out.println(employeInfo[0]);
                listEmploye.getItems().add(new Employe(Integer.parseInt(employeInfo[0]), employeInfo[1], employeInfo[2]));
            }
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
        }
    }

    public void deleteEmploye(){
        Employe selectedEmploye = listEmploye.getSelectionModel().getSelectedItem();
        Database db = new Database();
        db.DeleteEmploye(selectedEmploye.id);
        refreshEmployee();
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
}
