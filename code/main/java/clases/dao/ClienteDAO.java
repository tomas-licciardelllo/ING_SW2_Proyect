package clases.dao;

import clases.control.Conexion;
import clases.gui.ClienteScreen;
import clases.model.cliente;
import clases.model.ordentrabajo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements dao<cliente> {


    @Override
    public boolean create(cliente c) {
        String sql = "INSERT INTO persona(nombre, telefono) VALUES (?, ?)";

        Connection conn = Conexion.getInstance().getConnection(); try(

             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, c.getNombre());
            pstmt.setString(2, c.getTelefono());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public cliente read(int id) {
        String sql = "SELECT id, nombre, telefono FROM persona WHERE id = ?";
        cliente c = null;

        Connection conn = Conexion.getInstance().getConnection(); try(

             PreparedStatement pstmt = conn.prepareStatement(sql);){
             pstmt.setInt(1, id);
             ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
            c = new cliente(
                    rs.getInt("Id"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    new ArrayList<>(),
                    new ArrayList<>()
            );
        }

    } catch (SQLException e) {
        System.out.println("Error al leer cliente: " + e.getMessage());
    }
        return c;
}

@Override
public boolean update(cliente c) {
    String sql = "UPDATE persona SET nombre = ?, telefono = ? WHERE id = ?";

    Connection conn = Conexion.getInstance().getConnection(); try(

         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, c.getNombre());
        pstmt.setString(2, c.getTelefono());
        // ⚠️ Ojo: tu clase cliente no tiene `id`, deberías agregárselo
        pstmt.setInt(3, c.getIdBD());
        pstmt.executeUpdate();
        return true;

    } catch (SQLException e) {
        System.out.println("Error al actualizar cliente: " + e.getMessage());
        return false;
    }
}

@Override
public boolean delete(int id) {
    String sql = "DELETE FROM persona WHERE id = ?";

    Connection conn = Conexion.getInstance().getConnection(); try(

         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, id);
        pstmt.executeUpdate();
        return true;

    } catch (SQLException e) {
        System.out.println("Error al borrar cliente: " + e.getMessage());
        return false;
    }
}

@Override
public List<cliente> getAll() {
    List<cliente> lista = new ArrayList<>();
    String sql = "SELECT id, nombre, telefono FROM persona";

    Connection conn = Conexion.getInstance().getConnection();
    try(Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            lista.add(new cliente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    new ArrayList<>(),
                    new ArrayList<>()
            ));
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener clientes: " + e.getMessage());
    }

    return lista;
}

public List<cliente> getAllwID() {
    List<cliente> lista = new ArrayList<>();
    String sql = "SELECT id, nombre, telefono FROM persona";

    Connection conn = Conexion.getInstance().getConnection();
    try( Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            lista.add(new cliente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    new ArrayList<>(),
                    new ArrayList<>()
            ));
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener clientes: " + e.getMessage());
    }

    return lista;
}

public List<Integer> getAllId(){
    List<Integer> arr = new ArrayList<>();
    String sql = "SELECT id FROM persona";
    Connection conn = Conexion.getInstance().getConnection();
    try(
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql))
    {
        while(rs.next()){
            arr.add(rs.getInt("id"));
        }
    }

    catch(SQLException e)
    {
        System.out.println("Error al obtener los id: " + e.getMessage());
    }

    return arr;
}
public int createAndGetID(cliente o){return 1;}

//create auxiliar para poder usarlo en el modificar luego
public cliente createMOD(cliente c) {
    String sql = "INSERT INTO persona(nombre, telefono) VALUES (?, ?)";

    Connection conn = Conexion.getInstance().getConnection();
    try(PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
        pstmt.setString(1, c.getNombre());
        pstmt.setString(2, c.getTelefono());
        int columnasafectadas = pstmt.executeUpdate();
        if(columnasafectadas > 0){
            try(ResultSet generatedKeys = pstmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    c.setIdBD(generatedKeys.getInt(1));
                }
                else
                {
                    throw new SQLException("Fallo al crear el cliente, no se obtuvo el id");
                }
            }

        }
    } catch (SQLException e) {
        System.out.println("Error al insertar cliente: " + e.getMessage());
    }
    return c;
}

}
