package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.sql.*;

public class LoginController {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:mydatabase.db";
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
                "\t`poste` TEXT NOT NULL,\n" +
                "\t`date_naissance` REAL NOT NULL,\n" +
                "FOREIGN KEY(`restau_id`) REFERENCES `Utilisateur`(`id`)\n" +
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
}