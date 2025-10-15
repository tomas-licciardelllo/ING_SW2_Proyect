package clases.model;

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
        this.telefono = telefono;
        this.autos = autos;
        this.presupuestos = presupuestos;
    }
    public cliente(int id, String nombre, String telefono, List<auto> autos, List<presupuesto> presupuestos) {
        this.idBD = id;
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

    public  void setIdBD(int id){this.idBD = id;}

    public int getIdBD(){return idBD;}
    

}
