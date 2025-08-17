package com.example.prueba;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyValue;

public class Header {

    public static Node createHeader(Stage stage) {
        // Contenedor principal
        StackPane header = new StackPane();
        header.getStyleClass().add("header");
        header.setPadding(new Insets(5, 10, 5, 10));

        // Label centrado
        Label headerLabel = new Label("Taller Sposetti");
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
        StackPane.setAlignment(botones, Pos.CENTER_RIGHT);

        header.getChildren().addAll(headerLabel, botones);

        // Ajustar altura dinámica al 10% de la ventana
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double headerHeight = newVal.doubleValue() * 0.10;
            header.setPrefHeight(headerHeight);
        });

        return header;
    }
}
