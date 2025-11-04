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

    public List<tarea> getTareasPorOrden(int idOrden) {
        List<tarea> tareas = new ArrayList<>();

        // 1. Este SQL une 'trabajos' y 'tarea' para encontrar las tareas de UNA orden
        String sql = """
    SELECT 
        t.id, t.descripcion, t.empleadoCargo 
    FROM tareas t
    JOIN trabajos j ON t.id = j.tareaRealizar
    WHERE j.ordenPertenece = ?
    """;

        // (Este DAO es solo un ejemplo, asume que tienes un empleadoDAO)
        empleadoDAO empDAO = new empleadoDAO();
        Connection conn = Conexion.getInstance().getConnection();

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idOrden);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    // 2. Leemos los datos de la tarea
                    int idTarea = rs.getInt("id");
                    String descripcion = rs.getString("descripcion");
                    int idEmpleado = rs.getInt("empleadoCargo"); // Esto será 0 para "Reparación"

                    // 3. Creamos el objeto tarea
                    tarea t = new tarea(descripcion);
                    t.setId(idTarea); // Asignamos su ID real

                    // 4. Cargamos el empleado (si tiene uno)
                    if (idEmpleado > 0) {
                        // (Asumo que tienes un 'read' en empleadoDAO)
                        empleado emp = empDAO.read(idEmpleado);
                        t.setEmpleado(emp);
                    }

                    tareas.add(t);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar tareas por orden: " + e.getMessage());
            e.printStackTrace();
        }

        // 5. Devolvemos la lista de tareas (ahora sí incluye "Reparación")
        return tareas;
    }

    //Necesito que me devuelva el ID
    public int createAux(tarea tarea)throws SQLException {
        String sql = "INSERT INTO tareas (descripcion, empleadoCargo) VALUES (?, ?)";
        Connection conn = Conexion.getInstance().getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            empleado empleadoAsignado = tarea.getEmpleado();
            if (empleadoAsignado == null) {
                pst.setString(1, tarea.getDescripcion());
                pst.setLong(2, 0);
            }else {
                pst.setString(1, tarea.getDescripcion());
                pst.setLong(2, empleadoAsignado.getDocumento());
            }

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

    public boolean deleteAllTrabajosByOrdenID(int idOrden) {
        String sql = "DELETE FROM trabajos WHERE ordenPertenece = ?";
        Connection conn = Conexion.getInstance().getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idOrden);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al limpiar trabajos antiguos: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
