package main.java.clases.gui;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Font.loadFont(getClass().getResourceAsStream("/font/Livvic-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/font/Livvic-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/font/Livvic-Black.ttf"), 14);

        //Un poco de carga, Chiche y Lirico, ustedes opinaran
        Label cargando = new Label("INGRESANDO ...");

        ProgressBar barra = new ProgressBar();
        barra.setPrefWidth(200);
        barra.getStyleClass().add("barraprogreso");

        cargando.getStyleClass().add("textocarga");
        VBox carga = new VBox(15, cargando, barra);
        carga.getStyleClass().add("cajacarga");

        Scene load = new Scene(carga,300,200);
        load.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());



        FadeTransition transicion = new FadeTransition(Duration.seconds(2), cargando);
        transicion.setFromValue(0.2);
        transicion.setToValue(1.0);
        transicion.setCycleCount(FadeTransition.INDEFINITE);
        transicion.setAutoReverse(true);
        transicion.play();

        Timeline animabarra = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(barra.progressProperty(), 0)),             //Arranca en 0
                new KeyFrame(Duration.seconds(2.5), new KeyValue(barra.progressProperty(), 1))   //Dura 2.5 seg o lo que quieran
        );
        animabarra.setCycleCount(1);
        animabarra.play();

        stage.setScene(load);
        stage.setTitle("TALLER JDSposseti");
        stage.show();


        PauseTransition pausa = new PauseTransition(Duration.seconds(2.5));
        pausa.setOnFinished(e -> {
            BorderPane root = new BorderPane();
            root.setTop(Header.createHeader(stage));
            root.getStyleClass().add("fondo");

            Scene principal = new Scene(root, 900, 600);
            principal.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            stage.setScene(principal);
            stage.setMaximized(true);
        });
        pausa.play();
    }

    public static void main(String[] args) {

        launch();
        Conexion conn = new Conexion();
        cliente PEPE = new cliente("PEPE3","PEPEFON2","PEPESEGUROS");
        String SQL = PEPE.insCliente();
        conn.executeSQL(SQL);

    }
}
