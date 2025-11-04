package clases.gui;

import clases.Manager.autoManager;
import clases.model.auto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.util.List;

public class AutoScreen {
    public AutoScreen(Stage stage){
        autoManager autoManager = new autoManager();
        List<auto> listaAuto = autoManager.getAll();
        ObservableList<auto> data = FXCollections.observableArrayList(listaAuto);
        VBox formulario = new VBox(10);
        formulario.setMinSize(300, 200);
        formulario.setMaxSize(600, 400);
        double anchoPantalla = Screen.getPrimary().getBounds().getWidth();
        double altoPantalla = Screen.getPrimary().getBounds().getHeight();
        formulario.setStyle(
                "-fx-padding: 20;" +
                        "-fx-background-color: #847770;" + // blanco
                        "-fx-background-radius: 15;" +     // bordes redondeados
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: #cccccc;" +     // borde gris
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0, 0, 4);" // sombra
        );

        TableView<auto> tablaAutos = new TableView<>(data);
        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar auto...");

        Button btnBuscar = new Button("Buscar");
        //btnBuscar.getStyleClass().add("botonbuscar");

        HBox barra = new HBox(10);
        barra.setStyle("-fx-padding: 10; -fx-background-color: #dddddd;");
        barra.setPrefWidth(anchoPantalla);
        barra.setAlignment(Pos.CENTER);
        Region spacer = new Region();
        Region sapacerIz = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setHgrow(sapacerIz, Priority.ALWAYS);
        barra.getChildren().addAll(sapacerIz,txtBuscar,btnBuscar,spacer);

        txtBuscar.prefWidthProperty().bind(barra.widthProperty().multiply(0.6));


        tablaAutos.setFixedCellSize(25);
        tablaAutos.prefHeightProperty().bind(tablaAutos.fixedCellSizeProperty().multiply( javafx.beans.binding.Bindings.size(tablaAutos.getItems()).add(1)));
        TableColumn<auto,String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("Tipo"));

        TableColumn<auto,String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("Marca"));

        TableColumn<auto,Integer> colModelo = new TableColumn<>("Modelo");
        colModelo.setCellValueFactory(new PropertyValueFactory<>("Modelo"));

        TableColumn<auto,Integer> colAño = new TableColumn<>("Año");
        colModelo.setCellValueFactory(new PropertyValueFactory<>("Año"));

        TableColumn<auto,String> colPatente = new TableColumn<>("Patente");
        colPatente.setCellValueFactory(new PropertyValueFactory<>("Patente"));

        colTipo.prefWidthProperty().bind(tablaAutos.widthProperty().multiply(0.2));
        colMarca.prefWidthProperty().bind(tablaAutos.widthProperty().multiply(0.2));
        colModelo.prefWidthProperty().bind(tablaAutos.widthProperty().multiply(0.2));
        colAño.prefWidthProperty().bind(tablaAutos.widthProperty().multiply(0.2));
        colPatente.prefWidthProperty().bind(tablaAutos.widthProperty().multiply(0.2));
        tablaAutos.getColumns().addAll(colTipo,colMarca,colModelo,colAño,colPatente);

        Button btnUsos = new Button("Asignar");
        HBox inferior = new HBox(btnUsos);
        inferior.setAlignment(Pos.CENTER_RIGHT);
        inferior.setPrefHeight(40);

        VBox panel = new VBox(10,tablaAutos,inferior);
        panel.setStyle("-fx-padding: 20; -fx-background-color: lightgray;");
        BorderPane root = new BorderPane();
        root.setTop(barra);
        root.setCenter(panel);
        root.getStyleClass().add("fondo");

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ChapAPP - Gestión de Vehículos");
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }


    private VBox ModificarAuto(BorderPane root, int idAutoBuscado, HBox anterior, VBox pantallaant){
        Node aux = root.getTop();
        root.setTop(null);
        autoManager autoMan = new autoManager();
        auto auxAuto = autoMan.traerAutoId(idAutoBuscado);
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
            autoMan.actualizarAuto(auxAuto);
        });
        return pantalla;
    }
}
