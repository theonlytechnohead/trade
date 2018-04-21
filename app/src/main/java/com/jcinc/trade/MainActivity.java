package com.jcinc.trade;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
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
    public  Document docFinal;

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

        final String url = "http://35.197.91.51/api.php";

        AsyncTask execute = new AsyncTask() {

            @Override
            protected Document doInBackground(Object[] objects) {
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get();

                } catch (Exception e) {
                    doc = null;
                }

                return doc;
            }

            @Override
            protected void onPostExecute(Object result) {
                if (result != null) {
                    textView.setText("Loading website");
                    docFinal = (Document) result;
                    POST();

                } else {
                    textView.setText("Error");
                }
            }

        };

        execute.execute();


        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                setTitleText("Home");
            }
        }.start();
    }




    public String POST () {
        textView.setText(docFinal.text());
        //String data;
        //String url = "http://35.197.91.51/api.php";
        //try {
        //    Document doc = Jsoup.connect("http://35.197.91.51/api.php").get();
        //    data = doc.text();
        //}
        //catch (Exception e){
        //    e.printStackTrace();
        //    data = String.valueOf(e.getCause());
        //}

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
        //return data;
        return null;
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
