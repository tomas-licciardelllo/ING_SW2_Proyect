package clases.gui;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import clases.control.Conexion;
import clases.model.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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
        load.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());



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


            // --- Aquí creamos el botón ---
            Button btnClientes = new Button("Clientes");
            btnClientes.setOnAction(ev -> {
                new ClienteScreen(stage); // muestra la pantalla de clientes en el mismo Stage
            });


            Button btnPresupuestos = new Button("Presupuestos");
            btnPresupuestos.setOnAction(ev->{
                new PresupuestoScreen(stage);
            });

            // Lo ponemos en el centro (puede ser un VBox si querés agregar más elementos)
            VBox centro = new VBox(10, btnClientes,btnPresupuestos);
            centro.setStyle("-fx-padding: 20;");
            root.setCenter(centro);

            Scene principal = new Scene(root, 900, 600);
            principal.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());

            stage.setScene(principal);
            stage.setMaximized(true);
        });

        pausa.setOnFinished(e -> {
            stage.setScene(mAppVolver(stage));
            stage.setMaximized(true);
        });

        pausa.play();
    }

    public static Scene mAppVolver(Stage stage) {
        BorderPane root = new BorderPane();
        root.setTop(Header.createHeader(stage));
        root.getStyleClass().add("fondo");

        // Botón Clientes
        Button btnClientes = new Button("Clientes");
        btnClientes.setOnAction(ev -> {
            new ClienteScreen(stage);
        });

        // Botón Presupuestos
        Button btnPresupuestos = new Button("Presupuestos");
        btnPresupuestos.setOnAction(ev -> {
            new PresupuestoScreen(stage);
        });

        // Centro con botones
        VBox centro = new VBox(10, btnClientes, btnPresupuestos);
        centro.setStyle("-fx-padding: 20;");
        root.setCenter(centro);

        Scene principal = new Scene(root, 900, 600);
        principal.getStylesheets().add(MainApp.class.getResource("/resources/styles.css").toExternalForm());

        return principal;
    }


    public static void main(String[] args) {
        launch();

        List<auto> l1 = new ArrayList<>();
        List<parte> l2 = new ArrayList<>();
        List<auto> l3 = new ArrayList<>();
        List<presupuesto> l4 = new ArrayList<>();
        l2.add(new parte(l3,"Manija"));
        l1.add(new auto("Toyota", l2,"Corolla",2003, "css", "2993"));
        cliente c1 = new cliente("Jorge", "1124233",l1,l4);
        Connection conn = Conexion.getConnection();

    }
}
