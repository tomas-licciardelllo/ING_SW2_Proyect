package clases.dao;
import clases.control.Conexion;
import clases.model.cliente;
import clases.model.ordentrabajo;
import clases.model.presupuesto;
import clases.model.tarea;
import clases.model.auto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdenDAO implements dao<ordentrabajo> {
    @Override
    public boolean create(ordentrabajo o) {
        String sql = "INSERT INTO orden_trabajo(fecha_inicio,fecha_fin, tareas, estado, pID) VALUES (?,?,?,?,?)";

        Connection conn = Conexion.getInstance().getConnection(); try(

             PreparedStatement prep = conn.prepareStatement(sql)){
            prep.setInt(1, o.getNumeroOrden());
            prep.setString(1, String.valueOf(Date.valueOf(o.getFecha_inicio())));
            prep.setString(2,String.valueOf(Date.valueOf(o.getFecha_final())));
            prep.setString(3, String.valueOf(o.getTareasDesc()));
            prep.setInt(4, o.getEstado().toInt());
            prep.setInt(5, o.getPresupuesto().getNumero());
            prep.executeUpdate();
            return  true;

        }catch (SQLException e)
        {
            System.out.println("Error al insertar orden de Trabajo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(ordentrabajo o) {
        return true;
    }

    @Override
    public boolean delete(int id) {
        return true;
    }

    @Override
    public ordentrabajo read(int id) {
        String sql = "SELECT id, fecha_inicio,fecha_fin, tareas, estado, pID FROM orden_trabajo WHERE id = ?";
        ordentrabajo c = null;

        Connection conn = Conexion.getInstance().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            ordentrabajo.Estado estado = ordentrabajo.Estado.valueOf(rs.getString("estado"));
            int presupuestoId = rs.getInt("pID");
            presupuesto p = new PresupuestoDAO().read(presupuestoId);


            if (rs.next()) {
                c = new ordentrabajo(
                        estado,
                        LocalDate.parse(rs.getString("fecha_inicio")), //Le cambie el nombre
                        //rs.getDate("fecha_Fin").toLocalDate(),
                        LocalDate.now(),
                        p,
                        new ArrayList<tarea>()
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al leer las ordenes: " + e.getMessage());
        }
        return c;
    }

    @Override
    public List<ordentrabajo> getAll() {
        List<ordentrabajo> lista = new ArrayList<>();
        String sql = "SELECT id,fecha_inicio,fecha_fin, tareas, estado, pID FROM orden_trabajo";
        Connection conn = Conexion.getInstance().getConnection(); try(

             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int estadoNum = rs.getInt("estado");
                ordentrabajo.Estado estado = ordentrabajo.Estado.fromInt(estadoNum);
                int presupuestoId = rs.getInt("pID");
                presupuesto p = new PresupuestoDAO().read(presupuestoId);
                lista.add(new ordentrabajo(
                                rs.getInt("id"),
                                estado,
                                LocalDate.parse(rs.getString("fecha_inicio")),
                                LocalDate.parse(rs.getString("fecha_fin")),
                                p,
                                new ArrayList<tarea>()
                        )
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las ordenes de trabajo: " + e.getMessage());
        }

        return lista;
    }


    /*
    public List<ordentrabajo> getPendientes() {
        List<ordentrabajo> lista = new ArrayList<>();
        String sql = "SELECT id, fecha_inicio,fecha_fin, tareas, estado, pID FROM orden_trabajo WHERE estado = 0";
        Connection conn = Conexion.getInstance().getConnection(); try(
();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int estadoNum = rs.getInt("estado");
                ordentrabajo.Estado estado = ordentrabajo.Estado.fromInt(estadoNum);
                int presupuestoId = rs.getInt("pID");
                presupuesto p = new PresupuestoDAO().read(presupuestoId);
                lista.add(new ordentrabajo(
                                rs.getInt("id"),
                                estado,
                                LocalDate.parse(rs.getString("fecha_inicio")),
                                LocalDate.now(),
                                p,
                                new ArrayList<tarea>()
                        )
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las ordenes de trabajo: " + e.getMessage());
        }

        return lista;
    }*/


    public int createAndGetID(ordentrabajo o){return 1;}

    public List<ordentrabajo> getPendientes() {
        List<ordentrabajo> lista = new ArrayList<>();

        // ✍️ Esta consulta SQL une las 4 tablas para obtener toda la información de una vez.
        // Asegúrate que los nombres de tablas (orden_trabajo, presupuesto, persona, autos)
        // y columnas (pID, idPresupuesto, id_cliente, idBD, id_auto, aID) sean correctos.
        String sql = "SELECT " +
                "    ot.id AS orden_id, ot.fecha_inicio, ot.estado, " +
                "    p.idPresupuesto, p.t_trabajo, p.t_pintura, p.d_chapa, p.costo_total," +
                "    c.id AS cliente_id, c.nombre AS cliente_nombre, c.telefono AS cliente_telefono, " +
                "    a.aID AS auto_id, a.marca, a.modelo, a.anio, a.patente, a.tipo " +
                "FROM orden_trabajo ot " +
                "JOIN presupuesto p ON ot.pID = p.idPresupuesto " +
                "JOIN persona c ON p.id_cliente = c.id " +
                "JOIN auto a ON p.id_auto = a.aID " +
                "WHERE ot.estado = 0";

        Connection conn = Conexion.getInstance().getConnection(); try(

             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // 1. Construimos el objeto 'cliente' desde los resultados de la consulta
                cliente cli = new cliente(
                        rs.getInt("cliente_id"),
                        rs.getString("cliente_nombre"),
                        rs.getString("cliente_telefono"),
                        new ArrayList<>(),
                        new ArrayList<>()
                );

                // 2. Construimos el objeto 'auto'
                auto aut = new auto(
                        rs.getString("tipo"),
                        rs.getString("patente"),
                        rs.getInt("anio"),
                        rs.getString("marca"),
                        rs.getString("modelo")
                );
                String fechaStr = rs.getString("fecha_inicio");
                LocalDate fecha = null;
                if (fechaStr != null && !fechaStr.isEmpty()) {
                    try {
                        fecha = LocalDate.parse(fechaStr);
                    } catch (Exception e) {
                        System.out.println("Fecha con formato inesperado: " + fechaStr);
                    }
                }

                // 3. Construimos el objeto 'presupuesto' y le pasamos el cliente y el auto
                presupuesto presu = new presupuesto(
                        rs.getInt("idPresupuesto"),
                        fecha,
                        null,
                        rs.getString("t_trabajo"),
                        rs.getString("t_pintura"),
                        rs.getInt("d_chapa"),
                        rs.getFloat("costo_total"),
                        cli, // Le pasamos el objeto cliente completo
                        aut,
                        null// Le pasamos el objeto auto completo
                );

                // 4. Finalmente, construimos el objeto 'ordentrabajo'
                ordentrabajo orden = new ordentrabajo(
                        rs.getInt("orden_id"),
                        ordentrabajo.Estado.fromInt(rs.getInt("estado")),
                        LocalDate.parse(rs.getString("fecha_inicio")), // Asumiendo que se guarda como YYYY-MM-DD
                        null, // fecha_final (si la tienes)
                        presu, // Le pasamos el objeto presupuesto completo
                        new ArrayList<>() // lista de tareas (si la usas)
                );

                lista.add(orden);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las órdenes de trabajo pendientes: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}