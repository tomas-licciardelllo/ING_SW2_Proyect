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
        ComboBox<String> cmbTipoPintura = new ComboBox<>();
        cmbTipoPintura.getItems().addAll("Bicapa", "Tricapa");
        cmbTipoPintura.setPromptText("Seleccionar tipo");

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
        TextField txtPanosPintura = new TextField();
        txtPanosPintura.setPromptText("Paños");
        txtPanosPintura.setPrefWidth(70);
        CheckBox chkCambio = new CheckBox("Cambio");
        Button btnAgregar = new Button("Agregar");
        Button btnEliminar = new Button("Eliminar");


        ListView<parte> lista = new ListView(listaPartes);
        lista.setPrefHeight(200);
        lista.setMaxHeight(250);

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
            boolean esCambio = chkCambio.isSelected();

            if(nombre == null || nombre.isEmpty()){
                mostrarAlertaAux(Alert.AlertType.WARNING, "ERROR", "Error de Selección", "Debe seleccionar una Parte");
                return;
            }

            String panosStr = txtPanosPintura.getText().trim();
            if (panosStr.isEmpty()) {
                mostrarAlertaAux(Alert.AlertType.WARNING, "ERROR", "Campo Vacío", "Debe ingresar la cantidad de paños de pintura.");
                return;
            }

            float panos;
            try {
                panos = Float.parseFloat(panosStr);
                if (panos < 0) {
                    mostrarAlertaAux(Alert.AlertType.WARNING, "ERROR", "Valor Inválido", "La cantidad de paños debe ser un número positivo.");
                    return;
                }
            } catch (NumberFormatException ex) {
                mostrarAlertaAux(Alert.AlertType.WARNING, "ERROR", "Formato Inválido", "Ingrese un número válido para los paños (ej: 1 o 2.5).");
                return;
            }

            parteDAO parteDAO = new parteDAO();
            parte aux = new parte(nombre, panos, esCambio);
            listaPartes.add(aux);
            partes.getSelectionModel().clearSelection();
            txtPanosPintura.clear();
            chkCambio.setSelected(false);
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
        Button btnLimpiar = new Button("Limpiar");
        Button btnAtras = new Button("Atras");

        HBox Rep = new HBox(10, partes, txtPanosPintura, chkCambio, btnAgregar);
        Rep.setAlignment(Pos.CENTER);

        HBox controlLista = new HBox(10, lista, btnEliminar);
        controlLista.setAlignment(Pos.CENTER);

        btnAtras.setOnAction(e ->{
            Stage ss = (Stage) btnAtras.getScene().getWindow();
            stage.setScene(MainApp.mAppVolver(stage));
        });

        Button agrCliente = new Button("Asignar Cliente");

        btnLimpiar.setOnAction(e->{
           txtCostoTotal.clear();
           txtCostoDia.clear();
           txtDiasTrabajo.clear();
           txtPanosPintura.clear();
           txtDiasTrabajo.clear();
           chkCambio.setSelected(false);
           listaPartes.clear();
        });

        btnGuardar.setOnAction(e->{
            if (listaPartes.isEmpty() || txtTipoTrabajo.getText().isEmpty() || cmbTipoPintura.getValue() == null || txtDiasTrabajo.getText().isEmpty()) {
                mostrarAlertaAux(Alert.AlertType.ERROR, "ERROR", "Campos Faltantes", "Complete todos los campos (incluyendo tipo de pintura) y agregue al menos una parte.");
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
            alerta.setContentText("¿El Cliente acepta el Presupuesto?");
            Optional<ButtonType> resultado = alerta.showAndWait();

            if(resultado.isPresent() && resultado.get() == ButtonType.OK){
                /*


                Aca ponemos la lógica para cargar todo el presupuesto nuevo
                a la Base de Datos


                */
                List<parte> partesnuevas = new ArrayList<>();
                for(int i = 0; i < listaPartes.size(); i++){
                    partesnuevas.add(listaPartes.get(i));
                }

                cliente c = new cliente("jose", "222", new ArrayList<>(), new ArrayList<>());
                auto a = new auto("auto", "222", 2024, "audi", "tt");
                pago p = new pago(0);
                presupuesto presu = new presupuesto(0, LocalDate.now(), partesnuevas, txtTipoTrabajo.getText(), txtPanosPintura.getText(), Integer.parseInt(txtDiasTrabajo.getText()), Float.parseFloat(txtCostoTotal.getText()), c, a, p);
                GenOrdenScreen generarOrden = new GenOrdenScreen(stage, presu);
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
        HBox acciones = new HBox(10, btnGuardar, btnLimpiar,agrCliente);
        acciones.setAlignment(Pos.CENTER);
        HBox inferior = new HBox();
        Region espacio= new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        HBox contenedorCosto = new HBox();
        contenedorCosto.setAlignment(Pos.CENTER); // Centra el TextField en el VBox
        contenedorCosto.getChildren().add(txtCostoTotal);

        formulario.getChildren().addAll(lblFecha,lblRepuestos,Rep, controlLista,
                lblTipoTrabajo,txtTipoTrabajo,lblTipoPintura,cmbTipoPintura,
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
        /*float totalRepuestos = 0.0f;
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
        }*/
    }

    private void mostrarAlertaAux (Alert.AlertType tipo, String titulo, String encabezado, String contenido){
        Alert alerta = new  Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
