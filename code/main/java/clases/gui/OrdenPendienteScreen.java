package clases.gui;

import clases.Manager.ordentrabajoManager;
import clases.Manager.tareaManager;
import clases.control.generarPDF;
import clases.model.auto;
import clases.model.tarea;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import clases.dao.OrdenDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import clases.model.ordentrabajo;
import clases.model.presupuesto;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OrdenPendienteScreen {
    public OrdenPendienteScreen(Stage stage) {
        ordentrabajoManager ordenMan =  new ordentrabajoManager();
        tareaManager tareaMan = new tareaManager();
        List<ordentrabajo> lista = ordenMan.obtenerPendientes();
        ObservableList<ordentrabajo> data = FXCollections.observableArrayList(lista);
        FilteredList<ordentrabajo> filtroData = new FilteredList<>(data, p->true);

        VBox principal = new VBox(10);
        principal.setPadding(new Insets(15));
        Label lblTitulo = new Label("Listado de Ordenes de Trabajo Pendientes");
        lblTitulo.setStyle("-fx-font-size: 25; -fx-font-weight: bold");

        HBox panelSup =  new HBox(10);
        panelSup.setAlignment(Pos.CENTER);
        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar por patente, cliente, Número de Orden...");
        txtBuscar.getStyleClass().add("barraBusqueda");
        HBox.setHgrow(txtBuscar, Priority.ALWAYS);

        panelSup.getChildren().addAll(txtBuscar);

        TableView<ordentrabajo> tabla = new TableView<>();
        tabla.getStyleClass().add("table-view");
        SortedList<ordentrabajo> sortedData = new SortedList<>(filtroData);
        sortedData.comparatorProperty().bind(tabla.comparatorProperty());
        tabla.setItems(sortedData);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ordentrabajo, Integer> colNumero = new TableColumn<>("Número de Orden");
        colNumero.setCellValueFactory(new PropertyValueFactory<>("Id"));
        TableColumn<ordentrabajo, Date> colFecha = new TableColumn<>("Fecha Ingreso");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));
        TableColumn<ordentrabajo, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(new PropertyValueFactory<>("clienteNombre"));
        TableColumn<ordentrabajo, String> colMarca = new TableColumn<>("Vehículo Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("vehiculoMarca"));
        TableColumn<ordentrabajo, String> colModelo = new TableColumn<>("Vehículo Modelo");
        colModelo.setCellValueFactory(new PropertyValueFactory<>("vehiculoModelo"));
        TableColumn<ordentrabajo, String> colPatente = new TableColumn<>("Patente");
        colPatente.setCellValueFactory(new PropertyValueFactory<>("vehiculoPat"));
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
        TableColumn<ordentrabajo, Void> colAcciones = new TableColumn<>("Acciones");

        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnPdf = new Button();
            private final Button btnVer = new Button();
            private final HBox pane = new HBox(5, btnVer, btnPdf);
            {
                pane.setAlignment(Pos.CENTER);
                try {
                    URL pdfURL = getClass().getResource("/resources/img/PDF_file_icon.png");
                    ImageView pdfLogo = new ImageView(new Image(pdfURL.toExternalForm()));
                    pdfLogo.setFitHeight(20);
                    pdfLogo.setFitWidth(20);
                    btnPdf.setGraphic(pdfLogo);
                    btnPdf.setTooltip(new Tooltip("Generar PDF"));
                } catch (Exception e) {
                    btnPdf.setText("PDF/Desarrollo");
                }

                try {
                    URL ojoIconUrl = getClass().getResource("/resources/img/ojo.png");
                    ImageView ojoIcon = new ImageView(new Image(ojoIconUrl.toExternalForm()));
                    ojoIcon.setFitHeight(20);
                    ojoIcon.setFitWidth(20);
                    btnVer.setGraphic(ojoIcon);
                    btnVer.setTooltip(new Tooltip("Ver Orden"));
                } catch (Exception e) {
                    btnVer.setText("Ver");
                }


                btnPdf.setOnAction(event -> {
                    ordentrabajo orden = getTableView().getItems().get(getIndex());
                    ordentrabajoManager ordenMan = new ordentrabajoManager();
                    generarPDF nuevo = new generarPDF();
                    nuevo.generarpdf(orden);
                    //1 = Desarrollo
                    if (ordenMan.actualizarOrden(orden, 1)) {
                        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                        alerta.setTitle("ÉXITO");
                        alerta.setContentText("La orden de trabajo fue guardada y puesta en desarrollo de manera correcta!");
                        alerta.showAndWait();
                        stage.setScene(MainApp.mAppVolver(stage));
                    }
                    else{
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setTitle("ERROR");
                        alerta.setContentText("Ocurrió un error y la orden de trabajo no fue guardada correctamente");
                        alerta.showAndWait();
                        stage.setScene(MainApp.mAppVolver(stage));
                    }
                });

                btnVer.setOnAction(event -> {
                    ordentrabajo orden = getTableView().getItems().get(getIndex());
                    mostrarOrden(orden);
                });
            }

            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane); //Si esta en blanco, no muestra
            }
        });

        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            filtroData.setPredicate(ordentrabajo -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase().trim();

                String fechaStr = "";
                if (ordentrabajo.getFecha_inicio() != null) {
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    fechaStr = ordentrabajo.getFecha_inicio().format(formatter);
                }
                if (String.valueOf(ordentrabajo.getPresupuesto().getNumero()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (ordentrabajo.getPresupuesto().getCliente().getNombre() != null && ordentrabajo.getPresupuesto().getCliente().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (ordentrabajo.getPresupuesto().getAuto().getMarca() != null && ordentrabajo.getPresupuesto().getAuto().getMarca().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (ordentrabajo.getPresupuesto().getAuto().getModelo() != null && ordentrabajo.getPresupuesto().getAuto().getModelo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (ordentrabajo.getPresupuesto().getAuto().getPatente() != null && ordentrabajo.getPresupuesto().getAuto().getPatente().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (fechaStr.contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        tabla.getColumns().addAll(colNumero, colFecha, colCliente, colMarca, colModelo, colPatente, colAcciones,colTareas);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        HBox panelInferior = new HBox();
        panelInferior.setPadding(new Insets(10, 0, 0, 0));
        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(event -> {
            stage.setScene(MainApp.mAppVolver(stage));
        });
        panelInferior.getChildren().add(btnVolver);


        principal.getChildren().addAll(lblTitulo, panelSup, tabla, panelInferior);
        Scene scene = new Scene(principal);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ChapAPP - Órdenes Pendientes");
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }

    public void mostrarOrden(ordentrabajo orden) {
        tareaManager tareaManager = new tareaManager();
        List<tarea> listita = tareaManager.getTareasPorID(orden.getID());
        Dialog<Void> ventana = new Dialog<>();
        ventana.setTitle("Detalle de la Orden de Trabajo");
        ventana.setHeaderText("Información de la Orden N°: " + orden.getPresupuesto().getNumero());
        ventana.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("Cliente:"), 0, 0);
        grid.add(new Label(orden.getPresupuesto().getCliente().getNombre()), 1, 0);
        grid.add(new Label("Marca del Vehículo:"), 0, 1);
        grid.add(new Label(orden.getPresupuesto().getAuto().getMarca()), 1, 1);
        grid.add(new Label("Modelo del Vehículo:"), 0, 2);
        grid.add(new Label(orden.getPresupuesto().getAuto().getModelo()), 1, 2);
        grid.add(new Label("Patente:"), 0, 3);
        grid.add(new Label(orden.getPresupuesto().getAuto().getPatente()), 1, 3);
        grid.add(new Label("Fecha Ingreso:"), 0, 4);
        grid.add(new Label(orden.getFecha_inicio().toString()), 1, 4);
        grid.add(new Label("Tareas:"), 0, 5);
        grid.add(new Label(listita.toString()), 1, 5);
        ventana.getDialogPane().setContent(grid);
        ventana.showAndWait();
    }

    public String toStringLista (List<tarea> lista) {
        String s = "";
        for (int i = 0; i < lista.size(); i++) {

        }
        return s;
    }
}
