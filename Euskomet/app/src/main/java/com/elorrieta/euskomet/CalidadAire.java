package com.elorrieta.euskomet;

public class CalidadAire {

    private String fecha_hora;
    private String calidad;
    private int cod_estacion;

    public CalidadAire(String fecha_hora, String calidad, int cod_estacion) {
        this.fecha_hora = fecha_hora;
        this.calidad = calidad;
        this.cod_estacion = cod_estacion;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getCalidad() {
        return calidad;
    }

    public void setCalidad(String calidad) {
        this.calidad = calidad;
    }

    public int getCod_estacion() {
        return cod_estacion;
    }

    public void setCod_estacion(int cod_estacion) {
        this.cod_estacion = cod_estacion;
    }
}
