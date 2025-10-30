package clases.Manager;
import clases.dao.OrdenDAO;
import clases.dao.tareasDAO;
import clases.model.*;

import java.util.List;

public class ordentrabajoManager {

    private OrdenDAO ordenDAO;
    private tareasDAO tareasDAO;

    public boolean generarOrdenDeTrabajo(ordentrabajo orden) {
        try{
            int idOrden = ordenDAO.createAux(orden);
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

    public List<ordentrabajo> obtenerPendientes(){
        OrdenDAO ordencita = new  OrdenDAO();
        return ordencita.getPendientes();
    }
}
