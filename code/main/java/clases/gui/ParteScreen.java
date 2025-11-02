package clases.gui;

import clases.Manager.parteManager;
import clases.model.parte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class ParteScreen {

    // Se saca man del constructor para que sea accesible en los métodos
    private parteManager manager;
    private ObservableList<parte> data;
    private TableView<parte> tablaPartes;

    public ParteScreen(Stage stage) {
        manager = new parteManager();
        double anchoPantalla = Screen.getPrimary().getBounds().getWidth();
        double altoPantalla = Screen.getPrimary().getBounds().getHeight();

        // Carga de datos
        List<parte> listaPartes = manager.traerTodas();
        data = FXCollections.observableArrayList(listaPartes);
        FilteredList<parte> filtrodata = new FilteredList<>(data, p -> true);

        // Tabla
        tablaPartes = new TableView<>();
        TableColumn<parte, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));

        tablaPartes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaPartes.getColumns().addAll(colNombre);

        // Enlazar datos filtrados y ordenados a la tabla
        SortedList<parte> sortedData = new SortedList<>(filtrodata);
        sortedData.comparatorProperty().bind(tablaPartes.comparatorProperty());
        tablaPartes.setItems(sortedData);

        // Barra de Búsqueda y Acciones Superiores
        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar parte por nombre...");
        txtBuscar.getStyleClass().add("barraBusqueda");
        txtBuscar.setPrefWidth(600);

        Button btnAgregar = new Button("Agregar Parte");
        btnAgregar.getStyleClass().add("botonNormal");

        Region espacioIzq = new Region();
        HBox.setHgrow(espacioIzq, Priority.ALWAYS);
        Region espacioDer = new Region();
        HBox.setHgrow(espacioDer, Priority.ALWAYS);
        HBox barraBusqueda = new HBox(espacioIzq, txtBuscar, espacioDer, btnAgregar);

        // Lógica de filtro
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            filtrodata.setPredicate(parte -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return parte.getNombre().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Panel Inferior con Acciones de Tabla
        Button btnModificar = new Button("Modificar");
        btnModificar.getStyleClass().add("botonModificar");

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.getStyleClass().add("botonEliminar");

        Button btnVolver = new Button("Volver");
        btnVolver.getStyleClass().add("botonNormal");

        Region spacerInf = new Region();
        HBox.setHgrow(spacerInf, Priority.ALWAYS);
        HBox inferior = new HBox(10, btnModificar, btnEliminar, spacerInf, btnVolver);
        inferior.setPadding(new Insets(10, 0, 0, 0));
        inferior.setAlignment(Pos.CENTER);

        // Contenedor principal de la tabla
        VBox panelTabla = new VBox(10, tablaPartes, inferior);
        panelTabla.setStyle("-fx-padding: 20; -fx-background-color: white;");
        VBox.setVgrow(tablaPartes, Priority.ALWAYS); // Hacer que la tabla crezca

        // Layout Raíz (BorderPane)
        BorderPane root = new BorderPane();
        root.setTop(barraBusqueda);
        root.setCenter(panelTabla);

        // --- ACCIONES DE BOTONES ---

        // Acción Volver
        btnVolver.setOnAction(e -> {
            stage.setScene(MainApp.mAppVolver(stage));
        });

        // Acción Agregar
        btnAgregar.setOnAction(e -> {
            // Llama al formulario en modo "Agregar" (pasando null)
            VBox formulario = crearFormularioParte(stage, root, panelTabla, null);
            root.setCenter(formulario);
        });

        // Acción Modificar
        btnModificar.setOnAction(e -> {
            parte parteSeleccionada = tablaPartes.getSelectionModel().getSelectedItem();
            if (parteSeleccionada == null) {
                mostrarAlertaAux(Alert.AlertType.WARNING, "Atención", "Ninguna parte seleccionada", "Por favor, seleccione una parte de la tabla para modificar.");
                return;
            }
            // Llama al formulario en modo "Modificar" (pasando la parte)
            VBox formulario = crearFormularioParte(stage, root, panelTabla, parteSeleccionada);
            root.setCenter(formulario);
        });

        btnEliminar.setOnAction(e -> {
            parte parteSeleccionada = tablaPartes.getSelectionModel().getSelectedItem();
            if (parteSeleccionada == null) {
                mostrarAlertaAux(Alert.AlertType.WARNING, "Atención", "Ninguna parte seleccionada", "Por favor, seleccione una parte de la tabla para eliminar.");
                return;
            }

            // Diálogo de confirmación
            Alert alertaConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            alertaConfirm.setTitle("Confirmar Eliminación");
            alertaConfirm.setHeaderText("¿Está seguro de que desea eliminar la parte: " + parteSeleccionada.getNombre() + "?");
            alertaConfirm.setContentText("Esta acción no se puede deshacer.");

            Optional<ButtonType> resultado = alertaConfirm.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

                boolean exito = manager.eliminarParte(parteSeleccionada);

                if (exito) {
                    mostrarAlertaAux(Alert.AlertType.INFORMATION, "Éxito", "Parte Eliminada", "La parte se ha eliminado correctamente.");
                    data.remove(parteSeleccionada); // Quitar de la lista observable
                } else {
                    mostrarAlertaAux(Alert.AlertType.ERROR, "Error", "No se pudo eliminar", "Hubo un error al intentar eliminar la parte de la base de datos.");
                }
            }
        });

        // Configuración de la Escena
        root.getStyleClass().add("fondo");
        Scene scene = new Scene(root, anchoPantalla * 0.8, altoPantalla * 0.8);
        try {
            scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("No se pudo cargar la hoja de estilos: " + e.getMessage());
        }
        stage.setScene(scene);
        stage.setTitle("ChapAPP - Gestión de Partes del Vehículo");
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }

    public VBox crearFormularioParte(Stage stage, BorderPane root, VBox pantallaAnt, parte parteExistente) {

        boolean esModoEdicion = (parteExistente != null);

        VBox pantalla = new VBox();
        pantalla.setMinSize(300, 200);
        pantalla.setMaxSize(600, 400);
        pantalla.setSpacing(15);
        pantalla.setAlignment(Pos.CENTER);
        pantalla.setPadding(new Insets(25));
        pantalla.setStyle(
                "-fx-background-color: #FFFFFF; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-radius: 8; " +
                        "-fx-border-color: #CFD8DC; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);"
        );

        Label lblTitulo = new Label(esModoEdicion ? "Modificar Parte" : "Agregar Nueva Parte");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label lblNombre = new Label("Nombre:");
        TextField nombreField = new TextField();
        if (esModoEdicion) {
            nombreField.setText(parteExistente.getNombre());
        }

        Button btnGuardar = new Button(esModoEdicion ? "Guardar Cambios" : "Guardar");
        Button btnCancelar = new Button("Cancelar");
        btnGuardar.setStyle("-fx-background-color: #0277BD; -fx-text-fill: white; -fx-font-weight: bold;");
        btnCancelar.setStyle("-fx-cursor: hand;");

        btnCancelar.setOnAction(e -> {
            root.setCenter(pantallaAnt); // Vuelve a la pantalla de la tabla
        });

        HBox acciones = new HBox(20, btnGuardar, btnCancelar);
        acciones.setAlignment(Pos.CENTER);
        pantalla.getChildren().addAll(lblTitulo, lblNombre, nombreField, acciones);

        btnGuardar.setOnAction(e -> {
            String nombre = nombreField.getText().trim();
            if (nombre.isEmpty()) {
                mostrarAlertaAux(Alert.AlertType.WARNING, "Error", "Campo Vacío", "El nombre de la parte no puede estar vacío.");
                return;
            }

            if (esModoEdicion) {
                // --- LÓGICA DE MODIFICAR ---
                parteExistente.setNombre(nombre);

                // Asumiendo que existe un método 'modificarParte(parte)' en el manager
                boolean exito = manager.modificarParte(parteExistente);

                if (exito) {
                    mostrarAlertaAux(Alert.AlertType.INFORMATION, "Aceptada", "Se actualizó correctamente la parte.", "Éxito");
                    tablaPartes.refresh(); // Refresca la tabla para mostrar el cambio
                    root.setCenter(pantallaAnt); // Vuelve a la tabla
                } else {
                    mostrarAlertaAux(Alert.AlertType.ERROR, "Error", "No se pudo modificar", "Ha habido un error al guardar los cambios.");
                }

            } else {
                // --- LÓGICA DE AGREGAR ---
                parte parteNueva = new parte(nombre, 1, true);

                if (manager.insertarParte(parteNueva)) {
                    mostrarAlertaAux(Alert.AlertType.INFORMATION, "Aceptada", "Se creó correctamente la parte.", "Éxito");
                    data.add(parteNueva); // Añade la nueva parte a la lista observable
                    root.setCenter(pantallaAnt); // Vuelve a la tabla
                } else {
                    mostrarAlertaAux(Alert.AlertType.ERROR, "Error", "No se pudo crear", "Ha habido un error al crear la parte.");
                }
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