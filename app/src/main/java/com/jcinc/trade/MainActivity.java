package com.jcinc.trade;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public Document docFinal;
    ArrayList<Item> items = new ArrayList<>();
    ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_items:
                    viewPager.setCurrentItem(1);
                    Connect();
                    return true;
                case R.id.navigation_actions:
                    viewPager.setCurrentItem(2);
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

        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new HomeLayoutFragment());
        pagerAdapter.addFragment(new ItemLayoutFragment());
        pagerAdapter.addFragment(new ActionLayoutFragment());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    navigation.setSelectedItemId(R.id.navigation_home);
                } else if (position == 1) {
                    navigation.setSelectedItemId(R.id.navigation_items);
                } else if (position == 2) {
                    navigation.setSelectedItemId(R.id.navigation_actions);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

    public void Connect () {
        final String url = "http://35.197.91.51/api.php";
        @SuppressLint("StaticFieldLeak") AsyncTask postTask = new AsyncTask() {

            @Override
            protected Document doInBackground(Object[] objects) {
                Document doc;
                try {
                    doc = Jsoup.connect(url).get();
                } catch (Exception e) {
                    e.printStackTrace();
                    doc = null;
                }
                return doc;
            }

            @Override
            protected void onPostExecute(Object result) {
                if (result != null) {
                    docFinal = (Document) result;
                    try {
                        DisplayItems();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        postTask.execute();
    }

    public void DisplayItems() throws JSONException {
        items.clear();
        JSONArray jsonArray = new JSONArray(docFinal.text());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            items.add(new Item(jsonObject.getString("item_name"), jsonObject.getString("item_id"), R.drawable.ic_launcher_background));
        }
        SetupRecyclerView();
    }

    public void SetupRecyclerView () {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new ItemAdaptor(items);
        recyclerView.setAdapter(adapter);
        HideProgressBar();
    }

    public void HideProgressBar () {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onRefresh() {
        Connect();
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem (int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount () {
            return fragmentList.size();
        }

        void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }

    }
}
