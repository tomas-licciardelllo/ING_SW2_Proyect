module com.example.prueba {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    requires java.net.http;
    requires java.sql;


    opens com.example.prueba to javafx.fxml;
    exports com.example.prueba;
}