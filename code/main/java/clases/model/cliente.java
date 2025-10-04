package clases.model;

public class cliente {
    private String nombre;
    private String Telefono;
    private String Seguro;

    public cliente(String nombre, String telefono, String seguro) {

import java.util.ArrayList;
import java.util.List;

public class cliente {
    private String nombre;
    private String telefono;
    private List<auto> autos;
    private List<presupuesto> presupuestos;
    private int idBD;
    public cliente(String nombre, String telefono, List<auto> autos, List<presupuesto> presupuestos) {
        this.nombre = nombre;
        Telefono = telefono;
        Seguro = seguro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getSeguro() {
        return Seguro;
    }

    public void setSeguro(String seguro) {
        Seguro = seguro;
    }

    public String insCliente()
    {
        return "INSERT INTO persona(nombre,telefono,seguro) VALUES('"
                + nombre+"','"+Telefono+"','"+Seguro+"')";
    public List<auto> getAutos() {
        return autos;
    }

    public void setAutos(List<auto> autos) {
        this.autos = autos;
    }

    public  void setIdBD(int id){this.idBD = id;}

    public int getIdBD(){return idBD;}

    public String insCliente()
    {
        return "INSERT INTO persona(nombre,telefono) VALUES('"
                + nombre+"','"+telefono+"')";
    }

}
