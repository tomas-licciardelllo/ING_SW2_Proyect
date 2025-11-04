package clases.Manager;

import clases.dao.OrdenDAO;
import clases.dao.tareasDAO;
import clases.model.tarea;

import java.util.ArrayList;
import java.util.List;

public class tareaManager {

    public List<tarea> getTareasPorID(int idOrden) {
        List<tarea> lista = new ArrayList();
        OrdenDAO ordenDAO = new OrdenDAO();
        lista = ordenDAO.getAllTrabajos(idOrden);
        return lista;
    }
    public List<tarea>getTareasXOrden(int id)
    {
        List<tarea> tareas = new ArrayList<>();
        tareasDAO tDao = new tareasDAO();
        tareas = tDao.getTareasPorOrden(id);
        return tareas;
    }
}
