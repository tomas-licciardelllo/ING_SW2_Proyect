package clases.gui;

import clases.Manager.clienteManager;
import clases.model.cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeleccionCliente extends Dialog<cliente> {

    private ObservableList<cliente> data;
    private clienteManager clienteMan = new clienteManager();

    public SeleccionCliente() {
        // --- Configuración básica del Diálogo ---
        setTitle("Seleccionar Cliente");
        initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal
        initStyle(StageStyle.UTILITY);

        // --- Cargar datos ---
        List<cliente> listaClientes = clienteMan.obtenerTodosID();
        data = FXCollections.observableArrayList(listaClientes);
        FilteredList<cliente> filtroData = new FilteredList<>(data, p -> true);

        // --- UI del Diálogo (muy similar a tu ClienteScreen) ---
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 400);

        // Barra de búsqueda
        TextField txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar cliente por nombre...");
        HBox barraBusqueda = new HBox(txtBuscar);
        barraBusqueda.setPadding(new Insets(10));
        root.setTop(barraBusqueda);

        // Tabla de clientes
        TableView<cliente> tablaClientes = new TableView<>();
        tablaClientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idBD"));
        TableColumn<cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre")); // Usar "nombre" en minúscula
        TableColumn<cliente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono")); // Usar "telefono" en minúscula
        tablaClientes.getColumns().addAll(colId, colNombre, colTelefono);

        // Lógica de filtro
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> {
            filtroData.setPredicate(cliente -> {
                if (newValue == null || newValue.isEmpty()) return true;
                return cliente.getNombre().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        SortedList<cliente> sortedData = new SortedList<>(filtroData);
        sortedData.comparatorProperty().bind(tablaClientes.comparatorProperty());
        tablaClientes.setItems(sortedData);

        root.setCenter(tablaClientes);

        // --- Botones del Diálogo ---
        ButtonType seleccionarButtonType = new ButtonType("Seleccionar", ButtonBar.ButtonData.OK_DONE);
        ButtonType crearButtonType = new ButtonType("Crear Nuevo", ButtonBar.ButtonData.OTHER);
        getDialogPane().getButtonTypes().addAll(seleccionarButtonType, crearButtonType, ButtonType.CANCEL);

        // Deshabilitar "Seleccionar" si no hay nada seleccionado en la tabla
        getDialogPane().lookupButton(seleccionarButtonType).disableProperty().bind(
                tablaClientes.getSelectionModel().selectedItemProperty().isNull()
        );

        // Lógica para el botón "Crear Nuevo"
        Button btnCrear = (Button) getDialogPane().lookupButton(crearButtonType);
        btnCrear.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            event.consume(); // Evita que el diálogo se cierre al presionar "Crear"
            crearNuevoCliente(); // Llama al método para abrir el formulario de creación
        });

        getDialogPane().setContent(root);

        // --- Conversor de Resultado ---
        // Esto define QUÉ devuelve el diálogo cuando se cierra
        this.setResultConverter(dialogButton -> {
            if (dialogButton == seleccionarButtonType) {
                return tablaClientes.getSelectionModel().getSelectedItem();
            }
            return null; // Si se presiona Cancelar u otra cosa, devuelve null
        });
    }

    /**
     * Muestra un pequeño diálogo para crear un nuevo cliente y lo agrega a la tabla.
     */
    private void crearNuevoCliente() {
        Dialog<cliente> dialog = new Dialog<>();
        dialog.setTitle("Crear Nuevo Cliente");
        dialog.setHeaderText("Ingrese los datos del nuevo cliente.");

        ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre y Apellido");
        TextField txtTelefono = new TextField();
        txtTelefono.setPromptText("Teléfono");

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Teléfono:"), 0, 1);
        grid.add(txtTelefono, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convierte el resultado a un objeto cliente al presionar "Guardar"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == guardarButtonType) {
                if (txtNombre.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()) {
                    // Aquí podrías mostrar una alerta de error
                    return null;
                }
                return new cliente(txtNombre.getText(), txtTelefono.getText(), new ArrayList<>(), new ArrayList<>());
            }
            return null;
        });

        Optional<cliente> resultado = dialog.showAndWait();

        resultado.ifPresent(nuevoCliente -> {
            clienteMan.crearCliente(nuevoCliente);
            data.add(nuevoCliente); // Agregar a la lista observable para que aparezca en la tabla
        });
    }
}