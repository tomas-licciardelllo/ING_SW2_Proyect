package clases.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class Header extends Parent {

    public static Node createHeader(Stage stage) {
        // Contenedor principal
        StackPane header = new StackPane();
        header.getStyleClass().add("header");
        header.setPadding(new Insets(5, 10, 5, 10));




        URL url = Header.class.getResource("/resources/img/logoJD.jpg");
        if (url != null) {
            Image logo = new Image(url.toExternalForm());
            ImageView logoIV = new ImageView(logo);
            logoIV.getStyleClass().add("img-logo");

            logoIV.setFitWidth(150);
            logoIV.setPreserveRatio(true);

            // Agregamos directamente al StackPane "header"
            header.getChildren().add(logoIV);
            StackPane.setAlignment(logoIV, Pos.CENTER); // aseguramos que quede centrado
        } else {
            System.out.println("ERROR! No se encontro la img");
        }




        // Ajustar altura dinámica al 10% de la ventana
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double headerHeight = newVal.doubleValue() * 0.10;
            header.setPrefHeight(headerHeight);
        });

        return header;
    }
}
