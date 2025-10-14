package clases.model;
import java.time.*;
import java.util.List;

public class ordentrabajo {
    public enum Estado {Pendiente, Desarrollo, Finalizada;
        public static Estado fromInt(int value) {
            return switch (value) {
                case 0 -> Pendiente;
                case 1 -> Desarrollo;
                case 2 -> Finalizada;
                default -> throw new IllegalArgumentException("Valor inválido para Estado: " + value);
            };
        }
        public int toInt() {
            return this.ordinal();
        }
    };
    private int id;
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

    public ordentrabajo(int id, Estado estado, LocalDate fecha_inicio, LocalDate fecha_final, presupuesto presupuesto, List<tarea> tareas) {
        this.id = id;
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

    public int getNumero() {
        return switch(this.estado){
            case Pendiente -> 1;
            case Desarrollo -> 2;
            case Finalizada -> 3;
        };
    }

    /*
    GETTERS Y SETTERS UNICAMENTE PARA MOSTRAR
     */

    public int getNumeroOrden() {
        return this.presupuesto.getNumero();
    }

    public int getID(){return id;}

    public LocalDate getFechaIngreso() {
        return this.fecha_inicio;
    }

    public String getClienteNombre() {
        return this.presupuesto.getCliente().getNombre();
    }

    public String getVehiculoMarca() {
        return this.presupuesto.getAuto().getMarca();
    }

    public String getVehiculoModelo() {
        return this.presupuesto.getAuto().getModelo();
    }

    public String getVehiculoPat() {
        return this.presupuesto.getAuto().getPatente();
    }

    public int getId()
    {
        return this.id;
    }
}

