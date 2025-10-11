package clases.gui;
import clases.dao.AutoDAO;
import clases.dao.ClienteDAO;
import clases.model.auto;
import clases.model.cliente;
import clases.model.parte;
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

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteScreen {

    public ClienteScreen(Stage stage) {
        ClienteDAO clienteDAO = new ClienteDAO();
        AutoDAO autoDao = new AutoDAO();
        List<cliente> listaClientes = clienteDAO.getAll();
        ObservableList<cliente> data = FXCollections.observableArrayList(listaClientes);


        FilteredList<cliente> filtroData = new FilteredList<>(data,p->true);
        double anchoPantalla = Screen.getPrimary().getBounds().getWidth();
        double altoPantalla = Screen.getPrimary().getBounds().getHeight();

        // Barra de búsqueda (queda siempre arriba)
        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar cliente...");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("btnormal");

        Button btnAgregar = new Button("Agregar");
        btnAgregar.getStyleClass().add("btnormal");

        Button btnVolver = new Button("Volver");
        btnVolver.getStyleClass().add("btnormal");

        btnBuscar.setOnAction(e->{
            auto nuevo = new auto("Camioneta",new ArrayList<parte>(),"HG 234 UH",2003,"Toyota", "Corolla");
            autoDao.create(nuevo);
        });

       btnVolver.setOnAction(e->{
           Stage ss = (Stage) btnVolver.getScene().getWindow();
           stage.setScene(MainApp.mAppVolver(stage));
        });

        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setStyle("-fx-padding: 10; -fx-background-color: #dddddd;");
        barraBusqueda.setPrefWidth(anchoPantalla);
        barraBusqueda.setAlignment(Pos.CENTER);
        Region spacer = new Region();
        Region spacerIz = new Region();
        HBox.setHgrow(spacer,Priority.ALWAYS);
        HBox.setHgrow(spacerIz,Priority.ALWAYS);
        barraBusqueda.getChildren().addAll(spacerIz,txtBuscar, btnBuscar,spacer, btnAgregar,btnVolver);


        txtBuscar.prefWidthProperty().bind(barraBusqueda.widthProperty().multiply(0.6));

        // desde aca es lo original


        TableView<cliente> tablaClientes = new TableView<>(data);
        ClienteDAO auxCDAO = new ClienteDAO();

        List<Integer> ides = new ArrayList<>();
        ides = auxCDAO.getAllId();
        System.out.println("IDES"+ides);
        tablaClientes.setFixedCellSize(25);
        tablaClientes.prefHeightProperty().bind(tablaClientes.fixedCellSizeProperty().multiply( javafx.beans.binding.Bindings.size(tablaClientes.getItems()).add(1)));
        TableColumn<cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        colNombre.prefWidthProperty().bind(tablaClientes.widthProperty().multiply(0.5));

        TableColumn<cliente, String> colTelefono = new TableColumn<>("Telefono");
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("Telefono"));
        colTelefono.prefWidthProperty().bind(tablaClientes.widthProperty().multiply(0.5));
        tablaClientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        TableColumn<cliente,Integer> colIdes = new TableColumn<>("id");
        colIdes.setCellValueFactory(new PropertyValueFactory<>("id"));

        tablaClientes.getColumns().addAll(colNombre, colTelefono);



        Button btnModificar = new Button("Modificar");
        HBox inferior = new HBox(btnModificar);
        inferior.setAlignment(Pos.CENTER_RIGHT);
        inferior.setPrefHeight(40);
        inferior.setSpacing(10);

        List<Integer> finalIdes = ides;


        txtBuscar.textProperty().addListener((obs,oldValue,newValue)->{
            filtroData.setPredicate(cliente->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return cliente.getNombre().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<cliente> sortedData = new SortedList<>(filtroData);
        sortedData.comparatorProperty().bind(tablaClientes.comparatorProperty());
        tablaClientes.setItems(sortedData);
        VBox panel = new VBox(10, tablaClientes, inferior);
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

        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
            if(newSelection != null){
                Integer id = tablaClientes.getSelectionModel().getSelectedIndex();
                Integer idSeleccionado = finalIdes.get(id);
                System.out.println("SE ENTRO"+id);
                btnModificar.setOnAction(e->root.setCenter(crearPanelModificacion(root,idSeleccionado, barraBusqueda,panel)));

            }
        });


        stage.setScene(scene);
        stage.show();
    }



    private VBox crearPanelModificacion(BorderPane root, int idClienteBuscado, HBox anterior, VBox pantallaant) {
        Node aux = root.getTop();
        root.setTop(null);
        ClienteDAO cl = new ClienteDAO();
        AutoDAO au = new AutoDAO();
        //auto auxAuto = au.read()
        cliente auxCliente = cl.read(idClienteBuscado);
        VBox pantalla = new VBox();
        pantalla.setMinSize(300,200);
        pantalla.setMaxSize(600,400);




        pantalla.setAlignment(Pos.CENTER);
        Label nombre = new Label("Nombre");
        Label telefono = new Label("Telefono");
        Label Tipo = new Label("Tipo");
        Label Marca = new Label("Marca");
        Label Modelo = new Label("Modelo");
        Label Año = new Label("Año");
        Label Patente = new Label("Patente");
        Label Auto = new Label("Auto");

        TextField nombreField = new TextField(auxCliente.getNombre());
        TextField telefonoField = new TextField(auxCliente.getTelefono());
        TextField tipoField = new TextField();
        TextField marcaField = new TextField();
        TextField modeloField = new TextField();
        TextField añoField = new TextField();
        TextField patenteField = new TextField();


        Button btnGuardar = new Button("Modificar");
        Button btnCancelar = new Button("Cancelar");

        //Aca Guardariamos los cambios hechos
        //btnGuardar.setOnAction(e->{})

        btnCancelar.setOnAction(e->{
            root.setTop(anterior);
            root.setCenter(pantallaant);

        });

        HBox acciones = new HBox(20, btnGuardar,btnCancelar);

        acciones.setAlignment(Pos.CENTER);
        pantalla.getChildren().addAll(nombre,nombreField,telefono,telefonoField,Auto,Tipo,tipoField,Marca,marcaField,Modelo,modeloField,Año,añoField,Patente,patenteField, acciones);
        pantalla.getStyleClass().add("formulario");
        return pantalla;
    }
    /*private VBox crearFormulario(Stage stage) {
        ClienteDAO clienteD = new ClienteDAO();
        Node n = root.getTop();
        root.setTop(null);
        VBox formulario = new VBox(10);
        formulario.setMinSize(300, 200);
        formulario.setMaxSize(600, 400);

        formulario.setStyle(
                "-fx-padding: 20;" +
                        "-fx-background-color: #847770;" + // blanco
                        "-fx-background-radius: 15;" +     // bordes redondeados
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: #cccccc;" +     // borde gris
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0, 0, 4);" // sombra
        );
        formulario.setAlignment(Pos.CENTER);

        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField();

        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField();

        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");
        Button btnAtras = new Button("Atras");

        HBox acciones = new HBox(10, btnGuardar, btnCancelar);
        acciones.setAlignment(Pos.CENTER);
        HBox inferior = new HBox();
        Region espacio= new Region();
        HBox.setHgrow(espacio,Priority.ALWAYS);
        formulario.getChildren().addAll(lblNombre, txtNombre, lblTelefono, txtTelefono, acciones);
        HBox.setMargin(btnAtras, new Insets(0,8,8,0));
        inferior.getChildren().addAll(espacio,btnAtras);

        root.setBottom(inferior);

        // Acción guardar
        btnGuardar.setOnAction(e -> {
            cliente nuevo = new cliente(txtNombre.getText(), txtTelefono.getText(), new ArrayList<>(),new ArrayList<>());
            // Agregar al DAO y refrescar la tabla
            data.add(nuevo);
            // acá también podés llamar a clienteDAO.insert(nuevo);
            clienteD.create(nuevo);
            // Volver a mostrar la tabla
            root.setCenter(ClienteScreen());
        });

        // Acción cancelar → volver a la tabla
        btnCancelar.setOnAction(e ->  {root.setCenter(crearPanelCentral(data, root)); root.setBottom(null);root.setTop(n);});
        btnAtras.setOnAction(e -> {root.setCenter(crearPanelCentral(data, root)); root.setBottom(null);root.setTop(n);});
        return formulario;
    }*/

}
