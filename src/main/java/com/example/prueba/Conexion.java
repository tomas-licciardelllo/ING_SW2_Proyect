package com.example.prueba;
import java.sql.*;

public class Conexion{

    private static final String URL = "jdbc:sqlite:C:\\Users\\Administrator\\IdeaProjects\\ING_SW2_Proyect\\TDB.db"; // COMO LO HICE DESDE LA CLI SE CREÓ EN SYS32 NO SE PREOCUPEN, DESPUÉS CUANDO LO PONEMOS EN LA CARPETA QUEDA.
    private static Connection conn = null;

    public Conexion (){}

    public static Connection getConnection(){ // LO CAMBIE SÓLO PARA VER SI FUNCIONA, DESPUÉS SI QUIEREN HACEMOS LA FUNCIÓN DE 1 LINEA QUE HABÍA HECHO LEO
        try
        {
            if (conn == null || conn.isClosed())
            {
                conn = DriverManager.getConnection(URL);
                System.out.println("CONESION ETAVLESIDA\n"); // ESTO DESPUÉS SE BORRA
            }
        }catch (SQLException e)
        {
            System.out.println("NO SE A PODIO HACER LA CONETSION: "+e);
        }
        return conn;
    }

    public static Connection closeConnection()
    {
        try
        {
            if (conn != null && !conn.isClosed())
            {
                conn.close();
                System.out.println("CERRAÁ LA WEA WEON\n");
            }
        }catch (SQLException e)
        {
            System.out.println("ERROR AL CERRAR LA CONETSION "+e);
        }
        return conn;
    }

    public void executeSQL(String SQL)
    {
        try(Connection conn= getConnection();
            Statement  stmnt = conn.createStatement())
        {
            stmnt.executeUpdate(SQL);
            System.out.println("ELE TE A\n");
        }catch (SQLException e)
        {
            System.out.println("!(ELE TE A):" + e);
            conn = closeConnection();
        }
    }
}
