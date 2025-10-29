package clases.dao;

import clases.control.Conexion;
import clases.model.parte;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class parteDAO implements  dao<parte>{
    @Override
    public boolean create(parte parte) {
        String sql = "INSERT INTO parte(nombre) VALUES (?)";
        Connection conn = Conexion.getInstance().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql))
        {

            pstmt.setString(1,parte.getNombre());
            pstmt.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Error al insertar parte"+ e.getMessage());
            return  false;
        }
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
    public boolean update(parte parte) {
        return false;
    }

    @Override
    public parte read(int id) {
        return null;
    }

    @Override
    public List<parte> getAll() {
        List<parte> lista = new ArrayList<>();
        String sql = "SELECT nombre FROM parte";
        Connection conn = Conexion.getInstance().getConnection();
        try(
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql))
        {
            while(rs.next()){
                parte p = new parte(
                        rs.getString("nombre"),
                        0,
                        true
                );
              lista.add(p);
            }
        }
        catch(SQLException e)
        {
            System.out.println("Error al obtener las partes:" + e.getMessage());
        }
        return lista;
    }

    public float obtenerPrecio(String nombre){
        return 10;
    }
    public int createAndGetID(parte o){return 1;}
}
