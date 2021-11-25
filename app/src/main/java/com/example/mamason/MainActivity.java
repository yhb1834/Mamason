package com.example.mamason;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.mamason.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mamason.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private Fragment HomeFragment;

    //private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        showContacts();
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
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //@RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(resultCode == Activity.RESULT_OK)
        {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            String name = cursor.getString(0);        //0은 이름을 얻어옵니다.
            String number = cursor.getString(1);   //1은 번호를 받아옵니다.
            Toast.makeText(getApplicationContext(), "연락처 이름 : " + name + "\n연락처 전화번호 : " + number, Toast.LENGTH_LONG).show();

            cursor.close();
        }*/
        /*if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 10:
                    contactPicked(data);

            }
        } else {
            Toast.makeText(this, "연락처에서 가져오지 못했습니다", Toast.LENGTH_LONG).show();
        }*/
            /*Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER},
                    null, null, null);*/

           String[] projection = {ContactsContract.Contacts._ID// 인덱스 값, 중복될 수 있음 -- 한 사람 번호가 여러개인 경우
                        , ContactsContract.Contacts.DISPLAY_NAME};

           Cursor cursor = getApplicationContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,projection, ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1",
        null, null);

            cursor.moveToFirst();
            //이름획득
            String receiveName = cursor.getString(0);
            //전화번호 획득
            String receivePhone = cursor.getString(1);
            Toast.makeText(getApplicationContext(), "연락처 이름 : " + receiveName + "\n연락처 전화번호 : " + receivePhone, Toast.LENGTH_LONG).show();

            cursor.close();


            //Uri receive = data.getData();
                /*String[] projection = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID // 인덱스 값, 중복될 수 있음 -- 한 사람 번호가 여러개인 경우
                        , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                        , ContactsContract.CommonDataKinds.Phone.NUMBER};


                Cursor cursor = getContentResolver().query(data.getData(), projection, null, null, null);
                cursor.moveToFirst();
                //System.out.println(cursor);
                int name = cursor.getColumnIndex(projection[1]);
                String getname = cursor.getString(name);
                Toast.makeText(getApplicationContext(), "연락처 이름 : " + name + "\n연락처 전화번호 : ", Toast.LENGTH_LONG).show();
                System.out.println(name);
                cursor.close();*/
        //System.out.println(name);
            /*if(cursor.moveToFirst()){
                //String name = cursor.getString(1);
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                Toast.makeText(getApplicationContext(), "연락처 이름 : " + name + "\n연락처 전화번호 : ", Toast.LENGTH_LONG).show();
                System.out.println(name);
                cursor.close();
            }*/

        //@SuppressLint("ResourceType") Fragment fragment = getSupportFragmentManager().findFragmentById(R.layout.fragment_home);
        //fragment.onActivityResult(requestCode, resultCode, data);
    }

/*
    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            String num = null;
            Uri uri = data.getData();
            System.out.println(uri);
            cursor = getContentResolver().query(uri, null, null, null, null);
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            num = cursor.getString(phoneIndex);
            Toast.makeText(this, num, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}