package org.exemple.spoonmate;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;


public class Database {
    static String url = "jdbc:sqlite:database.db";
    public static Integer util_id;
    public static Boolean admin = false;
    public static void main(String[] args) {
    }

    public int getRestauByEmployeId(int employeId) {
        String sql = "SELECT restau_id FROM Employe WHERE util_id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // On passe l'ID de l'employé en paramètre
            stmt.setInt(1, employeId);

            // Exécution de la requête et récupération du résultat
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("restau_id"); // Renvoie l'ID du restaurant
            } else {
                System.out.println("Employé non trouvé.");
                return -1; // Si l'employé n'est pas trouvé, on renvoie -1
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // En cas d'erreur, renvoie -1
        }
    }

    public void creationDb() {
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
            "\t`prepared` BOOLEAN NOT NULL DEFAULT 0,\n" +
            "FOREIGN KEY(`restau_id`) REFERENCES `Utilisateur`(`id`),\n" +
            "FOREIGN KEY(`table_id`) REFERENCES `Table`(`id`),\n" +
            "FOREIGN KEY(`plat_id`) REFERENCES `Plat`(`id`)\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS `Table` (\n" +
            "\t`id` integer primary key NOT NULL UNIQUE,\n" +
            "\t`restau_id` INTEGER NOT NULL,\n" +
            "\t`numero` INTEGER NOT NULL,\n" +
            "\t`taille` INTEGER NOT NULL,\n" +
            "\t`emplacement` INTEGER NOT NULL DEFAULT 1,\n" +
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

    private void doQuery(String sql) {
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement();) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void creationUtil(String nom, String email, String mdp) {
        String sql = "INSERT INTO Utilisateur(nom,email,mdp,admin)" +
                "Values('" + nom + "', '" + email + "', '" + mdp + "', 1)";
        doQuery(sql);
    }

    public Boolean tryConnectUtil(String email, String mdp) {
        String sql = "SELECT * FROM Utilisateur WHERE email = '" + email + "' AND mdp = '" + mdp + "'";

        try (Connection conn = DriverManager.getConnection(url); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                util_id = rs.getInt("id");
                admin = rs.getBoolean("admin");
                return true;
            }else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void ajouterRecette(int restau_id, String desc, double montant, String date) {
        String sql = "INSERT INTO Depenses_Recettes (restau_id, type, desc, montant, date) VALUES (" +
                restau_id + ", 1, '" + desc + "', " + montant + ", '" + date + "')";
        doQuery(sql);
    }

    public void ajouterDepense(int restau_id, String desc, double montant, String date) {
        String sql = "INSERT INTO Depenses_Recettes (restau_id, type, desc, montant, date) VALUES (" +
                restau_id + ", 0, '" + desc + "', " + montant + ", '" + date + "')";
        doQuery(sql);
    }

    public void insertPlat(int restauId, String nom, String desc, int prix, String image) {
        String sql = "INSERT INTO Plat (restau_id, nom, desc, prix, image) VALUES (" +
                restauId + ", '" + nom + "', '" + desc + "', " + prix + ", '" + image + "')";
        doQuery(sql);
    }

    public void insertTable(int restauId, int numero, int taille, int emplacement, boolean libre) {
        int libreInt = libre ? 1 : 0;
        String sql = "INSERT INTO `Table` (restau_id, numero, taille, emplacement, libre) VALUES (" +
                restauId + ", " + numero + ", " + taille + ", " + emplacement + ", " + libreInt + ")";
        doQuery(sql);
    }

    public void insertCommande(int restauId, int tableId, int platId) {
        String sql = "INSERT INTO Commande (restau_id, table_id, plat_id) VALUES (" +
                restauId + ", " + tableId + ", " + platId + ")";
        doQuery(sql);
    }

    // --------------------
    // Méthodes de lecture
    // --------------------
    public List<DashboardController.Meal> getAllMeals() {
        List<DashboardController.Meal> meals = new ArrayList<>();
        String sql = "SELECT nom, prix, desc, image FROM Plat";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("nom");
                double price = rs.getDouble("prix");
                String description = rs.getString("desc");
                List<String> ingredients = Arrays.asList(description);
                meals.add(new DashboardController.Meal(name, price, ingredients));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meals;
    }

    public List<DashboardController.RestaurantTable> getAllTables() {
        List<DashboardController.RestaurantTable> tables = new ArrayList<>();
        String sql = "SELECT numero, libre FROM `Table`";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int tableNumber = rs.getInt("numero");
                boolean isFree = rs.getInt("libre") == 1;
                tables.add(new DashboardController.RestaurantTable(tableNumber, isFree));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public List<DashboardController.Order> getAllOrders() {
        List<DashboardController.Order> orders = new ArrayList<>();
        String sql = "SELECT id, table_id, plat_id, prepared FROM Commande";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String clientName = "Client_" + id;
                String time = "??:??";
                boolean prepared = rs.getInt("prepared") == 1;

                // On passe l’ID à l’objet Order
                orders.add(new DashboardController.Order(clientName, time, prepared, id));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void updateCommandeStatus(int commandeId, boolean prepared) {
        int preparedValue = prepared ? 1 : 0;
        String sql = "UPDATE Commande SET prepared = " + preparedValue + " WHERE id = " + commandeId;
        doQuery(sql);
    }

    public void deleteCommande(int commandeId) {
        String sql = "DELETE FROM Commande WHERE id = " + commandeId;
        doQuery(sql);
    }


    public int getLastTableNumber() {
        int lastNumber = 0;
        try {
            Connection conn = DriverManager.getConnection(url);
            String query = "SELECT MAX(numero) FROM  \"table\" WHERE restau_id = '1'";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                lastNumber = rs.getInt(1);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastNumber;
    }

    public void updateTableStatus(int tableNumber, boolean isFree) {
        int freeValue = isFree ? 1 : 0;
        String sql = "UPDATE `Table` SET libre = " + freeValue + " WHERE numero = " + tableNumber;
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

    public List<String> GetEmployesPointage(){
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<String> list = new ArrayList<>();
        String sql = "SELECT c.id, c.employe_id, u.nom, c.debut, c.fin FROM Employe as e INNER JOIN Crenaux as c ON c.employe_id = e.util_id INNER JOIN Utilisateur as u ON u.id = e.util_id WHERE e.restau_id = "+ util_id;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String employe_id = rs.getString("employe_id");
                String nom = rs.getString("nom");
                String debut = rs.getString("debut");
                String fin = rs.getString("fin");
                if(Objects.equals(debut.split(" ")[0], formattedDate)){
                    list.add(id + "#:" + employe_id + "#:" + nom + "#:" + debut + "#:" + fin);
                }
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
        sql = "DELETE FROM Crenaux WHERE employe_id = " +id;
        doQuery(sql);
    }

    public void Pointer(Integer id, Integer util_id){
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String sql;
        if (id == 0){
            sql = "INSERT INTO Crenaux(employe_id, debut, fin) VALUES("+util_id+", '"+formattedDate+"', 0)";
        }else{
            sql = "UPDATE Crenaux SET fin = '" + formattedDate + "' WHERE id = " + id;
        }
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

                commandes.add(new CommandesController.Commande(id, restau, platNom, tableNumero, prepared));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    public CommandesController.Commande ajouterCommande(int restauId, int tableId,String tablenom, int platId, String platnom) {
        String sql = "INSERT INTO Commande (restau_id, table_id, plat_id, prepared) VALUES (" +
                restauId + ", " + tableId + ", " + platId + ", 0)";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // Exécuter la requête d'insertion
            int rowsAffected = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

            // Si la commande a été insérée avec succès, récupérer l'ID généré
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int commandeId = generatedKeys.getInt(1); // Récupérer l'ID généré
                    // Créer et retourner un objet Commande
                    return new CommandesController.Commande(commandeId, restauId, platnom, tablenom, false);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourner null en cas d'erreur
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

}
