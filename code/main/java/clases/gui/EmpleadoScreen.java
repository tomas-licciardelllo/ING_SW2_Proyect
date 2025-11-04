package clases.gui;

import clases.Manager.empleadoManager;
import clases.model.empleado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

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
        txtBuscar.getStyleClass().add("barraBusqueda");
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
        empleados.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        empleados.getColumns().addAll(colDNI, colNombre);
        empleados.setMaxHeight(600);
        javafx.scene.layout.VBox tablita = new javafx.scene.layout.VBox(empleados);
        tablita.setPadding(new Insets(10, 10, 0, 10));
        root.setCenter(tablita);

        Button btnModificar = new Button("Modificar");
        Button btnEliminar = new Button("Eliminar");
        Button btnVolver = new Button("Volver");
        btnModificar.getStyleClass().add("botonModificar");
        btnEliminar.getStyleClass().add("botonEliminar");
        btnVolver.getStyleClass().add("botonNormal");

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        HBox botones = new  HBox(10, btnModificar, btnEliminar, espacio, btnVolver);
        VBox centerPanel = new VBox(10);
        centerPanel.setPadding(new Insets(10, 10, 10, 10));
        centerPanel.setAlignment(Pos.TOP_CENTER);
        centerPanel.getChildren().addAll(empleados, botones);

        root.setCenter(centerPanel);

        root.setBottom(null);

        btnAgregar.setOnAction(e -> {
            new empleadoFormScreen(stage, empleadoManager, null);
        });
        btnModificar.setOnAction(e -> {
            empleado seleccionado = empleados.getSelectionModel().getSelectedItem();
            if (seleccionado == null) {
                mostrarAlerta("Atención", "Debe seleccionar un empleado para modificar.", Alert.AlertType.WARNING);
                return;
            }
            new empleadoFormScreen(stage, empleadoManager, seleccionado);
        });
        btnEliminar.setOnAction(e -> {
            empleado seleccionado = empleados.getSelectionModel().getSelectedItem();
            if (seleccionado == null) {
                mostrarAlerta("Atención", "Debe seleccionar un empleado para eliminar.", Alert.AlertType.WARNING);
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Está seguro de que desea eliminar a " + seleccionado.getNombre() + "?");
            confirmacion.setContentText("DNI: " + seleccionado.getDocumento() + "\nEsta acción no se puede deshacer.");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try {
                    if (empleadoManager.eliminarEmpleado(seleccionado.getDocumento())) {
                        allEmpleados.remove(seleccionado); // Actualiza la UI
                        mostrarAlerta("Éxito", "Empleado eliminado correctamente.", Alert.AlertType.INFORMATION);
                    } else {
                        mostrarAlerta("Error", "No se pudo eliminar el empleado.", Alert.AlertType.ERROR);
                    }
                } catch (Exception ex) {
                    mostrarAlerta("Error", "No se pudo eliminar el empleado. Es posible que esté asignado a otras tareas.", Alert.AlertType.ERROR);
                }
            }
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

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
