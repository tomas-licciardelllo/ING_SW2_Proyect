package clases.gui;

import clases.control.Navegar;
import clases.dao.PresupuestoDAO;
import clases.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class PresupuestoScreen {

    private float costoTotal;
    private cliente clienteSeleccionado;
    // La variable clienteSeleccionado ya no es necesaria a nivel de clase
    // private cliente clienteSeleccionado;

    public PresupuestoScreen(Stage stage) {

        double anchoPantalla = Screen.getPrimary().getBounds().getWidth();
        double altoPantalla = Screen.getPrimary().getBounds().getHeight();
        VBox formulario = new VBox(15);
        formulario.setPadding(new Insets(25));
        formulario.setMinSize(600, 500);
        formulario.setMaxSize(800, 700);
        formulario.setStyle(
                "-fx-background-color: #ECEFF1;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-radius: 20;" +
                        "-fx-border-color: #B0BEC5;" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5);"
        );
        formulario.setAlignment(Pos.TOP_CENTER);

        // Fecha
        Label lblFecha = new Label("Fecha: " + LocalDate.now());
        lblFecha.setStyle("-fx-font-size: 14px; -fx-text-fill: #546E7A;");
        HBox fechaBox = new HBox(lblFecha);
        fechaBox.setAlignment(Pos.CENTER_RIGHT);
        fechaBox.setPadding(new Insets(0, 10, 10, 0));

        // --- Sección Auto ---
        GridPane autoGrid = new GridPane();
        autoGrid.setHgap(20);
        autoGrid.setVgap(10);
        autoGrid.setAlignment(Pos.CENTER);

        autoGrid.add(new Label("Tipo:"), 0, 0);
        TextField txtTipoAuto = new TextField();
        txtTipoAuto.setPromptText("Ej: Auto, Camioneta");
        autoGrid.add(txtTipoAuto, 1, 0);

        autoGrid.add(new Label("Marca:"), 2, 0);
        TextField txtMarcaAuto = new TextField();
        txtMarcaAuto.setPromptText("Ej: Audi");
        autoGrid.add(txtMarcaAuto, 3, 0);

        autoGrid.add(new Label("Modelo:"), 0, 1);
        TextField txtModeloAuto = new TextField();
        txtModeloAuto.setPromptText("Ej: A4");
        autoGrid.add(txtModeloAuto, 1, 1);

        autoGrid.add(new Label("Año:"), 2, 1);
        TextField txtAnioAuto = new TextField();
        txtAnioAuto.setPromptText("Ej: 2024");
        autoGrid.add(txtAnioAuto, 3, 1);

        autoGrid.add(new Label("Patente:"), 0, 2);
        TextField txtPatenteAuto = new TextField();
        txtPatenteAuto.setPromptText("Ej: AA123BB");
        autoGrid.add(txtPatenteAuto, 1, 2);

        TitledPane vehiculoPane = new TitledPane("Datos del Vehículo", autoGrid);
        vehiculoPane.setCollapsible(false);

        // Sección Repuestos
        TitledPane repuestosPane = new TitledPane("Partes y Reparaciones", new VBox());
        repuestosPane.setCollapsible(false);
        VBox repuestosContent = new VBox(10);
        repuestosContent.setPadding(new Insets(10));

        ComboBox<String> partes = new ComboBox<>();
        partes.getItems().addAll("Puerta", "Espejo", "Retrovisor", "Paragolpes", "Capó");
        partes.setPromptText("Seleccionar Parte");
        TextField txtPanosPintura = new TextField();
        txtPanosPintura.setPromptText("Paños");
        txtPanosPintura.setPrefWidth(80);
        CheckBox chkCambio = new CheckBox("Cambio");
        Button btnAgregar = new Button("Agregar");
        HBox repuestosInputBox = new HBox(10, partes, txtPanosPintura, chkCambio, btnAgregar);
        repuestosInputBox.setAlignment(Pos.CENTER_LEFT);

        ObservableList<parte> listaPartes = FXCollections.observableArrayList();
        ListView<parte> listaView = new ListView<>(listaPartes);
        listaView.setPrefHeight(250);
        Button btnEliminar = new Button("Eliminar Seleccionado");
        VBox listaBox = new VBox(5, listaView, btnEliminar);
        listaBox.setAlignment(Pos.CENTER);

        listaView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(parte item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getNombre() == null) {
                    setText(null);
                } else {
                    String tipo = item.isCambio() ? "Cambio" : "Reparación";
                    setText(String.format("%s - Paños: %.1f (%s)", item.getNombre(), item.getPanioPintura(), tipo));
                }
            }
        });


        repuestosContent.getChildren().addAll(repuestosInputBox, listaBox);
        repuestosPane.setContent(repuestosContent);


        // Sección Detalles del Trabajo
        GridPane detallesGrid = new GridPane();
        detallesGrid.setHgap(20);
        detallesGrid.setVgap(10);
        detallesGrid.setAlignment(Pos.CENTER);

        detallesGrid.add(new Label("Tipo de Trabajo:"), 0, 0);
        TextField txtTipoTrabajo = new TextField();
        txtTipoTrabajo.setPromptText("Ej: Chapa y pintura");
        detallesGrid.add(txtTipoTrabajo, 1, 0);
        detallesGrid.add(new Label("Tipo de Pintura:"), 2, 0);
        ComboBox<String> cmbTipoPintura = new ComboBox<>();
        cmbTipoPintura.getItems().addAll("Bicapa", "Tricapa");
        cmbTipoPintura.setPromptText("Seleccionar");
        detallesGrid.add(cmbTipoPintura, 3, 0);
        detallesGrid.add(new Label("Días de Chapa:"), 0, 1);
        TextField txtDiasTrabajo = new TextField();
        detallesGrid.add(txtDiasTrabajo, 1, 1);
        detallesGrid.add(new Label("Precio dia de chapa:"), 2, 1);
        TextField txtCostoDia = new TextField();
        detallesGrid.add(txtCostoDia, 3, 1);
        detallesGrid.add(new Label("Precio por Paño:"), 0, 2);
        TextField txtPrecioPano = new TextField();
        detallesGrid.add(txtPrecioPano, 1, 2);
        detallesGrid.add(new Label("Total Paños:"), 2, 2);
        TextField txtTotalPanos = new TextField("0.0");
        txtTotalPanos.setEditable(false);
        detallesGrid.add(txtTotalPanos, 3, 2);

        TitledPane detallesPane = new TitledPane("Detalles del Trabajo", detallesGrid);
        detallesPane.setCollapsible(false);
        //SELECT CLIENTE
        Label lblClienteInfo = new Label("Ningún cliente seleccionado");
        lblClienteInfo.setStyle("-fx-font-style: italic; -fx-text-fill: #546E7A;");
        Button btnAsignarCliente = new Button("Asignar Cliente...");
        HBox clienteBox = new HBox(10, new Label("Cliente:"), lblClienteInfo, btnAsignarCliente);
        clienteBox.setAlignment(Pos.CENTER_LEFT);

        TitledPane clientePane = new TitledPane("Datos del Cliente", clienteBox);
        clientePane.setCollapsible(false);

        btnAsignarCliente.setOnAction(e -> {
            SeleccionCliente dialog = new SeleccionCliente();
            Optional<cliente> resultado = dialog.showAndWait();

            resultado.ifPresent(cliente -> {
                this.clienteSeleccionado = cliente; // Guardamos el cliente seleccionado
                lblClienteInfo.setText(cliente.getNombre()); // Actualizamos la etiqueta
                lblClienteInfo.setStyle("-fx-font-weight: bold; -fx-text-fill: #000;");
            });
        });

        // Sección Costo Total
        Label lblCostoTotal = new Label("COSTO TOTAL:");
        lblCostoTotal.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        TextField txtCostoTotal = new TextField("0.00");
        txtCostoTotal.setEditable(false);
        txtCostoTotal.setPrefWidth(150);
        txtCostoTotal.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-alignment: center-right;");
        HBox costoBox = new HBox(10, lblCostoTotal, txtCostoTotal);
        costoBox.setAlignment(Pos.CENTER_RIGHT);
        costoBox.setPadding(new Insets(10, 0, 20, 0));

        // Sección Acciones
        Button btnGuardar = new Button("Guardar Presupuesto");
        Button btnLimpiar = new Button("Limpiar Formulario");
        Button btnAtras = new Button("Atrás");
        HBox accionesBox = new HBox(20, btnGuardar, btnLimpiar);
        accionesBox.setAlignment(Pos.CENTER);

        // Lógica de Listeners
        listaPartes.addListener((javafx.collections.ListChangeListener<parte>) c -> calcularYActualizarTotal(listaPartes, txtCostoTotal, txtCostoDia, txtDiasTrabajo, txtPrecioPano, txtTotalPanos));
        txtCostoDia.textProperty().addListener((obs, oldVal, newVal) -> calcularYActualizarTotal(listaPartes, txtCostoTotal, txtCostoDia, txtDiasTrabajo, txtPrecioPano, txtTotalPanos));
        txtDiasTrabajo.textProperty().addListener((obs, oldVal, newVal) -> calcularYActualizarTotal(listaPartes, txtCostoTotal, txtCostoDia, txtDiasTrabajo, txtPrecioPano, txtTotalPanos));
        txtPrecioPano.textProperty().addListener((obs, oldVal, newVal) -> calcularYActualizarTotal(listaPartes, txtCostoTotal, txtCostoDia, txtDiasTrabajo, txtPrecioPano, txtTotalPanos));

        btnAgregar.setOnAction(e -> {
            String nombre = partes.getSelectionModel().getSelectedItem();
            if (nombre == null || nombre.isEmpty()) {
                mostrarAlertaAux(Alert.AlertType.WARNING, "Atención", "Error de Selección", "Debe seleccionar una Parte.");
                return;
            }
            try {
                float panos = Float.parseFloat(txtPanosPintura.getText().trim());
                if (panos <= 0) {
                    mostrarAlertaAux(Alert.AlertType.WARNING, "Atención", "Valor Inválido", "La cantidad de paños debe ser un número positivo.");
                    return;
                }
                parte aux = new parte(nombre, panos, chkCambio.isSelected());
                listaPartes.add(aux);
                partes.getSelectionModel().clearSelection();
                txtPanosPintura.clear();
                chkCambio.setSelected(false);
            } catch (NumberFormatException ex) {
                mostrarAlertaAux(Alert.AlertType.WARNING, "Atención", "Formato Inválido", "Ingrese un número válido para los paños.");
            }
        });

        btnEliminar.setOnAction(e -> {
            parte parteSeleccionada = listaView.getSelectionModel().getSelectedItem();
            if (parteSeleccionada != null) {
                listaPartes.remove(parteSeleccionada);
            } else {
                mostrarAlertaAux(Alert.AlertType.WARNING, "Atención", "Eliminar Parte", "Seleccione una parte de la lista para eliminar.");
            }
        });

        btnLimpiar.setOnAction(e -> {
            txtCostoTotal.setText("0.00");
            txtCostoDia.clear();
            txtDiasTrabajo.clear();
            txtPanosPintura.clear();
            chkCambio.setSelected(false);
            listaPartes.clear();
            txtTipoAuto.clear();
            txtMarcaAuto.clear();
            txtModeloAuto.clear();
            txtAnioAuto.clear();
            txtPatenteAuto.clear();
            txtTipoTrabajo.clear();
            txtPrecioPano.clear();
            cmbTipoPintura.getSelectionModel().clearSelection();
        });

        btnGuardar.setOnAction(e->{
            if (listaPartes.isEmpty() || txtTipoTrabajo.getText().isEmpty() || cmbTipoPintura.getValue() == null || txtDiasTrabajo.getText().isEmpty()
                    || txtTipoAuto.getText().isEmpty() || txtMarcaAuto.getText().isEmpty()
                    || txtModeloAuto.getText().isEmpty() || txtAnioAuto.getText().isEmpty() || txtPatenteAuto.getText().isEmpty()) {
                mostrarAlertaAux(Alert.AlertType.ERROR, "Error", "Campos Faltantes", "Complete todos los campos del vehículo y del presupuesto.");
                return;
            }

            try {
                float costoFinal = Float.parseFloat(txtCostoTotal.getText());

                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Confirmar Presupuesto");
                alerta.setHeaderText("Precio Final: $" + String.format("%.2f", costoFinal));
                alerta.setContentText("¿El cliente acepta el presupuesto?");
                Optional<ButtonType> resultado = alerta.showAndWait();

                if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                    // Creamos un cliente por defecto aquí, ya que no hay interfaz para seleccionarlo
                    cliente clienteDePrueba = new cliente("Cliente Genérico", "00000000", new ArrayList<>(), new ArrayList<>());
                    if (clienteSeleccionado != null) {
                        clienteDePrueba = clienteSeleccionado;
                    }
                    auto a = new auto(txtTipoAuto.getText(), txtPatenteAuto.getText(), Integer.parseInt(txtAnioAuto.getText()), txtMarcaAuto.getText(), txtModeloAuto.getText());
                    pago p = new pago(0);
                    presupuesto presu = new presupuesto(0, LocalDate.now(), new ArrayList<>(listaPartes), txtTipoTrabajo.getText(), cmbTipoPintura.getValue(), Integer.parseInt(txtDiasTrabajo.getText()), costoFinal, clienteDePrueba, a, p);

                    PresupuestoDAO pdao = new PresupuestoDAO();
                    pdao.create(presu);

                    new GenOrdenScreen(stage, presu);
                } else {
                    stage.setScene(MainApp.mAppVolver(stage));
                }
            } catch (NumberFormatException ex) {
                mostrarAlertaAux(Alert.AlertType.ERROR, "Error", "Datos Inválidos", "Revise que los campos numéricos (año, días, costo) sean correctos.");
            }
        });

        btnAtras.setOnAction(e ->{
            Scene volver = MainApp.mAppVolver(stage);
            Navegar.volver(stage, volver, "ChapAPP");
        });

        // --- ENSAMBLADO FINAL DE LA PANTALLA ---
        formulario.getChildren().addAll(
                fechaBox,
                new Separator(),
                clientePane,
                vehiculoPane,
                repuestosPane,
                detallesPane,
                new Separator(),
                costoBox,
                accionesBox
        );

        // Configuración de la Escena
        BorderPane root = new BorderPane();
        root.setCenter(formulario);
        BorderPane.setMargin(formulario, new Insets(20));

        HBox bottomBar = new HBox(btnAtras);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.setPadding(new Insets(10));
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, anchoPantalla * 0.8, altoPantalla * 0.8);
        stage.setScene(scene);
        stage.setTitle("Gestión de Presupuestos");
        stage.show();
    }

    // Métodos auxiliares
    private void calcularYActualizarTotal(ObservableList<parte> listaPartes, TextField txtTotal, TextField txtCostoChapa, TextField txtDias, TextField txtPrecioPano, TextField txtTotalPanos) {
        float totalPanos = 0.0f;
        float precioPorPano = 0.0f;
        float costoManoObra = 0.0f;
        float diasChapa = 0.0f;

        for (parte p : listaPartes) {
            totalPanos += p.getPanioPintura();
        }

        try {
            if (!txtPrecioPano.getText().trim().isEmpty()) {
                precioPorPano = Float.parseFloat(txtPrecioPano.getText().trim());
            }
            if (!txtCostoChapa.getText().trim().isEmpty()) {
                costoManoObra = Float.parseFloat(txtCostoChapa.getText().trim());
            }
            if (!txtDias.getText().trim().isEmpty()) {
                diasChapa = Float.parseFloat(txtDias.getText().trim());
            }

            float costoTotalPanos = totalPanos * precioPorPano;
            float costoChapa = costoManoObra * diasChapa;

            costoTotal = costoTotalPanos + costoChapa;

            // --- ACTUALIZAR CAMPOS DE TEXTO ---
            txtTotal.setText(Float.toString(costoTotal));
            txtTotalPanos.setText(Float.toString(totalPanos)); // Muestra el total de paños
            //txtTotal.setText(String.format("%.2f", costoTotal));
            //txtTotalPanos.setText(String.format("%.1f", totalPanos));

        } catch (NumberFormatException ex) {
            txtTotal.setText("Error");
        }
    }

    private void mostrarAlertaAux(Alert.AlertType tipo, String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

}