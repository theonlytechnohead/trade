package com.jcinc.trade;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    //RequestQueue queue = Volley.newRequestQueue(this);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // Go to home
                    return true;
                case R.id.navigation_items:
                    // Go to items
                    return true;
                case R.id.navigation_actions:
                    // Go to actions
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                setTitleText("Home");
                textView.setText(POST() + " Test");
            }
        }.start();
    }




    public String POST () {
        String data;
        String url = "http://35.197.91.51/api.php";
        try {
            Document doc = Jsoup.connect("http://35.197.91.51/api.php").get();
            data = doc.text();
        }
        catch (Exception e){
            e.printStackTrace();
            data = String.valueOf(e.getCause());
        }

        // StringRequest postRequest = new StringRequest(Request.Method.POST, url,
        //        new Response.Listener<String>() {
        //            @Override
        //            public void onResponse(String response) {
        //                Log.d("Response", response);
        //            }
        //        },
        //        new Response.ErrorListener() {
        //            @Override
        //            public void onErrorResponse(VolleyError error) {
        //                Log.d("Error.Response", error.toString());
        //            }
        //        }
        //    )
        //{
        //    @Override
        //    protected Map<String, String> getParams() {
        //        Map<String, String>  params = new HashMap<String, String>();
        //        //params.put("api_call", "login");
        //        //params.put("username", "cranderson");
        //        //params.put("password", "mranderson");
        //        return params;
        //    }
        //};
        //queue.add(postRequest);
        return data;
    }

    private void setTitleText (String titleText) {
        ActionBar actionBar = getSupportActionBar();
        try {
            actionBar.setTitle(titleText);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
