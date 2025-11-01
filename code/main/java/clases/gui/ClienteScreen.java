package clases.gui;
import clases.Manager.clienteManager;
import clases.model.cliente;
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

import java.util.ArrayList;
import java.util.List;

public class ClienteScreen {

    public ClienteScreen(Stage stage) {
        clienteManager manager = new clienteManager();
        List<cliente> listaClientes = manager.traerCLientes();
        ObservableList<cliente> data = FXCollections.observableArrayList(listaClientes);

        FilteredList<cliente> filtroData = new FilteredList<>(data,p->true);
        double anchoPantalla = Screen.getPrimary().getBounds().getWidth();
        double altoPantalla = Screen.getPrimary().getBounds().getHeight();

        // Barra de búsqueda (queda siempre arriba)
        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar cliente...");
        txtBuscar.getStyleClass().add("barraBusqueda");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("botonNormal");

        Button btnAgregar = new Button("Agregar");
        btnAgregar.getStyleClass().add("botonNormal");

        Button btnVolver = new Button("Volver");
        btnVolver.getStyleClass().add("botonNormal");

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
        btnModificar.getStyleClass().add("botonModificar");
        HBox inferior = new HBox(btnModificar);
        inferior.setAlignment(Pos.CENTER_RIGHT);
        inferior.setPrefHeight(40);
        inferior.setSpacing(10);



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

        btnAgregar.setOnAction(e -> {
            // Ocultamos la barra superior mientras estamos en el formulario
            root.setTop(null);
            root.setCenter(crearFormulario(root, data, barraBusqueda, panel));
        });


        root.getStyleClass().add("fondo");
        // Crear escena
        Scene scene = new Scene(root, anchoPantalla * 0.8, altoPantalla * 0.8);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());

        btnModificar.setOnAction(e->{if(tablaClientes.getSelectionModel().getSelectedItem() == null){
            mostrarAlertaAux(Alert.AlertType.ERROR, "Error", "Sin seleccion","Debe seleccionar un cliente para poder modificar");
        }});
        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
            if(newSelection != null){
                Integer id = tablaClientes.getSelectionModel().getSelectedItem().getIdBD();
                System.out.println("SE ENTRO"+id);
                btnModificar.setOnAction(e->root.setCenter(crearPanelModificacion(root,id, barraBusqueda,panel)));

            }
        });

        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ChapAPP - Gestión de Clientes");
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }

    private void mostrarAlertaAux(Alert.AlertType tipo, String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    private VBox crearPanelModificacion(BorderPane root, int idClienteBuscado, HBox anterior, VBox pantallaant) {
        Node aux = root.getTop();
        root.setTop(null);
        clienteManager manager = new clienteManager();
        cliente auxCliente = manager.traerCLiente(idClienteBuscado);
        VBox pantalla = new VBox();
        pantalla.setMinSize(300,200);
        pantalla.setMaxSize(600,400);
        pantalla.setSpacing(10);
        pantalla.setAlignment(Pos.CENTER);
        Label nombre = new Label("Nombre");
        Label telefono = new Label("Telefono");


        TextField nombreField = new TextField(auxCliente.getNombre());
        TextField telefonoField = new TextField(auxCliente.getTelefono());


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
        pantalla.getChildren().addAll(nombre,nombreField,telefono,telefonoField, acciones);
        pantalla.getStyleClass().add("formulario");

        btnGuardar.setOnAction(e->{
            System.out.println(auxCliente.getIdBD());
            auxCliente.setNombre(nombreField.getText());
            auxCliente.setTelefono(telefonoField.getText());
            manager.actualizarCliente(auxCliente);
        });
        return pantalla;
    }
    private VBox crearFormulario(BorderPane root, ObservableList<cliente> data, Node topBar, VBox panelTabla) {
        clienteManager manager  = new clienteManager();

        VBox formulario = new VBox(15); // Aumenté el espaciado
        formulario.setMaxSize(400, 300);
        formulario.setAlignment(Pos.CENTER);
        formulario.setStyle(
                "-fx-padding: 20;" +
                        "-fx-background-color: #f4f4f4;" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: #cccccc;" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 4);"
        );

        Label lblTitulo = new Label("Nuevo Cliente");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre y Apellido");

        TextField txtTelefono = new TextField();
        txtTelefono.setPromptText("Número de teléfono");

        Button btnGuardar = new Button("Guardar");
        btnGuardar.getStyleClass().add("btn-success"); // Un estilo diferente para guardar

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("btn-danger"); // Y para cancelar

        HBox acciones = new HBox(20, btnGuardar, btnCancelar);
        acciones.setAlignment(Pos.CENTER);

        formulario.getChildren().addAll(lblTitulo, new Label("Nombre:"), txtNombre, new Label("Teléfono:"), txtTelefono, acciones);

        btnGuardar.setOnAction(e -> {
            String nombre = txtNombre.getText();
            String telefono = txtTelefono.getText();

            // Validación simple para no guardar clientes vacíos
            if (nombre == null || nombre.trim().isEmpty() || telefono == null || telefono.trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error de Validación");
                alert.setHeaderText(null);
                alert.setContentText("El nombre y el teléfono no pueden estar vacíos.");
                alert.showAndWait();
                return; // Detiene la ejecución si hay un error
            }

            cliente nuevoCliente = new cliente(nombre, telefono, new ArrayList<>(), new ArrayList<>());

            // 1. Guardar en la base de datos
            cliente clienteGuardadoenlaBD = manager.subirCliente(nuevoCliente);

            // 2. Agregar a la lista observable (esto refresca la tabla automáticamente)
            data.add(nuevoCliente);

            // 3. Restaurar la vista principal
            root.setTop(topBar);
            root.setCenter(panelTabla);
        });

        btnCancelar.setOnAction(e -> {
            // Simplemente restauramos la vista principal sin guardar nada
            root.setTop(topBar);
            root.setCenter(panelTabla);
        });

        return formulario;
    }

}
