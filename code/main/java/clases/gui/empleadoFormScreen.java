package clases.gui;

import clases.Manager.empleadoManager;
import clases.model.empleado;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Pantalla de formulario para Agregar o Modificar un Empleado.
 */
public class empleadoFormScreen {

    private empleado empleadoEnEdicion;
    private long dniOriginal = -1; // Para guardar el DNI original en modo "Modificar"

    public empleadoFormScreen(Stage stage, empleadoManager manager, empleado emp) {
        this.empleadoEnEdicion = emp;
        if (emp != null) {
            this.dniOriginal = emp.getDocumento(); // Guardamos el DNI original
        }

        BorderPane root = new BorderPane();
        root.getStyleClass().add("contenedores");

        // --- Título ---
        Label lblTitulo = new Label(emp == null ? "Agregar Nuevo Empleado" : "Modificar Empleado");
        lblTitulo.getStyleClass().add("titulo"); // Asume un estilo .titulo o cámbialo
        HBox topBox = new HBox(lblTitulo);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20));
        root.setTop(topBox);

        // --- Formulario Central ---
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);
        grid.setMaxWidth(500);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre y Apellido");
        txtNombre.setPrefWidth(300);

        TextField txtDNI = new TextField();
        txtDNI.setPromptText("DNI (sin puntos)");

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("DNI:"), 0, 1);
        grid.add(txtDNI, 1, 1);

        // Si estamos en modo "Modificar", rellenamos los campos
        if (emp != null) {
            txtNombre.setText(emp.getNombre());
            txtDNI.setText(String.valueOf(emp.getDocumento()));
        }

        root.setCenter(grid);

        // --- Botones Inferiores ---
        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");
        btnGuardar.getStyleClass().add("botonNormal");
        btnCancelar.getStyleClass().add("botonEliminar"); // O "botonNormal"

        Region espacioForm = new Region();
        HBox.setHgrow(espacioForm, Priority.ALWAYS);
        HBox botonesForm = new HBox(espacioForm, btnGuardar, btnCancelar);
        botonesForm.setSpacing(10);
        botonesForm.setPadding(new Insets(10, 10, 10, 10));
        root.setBottom(botonesForm);

        // --- Acciones de Botones ---
        btnGuardar.setOnAction(e -> {
            String nombre = txtNombre.getText();
            String dniStr = txtDNI.getText();

            if (nombre == null || nombre.trim().isEmpty() || dniStr == null || dniStr.trim().isEmpty()) {
                mostrarAlerta("Error", "Ambos campos (Nombre y DNI) son obligatorios.", Alert.AlertType.ERROR);
                return;
            }

            try {
                long dni = Long.parseLong(dniStr);

                if (empleadoEnEdicion == null) {
                    // MODO AGREGAR
                    empleado nuevoEmpleado = new empleado(nombre, dni);
                    manager.agregarEmpleado(nuevoEmpleado);
                } else {
                    // MODO MODIFICAR
                    empleadoEnEdicion.setNombre(nombre);
                    empleadoEnEdicion.setDocumento(dni);
                    manager.modificarEmpleado(dniOriginal, empleadoEnEdicion);
                }

                // Si todo sale bien, volvemos a la pantalla de la tabla
                new EmpleadoScreen(stage);

            } catch (NumberFormatException ex) {
                mostrarAlerta("Error de formato", "El DNI debe ser un valor numérico.", Alert.AlertType.ERROR);
            } catch (Exception ex) {
                mostrarAlerta("Error en la base de datos", "No se pudo guardar el empleado. Verifique que el DNI no esté duplicado.", Alert.AlertType.ERROR);
            }
        });

        btnCancelar.setOnAction(e -> {
            // Simplemente volvemos a la pantalla de la tabla
            new EmpleadoScreen(stage);
        });

        // --- Configuración de la Escena ---
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle(emp == null ? "ChapAPP - Agregar Empleado" : "ChapAPP - Modificar Empleado");

        // Copiamos la configuración de maximizado de EmpleadoScreen
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}