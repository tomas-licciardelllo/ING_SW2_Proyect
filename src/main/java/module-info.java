module com.example.prueba {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.prueba to javafx.fxml;
    exports com.example.prueba;
}