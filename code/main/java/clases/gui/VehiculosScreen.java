package clases.gui;
import clases.Manager.autoManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import clases.model.*;


import java.util.List;

public class VehiculosScreen {
    public VehiculosScreen(Stage stage)
    {
        double anchoPantalla = Screen.getPrimary().getBounds().getWidth();
        double altoPantalla = Screen.getPrimary().getBounds().getHeight();
        autoManager mgr = new autoManager();
        List<auto> listaAutos = mgr.getAll();

        System.out.println("VehiculosScreen: Autos encontrados en la BD: " + listaAutos.size());
        ObservableList<auto> data = FXCollections.observableArrayList(listaAutos);
        FilteredList<auto> filtroData = new FilteredList<>(data, p->true);

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar vehículo...");
        txtBuscar.getStyleClass().add("barraBusqueda");
        txtBuscar.setPrefWidth(600);

        Region spacer = new Region();
        Region spacerIz = new Region();
        HBox.setHgrow(spacer,Priority.ALWAYS);
        HBox.setHgrow(spacerIz,Priority.ALWAYS);
        HBox barraBusqueda = new HBox(spacer, txtBuscar, spacerIz);

        TableView<auto> tabalAutos = new TableView<>(data);
        tabalAutos.getStyleClass().add("table-view");

        tabalAutos.setFixedCellSize(25);
        tabalAutos.prefHeightProperty().bind(tabalAutos.fixedCellSizeProperty().multiply( javafx.beans.binding.Bindings.size(tabalAutos.getItems()).add(1)));

        TableColumn<auto, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(cellData -> {
            cliente c = cellData.getValue().getCliente();
            return new SimpleStringProperty(c != null ? c.getNombre() : "");
        });
        colCliente.prefWidthProperty().bind(tabalAutos.widthProperty().multiply(0.5));

        TableColumn<auto, String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colMarca.prefWidthProperty().bind(tabalAutos.widthProperty().multiply(0.5));

        TableColumn<auto, String> colModelo = new TableColumn<>("Modelo");
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colModelo.prefWidthProperty().bind(tabalAutos.widthProperty().multiply(0.5));

        TableColumn<auto, Integer> colAnio = new TableColumn<>("Año");
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colAnio.prefWidthProperty().bind(tabalAutos.widthProperty().multiply(0.5));

        TableColumn<auto, Integer> colPatente = new TableColumn<>("Patente");
        colPatente.setCellValueFactory(new PropertyValueFactory<>("patente"));
        colPatente.prefWidthProperty().bind(tabalAutos.widthProperty().multiply(0.5));

        tabalAutos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        TableColumn<auto,Integer> colIdes = new TableColumn<>("id");
        colIdes.setCellValueFactory(new PropertyValueFactory<>("id"));

        tabalAutos.getColumns().addAll(colCliente, colMarca, colModelo,colAnio,colPatente);


        txtBuscar.textProperty().addListener((obs,oldValue,newValue)->{
            filtroData.setPredicate(auto->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase().replace(" ", "");

                // Traigo el nombre del cliente si existe
                String nombreCliente = (auto.getCliente() != null && auto.getCliente().getNombre() != null)
                        ? auto.getCliente().getNombre().toLowerCase()
                        : "";

                return (auto.getPatente().toLowerCase().replace(" ", "").contains(lowerCaseFilter)
                        || (auto.getMarca().toLowerCase().replace(" ", "").contains(lowerCaseFilter))
                        || (auto.getModelo().toLowerCase().replace(" ", "").contains(lowerCaseFilter))
                        || (nombreCliente.contains(lowerCaseFilter)));
            });
        });

        Button btnModificar = new Button("Modificar");
        Button btnVolver = new Button("Volver");
        btnModificar.getStyleClass().add("botonModificar");
        btnVolver.getStyleClass().add("botonNormal");

        Region spacerInf = new Region();
        HBox.setHgrow(spacerInf, Priority.ALWAYS);
        HBox inferior = new HBox(10, btnModificar, spacerInf, btnVolver);
        inferior.setPadding(new Insets(10, 0, 0, 0));
        inferior.setAlignment(Pos.CENTER);

        SortedList<auto> sortedData = new SortedList<>(filtroData);
        sortedData.comparatorProperty().bind(tabalAutos.comparatorProperty());
        tabalAutos.setItems(sortedData);
        VBox panel = new VBox(10, tabalAutos, inferior);
        panel.setStyle("-fx-padding: 20; -fx-background-color: lightgray;");

        // Contenedor principal
        BorderPane root = new BorderPane();
        root.setTop(barraBusqueda);

        // 🔹 Mostrar la tabla como vista inicial
        root.setCenter(panel);

        //btnAgregar.setOnAction(e->root.setCenter(crearFormulario(root,data)));
        root.getStyleClass().add("fondo");
        // Crear escena
        Scene scene = new Scene(root, anchoPantalla * 0.8, altoPantalla * 0.8);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());

        tabalAutos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
            if(newSelection != null){
                Integer id = tabalAutos.getSelectionModel().getSelectedItem().getIdBD();
                System.out.println("SE ENTRO"+id);
                btnModificar.setOnAction(e->root.setCenter(ModificarAuto(root,id,barraBusqueda,panel, mgr)));

            }
        });

        btnVolver.setOnAction(e -> {
            stage.setScene(MainApp.mAppVolver(stage));
        });

        stage.setScene(scene);
        stage.setTitle("ChapAPP - Gestión de Vehiculos");
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }

    private VBox ModificarAuto(BorderPane root, int idAutoBuscado, HBox anterior, VBox pantallaant, autoManager mgr){
        Node aux = root.getTop();
        root.setTop(null);
        auto auxAuto = mgr.traerAutoId(idAutoBuscado);
        VBox pantalla = new VBox();
        pantalla.setMinSize(300,200);
        pantalla.setMaxSize(600,400);
        pantalla.setSpacing(10);
        pantalla.setAlignment(Pos.CENTER);
        pantalla.setStyle("-fx-background-color: -fx-celeste;");

        Label marca = new Label("Marca");
        Label modelo= new Label("Modelo");
        Label anio = new Label("Año");
        Label  patente = new Label("Patente");
        marca.getStyleClass().add("fuenteWhite");
        modelo.getStyleClass().add("fuenteWhite");
        anio.getStyleClass().add("fuenteWhite");
        patente.getStyleClass().add("fuenteWhite");

        TextField marcaField = new TextField(auxAuto.getMarca());
        TextField modeloField = new TextField(auxAuto.getModelo());
        TextField anioField = new TextField(Integer.toString(auxAuto.getAnio()));
        TextField patenteField = new TextField(auxAuto.getPatente());

        Button btnGuardar = new Button("Modificar");
        btnGuardar.getStyleClass().add("botonNormal");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("botonEliminar");
        btnCancelar.setOnAction(e->{
            root.setTop(anterior);
            root.setCenter(pantallaant);

        });



        HBox acciones = new HBox(20, btnGuardar,btnCancelar);

        acciones.setAlignment(Pos.CENTER);
        pantalla.getChildren().addAll(marca,marcaField,modelo,modeloField, anio,anioField,patente,patenteField, acciones);
        pantalla.getStyleClass().add("formularioAz");

        btnGuardar.setOnAction(e->{
            auxAuto.setMarca(marcaField.getText());
            auxAuto.setModelo(modeloField.getText());
            auxAuto.setAnio(Integer.parseInt(anioField.getText()));
            auxAuto.setPatente(patenteField.getText());
            mgr.actualizarAuto(auxAuto);
        });
        return pantalla;
    }
}
