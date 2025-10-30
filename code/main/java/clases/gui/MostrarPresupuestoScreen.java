package clases.gui;

import clases.dao.AutoDAO;
import clases.dao.ClienteDAO;
import clases.dao.PresupuestoDAO;
import clases.model.auto;
import clases.model.cliente;
import clases.model.presupuesto;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MostrarPresupuestoScreen {

    public MostrarPresupuestoScreen(Stage stage) {

        // DAOs
        PresupuestoDAO presupuestoDAO = new PresupuestoDAO();


        // Traer todos los presupuestos
        List<presupuesto> listaPresupuesto = presupuestoDAO.getAll();
        ObservableList<presupuesto> data = FXCollections.observableArrayList(listaPresupuesto);

        // Filtro y orden
        FilteredList<presupuesto> filtroData = new FilteredList<>(data, p -> true);
        SortedList<presupuesto> sortedData = new SortedList<>(filtroData);

        // TableView
        TableView<presupuesto> tablaPresupuesto = new TableView<>();
        sortedData.comparatorProperty().bind(tablaPresupuesto.comparatorProperty());

        // Columnas
        TableColumn<presupuesto, Integer> nro = new TableColumn<>("Número");
        nro.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNumero()));

        TableColumn<presupuesto, LocalDate> fecha = new TableColumn<>("Fecha");
        fecha.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFecha()));

        TableColumn<presupuesto, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(cellData -> {
            cliente c = cellData.getValue().getCliente();
            return new SimpleStringProperty(c != null ? c.getNombre() : "");
        });

        TableColumn<presupuesto, String> colRepuestos = new TableColumn<>("Repuestos");
        colRepuestos.setCellValueFactory(cellData -> {
            List<clases.model.parte> listaPartes = cellData.getValue().getRepuestos();
            if (listaPartes == null || listaPartes.isEmpty()) {
                return new SimpleStringProperty("");
            }
            String repuestosStr = listaPartes.stream().map(clases.model.parte::toString).collect(Collectors.joining(" | "));
            return new SimpleStringProperty(repuestosStr);
        });

        TableColumn<presupuesto, Float> cTotal = new TableColumn<>("Cantidad a abonar");
        cTotal.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCostoTotal()));

        TableColumn<presupuesto, String> tipoTr = new TableColumn<>("Tipo de trabajo");
        tipoTr.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoTrabajo()));

        TableColumn<presupuesto, String> tipoPin = new TableColumn<>("Tipo de pintura");
        tipoPin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoPintura()));

        TableColumn<presupuesto, Integer> cDias = new TableColumn<>("Dias de chapa");
        cDias.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDiasChapa()));


        nro.prefWidthProperty().bind(
                tablaPresupuesto.widthProperty().multiply(0.14)
        );

        fecha.prefWidthProperty().bind(
                tablaPresupuesto.widthProperty().multiply(0.14)
        );

        colCliente.prefWidthProperty().bind(
                tablaPresupuesto.widthProperty().multiply(0.14)
        );

        colRepuestos.prefWidthProperty().bind(
                tablaPresupuesto.widthProperty().multiply(0.14)
        );

        cTotal.prefWidthProperty().bind(
                tablaPresupuesto.widthProperty().multiply(0.14)
        );

        tipoTr.prefWidthProperty().bind(
                tablaPresupuesto.widthProperty().multiply(0.14)
        );

        tipoPin.prefWidthProperty().bind(
                tablaPresupuesto.widthProperty().multiply(0.14)
        );

        cDias.prefWidthProperty().bind(
                tablaPresupuesto.widthProperty().multiply(0.14)
        );
        // Agregar columnas a la tabla
        tablaPresupuesto.getColumns().addAll(nro, fecha, colCliente, cTotal, tipoTr, tipoPin, cDias);

        // Set items
        tablaPresupuesto.setItems(sortedData);

        // Barra de búsqueda placeholder
        HBox barraBusqueda = new HBox(new Region());
        barraBusqueda.setAlignment(Pos.CENTER);

        HBox panelInferior = new HBox();
        panelInferior.setPadding(new Insets(10, 0, 0, 0));
        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(event -> {
            stage.setScene(MainApp.mAppVolver(stage));
        });
        panelInferior.getChildren().addAll(btnVolver);

        // Panel principal
        VBox panel = new VBox(10, tablaPresupuesto);
        panel.setStyle("-fx-padding: 20; -fx-background-color: lightgray;");
        panel.getChildren().addAll(barraBusqueda, panelInferior);

        BorderPane root = new BorderPane();
        root.setTop(barraBusqueda);
        root.setCenter(panel);

        double anchoPantalla = Screen.getPrimary().getBounds().getWidth();
        double altoPantalla = Screen.getPrimary().getBounds().getHeight();
        Scene scene = new Scene(root, anchoPantalla, altoPantalla);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ChapAPP - Presupuestos");
        stage.show();
    }
}
