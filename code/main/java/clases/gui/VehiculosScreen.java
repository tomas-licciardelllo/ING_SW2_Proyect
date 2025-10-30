package clases.gui;
import clases.dao.AutoDAO;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
        AutoDAO autoDAO = new AutoDAO();
        List<auto> listaAutos = autoDAO.getAll();
        ObservableList<auto> data = FXCollections.observableArrayList(listaAutos);
        FilteredList<auto> filtroData = new FilteredList<>(data, p->true);

        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar vehículo...");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("BotonNormal");

        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setStyle("-fx-padding: 10; -fx-background-color: #dddddd;");
        barraBusqueda.setPrefWidth(anchoPantalla);
        barraBusqueda.setAlignment(Pos.CENTER);
        Region spacer = new Region();
        Region spacerIz = new Region();
        HBox.setHgrow(spacer,Priority.ALWAYS);
        HBox.setHgrow(spacerIz,Priority.ALWAYS);
        barraBusqueda.getChildren().addAll(txtBuscar,btnBuscar);

        TableView<auto> tabalAutos = new TableView<>(data);

        tabalAutos.setFixedCellSize(25);
        tabalAutos.prefHeightProperty().bind(tabalAutos.fixedCellSizeProperty().multiply( javafx.beans.binding.Bindings.size(tabalAutos.getItems()).add(1)));

        TableColumn<auto, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(cellData -> {
            cliente c = cellData.getValue().getCliente();
            return new SimpleStringProperty(c != null ? c.getNombre() : "");
        });
        colCliente.prefWidthProperty().bind(tabalAutos.widthProperty().multiply(0.5));

        TableColumn<auto, String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("Marca"));
        colMarca.prefWidthProperty().bind(tabalAutos.widthProperty().multiply(0.5));

        TableColumn<auto, String> colModelo = new TableColumn<>("Modelo");
        colModelo.setCellValueFactory(new PropertyValueFactory<>("Modelo"));
        colModelo.prefWidthProperty().bind(tabalAutos.widthProperty().multiply(0.5));

        TableColumn<auto, Integer> colAnio = new TableColumn<>("Año");
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colAnio.prefWidthProperty().bind(tabalAutos.widthProperty().multiply(0.5));

        TableColumn<auto, Integer> colPatente = new TableColumn<>("Patente");
        colPatente.setCellValueFactory(new PropertyValueFactory<>("Patente"));
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
        HBox inferior = new HBox(btnModificar, btnVolver);
        inferior.setAlignment(Pos.CENTER_RIGHT);
        inferior.setPrefHeight(40);
        inferior.setSpacing(10);

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
                btnModificar.setOnAction(e->root.setCenter(ModificarAuto(root,id,barraBusqueda,panel)));

            }
        });

        btnVolver.setOnAction(e -> {
            stage.setScene(MainApp.mAppVolver(stage));
        });


        stage.setScene(scene);
        stage.show();
    }

    private VBox ModificarAuto(BorderPane root, int idAutoBuscado, HBox anterior, VBox pantallaant){
        Node aux = root.getTop();
        root.setTop(null);
        AutoDAO au = new AutoDAO();
        auto auxAuto = au.read(idAutoBuscado);
        VBox pantalla = new VBox();
        pantalla.setMinSize(300,200);
        pantalla.setMaxSize(600,400);
        pantalla.setSpacing(10);
        pantalla.setAlignment(Pos.CENTER);

        Label marca = new Label("Marca");
        Label modelo= new Label("Modelo");
        Label anio = new Label("Año");
        Label  patente = new Label("Patente");


        TextField marcaField = new TextField(auxAuto.getMarca());
        TextField modeloField = new TextField(auxAuto.getModelo());
        TextField anioField = new TextField(Integer.toString(auxAuto.getAnio()));
        TextField patenteField = new TextField(auxAuto.getPatente());

        Button btnGuardar = new Button("Modificar");
        Button btnCancelar = new Button("Cancelar");
        btnGuardar.setStyle("-fx-cursor: hand;");
        btnCancelar.setStyle("-fx-cursor: hand;");
        btnCancelar.setOnAction(e->{
            root.setTop(anterior);
            root.setCenter(pantallaant);

        });



        HBox acciones = new HBox(20, btnGuardar,btnCancelar);

        acciones.setAlignment(Pos.CENTER);
        pantalla.getChildren().addAll(marca,marcaField,modelo,modeloField, anio,anioField,patente,patenteField, acciones);
        pantalla.getStyleClass().add("formulario");

        btnGuardar.setOnAction(e->{
            auxAuto.setMarca(marcaField.getText());
            auxAuto.setModelo(modeloField.getText());
            auxAuto.setAnio(Integer.parseInt(anioField.getText()));
            auxAuto.setPatente(patenteField.getText());
            System.out.println(auxAuto.toString());
            au.update(auxAuto);
        });
        return pantalla;
    }
}
