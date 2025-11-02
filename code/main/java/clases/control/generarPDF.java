package clases.control;
import clases.gui.Header;
import clases.model.ordentrabajo;
import clases.model.tarea;
import com.aspose.pdf.*;
import com.aspose.pdf.TextSegment;
import javafx.geometry.Pos;
import com.aspose.pdf.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;

public class generarPDF {
    public void generarpdf(ordentrabajo orden){
        Document doc = new Document();
        Page pagina = doc.getPages().add();
        pagina.getPageInfo().getMargin().setLeft(40);
        pagina.getPageInfo().getMargin().setRight(40);
        pagina.getPageInfo().getMargin().setTop(20);
        pagina.getPageInfo().getMargin().setBottom(40);

        try {
            Table encabezado = new Table();
            encabezado.setColumnWidths("150 350");
            encabezado.setDefaultCellBorder(new BorderInfo(BorderSide.None));
            Cell logoCell = encabezado.getRows().add().getCells().add();
            Image logo = new Image();
            InputStream imageStream = getClass().getResourceAsStream("/img/logoJD.jpg");
            if (imageStream != null) {
                logo.setImageStream(imageStream);
                logo.setFixWidth(120);
                logoCell.getParagraphs().add(logo);
                logoCell.setVerticalAlignment(VerticalAlignment.Center);
            }
            else{
                System.out.println("No se encontro el imagen");
            }
            pagina.getParagraphs().add(encabezado);

            TextFragment titulo = new TextFragment("Orden de Trabajo Número " + orden.getNumeroOrden());
            titulo.getTextState().setFont(FontRepository.findFont("Helvetica-Bold"));
            titulo.getTextState().setFontSize(20);
            titulo.setHorizontalAlignment(HorizontalAlignment.Center);
            titulo.setMargin(new MarginInfo(30, 0, 0, 10));
            pagina.getParagraphs().add(titulo);

            TextFragment datosCliente = new TextFragment();
            String fecha = orden.getFecha_inicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            datosCliente.getSegments().add(new TextSegment("Fecha: " + fecha + "\n\n"));
            datosCliente.getSegments().add(new TextSegment("Cliente: " + orden.getPresupuesto().getCliente().getNombre() + "\n"));
            datosCliente.getSegments().add(new TextSegment("Vehículo: " + orden.getPresupuesto().getAuto().getMarca() + " " + orden.getPresupuesto().getAuto().getModelo() + " - Patente: " + orden.getPresupuesto().getAuto().getPatente() + "\n"));
            datosCliente.getTextState().setFontSize(12);
            datosCliente.setMargin(new MarginInfo(20, 0, 0, 10));
            pagina.getParagraphs().add(datosCliente);

            Table tablaTareas = new Table();
            tablaTareas.setColumnWidths("460");
            tablaTareas.setDefaultCellBorder(new BorderInfo(BorderSide.All, 0.5f, Color.getBlack()));
            tablaTareas.setMargin(new MarginInfo(20, 0, 0, 10));
            tablaTareas.setDefaultCellTextState(new TextState(12));

            Row headerRow = tablaTareas.getRows().add();
            headerRow.setDefaultCellTextState(new TextState());
            headerRow.getDefaultCellTextState().setFont(FontRepository.findFont("Helvetica-Bold"));
            headerRow.getDefaultCellTextState().setForegroundColor(Color.getWhite());
            Cell tareaHeader = headerRow.getCells().add("TAREAS y REPUESTOS");
            tareaHeader.setAlignment(HorizontalAlignment.Center);

            for (tarea t : orden.getTareas()) {
                Row dataRow = tablaTareas.getRows().add();
                dataRow.getCells().add(t.getDescripcion());
            }
            pagina.getParagraphs().add(tablaTareas);

            // Aca ponemos la ruta alla en el taller, yo puse para probar
            String rutaDeGuardado = System.getProperty("user.dir")+"code\\main\\resources\\pdf" + orden.getPresupuesto().getNumero() + ".pdf"; // Ejemplo de ruta
            doc.save(rutaDeGuardado);
            // Para test
            // System.out.println("PDF generado exitosamente en: " + rutaDeGuardado);

        } catch (Exception ex) {
            System.out.println("Error al generar el PDF: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
