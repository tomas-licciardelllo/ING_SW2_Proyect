package clases.control;
import clases.gui.ClienteScreen;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import clases.model.cliente;

import java.sql.*;

public class Conexion{


    private static Conexion INSTANCE = null;
    private static final String URL = "jdbc:sqlite:" + System.getProperty("user.dir") + "/code/main/resources/TDB.db";
    private Connection conn = null;

    private Conexion(){
        try {
                conn = DriverManager.getConnection(URL);
                System.out.println("CONEXION ESTABLECIDA\n"); // ESTO DESPUÉS SE BORRA
        }catch (SQLException e)
        {
            System.out.println("NO SE A PODIO ESTABLECER LA CONEXION: "+e);
        }
    }
/*
    private static void createInstance(){

        INSTANCE = new Conexion();
    }*/
    public static Conexion getInstance(){ // LO CAMBIE SÓLO PARA VER SI FUNCIONA, DESPUÉS SI QUIEREN HACEMOS LA FUNCIÓN DE 1 LINEA QUE HABÍA HECHO LEO
        if(INSTANCE == null)
        {
            INSTANCE = new Conexion();
        }
        return INSTANCE;
    }

    public Connection getConnection(){
        return conn;
    }


}
