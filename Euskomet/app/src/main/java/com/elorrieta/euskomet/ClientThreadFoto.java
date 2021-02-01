package com.elorrieta.euskomet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientThreadFoto implements Runnable {
    private byte byteFoto [] = null;
    private String sql="";
    private String tipoFoto;
    private File foto;
    private Bitmap bitmap;
    private ArrayList<Bitmap> arrBitmap = new ArrayList<Bitmap>();

    public ClientThreadFoto (String sql, String tipoFoto) {
        this.sql = sql;
        this.tipoFoto = tipoFoto;
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
            sIP = "192.168.106.28";
            //IP en casa
            //sIP = "192.168.1.136";
            sPuerto = "3306";
            sBBDD = "euskomet_db";
            String url = "jdbc:mysql://" + sIP + ":" + sPuerto + "/" + sBBDD + "?serverTimezone=UTC";
            con = DriverManager.getConnection( url, "user1", "");
            if (tipoFoto.equals("InsertF")) {
                FileInputStream fileInput = new FileInputStream(foto);
                st = con.prepareStatement(sql);
                st.setBlob(1, fileInput, foto.length());
                st.executeUpdate();
            } else if (tipoFoto.equals("SelectF")) {
                st = con.prepareStatement(sql);
                rs = st.executeQuery();
                while (rs.next()) {
                    Blob blob = rs.getBlob(1);
                    int blobSize = (int)blob.length();
                    byteFoto = blob.getBytes(1,blobSize);
                    bitmap = BitmapFactory.decodeByteArray(byteFoto,0,byteFoto.length);
                    arrBitmap.add(bitmap);
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



    public ArrayList<Bitmap> getarrBitmap() {
        return arrBitmap;
    }

    public void setFoto(File foto) {
        this.foto = foto;
    }
}