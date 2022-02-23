package com.hotel.hotelcaribe.objetos_bd;

public class sucursal {
    private String nombre;
    private int id;
    private String url;

    public sucursal(){

    }

    public sucursal(String nombre, int id, String url) {
        this.nombre = nombre;
        this.id = id;
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
