module org.exemple.spoonmate {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.exemple.spoonmate to javafx.fxml;
    exports org.exemple.spoonmate;
}