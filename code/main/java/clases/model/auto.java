package clases.model;

import java.util.List;

public class auto {
    private String tipo;
    private String marca;
    private String modelo;
    private int anio;
    private String patente;
    private seguro seguro;
    private int IdBD;
    private cliente cliente;

    //Constructor 1 sin el Seguro
    public auto(String tipo, String patente, int anio, String marca, String modelo) {
        this.tipo = tipo;
        this.patente = patente;
        this.anio = anio;
        this.marca = marca;
        this.modelo = modelo;
    }

    //Constructor 2 con el Seguro
    public auto(String tipo, seguro seguro, String patente, int anio, String modelo, String marca) {
        this.tipo = tipo;
        this.seguro = seguro;
        this.patente = patente;
        this.anio = anio;
        this.modelo = modelo;
        this.marca = marca;
    }

    //Constructor 3 con el idBD
    public auto(int id,String tipo, String patente, int anio, String marca, String modelo) {
        this.IdBD = id;
        this.tipo = tipo;
        this.patente = patente;
        this.anio = anio;
        this.marca = marca;
        this.modelo = modelo;
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

    public  void setIdBD(int id){this.IdBD = id;}

    public int getIdBD(){return IdBD;}

    public cliente getCliente() {return cliente;}

    public void setCliente(cliente cliente) {this.cliente = cliente;}
    @Override
    public String toString() {
        return "auto{" +
                "tipo='" + tipo + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", anio=" + anio +
                ", patente='" + patente + '\'' +
                ", seguro=" + seguro +
                ", IdBD=" + IdBD +
                ", cliente=" + cliente +
                '}';
    }
}
