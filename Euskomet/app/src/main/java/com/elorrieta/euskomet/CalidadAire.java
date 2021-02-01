package com.elorrieta.euskomet;

public class CalidadAire {

    private String fecha_hora;
    private String calidad;
    private double pm25;
    private double pm10;
    private double so2;
    private double no2;
    private double o3;
    private double co;
    private int cod_estacion;

    public CalidadAire(String fecha_hora, String calidad, double pm25, double pm10, double so2, double no2, double o3, double co, int cod_estacion) {
        this.fecha_hora = fecha_hora;
        this.calidad = calidad;
        this.cod_estacion = cod_estacion;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.so2 = so2;
        this.no2 = no2;
        this.o3 = o3;
        this.co = co;
    }

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

    public double getPm25() {
        return pm25;
    }

    public void setPm25(double pm25) {
        this.pm25 = pm25;
    }

    public double getPm10() {
        return pm10;
    }

    public void setPm10(double pm10) {
        this.pm10 = pm10;
    }

    public double getSo2() {
        return so2;
    }

    public void setSo2(double so2) {
        this.so2 = so2;
    }

    public double getNo2() {
        return no2;
    }

    public void setNo2(double no2) {
        this.no2 = no2;
    }

    public double getO3() {
        return o3;
    }

    public void setO3(double o3) {
        this.o3 = o3;
    }

    public double getCo() {
        return co;
    }

    public void setCo(double co) {
        this.co = co;
    }
}
