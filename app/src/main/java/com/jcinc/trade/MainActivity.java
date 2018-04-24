package com.jcinc.trade;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

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
                Document doc;
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

        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Wood", "76adfs", R.drawable.ic_notifications_black_24dp));
        items.add(new Item("Paper", "9a8sf7", R.drawable.ic_notifications_black_24dp));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new ItemAdaptor(items);
        recyclerView.setAdapter(adapter);
    }

    public void POST () {
        // Set text to the outcome of the php sql request. BELOW -IS- WORKING!
        textView.setText(docFinal.text());
    }


    private void setTitleText (String titleText) {
        ActionBar actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            actionBar.setTitle(titleText);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
