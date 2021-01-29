package com.elorrieta.euskomet;

public class Estacion {

    private int cod_estacion;
    private String nombreEstacion;
    private int cod_muni;

    public Estacion(int cod_estacion, String nombreEstacion, int cod_muni) {
        this.cod_estacion = cod_estacion;
        this.nombreEstacion = nombreEstacion;
        this.cod_muni = cod_muni;
    }

    public int getCod_estacion() {
        return cod_estacion;
    }

    public void setCod_estacion(int cod_estacion) {
        this.cod_estacion = cod_estacion;
    }

    public String getNombreEstacion() {
        return nombreEstacion;
    }

    public void setNombreEstacion(String nombreEstacion) {
        this.nombreEstacion = nombreEstacion;
    }

    public int getCod_muni() {
        return cod_muni;
    }

    public void setCod_muni(int cod_muni) {
        this.cod_muni = cod_muni;
    }
}
