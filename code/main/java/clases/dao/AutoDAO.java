package clases.dao;

import clases.control.Conexion;
import clases.model.auto;
import clases.model.cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutoDAO implements dao<auto>{

    @Override
    public boolean create (auto a){
        String sql = "INSERT INTO auto(marca,modelo,anio, patente, duenioID) VALUES (?,?,?,?,?)";
        try(Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1,a.getMarca());
            pstmt.setString(2,a.getModelo());
            pstmt.setInt(3, a.getAnio());
            pstmt.setString(4,a.getPatente());
            //pstmt.setString(5, String.valueOf(idCliente));
            pstmt.executeUpdate();
            return true;
        }
        catch (SQLException e){
            System.out.println("Error al insertar cliente: " + e.getMessage());
            return  false;
        }
    }

    @Override
    public boolean update(auto a){
        return true;
    }

    @Override
    public boolean delete(int id){
        return true;
    }

    @Override
    public auto read(int id){

        auto aux = new auto("f","p", 23,"j","d");
        return  aux;
    }

    @Override
    public List<auto> getAll(){
        List<auto> lista = new ArrayList<>();
        String sql = "SELECT a.aID, a.marca, a.modelo, a.anio, a.patente, a.duenioID, " +
                "c.id AS idCliente, c.nombre AS nombreCliente, c.telefono AS telCliente " +
                "FROM auto a " +
                "LEFT JOIN persona c ON a.duenioID = c.id";

        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Crear cliente
                cliente c = new cliente(
                        rs.getString("nombreCliente"),
                        rs.getString("telCliente"),
                        new ArrayList<>(),
                        new ArrayList<>()
                );

                auto a = new auto(
                        rs.getString("marca"),
                        rs.getString("patente"),
                        rs.getInt("anio"),
                        rs.getString("marca"),
                        rs.getString("modelo")
                );

                a.setCliente(c);
                a.setIdBD(rs.getInt("aID"));
                lista.add(a);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener auto: " + e.getMessage());
        }

        return lista;
    }

    public List<Integer> getAllId(){
        List<Integer> arr = new ArrayList<>();
        String sql = "SELECT aID FROM auto";

        try(Connection conn = Conexion.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql))
        {
            while(rs.next()){
                arr.add(rs.getInt("aID"));
            }
        }

        catch(SQLException e)
        {
            System.out.println("Error al obtener los id: " + e.getMessage());
        }

        return arr;
    }

}
