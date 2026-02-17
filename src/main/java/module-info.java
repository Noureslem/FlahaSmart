module com.example.flahasmarty {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;   // ✅ THIS LINE IS MANDATORY

    opens com.example.flahasmarty to javafx.fxml;
    exports com.example.flahasmarty;
}
