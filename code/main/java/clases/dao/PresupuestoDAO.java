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

        Connection conn = Conexion.getInstance().getConnection(); try(

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
    public presupuesto read(int idPresupuesto) {
        presupuesto p = null; // El presupuesto que vamos a devolver
        List<parte> listaDePartes = new ArrayList<>(); // 1. Crea la lista AFUERA

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
    WHERE p.idPresupuesto = ? 
    """;

        Connection conn = Conexion.getInstance().getConnection();

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idPresupuesto);
            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    // --- 2. Crear el Presupuesto (SÓLO 1 VEZ) ---
                    if (p == null) {
                        cliente c = new cliente(
                                rs.getString("nombreCliente"),
                                rs.getString("telCliente"),
                                new ArrayList<>(),
                                new ArrayList<>()
                        );

                        auto au = new auto(
                                rs.getString("tipoAuto"),
                                rs.getString("patenteAuto"),
                                rs.getInt("anioAuto"),
                                rs.getString("marcaAuto"),
                                rs.getString("modeloAuto")
                        );

                        String fechaStr = rs.getString("fecha");
                        LocalDate fecha = null;
                        if (fechaStr != null && !fechaStr.isEmpty()) {
                            try {
                                fecha = LocalDate.parse(fechaStr);
                            } catch (Exception e) {
                                System.out.println("Fecha con formato inesperado: " + fechaStr);
                            }
                        }

                        p = new presupuesto(
                                rs.getInt("idPresupuesto"),
                                fecha,
                                listaDePartes,
                                rs.getString("t_trabajo"),
                                rs.getString("t_pintura"),
                                rs.getInt("d_chapa"),
                                rs.getFloat("costo_total"),
                                c,
                                au,
                                null
                        );
                    }

                    // --- 3. Añadir Repuestos (CADA VEZ) ---
                    // ¡Este bloque está AFUERA del 'if (p == null)'!
                    // Se ejecutará en cada fila que devuelva la BD.
                    String nombreParte = rs.getString("nombreParte");
                    if (nombreParte != null && !nombreParte.isEmpty()) {
                        parte repuesto = new parte(
                                nombreParte,
                                rs.getFloat("panioPint"),
                                parte.intToBoolean(rs.getInt("cambioParte"))
                        );
                        // Añadimos a la lista. Como 'p' tiene una referencia
                        // a esta lista, 'p' se actualiza automáticamente.
                        listaDePartes.add(repuesto);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return p; // Devolvemos el 'p' (que ahora sí tiene la lista de repuestos completa)
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
        ORDER BY p.idPresupuesto
        """;

        Connection conn = Conexion.getInstance().getConnection();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            presupuesto presupuestoActual = null;
            List<parte> partesActuales = new ArrayList<>();
            int idPresupuestoActual = -1;

            while (rs.next()) {
                int idPresupuesto = rs.getInt("idPresupuesto");

                // Si cambiamos de presupuesto, guardamos el anterior
                if (idPresupuestoActual != -1 && idPresupuestoActual != idPresupuesto) {
                    if (presupuestoActual != null) {
                        lista.add(presupuestoActual);
                    }
                    partesActuales = new ArrayList<>();
                }

                // Si es un nuevo presupuesto o el primero
                if (idPresupuestoActual != idPresupuesto) {
                    cliente c = new cliente(
                            rs.getString("nombreCliente"),
                            rs.getString("telCliente"),
                            new ArrayList<>(),
                            new ArrayList<>()
                    );

                    auto au = new auto(
                            rs.getString("tipoAuto"),
                            rs.getString("patenteAuto"),
                            rs.getInt("anioAuto"),
                            rs.getString("marcaAuto"),
                            rs.getString("modeloAuto")
                    );

                    String fechaStr = rs.getString("fecha");
                    LocalDate fecha = null;
                    if (fechaStr != null && !fechaStr.isEmpty()) {
                        try {
                            fecha = LocalDate.parse(fechaStr);
                        } catch (Exception e) {
                            System.out.println("Fecha con formato inesperado: " + fechaStr);
                        }
                    }

                    presupuestoActual = new presupuesto(
                            idPresupuesto,
                            fecha,
                            partesActuales,
                            rs.getString("t_trabajo"),
                            rs.getString("t_pintura"),
                            rs.getInt("d_chapa"),
                            rs.getFloat("costo_total"),
                            c,
                            au,
                            null
                    );

                    idPresupuestoActual = idPresupuesto;
                }

                // Agregar la parte si existe
                String nombreParte = rs.getString("nombreParte");
                if (nombreParte != null && !nombreParte.isEmpty()) {
                    parte p = new parte(
                            nombreParte,
                            rs.getFloat("panioPint"),
                            parte.intToBoolean(rs.getInt("cambioParte"))
                    );
                    partesActuales.add(p);
                }
            }

            // Agregar el último presupuesto
            if (presupuestoActual != null) {
                lista.add(presupuestoActual);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public int createAndGetID(presupuesto p){
        String sql = "INSERT INTO presupuesto(fecha,repuestos,t_trabajo,t_pintura,d_chapa,costo_total,id_cliente,id_auto) VALUES (?,?,?,?,?,?,?,?)";
        int nuevoId = -1;
        Connection conn = Conexion.getInstance().getConnection(); try(

             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);){
            pstmt.setString(1, String.valueOf(Date.valueOf(p.getFecha())));
            pstmt.setString(2,p.getRepuestos().toString());
            pstmt.setString(3,p.getTipoTrabajo());
            pstmt.setString(4,p.getTipoPintura());
            pstmt.setString(5, String.valueOf(p.getDiasChapa()));
            pstmt.setFloat(6,p.getCostoTotal());
            pstmt.setInt(7,p.getCliente().getIdBD());
            pstmt.setInt(8,p.getAuto().getIdBD());
            //pstmt.executeUpdate();

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        nuevoId = generatedKeys.getInt(1); // Obtenemos el ID de la primera columna
                    }
                }
            }

        }catch (SQLException e)
        {
            System.out.println("Error al insertar presupuesto: " + e.getMessage());
            return nuevoId;
        }
        return nuevoId;
    }
}
