package org.exemple.spoonmate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    static String url = "jdbc:sqlite:database.db";

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
	            "\t`prepared` BOOLEAN NOT NULL DEFAULT 0," +
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

    public List<CommandesController.Commande> getAllCommandesByRestau(int restau) {
        List<CommandesController.Commande> commandes = new ArrayList<>();
        String sql = "SELECT c.id, t.numero AS table_numero, p.nom AS plat_nom, c.prepared\n" +
                    "\tFROM Commande AS c\n" +
                    "\tJOIN `Table` t ON c.table_id = t.id\n" +
                    "\tJOIN Plat p ON c.plat_id = p.id\n" +
                    "\tWHERE c.restau_id = " + restau;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String tableNumero = "Table_" + rs.getInt("table_numero");
                String platNom = rs.getString("plat_nom");
                boolean prepared = rs.getBoolean("prepared");;

                commandes.add(new CommandesController.Commande(platNom, tableNumero, prepared));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    public void ajouterCommande(int restauId, int tableId, int platId) {
        String sql = "INSERT INTO Commande (restau_id, table_id, plat_id, prepared) VALUES (" +
                restauId + ", " + tableId + ", " + platId + ", 0)";
        doQuery(sql);
    }

    public List<AjouterCommandeController.Plat> getAllPlatByRestau(int restau) {
        List<AjouterCommandeController.Plat> plats = new ArrayList<>();
        String sql = "SELECT * FROM Plat WHERE restau_id = " + restau;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String desc = rs.getString("desc");
                int prix = rs.getInt("prix");
                String image = rs.getString("image");

                plats.add(new AjouterCommandeController.Plat(id, nom, desc, prix, image));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plats;
    }

    public List<AjouterCommandeController.Table> getAllTableByRestau(int restau) {
        List<AjouterCommandeController.Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM `Table` WHERE restau_id = " + restau;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int numero = rs.getInt("numero");
                int taille = rs.getInt("taille");
                boolean libre = rs.getBoolean("libre");

                tables.add(new AjouterCommandeController.Table(id, numero, taille, libre));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public void updateCommandeStatus(int commandeId, boolean prepared) {
        String sql = "UPDATE `Commande` SET prepared = " + prepared + " WHERE id = " + commandeId;
        doQuery(sql);
    }
}
