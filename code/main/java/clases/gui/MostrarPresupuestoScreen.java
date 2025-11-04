package clases.gui;

import clases.Manager.ordentrabajoManager;
import clases.Manager.presupuestoManager;
import clases.Manager.tareaManager;
import clases.model.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MostrarPresupuestoScreen {

    public MostrarPresupuestoScreen(Stage stage) {

        // DAO
        presupuestoManager presupuestoMan = new presupuestoManager();
        ordentrabajoManager ordenManager = new ordentrabajoManager();

        // Traer todos los presupuestos

        List<presupuesto> listaPresupuesto = presupuestoMan.obtenerTodos();
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

        TableColumn<presupuesto, Void> orden = new TableColumn<>("Orden de Trabajo");
        orden.setCellFactory(cellData -> new TableCell<>() {
            private final Button generar = new  Button();
            private final HBox boton = new HBox(5, generar);
            {
                boton.setAlignment(Pos.CENTER);
                try {
                    URL ojoIconUrl = getClass().getResource("/resources/img/ojo.png");
                    ImageView ojoIcon = new ImageView(new Image(ojoIconUrl.toExternalForm()));
                    ojoIcon.setFitHeight(20);
                    ojoIcon.setFitWidth(20);
                    generar.setGraphic(ojoIcon);
                    generar.setTooltip(new Tooltip("Ver/Generar"));
                } catch (Exception e) {
                    generar.setText("Ver/Generar");
                }

                generar.setOnAction(e -> {
                    presupuesto presupuestoActual = getTableRow().getItem();
                    if(presupuestoActual != null){
                        System.out.println(presupuestoActual.getNumero());
                        ordentrabajo orden = ordenManager.obtenerOrdenPorPresu(presupuestoActual.getNumero());
                        Stage stage = (Stage) getTableView().getScene().getWindow();
                        if(orden != null && orden.getEstado() != ordentrabajo.Estado.Guardada){
                            mostrarOrden(orden, presupuestoActual);
                        }
                        else{
                            Alert alertaOrden = new Alert(Alert.AlertType.CONFIRMATION);
                            alertaOrden.setTitle("ORDEN DE TRABAJO");
                            alertaOrden.setContentText("¿Desea generar la Orden de Trabajo?");
                            Optional<ButtonType> resultado = alertaOrden.showAndWait();
                            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                                if(orden.getPresupuesto() == null){
                                    orden.setPresupuesto(presupuestoActual);
                                }
                                GenOrdenScreen genOrdenScreen = new GenOrdenScreen(stage, orden, presupuestoActual.getIdPresupuesto());
                            }
                        }
                    }
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : boton);
            }
        });

        // Agregar columnas a la tabla
        tablaPresupuesto.getColumns().addAll(nro, fecha, colCliente, cTotal, tipoTr, tipoPin, orden);
        tablaPresupuesto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set items
        tablaPresupuesto.setItems(sortedData);

        // Barra de búsqueda placeholder
        HBox barraBusqueda = new HBox(new Region());
        barraBusqueda.setAlignment(Pos.CENTER);

        HBox panelInferior = new HBox();
        panelInferior.setPadding(new Insets(10, 0, 0, 0));
        Button btnVolver = new Button("Volver");
        btnVolver.getStyleClass().add("botonNormal");
        btnVolver.setOnAction(event -> {
            stage.setScene(MainApp.mAppVolver(stage));
        });
        panelInferior.getChildren().addAll(btnVolver);

        // Panel principal
        VBox panel = new VBox(10, tablaPresupuesto);
        panel.setStyle("-fx-padding: 20; -fx-background-color: lightgray;");
        VBox.setVgrow(tablaPresupuesto, Priority.ALWAYS);
        panel.getChildren().addAll(panelInferior);

        BorderPane root = new BorderPane();
        root.setTop(barraBusqueda);
        root.setCenter(panel);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ChapAPP - Listado de Presupuesto");
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }

    public void mostrarOrden(ordentrabajo orden, presupuesto presu) {
        tareaManager tareaManager = new tareaManager();
        List<tarea> listita = tareaManager.getTareasPorID(orden.getID());

        Dialog<Void> ventana = new Dialog<>();
        ventana.setTitle("Detalle de la Orden de Trabajo");
        ventana.setHeaderText("Información de la Orden N°: " + presu.getNumero());
        ventana.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("Cliente:"), 0, 0);
        grid.add(new Label(presu.getCliente().getNombre()), 1, 0);
        grid.add(new Label("Marca del Vehículo:"), 0, 1);
        grid.add(new Label(presu.getAuto().getMarca()), 1, 1);
        grid.add(new Label("Modelo del Vehículo:"), 0, 2);
        grid.add(new Label(presu.getAuto().getModelo()), 1, 2);
        grid.add(new Label("Patente:"), 0, 3);
        grid.add(new Label(presu.getAuto().getPatente()), 1, 3);
        grid.add(new Label("Fecha Ingreso:"), 0, 4);
        grid.add(new Label(orden.getFecha_inicio().toString()), 1, 4);
        grid.add(new Label("Tareas:"), 0, 5);
        grid.add(new Label(listita.toString()), 1, 5);
        ventana.getDialogPane().setContent(grid);
        ventana.showAndWait();
    }

}