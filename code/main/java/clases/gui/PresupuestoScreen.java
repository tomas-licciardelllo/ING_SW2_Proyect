package clases.gui;

import clases.dao.PresupuestoDAO;
import clases.dao.parteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import clases.model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import clases.dao.*;

public class PresupuestoScreen {

    private float costoTotal;

    public PresupuestoScreen (Stage stage){

        double anchoPantalla = Screen.getPrimary().getBounds().getWidth();
        double altoPantalla = Screen.getPrimary().getBounds().getHeight();
        VBox formulario = new VBox(10);
        formulario.setMinSize(500, 400);
        formulario.setMaxSize(600, 500);
        formulario.setStyle(
                "-fx-padding: 20;" +
                        "-fx-background-color: #E0E0E0;" + // blanco
                        "-fx-background-radius: 15;" +     // bordes redondeados
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: #cccccc;" +     // borde gris
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0, 0, 4);" // sombra
        );
        formulario.setAlignment(Pos.CENTER);

        Label lblFecha = new Label(""+LocalDate.now());

        Label lblCostoDia = new Label("Costo por Día:");
        TextField txtCostoDia = new TextField();

        Label lblTipoTrabajo = new Label("Tipo de trabajo:");
        TextField txtTipoTrabajo = new TextField();

        Label lblTipoPintura = new Label("Tipo de pintura:");
        TextField txtTipoPintura = new TextField();

        Label lblDiasTrabajo = new Label("Dias de chapa:");
        TextField txtDiasTrabajo = new TextField();

        Label lblCostoTotal = new Label("COSTO TOTAL PRESUPUESTO");
        TextField txtCostoTotal = new TextField("0.0");
        txtCostoTotal.setEditable(false);
        txtCostoTotal.setPrefWidth(100);
        txtCostoTotal.setPrefHeight(30);
        txtCostoTotal.setStyle("-fx-font-size: 12pt;-fx-alignment: center-right; -fx-font-weight: bold;");



        Label lblRepuestos = new Label("Repuestos Seleccionados:");
        ObservableList<parte> listaPartes = FXCollections.observableArrayList();

        ComboBox<String> partes = new ComboBox<>();
        partes.getItems().addAll("Puerta","Espejo","Retrovisor");
        partes.setPromptText("Seleccionar Parte");
        CheckBox opcion = new CheckBox("Reparacion");
        CheckBox opcion2 = new CheckBox("Cambio");
        Button btnAgregar = new Button("Agregar");
        Button btnEliminar = new Button("Eliminar");


        ListView<parte> lista = new ListView(listaPartes);
        lista.setPrefHeight(200);
        lista.setMaxHeight(250);

        opcion.selectedProperty().addListener((obs, ant, nuevo) -> {
            if(nuevo){
                opcion2.setSelected(false);
            }
        });
        opcion2.selectedProperty().addListener((obs, ant, nuevo) -> {
            if(nuevo){
                opcion.setSelected(false);
            }
        });
        listaPartes.addListener((javafx.collections.ListChangeListener<parte>) c -> {
            calcularYActualizarTotal(listaPartes, txtCostoTotal, txtCostoDia, txtDiasTrabajo);
        });
        txtCostoDia.textProperty().addListener((obs, oldVal, newVal) -> {
            calcularYActualizarTotal(listaPartes, txtCostoTotal, txtCostoDia, txtDiasTrabajo);
        });
        txtDiasTrabajo.textProperty().addListener((obs, oldVal, newVal) -> {
            calcularYActualizarTotal(listaPartes, txtCostoTotal, txtCostoDia, txtDiasTrabajo);
        });

        btnAgregar.setOnAction(e->{
            String nombre = partes.getSelectionModel().getSelectedItem();
            boolean reparacion = opcion.isSelected();
            boolean cambio = opcion2.isSelected();

            if(nombre == null || nombre.isEmpty()){
                mostrarAlertaAux(Alert.AlertType.WARNING, "ERROR", "Error de Selección", "Debe seleccionar una Parte");
            }
            if(!reparacion && !cambio){
                mostrarAlertaAux(Alert.AlertType.WARNING, "ERROR", "Error de Selección", "Debe Seleccionar si es Cambio o Reparacion");
            }
            parteDAO parteDAO = new parteDAO();
            float precio = parteDAO.obtenerPrecio(nombre);
            parte aux = new parte(nombre, precio, reparacion, cambio);
            listaPartes.add(aux);
            partes.getSelectionModel().clearSelection();
            opcion.setSelected(false);
            opcion2.setSelected(false);
        });

        btnEliminar.setOnAction(e->{
            parte parteSeleccionada = lista.getSelectionModel().getSelectedItem();
            if(parteSeleccionada != null){
                listaPartes.remove(parteSeleccionada);
            }else{
                mostrarAlertaAux(Alert.AlertType.WARNING, "ERROR", "Eliminar Parte", "Seleccione una parte de la lista para eliminar.");
            }
        });

        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");
        Button btnAtras = new Button("Atras");
        VBox opc = new VBox(5, opcion,opcion2);
        opc.setPadding(new Insets(5, 0, 0, 0));
        opc.setAlignment(Pos.CENTER);

        HBox Rep = new HBox(15, partes, opc, btnAgregar);
        Rep.setAlignment(Pos.CENTER);

        HBox controlLista = new HBox(10, lista, btnEliminar);
        controlLista.setAlignment(Pos.CENTER);

        btnAtras.setOnAction(e ->{
            Stage ss = (Stage) btnAtras.getScene().getWindow();
            stage.setScene(MainApp.mAppVolver(stage));
        });

        Button agrCliente = new Button("Asignar Cliente");

        btnGuardar.setOnAction(e->{
            if (listaPartes.isEmpty() || txtTipoTrabajo.getText().isEmpty() || txtTipoPintura.getText().isEmpty() || txtDiasTrabajo.getText().isEmpty()) {
                mostrarAlertaAux(Alert.AlertType.ERROR, "ERROR", "Campos Faltantes", "Complete todos los campos y agregue al menos una parte.");
                return;
            }

            float costoFinal = 0.0f;
            try {
                costoFinal = Float.parseFloat(txtCostoTotal.getText());
            } catch (NumberFormatException ex) {
                mostrarAlertaAux(Alert.AlertType.ERROR, "ERROR", "Costo Inválido", "El Costo Total calculado no es un número válido.");
                return;
            }

            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("CONFIRMACIÓN");
            alerta.setHeaderText("Precio del Presupuesto: $" + String.format("%.2f", costoFinal));
            alerta.setContentText("¿El CLiente acepta el Presupuesto?");
            Optional<ButtonType> resultado = alerta.showAndWait();

            if(resultado.isPresent() && resultado.get() == ButtonType.OK){
                //Aca ponemos la lógica para cargar todo el presupuesto nuevo
            }else{ //No Acepto, volvemos al Menú
                stage.setScene(MainApp.mAppVolver(stage));
            }

            /*
            PresupuestoDAO pd = new PresupuestoDAO();
            ClienteDAO cd = new ClienteDAO();
            cliente c = cd.read(2);
            c.setIdBD(2);
            AutoDAO ad = new AutoDAO();
            auto a = ad.read(1);
            a.setIdBD(1);
            List<parte> repuestos = new ArrayList<>();
            List<parte> par = new List<>();
            par.add(partes)
            par.add(partes.getValue());
            pd.create(p);
            presupuesto p = new presupuesto(0,LocalDate.now(),repuestos,txtTipoTrabajo.getText(),txtTipoPintura.getText(),Integer.parseInt(txtDiasTrabajo.getText()),Integer.parseInt(txtCostoTotal.getText()),c,a);
            */
        });

        agrCliente.setOnAction(e-> {
            new ClienteScreen(stage);
        });
        HBox acciones = new HBox(10, btnGuardar, btnCancelar,agrCliente);
        acciones.setAlignment(Pos.CENTER);
        HBox inferior = new HBox();
        Region espacio= new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        HBox contenedorCosto = new HBox();
        contenedorCosto.setAlignment(Pos.CENTER); // Centra el TextField en el VBox
        contenedorCosto.getChildren().add(txtCostoTotal);

        formulario.getChildren().addAll(lblFecha,lblRepuestos,Rep, controlLista,
                lblTipoTrabajo,txtTipoTrabajo,lblTipoPintura,txtTipoPintura,
                lblDiasTrabajo,txtDiasTrabajo,lblCostoDia,txtCostoDia,
                lblCostoTotal,contenedorCosto, acciones);
        HBox.setMargin(btnAtras, new Insets(0,8,8,0));
        inferior.getChildren().addAll(espacio,btnAtras);

        BorderPane root = new BorderPane();
        root.setCenter(formulario);
        root.setBottom(inferior);
        Scene scene = new Scene(root,anchoPantalla * 0.8, altoPantalla * 0.8);
        stage.setScene(scene);
        stage.show();
    }

    private void calcularYActualizarTotal(ObservableList<parte> listaPartes, TextField txtTotal, TextField txtCostoChapa, TextField txtDias) {
        float totalRepuestos = 0.0f;
        float costoManoObra = 0.0f;
        float diasChapa = 0.0f;

        for (parte p : listaPartes) {
            totalRepuestos += p.getPrecio();
        }
        try {
            if (!txtCostoChapa.getText().trim().isEmpty()) {
                costoManoObra = Float.parseFloat(txtCostoChapa.getText().trim());
            }
            if (!txtDias.getText().trim().isEmpty()) {
                diasChapa = Float.parseFloat(txtDias.getText().trim());
            }
            float costoChapa = costoManoObra * diasChapa;

            costoTotal = totalRepuestos + costoChapa;
            txtTotal.setText(String.format("%.2f", costoTotal));

        } catch (NumberFormatException ex) {
            txtTotal.setText("ERROR");
        }
    }

    private void mostrarAlertaAux (Alert.AlertType tipo, String titulo, String encabezado, String contenido){
        Alert alerta = new  Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
