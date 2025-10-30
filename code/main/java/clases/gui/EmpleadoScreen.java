package clases.gui;

import clases.Manager.empleadoManager;
import clases.model.empleado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.List;

public class EmpleadoScreen {

    public EmpleadoScreen(Stage stage) {
        empleadoManager empleadoManager = new empleadoManager();
        List<empleado> lista = empleadoManager.obtenerTodos();
        ObservableList<empleado> allEmpleados =  FXCollections.observableArrayList(lista);
        FilteredList<empleado> filteredEmpleados = new FilteredList<>(allEmpleados, p -> true);
        BorderPane root = new BorderPane();
        root.getStyleClass().add("contenedores");

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar por nombre o DNI...");
        txtBuscar.getStyleClass().add("textoBusqueda");
        txtBuscar.setPrefWidth(600);

        Button btnAgregar = new Button("Agregar");
        btnAgregar.getStyleClass().add("botonNormal");

        Region topSpacerLeft = new Region();
        HBox.setHgrow(topSpacerLeft, Priority.ALWAYS);
        Region topSpacerRight = new Region();
        HBox.setHgrow(topSpacerRight, Priority.ALWAYS);
        HBox topBar = new HBox(topSpacerLeft, txtBuscar, topSpacerRight, btnAgregar);

        topBar.getStyleClass().add("barraArriba");
        root.setTop(topBar);

        TableView<empleado> empleados = new TableView<>(filteredEmpleados);
        empleados.getStyleClass().add("table-view");

        TableColumn<empleado, Long> colDNI = new TableColumn<>("DOCUMENTO");
        colDNI.setCellValueFactory(new PropertyValueFactory<>("documento"));
        TableColumn<empleado, String> colNombre = new TableColumn<>("NOMBRE");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDNI.prefWidthProperty().bind(empleados.widthProperty().multiply(0.3));
        colNombre.prefWidthProperty().bind(empleados.widthProperty().multiply(0.7));
        empleados.getColumns().addAll(colDNI, colNombre);
        empleados.setFixedCellSize(35);
        empleados.prefHeightProperty().bind(
                empleados.fixedCellSizeProperty().multiply(
                        javafx.beans.binding.Bindings.size(empleados.getItems()).add(1.01)
                )
        );
        empleados.setMaxHeight(600);
        javafx.scene.layout.VBox tablita = new javafx.scene.layout.VBox(empleados);
        tablita.setPadding(new Insets(10, 10, 0, 10));
        root.setCenter(tablita);

        Button btnModificar = new Button("Modificar");
        Button btnEliminar = new Button("Eliminar");
        Button btnVolver = new Button("Volver");
        btnModificar.getStyleClass().add("botonNormal");
        btnEliminar.getStyleClass().add("botonEliminar");
        btnVolver.getStyleClass().add("botonNormal");

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        HBox botones = new  HBox(btnModificar, btnEliminar, espacio, btnVolver);
        root.setBottom(botones);

        btnAgregar.setOnAction(e -> {

        });
        btnModificar.setOnAction(e -> {

        });
        btnEliminar.setOnAction(e -> {

        });
        btnVolver.setOnAction(e -> {
            stage.setScene(MainApp.mAppVolver(stage));
        });

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredEmpleados.setPredicate(empleado -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                if (empleado.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(empleado.getDocumento()).contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ChapAPP - Gestión de Empleados");
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }
}
