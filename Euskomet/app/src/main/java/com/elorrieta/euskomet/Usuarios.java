package com.elorrieta.euskomet;

import java.io.Serializable;

public class Usuarios implements Serializable {

    private int cod_usuario;
    private String nombre;
    private String contraseña;
    private String p_clave;

    public Usuarios(int cod_usuario, String nombre, String contraseña, String p_clave){
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.p_clave = p_clave;
        this.cod_usuario = cod_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getP_clave() {
        return p_clave;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setP_clave(String p_clave) {
        this.p_clave = p_clave;
    }

    public int getCod_usuario() {
        return cod_usuario;
    }

    public void setCod_usuario(int cod_usuario) {
        this.cod_usuario = cod_usuario;
    }
}
