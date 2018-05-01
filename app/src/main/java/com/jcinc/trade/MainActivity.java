package com.jcinc.trade;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public Document docFinal;
    ArrayList<Item> items = new ArrayList<>();
    ArrayList<Item> selectedItem = new ArrayList<>();

    ArrayList<Item> myItems = new ArrayList<>();

    ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    SurfaceView cameraPreview;
    TextView txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;

    PrefManager prefManager;
    String userId;
    FirebaseAuth auth;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    setTitleText("Home");
                    QRCodeSetup();
                    return true;
                case R.id.navigation_items:
                    viewPager.setCurrentItem(1);
                    Connect();
                    setTitleText("Items");
                    return true;
                case R.id.navigation_actions:
                    viewPager.setCurrentItem(2);
                    setTitleText("Actions");
                    setupActionRecylerView();
                    return true;
            }
            return false;
        }
    };


    public void QRCodeSetup () {
        cameraPreview = findViewById(R.id.cameraPreview);
        txtResult = findViewById(R.id.txtResult);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1080, 1920)
                .setRequestedFps(30.0f)
                .build();

        //Add Event
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(
                        getApplicationContext(),
                        android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        try {
            cameraSource.start(cameraPreview.getHolder());
        } catch (IOException | RuntimeException e) {
            cameraSource.stop();
        }

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0) txtResult.post(new Runnable() {
                    @Override
                    public void run() {
                        // Create vibrate
                        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(1000);
                        txtResult.setText(qrcodes.valueAt(0).displayValue);
                    }
                });
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        prefManager = new PrefManager(this);
        auth = FirebaseAuth.getInstance();
        auth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    if (currentFirebaseUser != null) {
                        userId = currentFirebaseUser.getUid();
                        prefManager.setUserId(userId);
                    }
                }
            }
        });

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

        new CountDownTimer(250, 250) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                setTitleText("Home");
                QRCodeSetup();
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
            items.add(new Item(
                    jsonObject.getString("item_name"),
                    jsonObject.getInt("item_condition"),
                    jsonObject.getString("item_id"),
                    R.drawable.ic_launcher_background,
                    jsonObject.getString("user")));

        }
        for (int i = 0; i < items.size(); i++){
            if(items.get(i).item_user_id.equals(userId)){
                myItems.add(items.get(i));
            }
        }
        SetupRecyclerView();
    }

    public void SetupRecyclerView () {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final ItemAdaptor adapter = new ItemAdaptor(myItems);

        adapter.setOnItemClickListener(new ItemAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                goToActions(adapter.itemID(position));
            }
        });
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

    public void goToActions(String itemId) {
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setSelectedItemId(R.id.navigation_actions);
        selectedItem.clear();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id.equals(itemId)) {
                selectedItem.add(new Item(
                        items.get(i).name,
                        items.get(i).condition,
                        items.get(i).id,
                        R.drawable.ic_launcher_background,
                        items.get(i).item_user_id)
                );
            }
        }
    }

    public void setupActionRecylerView () {
        RecyclerView action_recyclerView = findViewById(R.id.action_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        action_recyclerView.setLayoutManager(layoutManager);
        ItemAdaptor adapter = new ItemAdaptor(selectedItem);
        adapter.setOnItemClickListener(new ItemAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Nada
            }
        });
        action_recyclerView.setAdapter(adapter);
        TextView UIDtext = findViewById(R.id.UIDtext);
        UIDtext.setText(userId);
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
