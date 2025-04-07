module org.exemple.spoonmate {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.exemple.spoonmate to javafx.fxml;
    exports org.exemple.spoonmate;
}