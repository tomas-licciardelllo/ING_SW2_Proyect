package clases.Manager;

import clases.dao.empleadoDAO;
import clases.model.empleado;
import javafx.util.StringConverter;

import java.util.List;

public class empleadoManager {

    public List<empleado> obtenerTodos(){
        empleadoDAO eDao = new empleadoDAO();
        return eDao.getAll();
    }
}
