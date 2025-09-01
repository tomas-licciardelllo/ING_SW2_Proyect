package clases.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class Header {

    public static Node createHeader(Stage stage) {
        // Contenedor principal
        StackPane header = new StackPane();
        header.getStyleClass().add("header");
        header.setPadding(new Insets(5, 10, 5, 10));

        // Label centrado
        Label headerLabel = new Label("TALLER JD Sposetti");
        headerLabel.getStyleClass().add("titulo");
        StackPane.setAlignment(headerLabel, Pos.CENTER);

        // Botones a la derecha
        Button btn1 = new Button("Trabajos");
        Button btn2 = new Button("Facturas");
        Button btn3 = new Button("Presupuestos");
        Button btn4 = new Button("Pagos");
        btn1.getStyleClass().add("botonHeader");
        btn2.getStyleClass().add("botonHeader");
        btn3.getStyleClass().add("botonHeader");
        btn4.getStyleClass().add("botonHeader");
        HBox botones = new HBox(10, btn1, btn2, btn3, btn4);
        botones.setAlignment(Pos.CENTER_RIGHT);
        botones.setPadding(new Insets(0, 50, 0, 0));
        StackPane.setAlignment(botones, Pos.CENTER_RIGHT);


        URL url = Header.class.getResource("/img/logoJD.jpg");
        if (url != null) {
            Image logo = new Image(url.toExternalForm());
            ImageView logoIV = new ImageView(logo);
            logoIV.getStyleClass().add("img-logo");

            logoIV.setFitWidth(150);
            logoIV.setPreserveRatio(true);

            StackPane logoContainer = new StackPane(logoIV);
            logoContainer.getStyleClass().add("img-cont");

            header.getChildren().add(logoContainer);
        } else {
            System.out.println("ERROR! No se encontro la img");
        }

        // Imagen a la izquierda
        /*try {
            Image logo = new Image(Header.class.getClass().getResourceAsStream("/img/logoJD.jpg"));
            ImageView logoIV = new ImageView(logo);
            logoIV.getStyleClass().add("img-logo");

            StackPane logoContainer = new StackPane(logoIV);
            logoContainer.getStyleClass().add("img-cont");

            header.getChildren().add(logoContainer);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
        }*/


        header.getChildren().addAll(headerLabel, botones);

        // Ajustar altura dinámica al 10% de la ventana
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double headerHeight = newVal.doubleValue() * 0.10;
            header.setPrefHeight(headerHeight);
        });

        return header;
    }
}
