package clases.gui;
import clases.Manager.empleadoManager;
import clases.Manager.ordentrabajoManager;
import clases.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;
import clases.control.generarPDF;
import javafx.util.StringConverter;

public class GenOrdenScreen {
    public GenOrdenScreen(Stage stage, ordentrabajo ordenTrabajo, int l){
        Mensajes mensaje = new Mensajes();
        BorderPane root = new BorderPane();
        VBox panelCentral = new VBox(20);
        panelCentral.getStyleClass().add("fondoSubMenu");
        panelCentral.setPadding(new Insets(25));
        panelCentral.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 15;");
        panelCentral.setAlignment(Pos.TOP_CENTER);
        panelCentral.setMaxWidth(800);
        ordentrabajoManager ordenManager = new ordentrabajoManager();
        List<tarea> tareas = ordenManager.obtenerTareas(ordenTrabajo);

        Label lblTitulo = new Label("ORDEN DE TRABAJO DEL PRESUPUESTO N°" + ordenTrabajo.getPresupuesto().getNumero());
        lblTitulo.getStyleClass().add("titulo");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label lblClienteTitulo = new Label("Cliente:");
        lblClienteTitulo.getStyleClass().add("subtitulo");
        Label lblClienteDato = new Label(ordenTrabajo.getPresupuesto().getCliente().getNombre());
        grid.add(lblClienteTitulo, 0, 0);
        grid.add(lblClienteDato, 1, 0);

        Label lblAutoTitulo = new Label("Vehículo:");
        lblAutoTitulo.getStyleClass().add("subtitulo");
        Label lblAutoDato = new Label(ordenTrabajo.getPresupuesto().getAuto().getMarca() + " " + ordenTrabajo.getPresupuesto().getAuto().getModelo());
        grid.add(lblAutoTitulo, 0, 1);
        grid.add(lblAutoDato, 1, 1);

        Label lblFechaTitulo = new Label("Fecha:");
        lblFechaTitulo.getStyleClass().add("subtitulo");
        Label lblFecha = new Label(ordenTrabajo.getPresupuesto().getFecha().toString());
        grid.add(lblFechaTitulo, 0, 2);
        grid.add(lblFecha, 1, 2);

        Label lblPartesTitulo = new Label("Tareas y Repuestos:");
        lblPartesTitulo.getStyleClass().add("subtitulo");

        empleadoManager empleadoManager = new empleadoManager();
        List<empleado> empleadoResu = empleadoManager.obtenerTodos();

        VBox tareasContenedor = new VBox(10);
        tareasContenedor.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: #cccccc; -fx-border-radius: 5;");
        for(tarea t : tareas){
            Label lblTarea = new Label(t.getDescripcion());
            lblTarea.setMinWidth(Region.USE_PREF_SIZE);
            Region espacio = new Region();
            HBox.setHgrow(espacio, Priority.ALWAYS);
            ComboBox<empleado> empleados = new ComboBox<>();
            empleados.setPromptText("Asigne un Empleado...");
            empleados.getItems().addAll(empleadoResu);

            empleados.setConverter(new StringConverter<empleado>() {
                @Override public String toString(empleado e) { return e == null ? null : e.getNombre(); }
                @Override public empleado fromString(String s) { return null; }
            });

            empleados.valueProperty().addListener((obs, oldVal, newVal) -> {
                t.setEmpleado(newVal);
            });

            HBox tareaRow = new HBox(lblTarea,espacio,empleados);
            tareaRow.setAlignment(Pos.CENTER_LEFT);
            tareasContenedor.getChildren().add(tareaRow);
        }

        ScrollPane scrollPaneTareas = new ScrollPane(tareasContenedor);
        scrollPaneTareas.setFitToWidth(true);
        scrollPaneTareas.setPrefHeight(200);

        Button btnConfirmar = new Button("Confirmar");
        Button btnCancelar = new Button("Cancelar");
        btnConfirmar.getStyleClass().add("botonNormal");
        btnCancelar.getStyleClass().add("botonNormal");
        HBox panelBotones = new HBox(15, btnConfirmar, btnCancelar);
        panelBotones.setAlignment(Pos.CENTER);

        btnConfirmar.setOnAction(e -> {
            boolean auxiliar = true;
            for (tarea t : tareas) {
                if (t.getEmpleado() == null) {
                    auxiliar = false;
                    break;
                }
            }
            if (!auxiliar) {
                mensaje.alertaInformación("ATENCIÓN", "Por favor, asigne a TODAS las tareas un empleado a Cargo", "Faltan Empleados por Asignar");
                return;
            }

            ordenManager.actualizarOrden(ordenTrabajo, 0);
            if(ordenTrabajo.getEstado() == ordentrabajo.Estado.Pendiente){
                    generarPDF aux = new generarPDF();
                    aux.generarpdf(ordenTrabajo);
                    mensaje.alertaInformación("ÉXITO", "La orden de Trabajo fue generada Correctamente", "");
                    stage.setScene(MainApp.mAppVolver(stage));

            }
            else{
                mensaje.alertaError("ERROR", "Hubo un problema al generar la Orden de Trabajo, regresando al Menú...", "");
                stage.setScene(MainApp.mAppVolver(stage));
            }
        });

        btnCancelar.setOnAction(e -> {
            mensaje.alertaInformación("CANCELADA", "La orden de Trabajo fue cancelada Correctamente", "");
            stage.setScene(MainApp.mAppVolver(stage));
        });

        panelCentral.getChildren().addAll(lblTitulo, grid, lblPartesTitulo, scrollPaneTareas, panelBotones);

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
