package com.example.prueba;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Button btn = new Button("Haz clic");
        Font.loadFont(getClass().getResourceAsStream("/font/Livvic-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/font/Livvic-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/font/Livvic-Black.ttf"), 14);



        BorderPane root = new BorderPane();
        root.setTop(Header.createHeader(stage));
        root.getStyleClass().add("fondo");

        Scene scene = new Scene(root, 300, 200);

        // Agregar CSS
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("JavaFX con CSS");
        stage.show();
        stage.setMaximized(true);
        //btn.getStyleClass().add("boton-principal");



    }

    public static void main(String[] args) {
        launch();
    }
}
