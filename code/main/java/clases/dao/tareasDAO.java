package clases.dao;

import clases.control.Conexion;
import clases.model.empleado;
import clases.model.tarea;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class tareasDAO implements dao<tarea>{

    @Override
    public boolean create(tarea tarea) {
        return false;
    }

    @Override
    public int createAndGetID(tarea tarea) {
        return 0;
    }

    @Override
    public boolean update(tarea tarea) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public tarea read(int id) {
        return null;
    }

    @Override
    public List<tarea> getAll() {
        return List.of();
    }

    //Necesito que me devuelva el ID
    public int createAux(tarea tarea)throws SQLException {
        String sql = "INSERT INTO tareas (descripcion, empleadoCargo) VALUES (?, ?)";
        Connection conn = Conexion.getInstance().getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            empleado empleadoAsignado = tarea.getEmpleado();
            if (empleadoAsignado == null) {
                throw new SQLException("La tarea no tiene empleado.");
            }
            pst.setString(1, tarea.getDescripcion());
            pst.setLong(2, empleadoAsignado.getDocumento()); //

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Devuelve el ID de la tarea
                } else {
                    throw new SQLException("Fallo al crear la tarea, no se obtuvo ID.");
                }
            }
        }
    }

    public void createTrabajo (int idOrden, int idTarea)throws SQLException {
        String sql = "INSERT INTO trabajos (ordenPertenece, tareaRealizar) VALUES (?, ?)"; //
        Connection conn = Conexion.getInstance().getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, idOrden);
            pst.setLong(2, idTarea);
            pst.executeUpdate();
        }
    }
    
}
