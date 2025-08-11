package com.example.prueba;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Button btn = new Button("Haz clic");
        VBox root = new VBox(btn);
        root.setSpacing(10);

        Scene scene = new Scene(root, 300, 200);

        // Agregar CSS
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("JavaFX con CSS");
        stage.show();
        btn.getStyleClass().add("boton-principal");


    }

    public static void main(String[] args) {
        launch();
    }
}
