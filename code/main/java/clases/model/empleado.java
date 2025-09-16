package main.java.clases.model;

import java.util.ArrayList;
import java.util.List;

public class empleado {
    private String nombre;
    private long documento;
    private List<tarea> tareas;

    public empleado(String nombre, List<tarea> tareas, long documento) {
        this.nombre = nombre;
        this.tareas = tareas;
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<tarea> tareas) {
        this.tareas = tareas;
    }

    public long getDocumento() {
        return documento;
    }

    public void setDocumento(long documento) {
        this.documento = documento;
    }
}
