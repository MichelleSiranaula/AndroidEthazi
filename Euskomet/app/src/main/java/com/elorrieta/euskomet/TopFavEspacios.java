package com.elorrieta.euskomet;

public class TopFavEspacios {

    private int cont;
    private int cod_espacios;

    public TopFavEspacios(int cont, int cod_espacios) {
        this.cont = cont;
        this.cod_espacios = cod_espacios;
    }

    public int getCont() {
        return cont;
    }

    public void setCont(int cont) {
        this.cont = cont;
    }

    public int getCod_espacios() {
        return cod_espacios;
    }

    public void setCod_espacios(int cod_espacios) {
        this.cod_espacios = cod_espacios;
    }
}
