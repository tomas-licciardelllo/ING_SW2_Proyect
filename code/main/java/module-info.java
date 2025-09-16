module taller {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.net.http;
    requires java.sql;

    opens clases.model to javafx.fxml;
    opens clases.gui to javafx.fxml;
    opens clases.control to javafx.fxml;

    exports clases.control;
    exports clases.gui;
    exports clases.model;
}
