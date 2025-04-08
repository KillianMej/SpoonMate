package org.exemple.spoonmate;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML
    private ListView<String> servedOrdersList;

    @FXML
    private ListView<String> pendingOrdersList;

    @FXML
    private Label employeeStatsLabel;

    @FXML
    private Label menuStatsLabel;

    @FXML
    private TextField ingredientField;

    @FXML
    private ListView<String> searchResultList;

    @FXML
    private Label currentClientsTotalLabel;

    @FXML
    private Label leftClientsTotalLabel;

    private List<Order> allOrders;
    private List<Employee> employees;
    private List<Meal> menu;
    private List<Bill> bills;

    @FXML
    public void initialize() {
        setupData();
        loadOrders();
        loadStats();
        loadBills();
    }

    private void setupData() {
        allOrders = List.of(
                new Order("Alice", "12:30", true, true),
                new Order("Bob", "12:35", true, true),
                new Order("Chloe", "12:40", true, false),
                new Order("David", "12:50", false, false),
                new Order("Eve", "13:00", false, true),
                new Order("Frank", "13:05", false, true),
                new Order("Zoe", "13:10", false, false)
        );

        employees = List.of(
                new Employee("Marie", 25),
                new Employee("Jean", 31),
                new Employee("Luc", 44),
                new Employee("Nora", 47)
        );

        menu = List.of(
                new Meal("Burger", 9.99, List.of("pain", "steak", "salade")),
                new Meal("Salade César", 7.50, List.of("salade", "poulet", "croutons")),
                new Meal("Entrecôte", 19.00, List.of("boeuf", "sel", "poivre")),
                new Meal("Soupe", 5.00, List.of("carotte", "poireau", "eau"))
        );

        bills = List.of(
                new Bill(34.50, true),
                new Bill(21.90, true),
                new Bill(58.30, false),
                new Bill(15.20, false)
        );
    }

    private void loadOrders() {
        List<String> served = allOrders.stream()
                .filter(o -> o.served && o.clientPresent)
                .sorted(Comparator.comparing(o -> o.time))
                .map(o -> o.client + " à " + o.time)
                .limit(5)
                .collect(Collectors.toList());

        List<String> pending = allOrders.stream()
                .filter(o -> !o.served)
                .sorted(Comparator.comparing((Order o) -> o.client).thenComparing(o -> o.time))
                .map(o -> o.client + " - " + o.time)
                .collect(Collectors.toList());

        servedOrdersList.getItems().setAll(served);
        pendingOrdersList.getItems().setAll(pending);
    }

    private void loadStats() {
        long moins30 = employees.stream().filter(e -> e.age < 30).count();
        long entre30et45 = employees.stream().filter(e -> e.age >= 30 && e.age <= 45).count();
        long plus45 = employees.stream().filter(e -> e.age > 45).count();

        employeeStatsLabel.setText("Employés : <30 ans = " + moins30 + ", 30-45 = " + entre30et45 + ", >45 = " + plus45);

        Meal max = menu.stream().max(Comparator.comparingDouble(m -> m.price)).orElse(null);
        Meal min = menu.stream().min(Comparator.comparingDouble(m -> m.price)).orElse(null);
        double total = menu.stream().mapToDouble(m -> m.price).sum();

        menuStatsLabel.setText("Plat le + cher : " + max.name + " (" + max.price + " €), le - cher : " + min.name + " (" + min.price + " €), valeur totale : " + total + " €");
    }

    private void loadBills() {
        double current = bills.stream().filter(b -> b.clientPresent).mapToDouble(b -> b.amount).sum();
        double left = bills.stream().filter(b -> !b.clientPresent).mapToDouble(b -> b.amount).sum();

        currentClientsTotalLabel.setText(current + " €");
        leftClientsTotalLabel.setText(left + " €");
    }

    @FXML
    protected void onSearch() {
        String query = ingredientField.getText().toLowerCase();
        List<String> results = menu.stream()
                .filter(m -> m.ingredients.stream().anyMatch(i -> i.toLowerCase().contains(query)))
                .map(m -> m.name + " - " + m.price + " €")
                .collect(Collectors.toList());

        searchResultList.getItems().setAll(results);
    }

    public static class Order {
        String client;
        String time;
        boolean served;
        boolean clientPresent;

        public Order(String client, String time, boolean served, boolean clientPresent) {
            this.client = client;
            this.time = time;
            this.served = served;
            this.clientPresent = clientPresent;
        }
    }

    public static class Employee {
        String name;
        int age;

        public Employee(String name, int age) {
            this.name = name;
            this.age = age;
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
    }

    public static class Bill {
        double amount;
        boolean clientPresent;

        public Bill(double amount, boolean clientPresent) {
            this.amount = amount;
            this.clientPresent = clientPresent;
        }
    }
}
