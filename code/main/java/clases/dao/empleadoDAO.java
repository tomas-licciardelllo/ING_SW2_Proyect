package clases.dao;

import clases.control.Conexion;
import clases.model.empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class empleadoDAO implements dao<empleado>{
    @Override
    public boolean create(empleado empleado) {
        String sql = "INSERT INTO empleados(dni, nombre) VALUES (?, ?)";

        Connection conn = Conexion.getInstance().getConnection(); try(
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, empleado.getDocumento());
            pstmt.setString(2, empleado.getNombre());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar empleado: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(empleado empleado) {
        return this.update(empleado.getDocumento(), empleado);
    }

    public boolean update(long dniOriginal, empleado empleadoModificado) {
        String sql = "UPDATE empleados SET dni = ?, nombre = ? WHERE dni = ?";
        Connection conn = Conexion.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, empleadoModificado.getDocumento());
            pstmt.setString(2, empleadoModificado.getNombre());
            pstmt.setLong(3, dniOriginal); // Busca por el DNI original

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Verdadero si se actualizó al menos una fila

        } catch (SQLException e) {
            System.out.println("Error al actualizar empleado: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        return delete((long) id);
    }

    public boolean delete(long dni) {
        String sql = "DELETE FROM empleados WHERE dni = ?";
        Connection conn = Conexion.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, dni);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Verdadero si se eliminó al menos una fila

        } catch (SQLException e) {
            System.out.println("Error al eliminar empleado: " + e.getMessage());
            return false;
        }
    }

    @Override
    public empleado read(int doc) {
        String sql = "SELECT nombre FROM persona WHERE dni = ?";
        empleado emp = null;
        Connection conn = Conexion.getInstance().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setInt(1, doc);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                emp = new empleado(rs.getString("nombre"), new ArrayList<>(), rs.getLong("dni"));
            }

        } catch (SQLException e) {
            System.out.println("Error al leer empleado: " + e.getMessage());
        }
        return emp;
    }

    @Override
    public List<empleado> getAll() {
        List<empleado> lista = new ArrayList<>();
        String sql = "SELECT dni, nombre FROM empleados";
        Connection conn = Conexion.getInstance().getConnection();
        try(Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new empleado(rs.getString("nombre"), new ArrayList<>(), rs.getLong("dni")));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener empleados: " + e.getMessage());
        }

        return lista;
    }
    public int createAndGetID(empleado o){return 1;}

    public empleado getbyDNI(long dni){
        empleado emp = null;
        String sql = "SELECT dni, nombre FROM empleados WHERE dni = ?;";
        Connection conn = Conexion.getInstance().getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, dni);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                emp = new empleado(rs.getString("nombre"), new ArrayList<>(), rs.getLong("dni"));
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
        return emp;
}
}
