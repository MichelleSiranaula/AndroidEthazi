package com.elorrieta.euskomet;

import java.io.Serializable;

public class usuarios implements Serializable {

    private String nombre;
    private String contraseña;
    private String p_clave;

    public usuarios(String nombre,String contraseña,String p_clave){

        this.nombre = nombre;
        this.contraseña = contraseña;
        this.p_clave = p_clave;
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
}
