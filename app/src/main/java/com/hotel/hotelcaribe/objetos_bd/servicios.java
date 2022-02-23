package com.hotel.hotelcaribe.objetos_bd;

public class servicios {
    private int id;
    private String info;
    private int n_personas;
    private String nombre;
    private int precio;
    private String url;

    public servicios(){

    }

    public servicios(int id, String info, int n_personas, String nombre, int precio, String url) {
        this.id = id;
        this.info = info;
        this.n_personas = n_personas;
        this.nombre = nombre;
        this.precio = precio;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getN_personas() {
        return n_personas;
    }

    public void setN_personas(int n_personas) {
        this.n_personas = n_personas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
