package clases.gui;

import clases.dao.OrdenDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import clases.model.ordentrabajo;

import java.time.LocalDate;
import java.util.List;

public class OrdenScreen {
    public OrdenScreen (Stage stage){
        OrdenDAO ordenDAO = new OrdenDAO();
        List<ordentrabajo> listaOrdenes = ordenDAO.getAll();
        ObservableList<ordentrabajo> data = FXCollections.observableArrayList(listaOrdenes);
        TableView<ordentrabajo> tablaOrdenes = new TableView<>(data);
        double anchoPantalla = Screen.getPrimary().getBounds().getWidth();
        double altoPantalla = Screen.getPrimary().getBounds().getHeight();

        TableColumn<ordentrabajo, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("Estado"));
        colEstado.prefWidthProperty().bind(tablaOrdenes.widthProperty().multiply(0.5));

        TableColumn<ordentrabajo, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
        colFecha.prefWidthProperty().bind(tablaOrdenes.widthProperty().multiply(0.5));

        tablaOrdenes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tablaOrdenes.getColumns().addAll(colEstado,colFecha);

        VBox panel = new VBox(10,tablaOrdenes);
        panel.setStyle("-fx-padding: 20; -fx-background-color: lightgray;");
        BorderPane root = new BorderPane();
        root.setCenter(panel);

        root.getStyleClass().add("fondo");

        Scene scene = new Scene(root, anchoPantalla * 0.8, altoPantalla * 0.8);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
}
