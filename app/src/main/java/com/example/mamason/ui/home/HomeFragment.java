package com.example.mamason.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mamason.MainActivity;
import com.example.mamason.R;
import com.example.mamason.databinding.FragmentHomeBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    MainActivity mainActivity;
    public ArrayList<Phone> PhoneData;
    private RecyclerView mRecyclerView1;
    private PhoneAdapter phoneAdapter;

    // 메인 액티비티 위에 올린다.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    // 메인 액티비티에서 내려온다.
    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    private ArrayList<emalarm_data> emAlarmData;
    private RecyclerView mRecyclerView2;
    private emAdapter mEmapdater;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);


        //긴급 연락망 추가
        mRecyclerView1 = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager mlinear = new LinearLayoutManager(getActivity());
        mlinear.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView1.setLayoutManager(mlinear);

        //phoneAdapter = new PhoneAdapter();
        PhoneData = new ArrayList<Phone>();

        mRecyclerView1.setAdapter(phoneAdapter);

        SimpleItemTouchHelperCallback callback =  new SimpleItemTouchHelperCallback(phoneAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView1);


        //알람 추가
        mRecyclerView2 = (RecyclerView) v.findViewById(R.id.recyclerView2);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView2.setLayoutManager(mLinearLayoutManager);
        emAlarmData = new ArrayList<emalarm_data>();

        for(int i=0; i<10; i++){
            emAlarmData.add(new emalarm_data(i,String.valueOf(i),i, i, i,i,String.valueOf(i)));
        }

        mEmapdater = new emAdapter(emAlarmData);
        mRecyclerView2.setAdapter(mEmapdater);
        mEmapdater.notifyDataSetChanged();

        numberssearchDB(v);



        Button addnum = v.findViewById(R.id.addnum);
        addnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendSMS("01075411834", "엄마! ");

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                getActivity().startActivityForResult(intent, 10);
            }
        });


        Button timer = v.findViewById(R.id.timer1);
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                alert.setTitle("Input your name");
                alert.setMessage("Plz, input yourname");


                final EditText name = new EditText(getContext());
                final TimePicker timePicker = new TimePicker(getContext());

                alert.setView(name);
                alert.setView(timePicker);

                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String username = name.getText().toString();

                    }
                });


                alert.setNegativeButton("no",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();

            }
        });

        //EditText message1 = v.findViewById(R.id.message1);
        Button message = v.findViewById(R.id.message_setting);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                alert.setTitle("내용 및 예상 병명");
                alert.setMessage("내용을 입력하세요");


                final EditText content = new EditText(getContext());

                alert.setView(content);

                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String contents = content.getText().toString();
                        //message1.setText(contents);

                    }
                });

                alert.setNegativeButton("no",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();

            }
        });

        Button addAlarm = v.findViewById(R.id.add);
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeDB(v);
            }
        });


        return v;
    }



    private void sendSMS(String phoneNumber, String message){
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},1);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(getActivity(), "긴급 문자가 전송되었습니다", Toast.LENGTH_LONG).show();
    }

    public static class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context){
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table numbers (mName char(20), mNumber char(20));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists numbers;");
            onCreate(db);
        }
    }

    public void initializeDB(View view){
        myDBHelper myHelper = new myDBHelper(getActivity());
        SQLiteDatabase numbers = myHelper.getWritableDatabase();
        myHelper.onUpgrade(numbers,1,2);
        numbers.close();
        Toast.makeText(getActivity(), "Initialized", Toast.LENGTH_LONG).show();
    }

    /*public void insertDB(View view){
        myDBHelper myHelper = new myDBHelper(getActivity());
        SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
        sqlDB.execSQL("insert into groupTBL values('" +  + "','" + binding.editText2.getText().toString()+"');");
        sqlDB.close();
        Toast.makeText(getActivity(), "Inserted", Toast.LENGTH_LONG).show();

    }*/

    public void numberssearchDB(View view){
        myDBHelper myHelper = new myDBHelper(getActivity());
        SQLiteDatabase sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("select * from numbers;", null);


        while(cursor.moveToNext()){
            String string1 = cursor.getString(0) + System.lineSeparator();
            String string2 = cursor.getString(1) + System.lineSeparator();
            PhoneData.add(new Phone(string1,string2));
            phoneAdapter = new PhoneAdapter(PhoneData);
            mRecyclerView1.setAdapter(phoneAdapter);
            phoneAdapter.notifyDataSetChanged();
        }
        cursor.close();
        sqlDB.close();
    }


}