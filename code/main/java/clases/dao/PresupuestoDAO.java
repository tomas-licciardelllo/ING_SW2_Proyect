package clases.dao;
import clases.control.Conexion;
import clases.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PresupuestoDAO implements dao<presupuesto>{

    @Override
    public boolean create(presupuesto p){
        String sql = "INSERT INTO presupuesto(fecha,repuestos,t_trabajo,t_pintura,d_chapa,costo_total,id_cliente,id_auto) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, String.valueOf(Date.valueOf(p.getFecha())));
            pstmt.setString(2,p.getRepuestos().toString());
            pstmt.setString(3,p.getTipoTrabajo());
            pstmt.setString(4,p.getTipoPintura());
            pstmt.setString(5, String.valueOf(p.getDiasChapa()));
            pstmt.setFloat(6,p.getCostoTotal());
            pstmt.setInt(7,p.getCliente().getIdBD());
            pstmt.setInt(8,p.getAuto().getIdBD());
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
        //Prueba
        auto a = new auto("auto", "aa22", 2025, "audi", "r8");
        cliente c = new cliente("Juan Perez","111", new ArrayList<>(),  new ArrayList<>());
        pago p = new pago(0);
        List<parte> repuestos = new ArrayList<>();
        presupuesto aux = new presupuesto(1, LocalDate.now(), repuestos, "aa", "nueva", 2, 222, c, a, p);
        return  aux;
    }

    @Override
    /*public List<presupuesto> getAll(){
        return null;
    }*/

    public List<presupuesto> getAll() {
        List<presupuesto> lista = new ArrayList<>();
        String sql = """
        SELECT 
        p.idPresupuesto, p.fecha, p.t_trabajo, p.t_pintura, p.d_chapa, p.costo_total,
        c.id AS idCliente, c.nombre AS nombreCliente, c.telefono AS telCliente,
        au.aID AS idAuto, au.tipo AS tipoAuto, au.patente AS patenteAuto, 
        au.anio AS anioAuto, au.marca AS marcaAuto, au.modelo AS modeloAuto,
        pa.nombre AS nombreParte, pa.paniopint AS panioPint, pa.cambio AS cambioParte
        FROM presupuesto p
        LEFT JOIN persona c ON p.id_cliente = c.id
        LEFT JOIN auto au ON au.aID = p.id_auto
        LEFT JOIN parte pa ON pa.idPresupuesto = p.idPresupuesto
        """;

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
                // Crear parte
                List <parte> par = new ArrayList<>();
                parte repuestosStr = new parte(
                        rs.getString("nombreParte"),
                        rs.getFloat("panioPint"),
                        parte.intToBoolean(rs.getInt("cambioParte"))
                );
                par.add(repuestosStr);
                //
                auto au = new auto(
                        rs.getString("tipoAuto"),
                        rs.getString("patenteAuto"),
                        rs.getInt("anioAuto"),
                        rs.getString("marcaAuto"),
                        rs.getString("modeloAuto")
                );
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
                        rs.getInt("idPresupuesto"),
                        fecha,
                        par,
                        rs.getString("t_trabajo"),
                        rs.getString("t_pintura"),
                        rs.getInt("d_chapa"),
                        rs.getFloat("costo_total"),
                        c,
                        au,
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
