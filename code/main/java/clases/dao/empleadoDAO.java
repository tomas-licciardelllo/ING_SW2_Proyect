package clases.dao;

import clases.model.empleado;
import clases.model.ordentrabajo;

import java.util.List;

public class empleadoDAO implements dao<empleado>{
    @Override
    public boolean create(empleado empleado) {
        return false;
    }

    @Override
    public boolean update(empleado empleado) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public empleado read(int id) {
        return null;
    }

    @Override
    public List<empleado> getAll() {
        return List.of();
    }
    public int createAndGetID(empleado o){return 1;}
}
