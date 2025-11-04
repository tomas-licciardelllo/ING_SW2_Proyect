package clases.gui;

import clases.dao.OrdenDAO;
import java.net.URL;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Header {
    public static Node createHeader(Stage stage) {
        VBox barra = new VBox((double)20.0F);
        barra.getStyleClass().add("header");
        barra.setPadding(new Insets((double)20.0F));
        barra.setAlignment(Pos.TOP_CENTER);
        barra.setPrefWidth((double)220.0F);
        barra.prefHeightProperty().bind(stage.heightProperty());
        URL url = Header.class.getResource("/resources/img/logoJD.jpg");
        if (url != null) {
            Image logo = new Image(url.toExternalForm());
            ImageView logoIV = new ImageView(logo);
            logoIV.getStyleClass().add("img-logo");
            logoIV.setFitWidth((double)180.0F);
            logoIV.setPreserveRatio(true);
            StackPane logoContainer = new StackPane(new Node[]{logoIV});
            logoContainer.setAlignment(Pos.TOP_LEFT);
            logoContainer.getStyleClass().add("img-cont");
            barra.getChildren().add(logoContainer);
        } else {
            System.out.println("ERROR! No se encontró la imagen del logo.");
        }

        Button btnPartes = new Button("Partes");
        btnPartes.getStyleClass().add("botonHeader");
        btnPartes.setOnAction((ev) -> new ParteScreen(stage));
        Button btnClientes = new Button("Clientes");
        btnClientes.getStyleClass().add("botonHeader");
        btnClientes.setOnAction((ev) -> new ClienteScreen(stage));
        Button btnPresupuestos = new Button("Presupuestos");
        btnPresupuestos.getStyleClass().add("botonHeader");
        ContextMenu presupuestoContexto = new ContextMenu();
        MenuItem generarPresu = new MenuItem("Generar");
        generarPresu.setOnAction((ev) -> new PresupuestoScreen(stage));
        MenuItem mostrarPresu = new MenuItem("Mostrar");
        mostrarPresu.setOnAction((ev) -> new MostrarPresupuestoScreen(stage));
        MenuItem impagosPresu = new MenuItem("Sin Pagar");
        generarPresu.getStyleClass().add("menu-item");
        mostrarPresu.getStyleClass().add("menu-item");
        impagosPresu.getStyleClass().add("menu-item");
        presupuestoContexto.getStyleClass().add("context-menu");
        presupuestoContexto.getItems().addAll(new MenuItem[]{generarPresu, mostrarPresu, impagosPresu});
        btnPresupuestos.setOnAction((e) -> {
            Bounds bounds = btnPresupuestos.localToScreen(btnPresupuestos.getBoundsInLocal());
            double x = bounds.getMaxX();
            double y = bounds.getMinY();
            presupuestoContexto.show(btnPresupuestos, x, y);
        });
        Button btnVehiculos = new Button("Vehículos");
        btnVehiculos.getStyleClass().add("botonHeader");
        btnVehiculos.setOnAction((ev) -> new VehiculosScreen(stage));
        Button btnOrdenes = new Button("Ordenes");
        btnOrdenes.getStyleClass().add("botonHeader");
        ContextMenu ordenContexto = new ContextMenu();
        MenuItem mostrarOrdenes = new MenuItem("Mostrar Todas");
        mostrarOrdenes.setOnAction((e) -> new OrdenScreen(stage));
        MenuItem pendientesOrdenes = new MenuItem("Ver Pendientes");
        pendientesOrdenes.setOnAction((e) -> {
            OrdenDAO ordenDAO = new OrdenDAO();
            if (ordenDAO.getAll().isEmpty()) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Atención!");
                alert.setHeaderText("No hay ninguna Orden de Trabajo Registrada por el momento!");
                alert.showAndWait();
            } else {
                new OrdenPendienteScreen(stage);
            }

        });
        mostrarOrdenes.getStyleClass().add("menu-item");
        pendientesOrdenes.getStyleClass().add("menu-item");
        ordenContexto.getStyleClass().add("context-menu");
        ordenContexto.getItems().addAll(new MenuItem[]{mostrarOrdenes, pendientesOrdenes});
        btnOrdenes.setOnAction((e) -> {
            Bounds bounds = btnOrdenes.localToScreen(btnOrdenes.getBoundsInLocal());
            double x = bounds.getMaxX();
            double y = bounds.getMinY();
            ordenContexto.show(btnOrdenes, x, y);
        });
        Button btnFacturas = new Button("Facturas");
        btnFacturas.getStyleClass().add("botonHeader");
        btnFacturas.setOnAction((e) -> {
            Alert alerta = new Alert(AlertType.INFORMATION);
            alerta.setTitle("FACTURACIÓN!");
            alerta.setContentText("Servicio en Mantenimiento");
        });
        Button btnEmpleados = new Button("Empleados");
        btnEmpleados.getStyleClass().add("botonHeader");
        btnEmpleados.setOnAction((e) -> new EmpleadoScreen(stage));
        barra.getChildren().addAll(new Node[]{btnPresupuestos, btnOrdenes, btnClientes, btnVehiculos, btnFacturas, btnPartes, btnEmpleados});
        return barra;
    }
}
