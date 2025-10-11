package clases.model;

import java.util.List;

public class parte {
    private String nombre;
    private float precio;
    private boolean reparacion;
    private boolean cambio;
    private presupuesto presupuesto;
    private int idPresupuesto;  //Para la base de Datos

    public parte(String nombre, float precio, boolean reparacion, boolean cambio) {
        this.nombre = nombre;
        this.precio = precio;
        this.reparacion = reparacion;
        this.cambio = cambio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public boolean isReparacion() {
        return reparacion;
    }

    public void setReparacion(boolean reparacion) {
        this.reparacion = reparacion;
    }

    public boolean isCambio() {
        return cambio;
    }

    public void setCambio(boolean cambio) {
        this.cambio = cambio;
    }
}
