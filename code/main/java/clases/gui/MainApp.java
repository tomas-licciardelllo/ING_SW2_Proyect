package clases.gui;

import clases.Manager.ordentrabajoManager;
import clases.Manager.presupuestoManager;
import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import clases.control.Conexion;
import clases.model.*;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        // --- Cargar fuentes ---
        Font.loadFont(getClass().getResourceAsStream("/font/Livvic-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/font/Livvic-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/font/Livvic-Black.ttf"), 14);

        // --- Pantalla de carga ---
        Label cargando = new Label("INGRESANDO ...");
        cargando.getStyleClass().add("textocarga");

        ProgressBar barra = new ProgressBar();
        barra.setPrefWidth(200);
        barra.getStyleClass().add("barraprogreso");

        VBox carga = new VBox(15, cargando, barra);
        carga.getStyleClass().add("cajacarga");

        Scene load = new Scene(carga, 300, 200);
        load.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());

        FadeTransition transicion = new FadeTransition(Duration.seconds(1), cargando);
        transicion.setFromValue(0.1);
        transicion.setToValue(1.0);
        transicion.setCycleCount(FadeTransition.INDEFINITE);
        transicion.setAutoReverse(true);
        transicion.play();

        Timeline animabarra = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(barra.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(2.5), new KeyValue(barra.progressProperty(), 1))
        );
        animabarra.setCycleCount(1);
        animabarra.play();

        stage.setScene(load);
        stage.setTitle("ChapAPP");
        stage.show();

        PauseTransition pausa = new PauseTransition(Duration.seconds(1.5));
        pausa.setOnFinished(e -> {
            stage.setScene(mAppVolver(stage));
            stage.setMaximized(true);
        });
        pausa.play();
    }

    public static Scene mAppVolver(Stage stage) {
        BorderPane root = new BorderPane();

        root.setLeft(Header.createHeader(stage));

        StackPane contenido = new StackPane();
        contenido.getStyleClass().add("fondo");

        HBox gridContenedor = new HBox(20);
        gridContenedor.setAlignment(Pos.CENTER);

        GridPane grid1 = new GridPane();
        GridPane grid2 = new GridPane();
        grid1.setPadding(new Insets(20));
        grid2.setPadding(new Insets(20));
        ColumnConstraints colConstraint = new ColumnConstraints();
        colConstraint.setHgrow(Priority.ALWAYS);
        grid1.getColumnConstraints().add(colConstraint);
        grid2.getColumnConstraints().add(colConstraint);
        RowConstraints filaTitulo = new RowConstraints();
        filaTitulo.setVgrow(Priority.NEVER);
        RowConstraints filaTabla = new RowConstraints();
        filaTabla.setVgrow(Priority.ALWAYS);

        grid1.getRowConstraints().addAll(filaTitulo, filaTabla);
        grid2.getRowConstraints().addAll(filaTitulo, filaTabla);

        Label lblTitulo1 = new Label("PRESUPUESTO SIN COBRAR");
        Label lblTitulo2 = new Label("TRABAJOS EN DESARROLLO");
        lblTitulo1.getStyleClass().add("subtituloMenu");
        lblTitulo2.getStyleClass().add("subtituloMenu");

        GridPane.setHalignment(lblTitulo1, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(lblTitulo2, javafx.geometry.HPos.CENTER);

        TableView<presupuesto> tablitaPresu = tablaPresupuestosDeuda();
        TableView<ordentrabajo> tablitaOrdenes = tablaOrdenesenDesarrollo();

        HBox.setHgrow(grid1, Priority.ALWAYS);
        HBox.setHgrow(grid2, Priority.ALWAYS);
        GridPane.setVgrow(tablitaPresu, Priority.ALWAYS);
        GridPane.setVgrow(tablitaOrdenes, Priority.ALWAYS);
        tablitaPresu.setMaxWidth(Double.MAX_VALUE);
        tablitaOrdenes.setMaxWidth(Double.MAX_VALUE);

        grid1.add(lblTitulo1, 0, 0);
        grid1.add(tablitaPresu, 0, 1);
        grid2.add(lblTitulo2, 0, 0);
        grid2.add(tablitaOrdenes, 0, 1);

        gridContenedor.getChildren().addAll(grid1, grid2);
        root.setCenter(gridContenedor);
        gridContenedor.getStyleClass().add("fondo");

        Scene principal = new Scene(root, 900, 600);
        principal.getStylesheets().add(MainApp.class.getResource("/resources/styles.css").toExternalForm());
        stage.setTitle("ChapAPP - Menú Principal");
        return principal;
    }


    public static void main(String[] args) {
        launch();

        List<auto> l1 = new ArrayList<>();
        List<parte> l2 = new ArrayList<>();
        List<auto> l3 = new ArrayList<>();
        List<presupuesto> l4 = new ArrayList<>();
        l1.add(new auto("Toyota","Corolla",2003, "css", "2993"));
        cliente c1 = new cliente("Jorge", "1124233",l1,l4);

    }


    public static TableView<ordentrabajo> tablaOrdenesenDesarrollo(){
        TableView<ordentrabajo> tabla = new TableView<>();
        TableColumn<ordentrabajo,Integer> colNumero = new TableColumn<>("Numero de Orden");
        TableColumn<ordentrabajo,String> colFecha = new TableColumn<>("Fecha Ingreso");
        TableColumn<ordentrabajo,String> colAuto = new TableColumn<>("Auto");
        TableColumn<ordentrabajo,String> colPatente = new TableColumn<>("Patente");

        colNumero.setStyle("-fx-alignment: CENTER;");
        colFecha.setStyle("-fx-alignment: CENTER;");
        colAuto.setStyle("-fx-alignment: CENTER;");
        colPatente.setStyle("-fx-alignment: CENTER;");

        colNumero.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha_inicio"));
        colAuto.setCellValueFactory(cellData->{
            ordentrabajo orden = cellData.getValue();
            auto auto = orden.getPresupuesto().getAuto();
            String marca = auto.getMarca();
            String modelo = auto.getModelo();
            String respuesta = marca + "-" + modelo;
            return new javafx.beans.property.SimpleStringProperty(respuesta);
        });
        colPatente.setCellValueFactory(new PropertyValueFactory<>("vehiculoPat"));

        tabla.getColumns().addAll(colNumero,colFecha,colAuto,colPatente);

        ordentrabajoManager ordenMan =  new ordentrabajoManager();
        List<ordentrabajo> resultado = ordenMan.obtenerDesarrollo();
        tabla.setItems(FXCollections.observableArrayList(resultado));
        tabla.setPrefHeight(500);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return tabla;
    }
    public static TableView<presupuesto> tablaPresupuestosDeuda(){
        TableView<presupuesto> tabla = new TableView<>();
        TableColumn<presupuesto,Integer> colNumero = new TableColumn<>("Numero");
        TableColumn<presupuesto,LocalDate> colFecha = new TableColumn<>("Fecha");
        TableColumn<presupuesto,String> colCliente = new TableColumn<>("Cliente");
        TableColumn<presupuesto,Float> colMonto = new TableColumn<>("Monto");

        colNumero.setStyle("-fx-alignment: CENTER;");
        colFecha.setStyle("-fx-alignment: CENTER;");
        colCliente.setStyle("-fx-alignment: CENTER;");
        colMonto.setStyle("-fx-alignment: CENTER;");

        colNumero.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNumero()));
        colFecha.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFecha()));
        colCliente.setCellValueFactory(cellData -> {
            cliente c = cellData.getValue().getCliente();
            return new SimpleStringProperty(c != null ? c.getNombre() : "");
        });
        colMonto.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCostoTotal()));

        tabla.getColumns().addAll(colNumero,colFecha,colCliente,colMonto);

        presupuestoManager presuMan =  new presupuestoManager();
        List<presupuesto> debe = presuMan.obtenerDeudas();
        tabla.setItems(FXCollections.observableArrayList(debe));
        tabla.setPrefHeight(500);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return tabla;
    }
}
