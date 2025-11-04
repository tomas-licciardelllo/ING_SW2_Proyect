package clases.dao;
import clases.control.Conexion;
import clases.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdenDAO implements dao<ordentrabajo> {
    @Override
    public boolean create(ordentrabajo o) {
        String sql = "INSERT INTO orden_trabajo(fecha_inicio, fecha_fin, estado, pID) VALUES (?,?,?,?)";

        Connection conn = Conexion.getInstance().getConnection(); try(

             PreparedStatement prep = conn.prepareStatement(sql)){
            prep.setString(1, String.valueOf(Date.valueOf(o.getFecha_inicio())));
            prep.setString(2,String.valueOf(Date.valueOf(o.getFecha_final())));
            prep.setInt(3, o.getEstado().toInt());
            prep.setInt(4, o.getPresupuesto().getNumero());
            prep.executeUpdate();
            return  true;

        }catch (SQLException e)
        {
            System.out.println("Error al insertar orden de Trabajo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int createAndGetID(ordentrabajo ordentrabajo) {
        int presupuestoID = ordentrabajo.getPresupuesto().getNumero();

        String sqlInsert = "INSERT INTO orden_trabajo (fecha_inicio, fecha_fin, estado, pID) VALUES (?, ?, ?, ?)";

        Connection conn = Conexion.getInstance().getConnection();

        try (PreparedStatement pstInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            pstInsert.setObject(1, ordentrabajo.getFecha_inicio());
            pstInsert.setObject(2, ordentrabajo.getFecha_final());
            pstInsert.setInt(3, ordentrabajo.getEstado().toInt());
            pstInsert.setInt(4, presupuestoID);

            int filasAfectadas = pstInsert.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("Fallo al crear la orden, 0 filas afectadas.");
            }

            try (ResultSet r = pstInsert.getGeneratedKeys()) {
                if (r.next()) {
                    return r.getInt(1);
                } else {
                    throw new SQLException("Fallo al crear la orden, no se obtuvo ID.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la orden de trabajo: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public boolean updateEstado(int idOrden, ordentrabajo.Estado estado) {
        String sql = "UPDATE orden_trabajo SET estado = ? WHERE id = ?";
        Connection conn = Conexion.getInstance().getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, estado.Pendiente.toInt());
            pst.setInt(2, idOrden);
            int filasAfectadas = pst.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar estado de orden: " + e.getMessage());
            e.printStackTrace();
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
        String sql = "SELECT id, fecha_inicio,fecha_fin, estado, pID FROM orden_trabajo WHERE id = ?";
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
        String sql = "SELECT id,fecha_inicio,fecha_fin, estado, pID FROM orden_trabajo";
        Connection conn = Conexion.getInstance().getConnection();
        try(Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int estadoNum = rs.getInt("estado");
                ordentrabajo.Estado estado = ordentrabajo.Estado.fromInt(estadoNum);
                int presupuestoId = rs.getInt("pID");
                presupuesto p = new PresupuestoDAO().read(presupuestoId);
                List<tarea> tareas = getAllTrabajos(rs.getInt("id"));
                String fechaInicioAux = rs.getString("fecha_inicio");
                LocalDate fechaInicio = null;
                if (fechaInicioAux != null && !fechaInicioAux.isEmpty()) {
                    try {
                        fechaInicio = LocalDate.parse(fechaInicioAux);
                    } catch (Exception e) {
                        System.out.println("Fecha con formato inesperado: " + fechaInicio);
                    }
                }
                String fechaFinAux = rs.getString("fecha_fin");
                LocalDate fechaFin = null;
                if (fechaFinAux != null && !fechaFinAux.isEmpty()) {
                    try {
                        fechaFin = LocalDate.parse(fechaFinAux);
                    } catch (Exception e) {
                        System.out.println("Fecha con formato inesperado: " + fechaInicio);
                    }
                }
                else{
                    //Como algunas ordenes no tienen fecha, le di una random
                    fechaFin = fechaInicio;
                }
                lista.add(new ordentrabajo(
                                rs.getInt("id"),
                                estado,
                                fechaInicio,
                                fechaFin,
                                p,
                                tareas
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

    public List<ordentrabajo> getDesarrollo() {
        List<ordentrabajo> lista = new ArrayList<>();

        String sql = "SELECT " +
                "    ot.id AS orden_id, ot.fecha_inicio, ot.estado, " +
                "    p.idPresupuesto, p.t_trabajo, p.t_pintura, p.d_chapa, p.costo_total," +
                "    c.id AS cliente_id, c.nombre AS cliente_nombre, c.telefono AS cliente_telefono, " +
                "    a.aID AS auto_id, a.marca, a.modelo, a.anio, a.patente, a.tipo " +
                "FROM orden_trabajo ot " +
                "JOIN presupuesto p ON ot.pID = p.idPresupuesto " +
                "JOIN persona c ON p.id_cliente = c.id " +
                "JOIN auto a ON p.id_auto = a.aID " +
                "WHERE ot.estado = 1";

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

    public List<tarea> getAllTrabajos(int idOrden) {
        List<tarea> lista = new ArrayList<>();
        empleadoDAO empdao = new empleadoDAO();
        String sql = "SELECT T.descripcion, T.empleadoCargo " +
                "FROM tareas T " +
                "JOIN trabajos TR ON T.id = TR.tareaRealizar " +
                "JOIN orden_trabajo OT ON TR.ordenPertenece = OT.id " +
                "WHERE OT.id = ?;";
        Connection conn = Conexion.getInstance().getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idOrden);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String descripcionTarea = rs.getString("descripcion");
                    long dniEmp = rs.getLong("empleadoCargo");
                    empleado emp = empdao.getbyDNI(dniEmp);
                    tarea t = new tarea(descripcionTarea, new ArrayList<>(), emp);
                    lista.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ordentrabajo getPorPresupuesto(int idPresupuesto) {
        ordentrabajo respuesta = null;
        PresupuestoDAO presupuestoDAO = new PresupuestoDAO();
        presupuesto presu = presupuestoDAO.read(idPresupuesto);

        // ✅ Buscar CUALQUIER orden, incluso las de estado -1
        String sql = "SELECT * FROM orden_trabajo WHERE pID = ?";
        Connection conn = Conexion.getInstance().getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idPresupuesto);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int idOrden = rs.getInt("id");
                    List<tarea> lista = getAllTrabajos(idOrden); // ✅ Corregido

                    respuesta = new ordentrabajo(
                            idOrden,
                            ordentrabajo.Estado.fromInt(rs.getInt("estado")),
                            LocalDate.parse(rs.getString("fecha_inicio")),
                            LocalDate.parse(rs.getString("fecha_fin")),
                            presu,
                            lista
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar orden por pID: " + e.getMessage());
            e.printStackTrace();
        }
        return respuesta;
    }
}