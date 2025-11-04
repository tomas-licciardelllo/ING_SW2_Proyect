package clases.model;

import java.util.List;

public class tarea {
    private int id;
    private String descripcion;
    private List<ordentrabajo> ordenes;
    private empleado empleado;

    public tarea(String descripcion, List<ordentrabajo> ordenes, empleado empleado) {
        this.descripcion = descripcion;
        this.ordenes = ordenes;
        this.empleado = empleado;
    }

    public tarea(String descripcion, empleado empleado) {
        this.descripcion = descripcion;
        this.empleado = empleado;
    }

    public void setId(int id){this.id = id;}

    public tarea(String descripcion) {
        this.descripcion = descripcion;
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

    public String getTareasComoString(List<tarea> tareas) {
        if (tareas == null || tareas.isEmpty()) {
            return "No hay tareas asignadas.";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tareas.size(); i++) {
            sb.append(tareas.get(i).toString());
            if (i < tareas.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    public String toString() {
        // Ejemplo: "Lijar puerta (Reparación general)"
        return this.getDescripcion();
    }
}
