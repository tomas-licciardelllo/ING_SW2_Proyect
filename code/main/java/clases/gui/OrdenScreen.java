package clases.gui;

import clases.dao.OrdenDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import clases.model.ordentrabajo;
import clases.model.presupuesto;
import java.time.LocalDate;
import java.util.List;

public class OrdenScreen {
    public OrdenScreen (Stage stage){
        OrdenDAO ordenDAO = new OrdenDAO();
        List<ordentrabajo> listaOrdenes = ordenDAO.getAll();
        ObservableList<ordentrabajo> data = FXCollections.observableArrayList(listaOrdenes);

        TableView<ordentrabajo> tablaOrdenes = new TableView<>(data);


        TableColumn<ordentrabajo, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("Estado"));
        TableColumn<ordentrabajo, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha_inicio"));
        tablaOrdenes.getColumns().addAll(colEstado, colFecha);
        tablaOrdenes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox panelInferior = new HBox();
        panelInferior.setPadding(new Insets(10, 0, 0, 0));
        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(event -> {
            stage.setScene(MainApp.mAppVolver(stage));
        });
        panelInferior.getChildren().addAll(btnVolver);

        VBox panel = new VBox(10, tablaOrdenes);
        panel.setStyle("-fx-padding: 20; -fx-background-color: lightgray;");
        VBox.setVgrow(tablaOrdenes, Priority.ALWAYS);
        panel.getChildren().addAll(panelInferior);
        BorderPane root = new BorderPane();
        root.setCenter(panel);
        root.getStyleClass().add("fondo");

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ChapAPP - Órdenes de Trabajo");
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }
}
