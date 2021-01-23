package com.elorrieta.euskomet;

import java.io.Serializable;

public class EspaciosNaturales implements Serializable {

    private int cod_enatural;
    private String nombre;
    private String descripcion;
    private String tipo;
    private Double latitud;
    private Double longitud;
    private String foto;
    private int cod_prov;

    public EspaciosNaturales(int cod_enatural, String nombre, String descripcion, String tipo, Double latitud, Double longitud, String foto, int cod_prov) {
        this.cod_enatural = cod_enatural;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.foto = foto;
        this.cod_prov = cod_prov;
    }

    public EspaciosNaturales(int cod_enatural, String nombre, String descripcion, String tipo, Double latitud, Double longitud, String foto) {
        this.cod_enatural = cod_enatural;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.foto = foto;
    }

    public EspaciosNaturales() {

    }

    public int getCod_enatural() {
        return cod_enatural;
    }

    public void setCod_enatural(int cod_enatural) {
        this.cod_enatural = cod_enatural;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getCod_prov() {
        return cod_prov;
    }

    public void setCod_prov(int cod_prov) {
        this.cod_prov = cod_prov;
    }
}
