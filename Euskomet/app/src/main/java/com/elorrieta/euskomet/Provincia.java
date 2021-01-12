package com.elorrieta.euskomet;

public class Provincia {

    private int cod_prov;
    private String nombreProv;

    public Provincia(int cod_prov, String nombreProv) {
        this.cod_prov = cod_prov;
        this.nombreProv = nombreProv;
    }

    public int getCod_prov() {
        return cod_prov;
    }

    public String getNombreProv() {
        return nombreProv;
    }

    public void setCod_prov(int cod_prov) {
        this.cod_prov = cod_prov;
    }

    public void setNombreProv(String nombreProv) {
        this.nombreProv = nombreProv;
    }
}
