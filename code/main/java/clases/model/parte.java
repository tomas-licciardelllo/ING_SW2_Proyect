package clases.model;

import java.util.List;

public class parte {
    private String nombre;
    private boolean cambio;
    private float panioPintura;
    private presupuesto presupuesto;
    private int idPresupuesto;  //Para la base de Datos

    public parte(String nombre, float panioPintura, boolean cambio) {
        this.nombre = nombre;
        this.panioPintura = panioPintura;
        this.cambio = cambio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPanioPintura(){ return panioPintura; }

    public void setPanioPintura(float panioPintura){ this.panioPintura = panioPintura; }

    public presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public boolean isCambio() {
        return cambio;
    }

    public void setCambio(boolean cambio) {
        this.cambio = cambio;
    }

    @Override
    public String toString() {
        return this.nombre; // Devuelve solo el nombre
    }
}
