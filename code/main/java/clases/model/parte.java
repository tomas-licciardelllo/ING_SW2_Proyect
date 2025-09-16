package main.java.clases.model;

import java.util.List;

public class parte {
    private List<auto> autos;
    private String nombre;

    public parte(List<auto> autos, String nombre) {
        this.autos = autos;
        this.nombre = nombre;
    }

    public List<auto> getAutos() {
        return autos;
    }

    public void setAutos(List<auto> autos) {
        this.autos = autos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
