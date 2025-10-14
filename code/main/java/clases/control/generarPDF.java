package clases.control;
import clases.model.ordentrabajo;
import clases.model.tarea;
import com.aspose.pdf.*;
import com.aspose.pdf.TextSegment;
import java.time.format.DateTimeFormatter;

public class generarPDF {
    public void generarpdf(ordentrabajo orden){
        Document doc = new Document();
        Page pagina = doc.getPages().add();
        pagina.getPageInfo().getMargin().setLeft(50);
        pagina.getPageInfo().getMargin().setRight(50);
        pagina.getPageInfo().getMargin().setTop(30);
        pagina.getPageInfo().getMargin().setBottom(30);

        try {
            TextFragment titulo = new TextFragment("Orden de Trabajo");
            titulo.getTextState().setFont(FontRepository.findFont("Helvetica-Bold"));
            titulo.getTextState().setFontSize(22);
            titulo.setHorizontalAlignment(HorizontalAlignment.Center);
            titulo.setPosition(new Position(0, 800));
            pagina.getParagraphs().add(titulo);

            TextFragment datosCliente = new TextFragment();
            String fecha = orden.getFecha_inicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            datosCliente.getSegments().add(new TextSegment("Fecha: " + fecha + "\n\n"));
            datosCliente.getSegments().add(new TextSegment("Cliente: " + orden.getPresupuesto().getCliente().getNombre() + "\n"));
            datosCliente.getSegments().add(new TextSegment("Vehículo: " + orden.getPresupuesto().getAuto().getMarca() + " " + orden.getPresupuesto().getAuto().getModelo() + " - Patente: " + orden.getPresupuesto().getAuto().getPatente() + "\n"));
            datosCliente.getTextState().setFontSize(12);
            datosCliente.setMargin(new MarginInfo(30, 0, 0, 10));
            pagina.getParagraphs().add(datosCliente);

            Table tablaTareas = new Table();
            tablaTareas.setDefaultCellBorder(new BorderInfo(BorderSide.All, 0.5f, Color.getBlack()));
            tablaTareas.setColumnWidths("250 100 100");

            Row headerRow = tablaTareas.getRows().add();
            Cell tareaHeader = headerRow.getCells().add("Tareas");
            tareaHeader.setAlignment(HorizontalAlignment.Center);

            for (tarea t : orden.getTareas()) {
                Row dataRow = tablaTareas.getRows().add();
                dataRow.getCells().add(t.getDescripcion());
            }
            pagina.getParagraphs().add(tablaTareas);

            // Aca ponemos la ruta alla en el taller, yo puse para probar
            String rutaDeGuardado = "C:\\Users\\ibane\\Desktop\\Leo\\Universidad\\Orden-" + orden.getPresupuesto().getNumero() + ".pdf"; // Ejemplo de ruta
            doc.save(rutaDeGuardado);
            // Para test
            // System.out.println("PDF generado exitosamente en: " + rutaDeGuardado);

        } catch (Exception ex) {
            System.out.println("Error al generar el PDF: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
