package main.java.clases.model;

import java.util.List;

public class tarea {
    private String descripcion;
    private List<ordentrabajo> ordenes;
    private empleado empleado;

    public tarea(String descripcion, List<ordentrabajo> ordenes, empleado empleado) {
        this.descripcion = descripcion;
        this.ordenes = ordenes;
        this.empleado = empleado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<ordentrabajo> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<ordentrabajo> ordenes) {
        this.ordenes = ordenes;
    }

    public empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(empleado empleado) {
        this.empleado = empleado;
    }
}
