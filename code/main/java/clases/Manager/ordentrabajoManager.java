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
        OrdenDAO ord = new OrdenDAO();
        try {
            ordentrabajo ordenExistente = obtenerOrdenPorPresu(orden.getPresupuesto().getNumero());
            int idOrden;

            if (ordenExistente != null) {
                idOrden = ordenExistente.getID();
                // Solo actualizar el estado y fechas
                boolean actualizado = ord.updateEstado(idOrden, orden.getEstado());
                if (!actualizado) {
                    return false;
                }
                for (tarea tareaConEmpleado : orden.getTareas()) {
                    tareasDAO.actualizarEmpleado(tareaConEmpleado);
                }

            } else {
                idOrden = ordenDAO.createAndGetID(orden);

                if (idOrden == -1) {
                    return false;
                }
                for (tarea tarea : orden.getTareas()) {
                    int idTarea = tareasDAO.createAux(tarea);
                    if (idTarea != -1) {
                        tareasDAO.createTrabajo(idOrden, idTarea);
                    }
                }
            }
            return true;
        } catch (Exception e) {
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

    public List<ordentrabajo> obtenerDesarrollo(){
        OrdenDAO ordencita = new  OrdenDAO();
        return ordencita.getDesarrollo();
    }

    public boolean actualizarOrden(ordentrabajo orden, int estadoNuevo) {
        if(estadoNuevo == 0 || estadoNuevo == 1 || estadoNuevo == 2 || estadoNuevo == -1) {
            OrdenDAO ordenDAO = new OrdenDAO();
            //Cambiamos el estado de la Orden
            orden.setEstado(ordentrabajo.Estado.fromInt(estadoNuevo));
            ordenDAO.createAndGetID(orden);
            return true;
        }
        else{
            return false;
        }
    }
}
