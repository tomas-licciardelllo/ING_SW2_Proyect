package main.java.clases.model;

import java.util.ArrayList;
import java.util.List;

public class cliente {
    private String nombre;
    private String telefono;
    private List<auto> autos;
    private List<presupuesto> presupuestos;

    public cliente(String nombre, String telefono, List<auto> autos, List<presupuesto> presupuestos) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.autos = autos;
        this.presupuestos = presupuestos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<presupuesto> getPresupuestos() {
        return presupuestos;
    }

    public void setPresupuestos(List<presupuesto> presupuestos) {
        this.presupuestos = presupuestos;
    }

    public List<auto> getAutos() {
        return autos;
    }

    public void setAutos(List<auto> autos) {
        this.autos = autos;
    }

    /*public String insCliente()
    {
        return "INSERT INTO persona(nombre,telefono,seguro) VALUES('"
                + nombre+"','"+Telefono+"','"+Seguro+"')";
    }*/

}
