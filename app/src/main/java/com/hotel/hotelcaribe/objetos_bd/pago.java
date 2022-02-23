package com.hotel.hotelcaribe.objetos_bd;

public class pago {
    private String numero;
    private String nombre;
    private String fecha;
    private int codigo;
    private String direccion;
    private long cedula;
    private String tipoId;

    public pago(){

    }

    public pago(String numero, String nombre, String fecha, int codigo, String direccion, long cedula, String tipoId) {
        this.numero = numero;
        this.nombre = nombre;
        this.fecha = fecha;
        this.codigo = codigo;
        this.direccion = direccion;
        this.cedula = cedula;
        this.tipoId = tipoId;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public long getCedula() {
        return cedula;
    }

    public void setCedula(long cedula) {
        this.cedula = cedula;
    }

    public String getTipoId() {
        return tipoId;
    }

    public void setTipoId(String tipoId) {
        this.tipoId = tipoId;
    }
}
