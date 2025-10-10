package clases.dao;
import clases.control.Conexion;
import clases.model.auto;
import clases.model.cliente;
import clases.model.parte;
import clases.model.presupuesto;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PresupuestoDAO implements dao<presupuesto>{

    @Override
    public boolean create(presupuesto p){
        String sql = "INSERT INTO presupuesto(numero,fecha,repuestos,t_trabajo,t_pintura,d_chapa,costo_total,id_cliente,id_auto) VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,p.getNumero());
            pstmt.setString(2, String.valueOf(Date.valueOf(p.getFecha())));
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
        presupuesto aux = new presupuesto(1, LocalDate.now(), new ArrayList<String>(),"j","d",3,23,new cliente("w","2",new ArrayList<auto>(),new ArrayList<presupuesto>()),new auto("W",new ArrayList<parte>(),"w",2,"w","e"));
        return  aux;
    }

    @Override
    public List<presupuesto> getAll() {
        List<presupuesto> lista = new ArrayList<>();
        String sql = "SELECT p.numero, p.fecha, p.repuestos, p.t_trabajo, p.t_pintura, p.d_chapa, p.costo_total, " +
                "c.id AS idCliente, c.nombre AS nombreCliente, c.telefono AS telCliente " +
                "FROM presupuesto p " +
                "LEFT JOIN persona c ON p.id_cliente = c.id";

        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Crear cliente directamente desde ResultSet
                cliente c = new cliente(
                        rs.getString("nombreCliente"),
                        rs.getString("telCliente"),
                        new ArrayList<>(),
                        new ArrayList<>()
                );

                // Parsear repuestos
                String repuestosStr = rs.getString("repuestos");
                ArrayList<String> repuestos = new ArrayList<>();
                if (repuestosStr != null && !repuestosStr.isEmpty()) {
                    repuestosStr = repuestosStr.replaceAll("\\[|\\]", "");
                    repuestos.addAll(Arrays.asList(repuestosStr.split(", ")));
                }

                // Arregla el problema de la fecha
                String fechaStr = rs.getString("fecha");
                LocalDate fecha = null;
                if (fechaStr != null && !fechaStr.isEmpty()) {
                    try {
                        fecha = LocalDate.parse(fechaStr);
                    } catch (Exception e) {
                        System.out.println("Fecha con formato inesperado: " + fechaStr);
                    }
                }

                // Crear presupuesto
                presupuesto p = new presupuesto(
                        rs.getInt("numero"),
                        fecha,
                        repuestos,
                        rs.getString("t_trabajo"),
                        rs.getString("t_pintura"),
                        rs.getInt("d_chapa"),
                        rs.getFloat("costo_total"),
                        c,
                        null
                );

                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }





}
