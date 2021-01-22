package com.elorrieta.euskomet;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ClientThread implements Runnable {
    private String sql = "";
    private String tipoObjeto = "";

    public ClientThread(String sql) {
        this.sql = sql;
        this.tipoObjeto = tipoObjeto;
    }

    @Override
    public void run() {
        Statement st = null;
        Connection con = null;
        String sIP;
        String sPuerto;
        String sBBDD;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            //IP de clase
            sIP = "192.168.106.28";
            //IP en casa
            //sIP = "192.168.1.136";
            sPuerto = "3306";
            sBBDD = "euskomet_db";
            String url = "jdbc:mysql://" + sIP + ":" + sPuerto + "/" + sBBDD + "?serverTimezone=UTC";
            con = DriverManager.getConnection( url, "user1", "");
            st = con.createStatement();
            st.executeUpdate(sql);
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

}
