package main.java.clases.model;
import java.time.*;
import java.util.Date;


public class factura {
    public enum tipofac {A, B, C, X};

    private int numero;
    private LocalDate fecha;
    private float total;
    private tipofac tipo;
    private presupuesto presupuestoasoc;

    public factura(int numero, presupuesto presupuestoasoc, float total, tipofac tipo, LocalDate fecha) {
        this.numero = numero;
        this.presupuestoasoc = presupuestoasoc;
        this.total = total;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public tipofac getTipo() {
        return tipo;
    }

    public void setTipo(tipofac tipo) {
        this.tipo = tipo;
    }

    public presupuesto getPresupuestoasoc() {
        return presupuestoasoc;
    }

    public void setPresupuestoasoc(presupuesto presupuestoasoc) {
        this.presupuestoasoc = presupuestoasoc;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
