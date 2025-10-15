package clases.dao;
import clases.control.Conexion;
import clases.model.cliente;
import clases.model.ordentrabajo;
import clases.model.presupuesto;
import clases.model.tarea;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdenDAO implements dao<ordentrabajo> {
    @Override
    public boolean create(ordentrabajo o) {
        String sql = "INSERT INTO orden_trabajo(fecha_inicio,fecha_fin, tareas, estado, pID) VALUES (?,?,?,?,?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement prep = conn.prepareStatement(sql)){
            prep.setInt(1, o.getNumeroOrden());
            prep.setString(1, String.valueOf(Date.valueOf(o.getFecha_inicio())));
            prep.setString(2,String.valueOf(Date.valueOf(o.getFecha_final())));
            prep.setString(3, o.getTareas().toString());
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

        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
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
        try (Connection conn = Conexion.getConnection();
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

    public List<ordentrabajo> getPendientes() {
        List<ordentrabajo> lista = new ArrayList<>();
        String sql = "SELECT id, fecha_inicio,fecha_fin, tareas, estado, pID FROM orden_trabajo WHERE estado = 0";
        try (Connection conn = Conexion.getConnection();
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
    }
}