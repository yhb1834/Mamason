package com.example.mamason;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mamason.ui.dashboard.medicineFragment;
import com.example.mamason.ui.home.AlarmAddFragment;
import com.example.mamason.ui.home.HomeFragment;
import com.example.mamason.ui.home.MessageFragment;
import com.example.mamason.ui.home.Phone;
import com.example.mamason.ui.home.PhoneAdapter;
import com.example.mamason.ui.home.SimpleItemTouchHelperCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity<OnBackPressedListener> extends AppCompatActivity {
    private Fragment HomeFragment;
    public ArrayList<Phone> PhoneData;
    private RecyclerView mRecyclerView1;
    private PhoneAdapter phoneAdapter;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    boolean needRequest = false;


    private static final int REQUEST_CODE_LOCATION = 2000;//????????? ????????? ??????
    private static final int REQUEST_CODE_GPS = 2001;//????????? ????????? ??????
    private GoogleMap googleMap;
    LocationManager locationManager;
    MapFragment mapFragment;
    boolean setGPS = false;

    //private ActivityMainBinding binding;

    //public static Stack<Fragment> fragmentStack;
    OnBackPressedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);

        mRecyclerView1 = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager mlinear = new LinearLayoutManager(this);
        mlinear.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView1.setLayoutManager(mlinear);

        //phoneAdapter = new PhoneAdapter();
        PhoneData = new ArrayList<Phone>();

        mRecyclerView1.setAdapter(phoneAdapter);

        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(phoneAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView1);

        numberssearchDB();

        /*Button message = findViewById(R.id.message_setting);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment_activity_main, new MessageFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });*/


        /*Button alarm = findViewById(R.id.add);
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment_activity_main, new AlarmAddFragment());
                ft.addToBackStack(null);
                ft.commit();
                //getSupportFragmentManager().beginTransaction()
                //        .replace(R.id.container, new AlarmAddFragment()).addToBackStack(null).commit();
            }
        });*/


        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        showContacts();
    }

    @Override
    public void onBackPressed() {
        /*if(listener != null) {
            listener.onBackPressed();
        }else{
            super.onBackPressed();
        }*/
        super.onBackPressed();
    }

    public void moveTonum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    /**
     * Show the contacts in the ListView.
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            //getContactNames();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "????????? ???????????? ?????????", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*//GPS ???????????? ?????? ?????????????????? ?????? ??????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_GPS:
                //Log.d(TAG,""+resultCode);
                //if (resultCode == RESULT_OK)
                //???????????? GPS ?????? ???????????? ??????
                if (locationManager == null)
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // GPS ??? ON?????? ??????????????? ?????? ??????.
                    setGPS = true;

                    mapFragment.getMapAsync((OnMapReadyCallback) MainActivity.this);
                }
                break;
        }
    }
*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*switch (requestCode) {
            case REQUEST_CODE_GPS:
                //Log.d(TAG,""+resultCode);
                //if (resultCode == RESULT_OK)
                //???????????? GPS ?????? ???????????? ??????
                if (locationManager == null)
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // GPS ??? ON?????? ??????????????? ?????? ??????.
                    setGPS = true;

                    mapFragment.getMapAsync((OnMapReadyCallback) MainActivity.this);
                }
                break;
        }
*/
        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //???????????? GPS ?????? ???????????? ??????
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : GPS ????????? ?????????");


                        needRequest = true;

                        return;
                    }
                }

                break;
        }

        String[] projection = {// ????????? ???, ????????? ??? ?????? -- ??? ?????? ????????? ???????????? ??????
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                , ContactsContract.CommonDataKinds.Phone.NUMBER};
        if (requestCode == 10) {
            Cursor cursor = getContentResolver().query(data.getData(), projection, null, null, null);
            cursor.moveToFirst();
            String getname = cursor.getString(0);
            String getphone = cursor.getString(1);
            Toast.makeText(getApplicationContext(), "????????? ?????? : " + getname + "\n????????? ???????????? : " + getphone, Toast.LENGTH_LONG).show();

            com.example.mamason.ui.home.HomeFragment.myDBHelper myHelper = new HomeFragment.myDBHelper(this);
            SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
            /*if (numberssearchDB2(getname, getphone) == 1) {
                sqlDB.execSQL("insert into numbers values('" + getname + "','" + getphone + "');");
                sqlDB.close();
                Toast.makeText(this, "Inserted", Toast.LENGTH_LONG).show();
                cursor.close();
                PhoneData.add(new Phone(getname, getphone));
                phoneAdapter = new PhoneAdapter(PhoneData);
                mRecyclerView1.setAdapter(phoneAdapter);
                phoneAdapter.notifyDataSetChanged();
            }*/
            //else { Toast.makeText(this, "?????? ????????? ?????????????????????.", Toast.LENGTH_LONG).show();}
            sqlDB.execSQL("insert into numbers values('" + getname + "','" + getphone + "');");
            sqlDB.close();
            Toast.makeText(this, "Inserted", Toast.LENGTH_LONG).show();
            cursor.close();
            PhoneData.add(new Phone(getname, getphone));
            phoneAdapter = new PhoneAdapter(PhoneData);
            mRecyclerView1.setAdapter(phoneAdapter);
            phoneAdapter.notifyDataSetChanged();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void numberssearchDB() {
        com.example.mamason.ui.home.HomeFragment.myDBHelper myHelper = new HomeFragment.myDBHelper(this);
        SQLiteDatabase sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("select * from numbers;", null);


        while (cursor.moveToNext()) {
            String string1 = cursor.getString(0) + System.lineSeparator();
            String string2 = cursor.getString(1) + System.lineSeparator();
            PhoneData.add(new Phone(string1, string2));
            phoneAdapter = new PhoneAdapter(PhoneData);
            mRecyclerView1.setAdapter(phoneAdapter);
            phoneAdapter.notifyDataSetChanged();
        }


        cursor.close();
        sqlDB.close();
    }

    public int numberssearchDB2(String name, String num) {
        int i=0;
        com.example.mamason.ui.home.HomeFragment.myDBHelper myHelper = new HomeFragment.myDBHelper(this);
        SQLiteDatabase sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("select * from numbers;", null);
        while (cursor.moveToNext()) {
            String string1 = cursor.getString(0) + System.lineSeparator();
            String string2 = cursor.getString(1) + System.lineSeparator();
            if (name.equals(string1) && num.equals(string2)) {
                i= 1;
            }
            else {i= 0;}
        }
        cursor.close();
        sqlDB.close();
        return i;
    }

}