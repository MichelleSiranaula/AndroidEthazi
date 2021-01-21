package com.elorrieta.euskomet;

import java.io.Serializable;

public class FavMunicipio implements Serializable {

    private int cod_muni;
    private int cod_usuario;

    public FavMunicipio(int cod_muni, int cod_usuario) {
        this.cod_muni = cod_muni;
        this.cod_usuario = cod_usuario;
    }

    public int getCod_muni() {
        return cod_muni;
    }

    public void setCod_muni(int cod_muni) {
        this.cod_muni = cod_muni;
    }

    public int getCod_usuario() {
        return cod_usuario;
    }

    public void setCod_usuario(int cod_usuario) {
        this.cod_usuario = cod_usuario;
    }
}
