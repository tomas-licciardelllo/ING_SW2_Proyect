package clases.gui;
import clases.dao.OrdenDAO;
import clases.dao.empleadoDAO;
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
import javafx.util.StringConverter;

public class GenOrdenScreen {
    public GenOrdenScreen(Stage stage, presupuesto presupuestoAprobado, int l,tarea ta){
        BorderPane root = new BorderPane();
        VBox panelCentral = new VBox(20);
        panelCentral.setPadding(new Insets(25));
        panelCentral.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 15;");
        panelCentral.setAlignment(Pos.TOP_CENTER);
        panelCentral.setMaxWidth(800);
        List<tarea> tareas = new ArrayList<>();

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

        String descPresu = "Tipo: "+presupuestoAprobado.getTipoTrabajo();
        String capasPresu = "Capas: " + presupuestoAprobado.getTipoPintura();
        tarea t1 = new tarea(descPresu);
        tarea t2 = new tarea(capasPresu);
        tareas.add(t1);
        tareas.add(t2);

        for (parte parte : presupuestoAprobado.getRepuestos()) {
            String descripcion = parte.parteRepuesto();
            tarea tareita = new tarea(descripcion);
            tareas.add(tareita);
        }

        Label lblPartesTitulo = new Label("Tareas y Repuestos:");
        lblPartesTitulo.setStyle("-fx-font-weight: bold;");
        ListView<String> listaTareas = new ListView<>();
        for(tarea t : tareas){
            listaTareas.getItems().add(t.getDescripcion());
        }
        listaTareas.setPrefHeight(150);

        Label lblEmpleado =  new Label("Asignar Empleado a Cargo:");
        lblEmpleado.setStyle("-fx-font-weight: bold;");
        ComboBox<empleado> comboEmpleado = new ComboBox<>();
        comboEmpleado.setPromptText("Seleccione un Empleado...");
        comboEmpleado.setPrefWidth(250);
        HBox empleados = new HBox(10, lblEmpleado, comboEmpleado);
        empleados.setAlignment(Pos.CENTER);

        empleadoDAO eDao = new empleadoDAO();
        comboEmpleado.getItems().addAll(eDao.getAll());
        comboEmpleado.getItems().add(new empleado("Juan", 11222333)); //ESTE ES DE PRUEBA Y FUNCIONA
        comboEmpleado.setConverter(new StringConverter<empleado>() {
            @Override public String toString(empleado e) { return e == null ? null : e.getNombre(); }
            @Override public empleado fromString(String s) { return null; }
        });

        Button btnConfirmar = new Button("Confirmar");
        Button btnCancelar = new Button("Cancelar");
        btnConfirmar.setStyle("-fx-cursor: hand; -fx-pref-width: 70px;-fx-min-width: 70px;-fx-max-width: 70px; -fx-pref-height: 40px;");
        btnCancelar.setStyle("-fx-cursor: hand; -fx-pref-width: 70px;-fx-min-width: 70px;-fx-max-width: 70px; -fx-pref-height: 40px;");
        HBox panelBotones = new HBox(15, btnConfirmar, btnCancelar);
        panelBotones.setAlignment(Pos.CENTER);

        btnConfirmar.setOnAction(e -> {
            empleado empleadito = comboEmpleado.getValue();
            if(empleadito == null){
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Atención");
                alerta.setHeaderText("Debe asignar un empleado");
                alerta.setContentText("Por favor, seleccione un empleado a cargo para continuar con la carga...");
                alerta.showAndWait();
                return;
            }

            OrdenDAO ordenDAO = new OrdenDAO();
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

        panelCentral.getChildren().addAll(lblTitulo, grid, empleados, lblPartesTitulo, listaTareas, panelBotones);

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
