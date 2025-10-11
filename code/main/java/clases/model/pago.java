package clases.model;

public class pago {
    private float pagoParcial;
    private presupuesto presupuesto;

    public pago(float pagoParcial) {
        this.pagoParcial = pagoParcial;
    }

    public float getPagoParcial() {
        return pagoParcial;
    }

    public void setPagoParcial(float pagoParcial) {
        this.pagoParcial = pagoParcial;
    }

    public presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }
}
