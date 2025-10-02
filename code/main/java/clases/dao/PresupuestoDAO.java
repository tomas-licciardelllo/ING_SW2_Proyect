package clases.dao;
import clases.control.Conexion;
import clases.model.auto;
import clases.model.cliente;
import clases.model.parte;
import clases.model.presupuesto;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PresupuestoDAO implements dao<presupuesto>{

    @Override
    public boolean create(presupuesto p){
        String sql = "INSERT INTO presupuesto(numero,fecha,repuestos,t_trabajo,t_pintura,d_chapa,costo_total,id_cliente,id_auto) VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,p.getNumero());
            pstmt.setDate(2, Date.valueOf(p.getFecha()));
            pstmt.setString(3,p.getRepuestos().toString());
            pstmt.setString(4,p.getTipoTrabajo());
            pstmt.setString(5,p.getTipoPintura());
            pstmt.setString(6, String.valueOf(p.getDiasChapa()));
            pstmt.setFloat(7,p.getCostoTotal());
            pstmt.setInt(8,p.getCliente().getIdBD());
            pstmt.setInt(9,p.getAuto().getIdBD());
            pstmt.executeUpdate();
            return  true;

        }catch (SQLException e)
        {
            System.out.println("Error al insertar presupuesto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(presupuesto a){
        return true;
    }

    @Override
    public boolean delete(int id){
        return true;
    }

    @Override
    public presupuesto read(int id){
        presupuesto aux = new presupuesto("f",1, LocalDate.now(), new ArrayList<String>(),"j","d",3,23,new cliente("w","2",new ArrayList<auto>(),new ArrayList<presupuesto>()),new auto("W",new ArrayList<parte>(),"w",2,"w","e"));
        return  aux;
    }

    @Override
    public List<presupuesto> getAll(){
        return  new ArrayList<presupuesto>();
    }

}
