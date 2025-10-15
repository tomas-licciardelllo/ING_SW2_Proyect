package clases.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class Header {

    public static Node createHeader(Stage stage) {
        VBox barra = new VBox(20);
        barra.getStyleClass().add("header");
        barra.setPadding(new Insets(20));
        barra.setAlignment(Pos.TOP_CENTER);
        barra.setPrefWidth(220);
        barra.prefHeightProperty().bind(stage.heightProperty());

        // Logo
        URL url = Header.class.getResource("/resources/img/logoJD.jpg");
        if (url != null) {
            Image logo = new Image(url.toExternalForm());
            ImageView logoIV = new ImageView(logo);
            logoIV.getStyleClass().add("img-logo");

            logoIV.setFitWidth(180);
            logoIV.setPreserveRatio(true);

            StackPane logoContainer = new StackPane(logoIV);
            logoContainer.setAlignment(Pos.TOP_LEFT);
            logoContainer.getStyleClass().add("img-cont");

            barra.getChildren().add(logoContainer);
        } else {
            System.out.println("ERROR! No se encontró la imagen del logo.");
        }

        // Botones
        Button btnClientes = new Button("Clientes");
        btnClientes.getStyleClass().add("botonHeader");
        btnClientes.setOnAction(ev -> new ClienteScreen(stage));

        Button btnPresupuestos = new Button("Presupuestos");
        btnPresupuestos.getStyleClass().add("botonHeader");

        ContextMenu presupuestoContexto = new ContextMenu();
        MenuItem generarPresu = new MenuItem("Generar");
        generarPresu.setOnAction(ev -> new PresupuestoScreen(stage));

        MenuItem mostrarPresu = new MenuItem("Mostrar");
        mostrarPresu.setOnAction(ev -> new MostrarPresupuestoScreen(stage));

        MenuItem impagosPresu = new MenuItem("Sin Pagar");
        //impagosPresu.setOnAction(ev -> {});

        generarPresu.getStyleClass().add("botonHeader");
        mostrarPresu.getStyleClass().add("botonHeader");
        impagosPresu.getStyleClass().add("botonHeader");
        presupuestoContexto.setStyle("-fx-background-color: transparent;");

        presupuestoContexto.getItems().addAll(generarPresu, mostrarPresu, impagosPresu);

        btnPresupuestos.setOnAction(e -> {
            var bounds = btnPresupuestos.localToScreen(btnPresupuestos.getBoundsInLocal());
            double x = bounds.getMaxX();
            double y = bounds.getMinY();
            presupuestoContexto.show(btnPresupuestos, x, y);
        });

        Button btnVehiculos = new Button("Vehículos");
        btnVehiculos.getStyleClass().add("botonHeader");
        btnVehiculos.setOnAction(ev -> new VehiculosScreen(stage));

        Button btnOrdenes = new Button("Ordenes");
        btnOrdenes.getStyleClass().add("botonHeader");

        ContextMenu ordenContexto = new ContextMenu();
        MenuItem mostrarOrdenes = new MenuItem("Mostrar Todas");
        mostrarOrdenes.setOnAction(e -> {
            new OrdenScreen(stage);
        });

        MenuItem pendientesOrdenes = new MenuItem("Ver Pendientes");
        pendientesOrdenes.setOnAction(e -> {
            new OrdenPendienteScreen(stage);
        });

        mostrarOrdenes.getStyleClass().add("botonHeader");
        pendientesOrdenes.getStyleClass().add("botonHeader");
        ordenContexto.setStyle("-fx-background-color: transparent;");

        ordenContexto.getItems().addAll(mostrarOrdenes, pendientesOrdenes);

        btnOrdenes.setOnAction(e -> {
            var bounds = btnOrdenes.localToScreen(btnOrdenes.getBoundsInLocal());
            double x = bounds.getMaxX();
            double y = bounds.getMinY();
            ordenContexto.show(btnOrdenes, x, y);
        });

        Button btnFacturas = new Button("Facturas");
        btnFacturas.getStyleClass().add("botonHeader");

        barra.getChildren().addAll(btnPresupuestos, btnOrdenes, btnClientes, btnVehiculos, btnFacturas);

        return barra;
    }

}
