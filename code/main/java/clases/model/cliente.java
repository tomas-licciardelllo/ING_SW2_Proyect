package com.example.prueba;

public class cliente {
    private String nombre;
    private String Telefono;
    private String Seguro;

    public cliente(String nombre, String telefono, String seguro) {
        this.nombre = nombre;
        Telefono = telefono;
        Seguro = seguro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getSeguro() {
        return Seguro;
    }

    public void setSeguro(String seguro) {
        Seguro = seguro;
    }

    public String insCliente()
    {
        return "INSERT INTO persona(nombre,telefono,seguro) VALUES('"
                + nombre+"','"+Telefono+"','"+Seguro+"')";
    }

}
