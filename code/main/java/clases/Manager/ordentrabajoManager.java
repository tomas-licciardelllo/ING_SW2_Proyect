package clases.Manager;
import clases.dao.OrdenDAO;
import clases.dao.tareasDAO;
import clases.model.*;

import java.util.ArrayList;
import java.util.List;

public class ordentrabajoManager {

    private final OrdenDAO ordenDAO;
    private final tareasDAO tareasDAO;

    public ordentrabajoManager() {
        this.ordenDAO = new OrdenDAO();     // Inicializa el DAO de Orden
        this.tareasDAO = new tareasDAO();   // Inicializa el DAO de Tareas
    }

    public boolean generarOrdenDeTrabajo(ordentrabajo orden) {
        try{
            int idOrden = ordenDAO.createAndGetID(orden);
            for(tarea tarea : orden.getTareas()){
                int idTarea = tareasDAO.createAux(tarea);
                tareasDAO.createTrabajo(idOrden, idTarea);
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<tarea> generarTareasDesdePresupuesto(presupuesto p) {
        List<tarea> tareas = new ArrayList<>();

        // Esta es la MISMA lógica que tenías en GenOrdenScreen
        String descPresu = "Tipo: " + p.getTipoTrabajo();
        String capasPresu = "Capas: " + p.getTipoPintura();
        tareas.add(new tarea(descPresu));
        tareas.add(new tarea(capasPresu));

        for (parte parte : p.getRepuestos()) {
            String descripcion = parte.parteRepuesto();
            tarea tareita = new tarea(descripcion);
            tareas.add(tareita);
        }
        return tareas;
    }

    public List<ordentrabajo> obtenerPendientes(){
        OrdenDAO ordencita = new  OrdenDAO();
        return ordencita.getPendientes();
    }

    public ordentrabajo obtenerOrdenPorPresu(int idPresupuesto){
        OrdenDAO ordencita = new  OrdenDAO();
        return ordencita.getPorPresupuesto(idPresupuesto);
    }
}
