package com.example.prueba;
import java.sql.*;

public class Conexion{

    private static final String URL = "...";
    private static final String USER = "...";   //Según la profe de ciberseguridad no hay que poner root || QUE LA MAMEISHON, a parte no vamos a tener que iniciar sesión
    private static final String PASSWORD = "...";

    public static  Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
}
