package main.java.clases.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class presupuesto {
    private String membrete;
    private int numero;
    private LocalDate fecha;
    private ArrayList<String> repuestos;
    private String tipoTrabajo;
    private String tipoPintura;
    private int diasChapa;
    private float costoTotal;
    private factura facturagen;
    private ordentrabajo ordentrabajo;
    private cliente cliente;
    private auto auto;


    //En el constructor no esta ni la orden ni la factura porque es 0 o más
    public presupuesto(String membrete, int numero, LocalDate fecha, ArrayList<String> repuestos, String tipoTrabajo, String tipoPintura, int diasChapa, float costoTotal, cliente cliente, auto auto) {
        this.membrete = membrete;
        this.numero = numero;
        this.fecha = fecha;
        this.repuestos = repuestos;
        this.tipoTrabajo = tipoTrabajo;
        this.tipoPintura = tipoPintura;
        this.diasChapa = diasChapa;
        this.costoTotal = costoTotal;
        this.cliente = cliente;
        this.auto = auto;
    }

    public String getMembrete() {
        return membrete;
    }

    public void setMembrete(String membrete) {
        this.membrete = membrete;
    }

    public auto getAuto() {
        return auto;
    }

    public void setAuto(auto auto) {
        this.auto = auto;
    }

    public cliente getCliente() {
        return cliente;
    }

    public void setCliente(cliente cliente) {
        this.cliente = cliente;
    }

    public ordentrabajo getOrdentrabajo() {
        return ordentrabajo;
    }

    public void setOrdentrabajo(ordentrabajo ordentrabajo) {
        this.ordentrabajo = ordentrabajo;
    }

    public factura getFacturagen() {
        return facturagen;
    }

    public void setFacturagen(factura facturagen) {
        this.facturagen = facturagen;
    }

    public float getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(float costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getTipoPintura() {
        return tipoPintura;
    }

    public void setTipoPintura(String tipoPintura) {
        this.tipoPintura = tipoPintura;
    }

    public int getDiasChapa() {
        return diasChapa;
    }

    public void setDiasChapa(int diasChapa) {
        this.diasChapa = diasChapa;
    }

    public ArrayList<String> getRepuestos() {
        return repuestos;
    }

    public void setRepuestos(ArrayList<String> repuestos) {
        this.repuestos = repuestos;
    }

    public String getTipoTrabajo() {
        return tipoTrabajo;
    }

    public void setTipoTrabajo(String tipoTrabajo) {
        this.tipoTrabajo = tipoTrabajo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
