package com.elorrieta.euskomet;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientThread implements Runnable {
    private String sql = "";
    private String tipoObjeto = "";

    ArrayList<Object> datos = new ArrayList<Object>();

    public ClientThread(String sql, String tipoObjeto) {
        this.sql = sql;
        this.tipoObjeto = tipoObjeto;
    }

    @Override
    public void run() {
        ResultSet rs = null;
        PreparedStatement st = null;
        Connection con = null;
        String sIP;
        String sPuerto;
        String sBBDD;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            //IP de clase
            //sIP = "192.168.106.28";
            //IP en casa
            sIP = "172.0.0.1";
            sPuerto = "3306";
            sBBDD = "euskomet_db";
            String url = "jdbc:mysql://" + sIP + ":" + sPuerto + "/" + sBBDD + "?serverTimezone=UTC";
            con = DriverManager.getConnection( url, "user1", "");
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                if (tipoObjeto.equals("Municipio")) {
                    Municipio m = new Municipio(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
                    datos.add(m);
                } else if (tipoObjeto.equals("Provincia")) {
                    Provincia p = new Provincia(rs.getInt(1), rs.getString(2));
                    datos.add(p);
                } else if (tipoObjeto.equals("Espacios")) {
                    EspaciosNaturales e = new EspaciosNaturales(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7), rs.getInt(8));
                    datos.add(e);
                }
            }
        } catch (ClassNotFoundException e) {
            Log.e("ClassNotFoundException", "");
            e.printStackTrace();
        } catch (SQLException e) {
            Log.e("SQLException", "");
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("Exception", "");
            e.printStackTrace();
        } finally {
            try {
                if(rs!=null) {
                    rs.close();
                }
                if(st!=null) {
                    st.close();
                }
                if(con!=null) {
                    con.close();
                }
            } catch (Exception e) {
                Log.e("Exception_cerrando todo", "");
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Object> getDatos() {
        return datos;
    }
}

