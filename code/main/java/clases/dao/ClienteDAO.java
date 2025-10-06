package clases.dao;

import clases.control.Conexion;
import clases.gui.ClienteScreen;
import clases.model.cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements dao<cliente> {


    @Override
    public boolean create(cliente c) {
        String sql = "INSERT INTO persona(nombre, telefono) VALUES (?, ?)";

        try (Connection conn = Conexion.getConnection();
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

        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                c = new cliente(
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

        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, c.getNombre());
            pstmt.setString(2, c.getTelefono());
            // ⚠️ Ojo: tu clase cliente no tiene `id`, deberías agregárselo
            pstmt.setInt(3, /* c.getId() */ 1);
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

        try (Connection conn = Conexion.getConnection();
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

        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new cliente(
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

        try(Connection conn = Conexion.getConnection();
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
}
