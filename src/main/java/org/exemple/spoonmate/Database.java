package org.exemple.spoonmate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    static String url = "jdbc:sqlite:database.db";
    Integer util_id = 1;

    public static void main(String[] args) {
    }

    public void creationDb(){
        String sql = "CREATE TABLE IF NOT EXISTS `Utilisateur` (\n" +
                "\t`id` integer primary key NOT NULL UNIQUE,\n" +
                "\t`nom` TEXT NOT NULL,\n" +
                "\t`email` TEXT NOT NULL UNIQUE,\n" +
                "\t`mdp` TEXT NOT NULL,\n" +
                "\t`admin` REAL NOT NULL DEFAULT '0'\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS `Plat` (\n" +
                "\t`id` integer primary key NOT NULL UNIQUE,\n" +
                "\t`restau_id` INTEGER NOT NULL,\n" +
                "\t`nom` TEXT NOT NULL,\n" +
                "\t`desc` TEXT NOT NULL,\n" +
                "\t`prix` INTEGER NOT NULL DEFAULT '0',\n" +
                "\t`image` TEXT NOT NULL,\n" +
                "FOREIGN KEY(`restau_id`) REFERENCES `Utilisateur`(`id`)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS `Commande` (\n" +
                "\t`id` integer primary key NOT NULL UNIQUE,\n" +
                "\t`restau_id` INTEGER NOT NULL,\n" +
                "\t`table_id` INTEGER NOT NULL,\n" +
                "\t`plat_id` INTEGER NOT NULL,\n" +
                "FOREIGN KEY(`restau_id`) REFERENCES `Utilisateur`(`id`),\n" +
                "FOREIGN KEY(`table_id`) REFERENCES `Table`(`id`),\n" +
                "FOREIGN KEY(`plat_id`) REFERENCES `Plat`(`id`)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS `Table` (\n" +
                "\t`id` integer primary key NOT NULL UNIQUE,\n" +
                "\t`restau_id` INTEGER NOT NULL,\n" +
                "\t`numero` INTEGER NOT NULL,\n" +
                "\t`taille` INTEGER NOT NULL,\n" +
                "\t`libre` REAL NOT NULL,\n" +
                "FOREIGN KEY(`restau_id`) REFERENCES `Utilisateur`(`id`)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS `Employe` (\n" +
                "\t`id` integer primary key NOT NULL UNIQUE,\n" +
                "\t`restau_id` INTEGER NOT NULL,\n" +
                "\t`util_id` INTEGER NOT NULL,\n" +
                "\t`poste` TEXT NOT NULL,\n" +
                "\t`date_naissance` REAL NOT NULL,\n" +
                "FOREIGN KEY(`restau_id`) REFERENCES `Utilisateur`(`id`)\n" +
                "FOREIGN KEY(`util_id`) REFERENCES `Utilisateur`(`id`)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS `Crenaux` (\n" +
                "\t`id` integer primary key NOT NULL UNIQUE,\n" +
                "\t`employe_id` INTEGER NOT NULL,\n" +
                "\t`debut` REAL NOT NULL,\n" +
                "\t`fin` REAL NOT NULL,\n" +
                "FOREIGN KEY(`employe_id`) REFERENCES `Employe`(`id`)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS `Depenses_Recettes` (\n" +
                "\t`id` integer primary key NOT NULL UNIQUE,\n" +
                "\t`restau_id` INTEGER NOT NULL,\n" +
                "\t`type` REAL NOT NULL,\n" +
                "\t`desc` TEXT NOT NULL,\n" +
                "\t`montant` REAL NOT NULL,\n" +
                "\t`date` REAL NOT NULL,\n" +
                "FOREIGN KEY(`restau_id`) REFERENCES `Utilisateur`(`id`)\n" +
                ");";
        String[] sqlStatements = sql.split(";");
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            for (String statement : sqlStatements) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement.trim() + ";");
                }
            }
            System.out.println("Table créée !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void doQuery(String sql){
        try(Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement();){
            stmt.execute(sql);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void creationUtil(String nom, String email, String mdp){
        String sql = "INSERT INTO Utilisateur(nom,email,mdp,admin)" +
                "Values('"+nom+"', '"+email+"', '"+mdp+"', 1)";
        doQuery(sql);
    }

    public List<String> GetEmployes(){
        List<String> list = new ArrayList<>();
        String sql = "SELECT u.id, u.nom, u.email FROM Employe as e INNER JOIN Utilisateur as u ON u.id = e.util_id WHERE e.restau_id = "+ util_id;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String nom = rs.getString("nom");
                String email = rs.getString("email");
                list.add(id + ":" + nom + ":" + email);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void AjouterEmploye(String nom, String email, String mdp, String poste, String dateDeNaissance){
        String sql = "INSERT INTO Utilisateur(nom, email, mdp) VALUES('"+nom+"','"+email+"','"+mdp+"');";
        int id;

        try (Connection conn = DriverManager.getConnection(url); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                    String sqlEmplois = "INSERT INTO Employe(restau_id, util_id, poste, date_naissance) VALUES("+util_id+", "+id+",'"+poste+"','"+dateDeNaissance+"')";
                    doQuery(sqlEmplois);
                }
                rs.close();
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void DeleteEmploye(Integer id){
        String sql = "DELETE FROM Utilisateur WHERE id = " + id;
        doQuery(sql);
        sql = "DELETE FROM Employe WHERE util_id = " + id;
        doQuery(sql);
    }
}
