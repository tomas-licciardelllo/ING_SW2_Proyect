package clases.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class Mensajes {
    public void alertaInformación(String titulo, String contenido, String header){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("alerta");
        alert.showAndWait();
    }

    public void alertaConfirmación(String titulo, String contenido, String header){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        DialogPane dialogPane = alert.getDialogPane();
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        if (okButton != null) {
            okButton.getStyleClass().add("ok-button");
        }
        Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        if (cancelButton != null) {
            cancelButton.getStyleClass().add("cancel-button");
        }
        dialogPane.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("alertaConfirmacion");
        alert.showAndWait();
    }

    public void alertaError(String titulo, String contenido, String header){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("alertaError");
        alert.showAndWait();
    }
}
