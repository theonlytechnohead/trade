package com.jcinc.trade;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionClass extends AppCompatActivity {

    private TextView textView;

    String ip = "anderserver.ddns.net";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "trade";
    String un = "trade";
    String password = "d4t4b4s3TRADE";





    @SuppressLint("NewAp1")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try{
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + "; " + "databaseName=" + db + ";user=" + un + ";password=" + password + ";";
            conn = DriverManager.getConnection(ConnURL);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }
}