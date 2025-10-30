package clases.dao;

import clases.control.Conexion;
import clases.model.cliente;
import clases.model.empleado;
import clases.model.ordentrabajo;

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
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public empleado read(int doc) {
        String sql = "SELECT nombre FROM persona WHERE dni = ?";
        empleado emp = null;

        Connection conn = Conexion.getInstance().getConnection(); try(

                PreparedStatement pstmt = conn.prepareStatement(sql);){
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

        Connection conn = Conexion.getInstance().getConnection(); try(

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

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

        try (Connection conn = Conexion.getInstance().getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)) {
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
