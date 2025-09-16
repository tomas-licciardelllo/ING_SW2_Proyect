package main.java.clases.model;

import java.util.List;

public class seguro {
    private String nombre;
    private String direccion;
    private int numero;
    private List<auto> autos;

    public seguro(String nombre, String direccion, int numero, List<auto> autos) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.numero = numero;
        this.autos = autos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<auto> getAutos() {
        return autos;
    }

    public void setAutos(List<auto> autos) {
        this.autos = autos;
    }
}
