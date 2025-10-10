package clases.gui;

import clases.dao.PresupuestoDAO;
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

import clases.dao.*;

public class PresupuestoScreen {

    public PresupuestoScreen (Stage stage){

        double anchoPantalla = Screen.getPrimary().getBounds().getWidth();
        double altoPantalla = Screen.getPrimary().getBounds().getHeight();
        VBox formulario = new VBox(10);
        formulario.setMinSize(300, 200);
        formulario.setMaxSize(600, 400);
        formulario.setStyle(
                "-fx-padding: 20;" +
                        "-fx-background-color: #847770;" + // blanco
                        "-fx-background-radius: 15;" +     // bordes redondeados
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: #cccccc;" +     // borde gris
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0, 0, 4);" // sombra
        );
        formulario.setAlignment(Pos.CENTER);

        Label lblFecha = new Label(""+LocalDate.now());
        Label lblRepuestos = new Label("Repuestos:");
        TextField txtRepuestos = new TextField();

        Label lblTipoTrabajo = new Label("Tipo de trabajo:");
        TextField txtTipoTrabajo = new TextField();

        Label lblTipoPintura = new Label("Tipo de pintura:");
        TextField txtTipoPintura = new TextField();

        Label lblDiasTrabajo = new Label("Dias de chapa:");
        TextField txtDiasTrabajo = new TextField();

        Label lblCostoTotal = new Label("Costo:");
        TextField txtCostoTotal = new TextField();

        ComboBox<String> partes = new ComboBox<>();
        partes.getItems().addAll("Puerta","Espejo","Retrovisor");
        CheckBox opcion = new CheckBox("Reparacion");
        CheckBox opcion2 = new CheckBox("Cambio");
        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");
        Button btnAtras = new Button("Atras");
        VBox opc = new VBox(opcion,opcion2);
        HBox Rep = new HBox(10,opc,partes);

        btnAtras.setOnAction(e ->{
            Stage ss = (Stage) btnAtras.getScene().getWindow();
            stage.setScene(MainApp.mAppVolver(stage));
        });

        Button agrCliente = new Button("Asignar Cliente");

        btnGuardar.setOnAction(e->{
            PresupuestoDAO pd = new PresupuestoDAO();
            ClienteDAO cd = new ClienteDAO();
            cliente c = cd.read(2);
            c.setIdBD(2);
            AutoDAO ad = new AutoDAO();
            auto a = ad.read(1);
            a.setIdBD(1);
            ArrayList<String> par = new ArrayList<>();
            par.add(partes.getValue());
            presupuesto p = new presupuesto(0,LocalDate.now(),par,txtTipoTrabajo.getText(),txtTipoPintura.getText(),Integer.parseInt(txtDiasTrabajo.getText()),Integer.parseInt(txtCostoTotal.getText()),c,a);
            pd.create(p);
        });

        agrCliente.setOnAction(e-> {
            new ClienteScreen(stage);
        });
        HBox acciones = new HBox(10, btnGuardar, btnCancelar,agrCliente);
        acciones.setAlignment(Pos.CENTER);
        HBox inferior = new HBox();
        Region espacio= new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);

        formulario.getChildren().addAll(lblFecha,lblRepuestos,Rep,lblTipoTrabajo,txtTipoTrabajo,lblTipoPintura,txtTipoPintura,
                lblDiasTrabajo,txtDiasTrabajo,lblCostoTotal,txtCostoTotal, acciones);
        HBox.setMargin(btnAtras, new Insets(0,8,8,0));
        inferior.getChildren().addAll(espacio,btnAtras);

        BorderPane root = new BorderPane();
        root.setCenter(formulario);
        root.setBottom(inferior);
        Scene scene = new Scene(root,anchoPantalla * 0.8, altoPantalla * 0.8);
        stage.setScene(scene);
        stage.show();
    }
}
