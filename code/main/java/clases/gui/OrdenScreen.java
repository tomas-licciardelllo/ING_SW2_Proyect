package clases.gui;

import clases.Manager.ordentrabajoManager;
import clases.Manager.tareaManager;
import clases.model.tarea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import clases.model.ordentrabajo;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class OrdenScreen {
    public OrdenScreen (Stage stage){
        tareaManager tareaMan = new tareaManager();
        ordentrabajoManager ordentrabajoMan = new ordentrabajoManager();
        List<ordentrabajo> listaOrdenes = ordentrabajoMan.obtenerTodas();
        ObservableList<ordentrabajo> data = FXCollections.observableArrayList(listaOrdenes);

        TableView<ordentrabajo> tablaOrdenes = new TableView<>(data);

        TableColumn<ordentrabajo, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("Estado"));
        TableColumn<ordentrabajo, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha_inicio"));
        TableColumn<ordentrabajo, String> colMarca = new TableColumn<>("Vehículo Marca");
        colMarca.setCellValueFactory(cellData -> {
            ordentrabajo orden = cellData.getValue();
            if (orden != null && orden.getPresupuesto() != null && orden.getPresupuesto().getAuto() != null) {
                return new javafx.beans.property.SimpleStringProperty(orden.getPresupuesto().getAuto().getMarca());
            }
            return new javafx.beans.property.SimpleStringProperty("Error! NO SE ENCONTRO");
        });

        TableColumn<ordentrabajo, String> colModelo = new TableColumn<>("Vehículo Modelo");
        colModelo.setCellValueFactory(cellData -> {
            ordentrabajo orden = cellData.getValue();
            if (orden != null && orden.getPresupuesto() != null && orden.getPresupuesto().getAuto() != null) {
                return new javafx.beans.property.SimpleStringProperty(orden.getPresupuesto().getAuto().getModelo());
            }
            return new javafx.beans.property.SimpleStringProperty("Error! NO SE ENCONTRO");
        });

        TableColumn<ordentrabajo, String> colPatente = new TableColumn<>("Patente");
        colPatente.setCellValueFactory(cellData -> {
            ordentrabajo orden = cellData.getValue();
            if (orden != null && orden.getPresupuesto() != null && orden.getPresupuesto().getAuto() != null) {
                return new javafx.beans.property.SimpleStringProperty(orden.getPresupuesto().getAuto().getPatente());
            }
            return new javafx.beans.property.SimpleStringProperty("Error! NO SE ENCONTRO");
        });
        TableColumn<ordentrabajo, String> colTareas = new TableColumn<>("Tareas");
        colTareas.setCellValueFactory(cellData -> {
            ordentrabajo ordenActual = cellData.getValue();
            List<tarea> listita = tareaMan.getTareasPorID(ordenActual.getID());
            if (listita == null || listita.isEmpty()) {
                return new javafx.beans.property.SimpleStringProperty("Sin tareas asignadas");
            }
            String tareas = listita.stream().map(tarea::getDescripcion).collect(Collectors.joining("\n"));
            return new javafx.beans.property.SimpleStringProperty(tareas);
        });
        tablaOrdenes.getColumns().addAll(colEstado, colFecha, colPatente, colModelo, colTareas);
        tablaOrdenes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox panelInferior = new HBox();
        panelInferior.setPadding(new Insets(10, 0, 0, 0));
        Button btnVolver = new Button("Volver");
        btnVolver.getStyleClass().add("botonNormal");
        btnVolver.setOnAction(event -> {
            stage.setScene(MainApp.mAppVolver(stage));
        });
        panelInferior.getChildren().addAll(btnVolver);

        VBox panel = new VBox(10, tablaOrdenes);
        panel.setStyle("-fx-padding: 20; -fx-background-color: lightgray;");
        VBox.setVgrow(tablaOrdenes, Priority.ALWAYS);
        panel.getChildren().addAll(panelInferior);
        BorderPane root = new BorderPane();
        root.setCenter(panel);
        root.getStyleClass().add("fondo");

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ChapAPP - Órdenes de Trabajo");
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }
}
