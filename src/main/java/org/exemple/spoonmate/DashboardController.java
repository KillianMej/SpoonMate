package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML
    private Label dishSummaryLabel;

    @FXML
    private TextField ingredientField;
    @FXML
    private ListView<String> searchResultList;

    @FXML
    private ListView<Meal> menuListView;   // Assure-toi que l'ID est le même dans le FXML
    @FXML
    private TextArea dishDetailArea;

    @FXML
    private ListView<Order> pendingOrdersList;  // Désormais stocke des Order
    @FXML
    private ListView<Order> preparedOrdersList; // Idem

    @FXML
    private ListView<String> freeTablesList;
    @FXML
    private ListView<String> occupiedTablesList;

    private List<Meal> menu;
    private List<Order> orders;
    private List<RestaurantTable> tables;

    @FXML
    public void initialize() {
        Database db = new Database();

        menu = db.getAllMeals();
        orders = db.getAllOrders();
        tables = db.getAllTables();

        loadMenuSummary();
        loadMenuList();
        loadOrders();
        loadTables();

        menuListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> showDishDetails(newVal)
        );
    }

    // --------------------
    // Section Menu (Plats)
    // --------------------
    private void loadMenuSummary() {
        int totalDishes = menu.size();
        Meal mostExpensive = menu.stream().max(Comparator.comparingDouble(m -> m.price)).orElse(null);

        String summary = "Total plats : " + totalDishes;
        if (mostExpensive != null) {
            summary += " | Plat le plus cher : " + mostExpensive.name
                    + " (" + mostExpensive.price + "€)";
        }
        dishSummaryLabel.setText(summary);
    }

    private void loadMenuList() {
        menuListView.getItems().setAll(menu);
    }

    private void showDishDetails(Meal dish) {
        if (dish == null) {
            dishDetailArea.clear();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Nom : ").append(dish.name).append("\n")
          .append("Prix : ").append(dish.price).append(" €\n")
          .append("Ingrédients : ").append(dish.ingredients).append("\n");
        dishDetailArea.setText(sb.toString());
    }

    @FXML
    protected void onSearch() {
        String query = ingredientField.getText().toLowerCase();
        List<String> results = menu.stream()
                .filter(m -> m.ingredients.stream()
                        .anyMatch(i -> i.toLowerCase().contains(query)))
                .map(m -> m.name + " - " + m.price + " €")
                .collect(Collectors.toList());
        searchResultList.getItems().setAll(results);
    }

    // ---------------
    // Section Orders
    // ---------------
    private void loadOrders() {
        // On s’appuie sur la liste orders déjà chargée
        // On sépare en attente vs préparées
        List<Order> waiting = orders.stream()
                .filter(o -> !o.prepared)
                .collect(Collectors.toList());
        pendingOrdersList.getItems().setAll(waiting);

        List<Order> done = orders.stream()
                .filter(o -> o.prepared)
                .collect(Collectors.toList());
        preparedOrdersList.getItems().setAll(done);
    }

    @FXML
    protected void onMarkPrepared() {
        // Récupérer la commande sélectionnée dans la liste "En attente"
        Order selectedOrder = pendingOrdersList.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) return;

        // Mettre à jour en base
        Database db = new Database();
        db.updateCommandeStatus(selectedOrder.id, true);

        // Mettre à jour localement l'objet
        selectedOrder.prepared = true;

        // Recharger l’affichage des commandes
        loadOrders();
    }

    // ---------------
    // Section Tables
    // ---------------
    private void loadTables() {
        List<String> free = tables.stream()
                .filter(RestaurantTable::isFree)
                .map(t -> "Table " + t.getTableNumber())
                .collect(Collectors.toList());
        freeTablesList.getItems().setAll(free);

        List<String> occupied = tables.stream()
                .filter(t -> !t.isFree())
                .map(t -> "Table " + t.getTableNumber())
                .collect(Collectors.toList());
        occupiedTablesList.getItems().setAll(occupied);
    }

    @FXML
    protected void onOccupyTable() {
        // Sélectionne une table libre (ex. "Table 3")
        String selectedFreeTable = freeTablesList.getSelectionModel().getSelectedItem();
        if (selectedFreeTable == null) return;

        int tableNumber = extractTableNumber(selectedFreeTable);

        // Mise à jour en base
        Database db = new Database();
        db.updateTableStatus(tableNumber, false);

        // Mettre à jour localement :
        tables.stream()
                .filter(t -> t.getTableNumber() == tableNumber)
                .forEach(t -> t.setFree(false));

        // Rafraîchir l’affichage
        loadTables();
    }

    @FXML
    protected void onFreeTable() {
        // Sélectionne une table occupée (ex. "Table 2")
        String selectedOccupiedTable = occupiedTablesList.getSelectionModel().getSelectedItem();
        if (selectedOccupiedTable == null) return;

        int tableNumber = extractTableNumber(selectedOccupiedTable);

        // Mise à jour en base
        Database db = new Database();
        db.updateTableStatus(tableNumber, true);

        // Mettre à jour localement :
        tables.stream()
                .filter(t -> t.getTableNumber() == tableNumber)
                .forEach(t -> t.setFree(true));

        // Rafraîchir l’affichage
        loadTables();
    }

    private int extractTableNumber(String label) {
        // "Table 3" -> 3
        return Integer.parseInt(label.replace("Table ", "").trim());
    }

    // -----------------
    // Classes internes
    // -----------------
    public static class Order {
        public int id;          // Identifiant unique de la commande
        public String clientName;
        public String time;
        public boolean prepared;

        public Order(String clientName, String time, boolean prepared, int id) {
            this.clientName = clientName;
            this.time = time;
            this.prepared = prepared;
            this.id = id;
        }
        @Override
        public String toString() {
            // Affichage dans la ListView
            return clientName + " - " + (prepared ? "Préparé" : "En attente") + " [" + time + "]";
        }
    }

    public static class Meal {
        String name;
        double price;
        List<String> ingredients;

        public Meal(String name, double price, List<String> ingredients) {
            this.name = name;
            this.price = price;
            this.ingredients = ingredients;
        }
        @Override
        public String toString() {
            return name + " (" + price + " €)";
        }
    }

    public static class RestaurantTable {
        private final int tableNumber;
        private boolean free;

        public RestaurantTable(int tableNumber, boolean free) {
            this.tableNumber = tableNumber;
            this.free = free;
        }

        public int getTableNumber() {
            return tableNumber;
        }

        public boolean isFree() {
            return free;
        }

        public void setFree(boolean free) {
            this.free = free;
        }
    }
}
