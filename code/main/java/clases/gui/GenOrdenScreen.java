package clases.gui;
import clases.dao.OrdenDAO;
import clases.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import clases.control.generarPDF;

public class GenOrdenScreen {
    public GenOrdenScreen(Stage stage, presupuesto presupuestoAprobado){
        BorderPane root = new BorderPane();
        VBox panelCentral = new VBox(20);
        panelCentral.setPadding(new Insets(25));
        panelCentral.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 15;");
        panelCentral.setAlignment(Pos.TOP_CENTER);
        panelCentral.setMaxWidth(800);

        Label lblTitulo = new Label("ORDEN DE TRABAJO DEL PRESUPUESTO N°" + presupuestoAprobado.getNumero());
        lblTitulo.setStyle("-fx-font-size: 25pt; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label lblClienteTitulo = new Label("Cliente:");
        lblClienteTitulo.setStyle("-fx-font-weight: bold;");
        Label lblClienteDato = new Label(presupuestoAprobado.getCliente().getNombre());
        grid.add(lblClienteTitulo, 0, 0);
        grid.add(lblClienteDato, 1, 0);

        Label lblAutoTitulo = new Label("Vehículo:");
        lblAutoTitulo.setStyle("-fx-font-weight: bold;");
        Label lblAutoDato = new Label(presupuestoAprobado.getAuto().getMarca() + " " + presupuestoAprobado.getAuto().getModelo());
        grid.add(lblAutoTitulo, 0, 1);
        grid.add(lblAutoDato, 1, 1);

        Label lblFechaTitulo = new Label("Fecha:");
        lblFechaTitulo.setStyle("-fx-font-weight: bold;");
        Label lblFecha = new Label(presupuestoAprobado.getFecha().toString());
        grid.add(lblFechaTitulo, 0, 2);
        grid.add(lblFecha, 1, 2);

        Label lblPartesTitulo = new Label("Repuestos y Tareas:");
        lblPartesTitulo.setStyle("-fx-font-weight: bold;");
        ListView<parte> listaRepuestos = new ListView<>();
        listaRepuestos.getItems().setAll(presupuestoAprobado.getRepuestos());
        listaRepuestos.setPrefHeight(150);

        Button btnConfirmar = new Button("Confirmar");
        Button btnCancelar = new Button("Cancelar");
        btnConfirmar.setStyle("-fx-cursor: hand; -fx-pref-width: 70px;-fx-min-width: 70px;-fx-max-width: 70px; -fx-pref-height: 40px;");
        btnCancelar.setStyle("-fx-cursor: hand; -fx-pref-width: 70px;-fx-min-width: 70px;-fx-max-width: 70px; -fx-pref-height: 40px;");
        HBox panelBotones = new HBox(15, btnConfirmar, btnCancelar);
        panelBotones.setAlignment(Pos.CENTER);

        btnConfirmar.setOnAction(e -> {
            OrdenDAO ordenDAO = new OrdenDAO();
            List<tarea> tareas = new ArrayList<>();
            //Orden de Prueba
            empleado emp =  new empleado("Juan", 11222333);
            tarea t = new tarea("hacer", emp);
            tareas.add(t);
            ordentrabajo nueva = new ordentrabajo(ordentrabajo.Estado.Pendiente, presupuestoAprobado.getFecha(), LocalDate.now(), presupuestoAprobado, tareas);
            if(nueva.getEstado() == ordentrabajo.Estado.Pendiente && !tareas.isEmpty()){
                ordenDAO.create(nueva);
                generarPDF aux = new generarPDF();
                aux.generarpdf(nueva);
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Éxito");
                alerta.setContentText("La orden de trabajo fue generada correctamente.");
                alerta.showAndWait();
                stage.setScene(MainApp.mAppVolver(stage));
            }
            else{
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("ERROR!");
                alerta.setContentText("Hubo un problema al Cargar la Orden de Trabajo. Regresando al Menú...");
                alerta.showAndWait();
                stage.setScene(MainApp.mAppVolver(stage));
            }
        });

        btnCancelar.setOnAction(e -> {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Cancelada!");
            alerta.setContentText("La orden de trabajo fue cancelada con éxito.");
            alerta.showAndWait();
            stage.setScene(MainApp.mAppVolver(stage));
        });

        panelCentral.getChildren().addAll(lblTitulo, grid, lblPartesTitulo, listaRepuestos, panelBotones);

        StackPane fondo = new StackPane(panelCentral);
        fondo.setStyle("-fx-background-color: #AEAEAE;");
        root.setCenter(fondo);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("ChapAPP - Generar Orden de Trabajo");
        stage.show();
    }
}
