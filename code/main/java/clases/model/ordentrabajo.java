package main.java.clases.model;
import java.time.*;
import java.util.List;

public class ordentrabajo {
    public enum Estado {Pendiente, Desarrollo, Finalizada};

    private Estado estado;
    private LocalDate fecha_inicio;
    private LocalDate fecha_final;
    private presupuesto presupuesto;
    private List<tarea> tareas;

    public ordentrabajo(Estado estado, LocalDate fecha_inicio, LocalDate fecha_final, presupuesto presupuesto, List<tarea> tareas) {
        this.estado = estado;
        this.fecha_inicio = fecha_inicio;
        this.fecha_final = fecha_final;
        this.presupuesto = presupuesto;
        this.tareas = tareas;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public LocalDate getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(LocalDate fecha_final) {
        this.fecha_final = fecha_final;
    }

    public LocalDate getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDate fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public List<tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<tarea> tareas) {
        this.tareas = tareas;
    }
}

