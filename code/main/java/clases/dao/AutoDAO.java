package clases.dao;

import clases.control.Conexion;
import clases.model.auto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

        auto aux = new auto("f",new ArrayList<>(),"p", 23,"j","d");
        return  aux;
    }

    @Override
    public List<auto> getAll(){
        return  new ArrayList<auto>();
    }

}
