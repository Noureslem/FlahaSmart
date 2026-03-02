module com.example.flahasmarty {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;

    // Java SQL module (built-in)
    requires java.sql;



    // Open your package to JavaFX
    opens com.example.flahasmarty to javafx.fxml;

    // Export your package
    exports com.example.flahasmarty;
}