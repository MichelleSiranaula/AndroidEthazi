package com.elorrieta.euskomet;

import android.util.Log;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientThreadFoto implements Runnable {
    private byte fotoDb [] = null;
    private String sql="";

    public ClientThreadFoto (String sql) {
        this.sql = sql;
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
            sIP = "192.168.1.136";
            sPuerto = "3306";
            sBBDD = "euskomet_db";
            String url = "jdbc:mysql://" + sIP + ":" + sPuerto + "/" + sBBDD + "?serverTimezone=UTC";
            con = DriverManager.getConnection( url, "user1", "");
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                if (rs != null) {
                    Blob blob = rs.getBlob(1);
                    int blobSize = (int)blob.length();
                    Log.i("sizeBLOB",blobSize+"");
                    fotoDb = blob.getBytes(1,blobSize);
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

    public byte[] getFotoDb() {
        return fotoDb;
    }
}