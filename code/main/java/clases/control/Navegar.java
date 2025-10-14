package clases.control;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navegar {

    public static void volver(Stage stage, Scene destino, String titulo) {
        stage.setScene(destino);
        stage.setTitle(titulo);
        stage.setMaximized(true);
        if (!stage.isShowing()) {
            stage.show();
        }
    }
}