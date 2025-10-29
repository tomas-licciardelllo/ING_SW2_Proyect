package clases.dao;

import clases.control.Conexion;
import clases.model.auto;
import clases.model.cliente;
import clases.model.ordentrabajo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutoDAO implements dao<auto>{

    @Override
    public boolean create (auto a){
        String sql = "INSERT INTO auto(marca,modelo,anio, patente, duenioID) VALUES (?,?,?,?,?)";
        Connection conn = Conexion.getInstance().getConnection();
        try(
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
        String sql = "UPDATE auto SET marca = ?, modelo = ?, anio = ?, patente = ? WHERE aID = ?";

        Connection conn = Conexion.getInstance().getConnection(); try(

            PreparedStatement pstmt =  conn.prepareStatement(sql)){
                pstmt.setString(1,a.getMarca());
                pstmt.setString(2,a.getModelo());
                pstmt.setInt(3,a.getAnio());
                pstmt.setString(4,a.getPatente());
                pstmt.setInt(5,a.getIdBD());
                pstmt.executeUpdate();
                return true;
        }
        catch (SQLException e){
            System.out.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id){
        return true;
    }

    @Override
    public auto read(int id){
        String sql = "SELECT aID,marca,modelo,anio,patente FROM auto WHERE aID = ?";
        auto a =null;

        try(Connection conn = Conexion.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setInt(1,id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                a = new auto(
                        rs.getInt("aID"),
                        "camioneta" ,
                        rs.getString("patente"),
                        rs.getInt("anio"),
                        rs.getString("marca"),
                        rs.getString("modelo")
                );
            }

        }
        catch(SQLException e){
            System.out.println("Error al leer cliente: " + e.getMessage());
        }
        //auto aux = new auto("f","p", 23,"j","d");
        return  a;
    }

    @Override
    public List<auto> getAll(){
        List<auto> lista = new ArrayList<>();
        String sql = "SELECT a.aID, a.marca, a.modelo, a.anio, a.patente, a.duenioID, " +
                "c.id AS idCliente, c.nombre AS nombreCliente, c.telefono AS telCliente " +
                "FROM auto a " +
                "LEFT JOIN persona c ON a.duenioID = c.id";

        Connection conn = Conexion.getInstance().getConnection(); try(

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
                        rs.getInt("aID"),
                        rs.getString("marca"),
                        rs.getString("patente"),
                        rs.getInt("anio"),
                        rs.getString("marca"),
                        rs.getString("modelo")
                );

                a.setCliente(c);
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

        try(Connection conn = Conexion.getInstance().getConnection();
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

    public int obtenerOcrearAutoPorPatente(auto auto) {
        String sqlSelect = "SELECT aID FROM auto WHERE patente = ?";
        Connection conn = Conexion.getInstance().getConnection(); try(

             PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {

            stmtSelect.setString(1, auto.getPatente());
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                System.out.println("Auto encontrado. ID: " + rs.getInt("aID"));
                return rs.getInt("aID");
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar el auto: " + e.getMessage());
            e.printStackTrace();
            return -1; // Devolvemos -1 para indicar un error
        }
        String sqlInsert = "INSERT INTO auto (marca, modelo, anio, patente, tipo) VALUES (?, ?, ?, ?, ?)";

        //Connection conn = Conexion.getInstance().getConnection();
        try(

             // Pedimos que nos devuelva las claves generadas (el nuevo ID)
             PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {

            stmtInsert.setString(1, auto.getMarca());
            stmtInsert.setString(2, auto.getModelo());
            stmtInsert.setInt(3, auto.getAnio());
            stmtInsert.setString(4, auto.getPatente());
            stmtInsert.setString(5, auto.getTipo());

            int affectedRows = stmtInsert.executeUpdate();

            if (affectedRows > 0) {
                // Obtenemos el ID del auto recién creado
                try (ResultSet generatedKeys = stmtInsert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int nuevoId = generatedKeys.getInt(1);
                        System.out.println("Auto nuevo creado. ID: " + nuevoId);
                        return nuevoId;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al crear el auto: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

    public List<auto> getAutosByClienteId(int clienteId) {
        List<auto> lista = new ArrayList<>();
        String sql = "SELECT aID, marca, modelo, anio, patente, tipo FROM auto WHERE duenioID = ?";

        Connection conn = Conexion.getInstance().getConnection(); try(

             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new auto(
                        rs.getString("tipo"),
                        rs.getString("patente"),
                        rs.getInt("anio"),
                        rs.getString("marca"),
                        rs.getString("modelo")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener autos por cliente: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public int createAndGetID(auto a){
        String sqlSelect = "SELECT aID FROM auto WHERE patente = ?";

        Connection conn = Conexion.getInstance().getConnection();
        try(

             PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {

            stmtSelect.setString(1, a.getPatente());
            ResultSet rs = stmtSelect.executeQuery();

            // 2. Si se encuentra, devolvemos su ID y terminamos el método.
            if (rs.next()) {
                int idExistente = rs.getInt("aID");
                System.out.println("Vehículo encontrado con patente " + a.getPatente() + ". ID: " + idExistente);
                return idExistente;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar el auto: " + e.getMessage());
            e.printStackTrace();
            return -1; // Devolvemos -1 para indicar un error
        }

        // 3. Si llegamos a este punto, el auto no existe. Procedemos a insertarlo.
        // Asegúrate de que tu tabla 'auto' y las columnas sean correctas.
        String sqlInsert = "INSERT INTO auto (marca, modelo, anio, patente, tipo, duenioID) VALUES (?, ?, ?, ?, ?, ?)";

        //Connection conn = Conexion.getInstance().getConnection();
        try(

             // Le pedimos a JDBC que nos devuelva las claves generadas (el nuevo aID)
             PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {

                stmtInsert.setString(1,a.getMarca());
                stmtInsert.setString(2,a.getModelo());
                stmtInsert.setInt(3, a.getAnio());
                stmtInsert.setString(4,a.getPatente());
                int affectedRows = stmtInsert.executeUpdate();
            if (affectedRows > 0) {
                // Obtenemos el ID del auto recién insertado
                try (ResultSet generatedKeys = stmtInsert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int nuevoId = generatedKeys.getInt(1);
                        System.out.println("Vehículo nuevo insertado con patente " + a.getPatente() + ". Nuevo ID: " + nuevoId);
                        return nuevoId; // Devolvemos el nuevo ID
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al crear el auto: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }
}
