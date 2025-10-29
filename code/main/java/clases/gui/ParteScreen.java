package clases.gui;

import clases.Manger.parteManager;
import clases.model.auto;
import clases.model.parte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.util.List;

public class ParteScreen {
    public ParteScreen(Stage stage){
     parteManager man = new parteManager();
     double anchoPantalla = Screen.getPrimary().getBounds().getWidth();
     double altoPantalla = Screen.getPrimary().getBounds().getHeight();
     List<parte> listaPartes= man.traerTodas();
     ObservableList<parte> data = FXCollections.observableArrayList(listaPartes);
     FilteredList<parte> filtrodata = new FilteredList<>(data,p->true);
     TableView<parte> tablaPartes = new TableView<>(data);

     TextField txtBuscar = new TextField();
     txtBuscar.setPromptText("Buscar vehículo...");

     Button btnBuscar = new Button("Buscar");
     btnBuscar.getStyleClass().add("botonbuscar");

     Button btnAgregar = new Button("Agregar");

     HBox barraBusqueda = new HBox(10);
     barraBusqueda.setStyle("-fx-padding: 10; -fx-background-color: #dddddd;");
     barraBusqueda.setPrefWidth(anchoPantalla);
     barraBusqueda.setAlignment(Pos.CENTER);
     Region spacer = new Region();
     Region spacerIz = new Region();
     HBox.setHgrow(spacer, Priority.ALWAYS);
     HBox.setHgrow(spacerIz,Priority.ALWAYS);
     barraBusqueda.getChildren().addAll(spacerIz,txtBuscar,btnBuscar,spacer,btnAgregar);

     tablaPartes.setFixedCellSize(25);
        var heightBinding = tablaPartes.fixedCellSizeProperty()
                .multiply(javafx.beans.binding.Bindings.size(tablaPartes.getItems()).add(1))
                .add(2);

        tablaPartes.minHeightProperty().bind(heightBinding);
        tablaPartes.prefHeightProperty().bind(heightBinding);

     TableColumn<parte,String> colNombre = new TableColumn<>("Nombre");
     colNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));


    tablaPartes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    tablaPartes.getColumns().addAll(colNombre);

    txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            filtrodata.setPredicate(parte -> {

                // 1. Si el filtro está vacío, muestra todos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // 2. Prepara el texto de búsqueda
                String lowerCaseFilter = newValue.toLowerCase().replace(" ", "");

                // 3. Comprueba solo el nombre de la parte
                if (parte.getNombre() != null) {
                    // Devuelve true si el nombre de la parte lo contiene
                    return parte.getNombre().toLowerCase().contains(lowerCaseFilter);
                }

                // 4. Si el nombre es nulo, no coincide
                return false;
            });
        });

        Button btnModificar = new Button("Modificar");
        Button btnVolver = new Button("Volver");

        HBox inferior = new HBox(btnModificar, btnVolver);
        inferior.setAlignment(Pos.CENTER_RIGHT);
        inferior.setPrefHeight(40);
        inferior.setSpacing(10);

        SortedList<parte> sortedData = new SortedList<>(filtrodata);
        sortedData.comparatorProperty().bind(tablaPartes.comparatorProperty());
        tablaPartes.setItems(sortedData);
        VBox panel = new VBox(10, tablaPartes, inferior);
        panel.setStyle("-fx-padding: 20; -fx-background-color: lightgray;");

        // Contenedor principal
        BorderPane root = new BorderPane();
        root.setTop(barraBusqueda);

        // 🔹 Mostrar la tabla como vista inicial
        root.setCenter(panel);

        btnAgregar.setOnAction(e->root.setCenter(agregarParte(stage,root,barraBusqueda,panel)));
        root.getStyleClass().add("fondo");
        // Crear escena
        Scene scene = new Scene(root, anchoPantalla * 0.8, altoPantalla * 0.8);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());

        btnVolver.setOnAction(e -> {
            stage.setScene(MainApp.mAppVolver(stage));
        });


        stage.setScene(scene);
        stage.show();

    }


    public VBox agregarParte(Stage stage, BorderPane root, HBox anterior, VBox pantallaant){

        Node aux = root.getTop();
        root.setTop(null);
        parteManager manager = new parteManager();
        parte auxparte = new parte("",1,true);
        VBox pantalla = new VBox();
        pantalla.setMinSize(300,200);
        pantalla.setMaxSize(600,400);
        pantalla.setSpacing(10);
        pantalla.setAlignment(Pos.CENTER);

        Label Nombre = new Label("Nombre");


        TextField nombreField = new TextField();



        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");
        btnGuardar.setStyle("-fx-cursor: hand;");
        btnCancelar.setStyle("-fx-cursor: hand;");
        btnCancelar.setOnAction(e->{
            root.setTop(anterior);
            root.setCenter(pantallaant);

        });



        HBox acciones = new HBox(20, btnGuardar,btnCancelar);

        acciones.setAlignment(Pos.CENTER);
        pantalla.getChildren().addAll(Nombre,nombreField, acciones);
        pantalla.getStyleClass().add("formulario");

        btnGuardar.setOnAction(e->{
            auxparte.setNombre(nombreField.getText());
            auxparte.toString();
            if(manager.insertarParte(auxparte) == true){
                mostrarAlertaAux(Alert.AlertType.INFORMATION,"Aceptada","Se cargo correctamente la informacion","Se creo la parte.");
            }
            else
            {
                mostrarAlertaAux(Alert.AlertType.ERROR, "Error", "No se puden cargar los datos", "Ha habido un error.");
            }
        });

        return pantalla;
    }


    private void mostrarAlertaAux(Alert.AlertType tipo, String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
