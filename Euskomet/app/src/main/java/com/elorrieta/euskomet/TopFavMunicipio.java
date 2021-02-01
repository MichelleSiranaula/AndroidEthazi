package com.elorrieta.euskomet;

public class TopFavMunicipio {

    private int cant;
    private int cod_muni;

    public TopFavMunicipio(int cant, int cod_muni) {
        this.cant = cant;
        this.cod_muni = cod_muni;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }

    public int getCod_muni() {
        return cod_muni;
    }

    public void setCod_muni(int cod_muni) {
        this.cod_muni = cod_muni;
    }
}
