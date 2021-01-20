package com.elorrieta.euskomet;

import java.io.Serializable;

public class Municipio implements Serializable {

    private int cod_muni;
    private String nombre;
    private String descripcion;
    private int cod_prov;
    private String foto;

    public Municipio(int cod_muni, String nombre, String descripcion, int cod_prov, String foto) {
        this.cod_muni = cod_muni;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cod_prov = cod_prov;
        this.foto = foto;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}

