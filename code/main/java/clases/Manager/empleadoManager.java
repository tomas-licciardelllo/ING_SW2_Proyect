package clases.Manager;

import clases.dao.empleadoDAO;
import clases.model.empleado;

import java.util.List;

public class empleadoManager {

    public List<empleado> obtenerTodos(){
        empleadoDAO eDao = new empleadoDAO();
        return eDao.getAll();
    }

    public boolean agregarEmpleado(empleado empleado){
        empleadoDAO eDao = new empleadoDAO();
        return eDao.create(empleado);
    }

    public boolean modificarEmpleado(long dniOriginal, empleado empleado){
        empleadoDAO eDao = new empleadoDAO();
        return eDao.update(dniOriginal, empleado);
    }

    public boolean eliminarEmpleado(long dni) {
        empleadoDAO eDao = new empleadoDAO();
        return eDao.delete(dni);
    }
}
