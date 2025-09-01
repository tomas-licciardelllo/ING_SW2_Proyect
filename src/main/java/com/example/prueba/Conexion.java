package com.example.prueba;
import java.sql.*;

public class Conexion{

    private static final String URL = "jdbc:sqlite:"+System.getProperty("user.dir") + "/src/main/resources/TDB.db";
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
        PreparedStatement stmnt = null;
        try
        {
            Connection conn= getConnection();
            stmnt = conn.prepareStatement(SQL);
            stmnt.executeUpdate();
            System.out.println("ELE TE A\n");
        }catch (SQLException e)
        {
            System.out.println("!(ELE TE A):" + e);
        }
        conn = closeConnection();
    }
}
