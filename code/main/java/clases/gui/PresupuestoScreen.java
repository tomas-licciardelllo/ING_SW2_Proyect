package clases.gui;

import clases.Manager.autoManager;
import clases.Manager.ordentrabajoManager;
import clases.Manager.presupuestoManager;
import clases.control.Navegar;
import clases.Manager.parteManager;


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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PresupuestoScreen {

    private float costoTotal;
    private cliente clienteSeleccionado;
    private auto autoSeleccionado;
    // La variable clienteSeleccionado ya no es necesaria a nivel de clase
    // private cliente clienteSeleccionado;

    public PresupuestoScreen(Stage stage) {

        double anchoPantalla = Screen.getPrimary().getBounds().getWidth();
        double altoPantalla = Screen.getPrimary().getBounds().getHeight();

        // Estilo para los paneles, simulando "tarjetas"
        String cardStyle = "-fx-background-color: #FFFFFF; " +
                "-fx-background-radius: 8; " +
                "-fx-border-radius: 8; " +
                "-fx-border-color: #CFD8DC; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);";

        // --- Layout Principal ---
        GridPane mainGrid = new GridPane();
        mainGrid.setPadding(new Insets(25));
        mainGrid.setHgap(20);
        mainGrid.setVgap(20);

        // Definir 2 columnas de igual tamaño
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        mainGrid.getColumnConstraints().addAll(col1, col2);


        // Fecha
        Label lblFecha = new Label("Fecha: " + LocalDate.now());
        lblFecha.setStyle("-fx-font-size: 14px; -fx-text-fill: #546E7A; -fx-font-weight: bold;");
        HBox fechaBox = new HBox(lblFecha);
        fechaBox.setAlignment(Pos.CENTER_RIGHT);
        fechaBox.setPadding(new Insets(0, 10, 0, 0));

        // --- Sección Cliente ---
        Label lblClienteInfo = new Label("Ningún cliente seleccionado");
        lblClienteInfo.setStyle("-fx-font-style: italic; -fx-text-fill: #546E7A;");
        Button btnAsignarCliente = new Button("Asignar Cliente...");
        btnAsignarCliente.setStyle("-fx-background-color: #546E7A; -fx-text-fill: white; -fx-font-weight: bold;");
        HBox clienteBox = new HBox(10, new Label("Cliente:"), lblClienteInfo, btnAsignarCliente);
        clienteBox.setAlignment(Pos.CENTER_LEFT);
        clienteBox.setPadding(new Insets(10));

        TitledPane clientePane = new TitledPane("1. Datos del Cliente", clienteBox);
        clientePane.setCollapsible(false);
        clientePane.setStyle(cardStyle);

        // --- Sección Auto ---
        GridPane autoGrid = new GridPane();
        autoGrid.setHgap(20);
        autoGrid.setVgap(10);
        autoGrid.setAlignment(Pos.CENTER_LEFT);
        autoGrid.setPadding(new Insets(10, 0, 0, 0)); // Padding interno

        ComboBox<auto> cmbAutosCliente = new ComboBox<>();
        cmbAutosCliente.setPromptText("Seleccionar vehículo");
        cmbAutosCliente.setPrefWidth(200);
        Button btnNuevoVehiculo = new Button("Ingresar Vehículo Nuevo");
        btnNuevoVehiculo.setStyle("-fx-background-color: -fx-azul; -fx-text-fill: white;");


        autoGrid.add(new Label("Vehículo del Cliente:"), 0, 0);
        autoGrid.add(cmbAutosCliente, 1, 0, 2, 1);
        autoGrid.add(btnNuevoVehiculo, 3, 0);

        autoGrid.add(new Label("Tipo:"), 0, 1);
        TextField txtTipoAuto = new TextField();
        autoGrid.add(txtTipoAuto, 1, 1);
        autoGrid.add(new Label("Marca:"), 2, 1);
        TextField txtMarcaAuto = new TextField();
        autoGrid.add(txtMarcaAuto, 3, 1);
        autoGrid.add(new Label("Modelo:"), 0, 2);
        TextField txtModeloAuto = new TextField();
        autoGrid.add(txtModeloAuto, 1, 2);
        autoGrid.add(new Label("Año:"), 2, 2);
        TextField txtAnioAuto = new TextField();
        autoGrid.add(txtAnioAuto, 3, 2);
        autoGrid.add(new Label("Patente:"), 0, 3);
        TextField txtPatenteAuto = new TextField();
        autoGrid.add(txtPatenteAuto, 1, 3);

        // Crecimiento horizontal para los campos
        GridPane.setHgrow(txtTipoAuto, Priority.ALWAYS);
        GridPane.setHgrow(txtMarcaAuto, Priority.ALWAYS);
        GridPane.setHgrow(txtModeloAuto, Priority.ALWAYS);
        GridPane.setHgrow(txtAnioAuto, Priority.ALWAYS);
        GridPane.setHgrow(txtPatenteAuto, Priority.ALWAYS);

        TitledPane vehiculoPane = new TitledPane("2. Datos del Vehículo", autoGrid);
        vehiculoPane.setCollapsible(false);
        vehiculoPane.setStyle(cardStyle);

        // --- Sección Detalles del Trabajo ---
        GridPane detallesGrid = new GridPane();
        detallesGrid.setHgap(20);
        detallesGrid.setVgap(10);
        detallesGrid.setAlignment(Pos.CENTER_LEFT);
        detallesGrid.setPadding(new Insets(10, 0, 0, 0));

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

        // Crecimiento horizontal
        GridPane.setHgrow(txtTipoTrabajo, Priority.ALWAYS);
        GridPane.setHgrow(cmbTipoPintura, Priority.ALWAYS);
        GridPane.setHgrow(txtDiasTrabajo, Priority.ALWAYS);
        GridPane.setHgrow(txtCostoDia, Priority.ALWAYS);
        GridPane.setHgrow(txtPrecioPano, Priority.ALWAYS);
        GridPane.setHgrow(txtTotalPanos, Priority.ALWAYS);
        cmbTipoPintura.setMaxWidth(Double.MAX_VALUE);

        TitledPane detallesPane = new TitledPane("3. Detalles del Trabajo", detallesGrid);
        detallesPane.setCollapsible(false);
        detallesPane.setStyle(cardStyle);

        // --- Sección Repuestos ---
        VBox repuestosContent = new VBox(10);
        repuestosContent.setPadding(new Insets(10));

        ComboBox<String> partes = new ComboBox<>();
        parteManager pManager = new parteManager();
        try {
            // Obtiene todas las partes de todos los presupuestos
            List<String> nombresDePartes = pManager.traerTodas().stream()
                    .map(parte::getNombre) // Extrae solo el nombre
                    .distinct()            // Obtiene nombres únicos
                    .sorted()              // Ordena alfabéticamente
                    .collect(Collectors.toList());

            partes.getItems().addAll(nombresDePartes);

        } catch (Exception ex) {
            // Manejar error si no se pueden cargar las partes
            System.err.println("Error al cargar partes desde la BD: " + ex.getMessage());
        }

        partes.setPromptText("Seleccionar Parte");
        partes.setMaxWidth(Double.MAX_VALUE);

        Button btnNuevaParte = new Button("+");
        btnNuevaParte.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        btnNuevaParte.setTooltip(new Tooltip("Agregar una parte nueva a la lista"));

        TextField txtPanosPintura = new TextField();
        txtPanosPintura.setPromptText("Paños");
        txtPanosPintura.setPrefWidth(80);

        CheckBox chkCambio = new CheckBox("Cambio");
        Button btnAgregar = new Button("Agregar");
        btnAgregar.setStyle("-fx-background-color: #43A047; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox repuestosInputBox = new HBox(10, partes, btnNuevaParte, txtPanosPintura, chkCambio, btnAgregar);

        repuestosInputBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(partes, Priority.ALWAYS);

        ObservableList<parte> listaPartes = FXCollections.observableArrayList();
        ListView<parte> listaView = new ListView<>(listaPartes);

        Button btnEliminar = new Button("Eliminar Seleccionado");
        btnEliminar.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-font-weight: bold;");
        btnEliminar.setMaxWidth(Double.MAX_VALUE); // Para que ocupe todo el ancho

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

        // Añadimos los 3 componentes directamente al VBox principal del panel
        repuestosContent.getChildren().addAll(repuestosInputBox, listaView, btnEliminar);

        // Le decimos SOLO a la lista que crezca verticalmente
        VBox.setVgrow(listaView, Priority.ALWAYS);


        TitledPane repuestosPane = new TitledPane("4. Partes y Reparaciones", repuestosContent);
        repuestosPane.setCollapsible(false);
        repuestosPane.setStyle(cardStyle);
        // Permitir que este panel crezca verticalmente
        GridPane.setVgrow(repuestosPane, Priority.ALWAYS);


        // --- Lógica de Clientes y Autos ---
        btnAsignarCliente.setOnAction(e -> {
            SeleccionCliente dialog = new SeleccionCliente();
            Optional<cliente> resultado = dialog.showAndWait();

            resultado.ifPresent(cliente -> {
                this.clienteSeleccionado = cliente;
                lblClienteInfo.setText(cliente.getNombre());
                lblClienteInfo.setStyle("-fx-font-weight: bold; -fx-text-fill: #000;");

                // Cargar los autos del cliente seleccionado en el ComboBox
                autoManager manager = new autoManager();
                List<auto> autosDelCliente = manager.traerAutos(cliente.getIdBD());
                cmbAutosCliente.setItems(FXCollections.observableArrayList(autosDelCliente));

                // Limpiar la selección anterior del auto
                btnNuevoVehiculo.fire();
            });
        });

        cmbAutosCliente.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                this.autoSeleccionado = newVal;
                // Rellenar y deshabilitar los campos
                configurarCamposAuto(newVal, txtTipoAuto, txtMarcaAuto, txtModeloAuto, txtAnioAuto, txtPatenteAuto, false);
            }
        });

        // Acción para el botón "Nuevo Vehículo"
        btnNuevoVehiculo.setOnAction(e -> {
            this.autoSeleccionado = null;
            cmbAutosCliente.getSelectionModel().clearSelection();
            // Limpiar y habilitar los campos
            configurarCamposAuto(null, txtTipoAuto, txtMarcaAuto, txtModeloAuto, txtAnioAuto, txtPatenteAuto, true);
        });


        // --- Sección Costo Total ---
        Label lblCostoTotal = new Label("COSTO TOTAL:");
        lblCostoTotal.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #37474F;");
        TextField txtCostoTotal = new TextField("0.00");
        txtCostoTotal.setEditable(false);
        txtCostoTotal.setPrefWidth(200);
        txtCostoTotal.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-alignment: center-right; -fx-text-fill: #004D40;");
        HBox costoBox = new HBox(10, lblCostoTotal, txtCostoTotal);
        costoBox.setAlignment(Pos.CENTER_RIGHT);
        costoBox.setPadding(new Insets(10, 10, 10, 0));

        // --- Sección Acciones ---
        Button btnGuardar = new Button("Guardar Presupuesto");
        btnGuardar.getStyleClass().add("botonNormal");
        Button btnLimpiar = new Button("Limpiar Formulario");
        btnLimpiar.getStyleClass().add("botonEliminar");
        HBox accionesBox = new HBox(20, btnGuardar, btnLimpiar);
        accionesBox.setAlignment(Pos.CENTER);

        // --- Botón Atrás ---
        Button btnAtras = new Button("Volver");
        btnAtras.getStyleClass().add("botonNormal");


        // --- Lógica de Listeners ---
        listaPartes.addListener((javafx.collections.ListChangeListener<parte>) c -> calcularYActualizarTotal(listaPartes, txtCostoTotal, txtCostoDia, txtDiasTrabajo, txtPrecioPano, txtTotalPanos));
        txtCostoDia.textProperty().addListener((obs, oldVal, newVal) -> calcularYActualizarTotal(listaPartes, txtCostoTotal, txtCostoDia, txtDiasTrabajo, txtPrecioPano, txtTotalPanos));
        txtDiasTrabajo.textProperty().addListener((obs, oldVal, newVal) -> calcularYActualizarTotal(listaPartes, txtCostoTotal, txtCostoDia, txtDiasTrabajo, txtPrecioPano, txtTotalPanos));
        txtPrecioPano.textProperty().addListener((obs, oldVal, newVal) -> calcularYActualizarTotal(listaPartes, txtCostoTotal, txtCostoDia, txtDiasTrabajo, txtPrecioPano, txtTotalPanos));

        btnNuevaParte.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nueva Parte");
            dialog.setHeaderText("Ingresar el nombre de la nueva parte");
            dialog.setContentText("Nombre:");

            Optional<String> resultado = dialog.showAndWait();

            resultado.ifPresent(nombreParte -> {
                if (nombreParte != null && !nombreParte.trim().isEmpty()) {
                    String nombreLimpio = nombreParte.trim().substring(0, 1).toUpperCase() + nombreParte.trim().substring(1).toLowerCase();

                    // Evitar duplicados (insensible a mayúsculas/minúsculas)
                    boolean existe = partes.getItems().stream()
                            .anyMatch(item -> item.equalsIgnoreCase(nombreLimpio));

                    if (!existe) {
                        try {
                            parte parteTemplate = new parte(nombreLimpio, 0, false);
                            pManager.insertarParte(parteTemplate);

                            // Si se guardó exitosamente, la añadimos al ComboBox
                            partes.getItems().add(nombreLimpio);
                            partes.setValue(nombreLimpio); // Seleccionar la nueva parte

                        } catch (Exception ex) {
                            System.err.println("Error al guardar la nueva parte en la BD: " + ex.getMessage());
                            mostrarAlertaAux(Alert.AlertType.ERROR, "Error de Base de Datos", "No se pudo guardar la nueva parte.", "Revise la consola para más detalles.");
                        }
                    } else {
                        // Si ya existe, solo seleccionarla
                        partes.setValue(partes.getItems().stream()
                                .filter(item -> item.equalsIgnoreCase(nombreLimpio))
                                .findFirst().orElse(null));
                    }
                } else {
                    mostrarAlertaAux(Alert.AlertType.WARNING, "Atención", "Nombre Inválido", "El nombre de la parte no puede estar vacío.");
                }
            });
        });

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
            // Limpiar también la selección de cliente y auto
            clienteSeleccionado = null;
            lblClienteInfo.setText("Ningún cliente seleccionado");
            lblClienteInfo.setStyle("-fx-font-style: italic; -fx-text-fill: #546E7A;");
            cmbAutosCliente.getItems().clear();
            btnNuevoVehiculo.fire();
        });

        btnGuardar.setOnAction(e->{
            if (listaPartes.isEmpty() || txtTipoTrabajo.getText().isEmpty() || cmbTipoPintura.getValue() == null || txtDiasTrabajo.getText().isEmpty()
                    || txtTipoAuto.getText().isEmpty() || txtMarcaAuto.getText().isEmpty()
                    || txtModeloAuto.getText().isEmpty() || txtAnioAuto.getText().isEmpty() || txtPatenteAuto.getText().isEmpty()) {
                mostrarAlertaAux(Alert.AlertType.ERROR, "Error", "Campos Faltantes", "Complete todos los campos del vehículo y del presupuesto.");
                return;
            }

            if (clienteSeleccionado == null) {
                mostrarAlertaAux(Alert.AlertType.ERROR, "Error", "Cliente Faltante", "Debe asignar un cliente al presupuesto.");
                return;
            }

            try {
                float costoFinal = Float.parseFloat(txtCostoTotal.getText());

                // 1. Confirmar que el cliente acepta el presupuesto
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Confirmar Presupuesto");
                alerta.setHeaderText("Precio Final: $" + String.format("%.2f", costoFinal));
                alerta.setContentText("¿El cliente acepta el presupuesto?");
                Optional<ButtonType> respuesta = alerta.showAndWait();

                // Si el cliente NO acepta el presupuesto (presiona Cancel), no hacemos nada.
                if (respuesta.isEmpty() || respuesta.get() != ButtonType.OK) {
                    return;
                }
                auto a = new auto(txtTipoAuto.getText(), txtPatenteAuto.getText(), Integer.parseInt(txtAnioAuto.getText()), txtMarcaAuto.getText(), txtModeloAuto.getText());
                a.setCliente(clienteSeleccionado);
                pago p = new pago(0);
                autoManager manager = new autoManager();
                int ida = manager.crearOtraerAutoXpatente(a);
                a.setIdBD(ida);

                presupuesto presu = new presupuesto(0, LocalDate.now(), new ArrayList<>(listaPartes), txtTipoTrabajo.getText(), cmbTipoPintura.getValue(), Integer.parseInt(txtDiasTrabajo.getText()), costoFinal, clienteSeleccionado, a, p);

                presupuestoManager managerPresupuesto = new presupuestoManager();
                int l = managerPresupuesto.crearYobtenerID(presu);
                presu.setNumero(l); // El presupuesto ya está guardado y tiene ID


                // Preguntar si quiere generar la orden AHORA
                Alert alertaOrden = new Alert(Alert.AlertType.CONFIRMATION);
                alertaOrden.setTitle("ORDEN DE TRABAJO");
                alertaOrden.setContentText("¿Desea asignar empleados y generar la Orden de Trabajo ahora?");
                Optional<ButtonType> resultado = alertaOrden.showAndWait();
                ordentrabajoManager ordenManager = new ordentrabajoManager();
                List<tarea> tareasDeLaOrden = ordenManager.generarTareasDesdePresupuesto(presu);

                ordentrabajo nuevaOrden;
                if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                    nuevaOrden = new ordentrabajo(ordentrabajo.Estado.Guardada, LocalDate.now(), LocalDate.now(), presu, tareasDeLaOrden);
                    ordenManager.generarOrdenDeTrabajo(nuevaOrden);
                    new GenOrdenScreen(stage, presu, 0, null);

                } else {
                    nuevaOrden = new ordentrabajo(ordentrabajo.Estado.Guardada, LocalDate.now(), LocalDate.now(), presu, tareasDeLaOrden);
                    ordenManager.generarOrdenDeTrabajo(nuevaOrden);
                    stage.setScene(MainApp.mAppVolver(stage));
                }
            } catch (NumberFormatException ex) {
                mostrarAlertaAux(Alert.AlertType.ERROR, "Error", "Datos Inválidos", "Revise que los campos numéricos...");
            }
        });

        btnAtras.setOnAction(e ->{
            Scene volver = MainApp.mAppVolver(stage);
            Navegar.volver(stage, volver, "ChapAPP");
        });

        // --- ENSAMBLADO FINAL DE LA PANTALLA ---

        // Fila 0: Fecha
        mainGrid.add(fechaBox, 0, 0, 2, 1); // Span 2 columnas
        // Fila 1: Cliente y Vehículo
        mainGrid.add(clientePane, 0, 1);
        mainGrid.add(vehiculoPane, 1, 1);
        // Fila 2: Detalles y Repuestos
        mainGrid.add(detallesPane, 0, 2);
        mainGrid.add(repuestosPane, 1, 2);
        // Fila 3: Costo Total
        mainGrid.add(costoBox, 0, 3, 2, 1); // Span 2 columnas
        // Fila 4: Acciones
        mainGrid.add(accionesBox, 0, 4, 2, 1); // Span 2 columnas

        // Hacer que las filas 1 y 2 (paneles) crezcan
        GridPane.setVgrow(clientePane, Priority.NEVER);
        GridPane.setVgrow(vehiculoPane, Priority.NEVER);
        GridPane.setVgrow(detallesPane, Priority.ALWAYS);
        GridPane.setVgrow(repuestosPane, Priority.ALWAYS); // La columna de repuestos crecerá


        // Configuración de la Escena
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ECEFF1;");

        // ScrollPane para el contenido principal
        ScrollPane scrollPane = new ScrollPane(mainGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        root.setCenter(scrollPane);

        // Barra inferior con el botón "Atrás"
        HBox bottomBar = new HBox(btnAtras);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.setPadding(new Insets(10, 25, 10, 25));
        root.setBottom(bottomBar);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("ChapAPP - Generar Presupuesto");
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }

    // --- MÉTODOS AUXILIARES ---
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

    private void rellenarCamposAuto(auto a, TextField tipo, TextField marca, TextField modelo, TextField anio, TextField patente, boolean editable) {
        if (a != null) {
            tipo.setText(a.getTipo());
            marca.setText(a.getMarca());
            modelo.setText(a.getModelo());
            anio.setText(String.valueOf(a.getAnio()));
            patente.setText(a.getPatente());
        } else {
            tipo.clear();
            marca.clear();
            modelo.clear();
            anio.clear();
            patente.clear();
        }

        tipo.setEditable(editable);
        marca.setEditable(editable);
        modelo.setEditable(editable);
        anio.setEditable(editable);
        patente.setEditable(editable);
    }

    private void configurarCamposAuto(auto a, TextField tipo, TextField marca, TextField modelo, TextField anio, TextField patente, boolean editable) {
        if (a != null) {
            tipo.setText(a.getTipo());
            marca.setText(a.getMarca());
            modelo.setText(a.getModelo());
            anio.setText(String.valueOf(a.getAnio()));
            patente.setText(a.getPatente());
        } else {
            tipo.clear();
            marca.clear();
            modelo.clear();
            anio.clear();
            patente.clear();
        }

        tipo.setEditable(editable);
        marca.setEditable(editable);
        modelo.setEditable(editable);
        anio.setEditable(editable);
        patente.setEditable(editable);
    }
}