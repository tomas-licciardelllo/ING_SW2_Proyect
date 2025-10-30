package clases.gui;

import javafx.animation.*;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import clases.control.Conexion;
import clases.model.*;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        // --- Cargar fuentes ---
        Font.loadFont(getClass().getResourceAsStream("/font/Livvic-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/font/Livvic-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/font/Livvic-Black.ttf"), 14);

        // --- Pantalla de carga ---
        Label cargando = new Label("INGRESANDO ...");
        cargando.getStyleClass().add("textocarga");

        ProgressBar barra = new ProgressBar();
        barra.setPrefWidth(200);
        barra.getStyleClass().add("barraprogreso");

        VBox carga = new VBox(15, cargando, barra);
        carga.getStyleClass().add("cajacarga");

        Scene load = new Scene(carga, 300, 200);
        load.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());

        // --- Animación texto ---
        FadeTransition transicion = new FadeTransition(Duration.seconds(1), cargando);
        transicion.setFromValue(0.1);
        transicion.setToValue(1.0);
        transicion.setCycleCount(FadeTransition.INDEFINITE);
        transicion.setAutoReverse(true);
        transicion.play();

        // --- Animación barra ---
        Timeline animabarra = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(barra.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(2.5), new KeyValue(barra.progressProperty(), 1))
        );
        animabarra.setCycleCount(1);
        animabarra.play();

        // --- Mostrar la pantalla de carga ---
        stage.setScene(load);
        stage.setTitle("ChapAPP");
        stage.show();

        // --- Después de 2.5 segundos, mostrar la pantalla principal ---
        PauseTransition pausa = new PauseTransition(Duration.seconds(2.5));
        pausa.setOnFinished(e -> {
            stage.setScene(mAppVolver(stage)); // <-- Reutilizamos el método que arma la pantalla principal
            stage.setMaximized(true);
        });
        pausa.play();
    }

    public static Scene mAppVolver(Stage stage) {
        BorderPane root = new BorderPane();

        // Colocamos el Header a la izquierda (ya incluye logo + botones)
        root.setLeft(Header.createHeader(stage));

        // Fondo o contenido principal (pantalla que se actualiza)
        StackPane contenido = new StackPane();
        contenido.getStyleClass().add("fondo");
        root.setCenter(contenido);

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
        l1.add(new auto("Toyota","Corolla",2003, "css", "2993"));
        cliente c1 = new cliente("Jorge", "1124233",l1,l4);
       // Connection conn = Conexion.getInstance().getConnection();

    }
}
