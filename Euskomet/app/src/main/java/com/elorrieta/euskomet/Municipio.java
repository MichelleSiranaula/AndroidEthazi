package com.elorrieta.euskomet;

import java.io.Serializable;

public class Municipio implements Serializable {

    private int cod_muni;
    private String nombre;
    private String descripcion;
    private int cod_prov;
    private double latitud;
    private double longitud;

    public Municipio(int cod_muni, String nombre, String descripcion, int cod_prov, double longitud, double latitud) {
        this.cod_muni = cod_muni;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cod_prov = cod_prov;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public Municipio() {

    }

    public int getCod_muni() {
        return cod_muni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCod_prov() {
        return cod_prov;
    }

    public void setCod_muni(int cod_muni) {
        this.cod_muni = cod_muni;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCod_prov(int cod_prov) {
        this.cod_prov = cod_prov;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}

