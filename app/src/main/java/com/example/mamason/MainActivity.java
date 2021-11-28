package com.example.mamason;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mamason.ui.dashboard.medicineFragment;
import com.example.mamason.ui.home.AlarmAddFragment;
import com.example.mamason.ui.home.MessageFragment;
import com.example.mamason.ui.home.Phone;
import com.example.mamason.ui.home.PhoneAdapter;
import com.example.mamason.ui.home.SimpleItemTouchHelperCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
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
    //private ActivityMainBinding binding;

    //public static Stack<Fragment> fragmentStack;
    OnBackPressedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //긴급 연락망 추가
        mRecyclerView1 = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager mlinear = new LinearLayoutManager(this);
        mlinear.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView1.setLayoutManager(mlinear);

        //phoneAdapter = new PhoneAdapter();
        PhoneData = new ArrayList<Phone>();

        mRecyclerView1.setAdapter(phoneAdapter);

        SimpleItemTouchHelperCallback callback =  new SimpleItemTouchHelperCallback(phoneAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView1);


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
    public void onBackPressed(){
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
                Toast.makeText(this, "권한을 승인해야 합니다", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String[] projection = {// 인덱스 값, 중복될 수 있음 -- 한 사람 번호가 여러개인 경우
                 ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                , ContactsContract.CommonDataKinds.Phone.NUMBER};
        if (requestCode == 10) {
            Cursor cursor = getContentResolver().query(data.getData(), projection, null, null, null);
            cursor.moveToFirst();
            String getname = cursor.getString(0);
            String getphone = cursor.getString(1);
            Toast.makeText(getApplicationContext(), "연락처 이름 : " + getname + "\n연락처 전화번호 : " + getphone, Toast.LENGTH_LONG).show();

            /*Bundle bundle = new Bundle();
            bundle.putString("name", getname);
            bundle.putString("phone", getphone);
*/
            PhoneData.add(new Phone(getname,getphone));
            phoneAdapter = new PhoneAdapter(PhoneData);
            mRecyclerView1.setAdapter(phoneAdapter);
            phoneAdapter.notifyDataSetChanged();

            cursor.close();

        }
    }
}