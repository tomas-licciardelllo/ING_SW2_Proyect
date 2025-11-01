package clases.model;

import java.util.List;

public class parte {
    private String nombre;
    private boolean cambio;
    private float panioPintura;
    private presupuesto presupuesto;
    private int idPresupuesto;  //Para la base de Datos
    private int idDB;

    public parte(String nombre, float panioPintura, boolean cambio) {
        this.nombre = nombre;
        this.panioPintura = panioPintura;
        this.cambio = cambio;
    }

    public parte(String nombre){
        this.nombre = nombre;
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

    public int getIdDB() { return idDB; }

    public void setIdDB(int id) { this.idDB = id; }

    @Override
    public String toString() {
        return this.nombre; // Devuelve solo el nombre
    }

    public boolean isEmpty() {
        return false;
    }
    public static boolean intToBoolean (int i){
        return i==1;
    }

    public String parteRepuesto(){
        if (this.cambio){
            String respuesta = "Cambio de " + this.nombre;
            return respuesta;
        }
        else{
            String respuesta = "Reparación de " + this.nombre;
            return respuesta;
        }
    }

}
