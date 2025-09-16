package main.java.clases.model;

import java.util.List;

public class auto {
    private String tipo;
    private String marca;
    private String modelo;
    private int anio;
    private String patente;
    private seguro seguro;
    private List<parte> partes;


    //Constructor 1 sin el Seguro
    public auto(String tipo, List<parte> partes, String patente, int anio, String marca, String modelo) {
        this.tipo = tipo;
        this.partes = partes;
        this.patente = patente;
        this.anio = anio;
        this.marca = marca;
        this.modelo = modelo;
    }

    //Constructor 2 con el Seguro
    public auto(String tipo, List<parte> partes, seguro seguro, String patente, int anio, String modelo, String marca) {
        this.tipo = tipo;
        this.partes = partes;
        this.seguro = seguro;
        this.patente = patente;
        this.anio = anio;
        this.modelo = modelo;
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public seguro getSeguro() {
        return seguro;
    }

    public void setSeguro(seguro seguro) {
        this.seguro = seguro;
    }

    public List<parte> getPartes() {
        return partes;
    }

    public void setPartes(List<parte> partes) {
        this.partes = partes;
    }
}
