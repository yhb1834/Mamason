package com.example.mamason.ui.home;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mamason.MainActivity;
import com.example.mamason.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment{ //implements SwipeRefreshLayout.OnRefreshListener {
    MainActivity mainActivity;
    public ArrayList<Phone> PhoneData;
    private RecyclerView mRecyclerView1;
    private PhoneAdapter phoneAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;//새로고침

    TextView mTextView;

    /*// 메인 액티비티 위에 올린다.
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
    }*/


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);


        //긴급 연락망 추가
        mRecyclerView1 = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager mlinear = new LinearLayoutManager(getActivity());
        mlinear.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView1.setLayoutManager(mlinear);
        //mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh2);//새로고침
        //mSwipeRefreshLayout.setOnRefreshListener(this);

        //phoneAdapter = new PhoneAdapter();
        PhoneData = new ArrayList<Phone>();

        mRecyclerView1.setAdapter(phoneAdapter);

        SimpleItemTouchHelperCallback callback =  new SimpleItemTouchHelperCallback(phoneAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView1);

        numberssearchDB(v);

        Button init = v.findViewById(R.id.init);
        init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeDB(v);
            }
        });

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


        mTextView =  v.findViewById(R.id.time1);

        Button button = (Button) v.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //DialogFragment timePicker = new TimePickerFragment();
                //timePicker.show(getActivity().getSupportFragmentManager(), "time picker");
                TimePicker timePicker = new TimePicker(getContext());
                timePicker.setIs24HourView(true);
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                alert.setTitle("알람 시간 설정");

                alert.setView(timePicker);
                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int hour, minute;
                        String am_pm;
                        if (Build.VERSION.SDK_INT >= 23 ){
                            hour = timePicker.getHour();
                            minute = timePicker.getMinute();
                            onTimeSet(timePicker, hour, minute);
                        }
                        else{
                            Toast.makeText(getContext(), "your Build.VERSION.SDK_INT < 23", Toast.LENGTH_SHORT).show();
                            hour = timePicker.getCurrentHour();
                            minute = timePicker.getCurrentMinute();
                        }
                        if(hour > 12) {
                            am_pm = "PM";
                            hour = hour - 12;
                        }
                        else
                        {
                            am_pm="AM";
                        }
                        mTextView.setText("Selected Date: "+am_pm+" " +hour +":"+ minute);
                    }
                });

                alert.setNegativeButton("no",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();

            }

        });

        Button buttonCancelAlarm = v.findViewById(R.id.cancel1);
        buttonCancelAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                cancelAlarm();
            }
        });

        /*Button timer = v.findViewById(R.id.timer1);
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                alert.setTitle("알람 시간 설정");
                final TimePicker timePicker = new TimePicker(getContext());

                alert.setView(timePicker);
                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.setNegativeButton("no",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();
            }
        });*/

        //EditText message1 = v.findViewById(R.id.message1);
        /*Button message = v.findViewById(R.id.message_setting);
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
        });*/

        /*Button addAlarm = v.findViewById(R.id.add);
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/



        return v;
    }


    /*public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //adapter.notifyDataSetChanged();
                //GettingPHP gPHP = new GettingPHP();
                //gPHP.execute(url_showPrescription);
                //listView.setAdapter(adapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 100);
    }
*/

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


    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
        startAlarm(c);
    }

    private void updateTimeText(Calendar c){
        String timeText = "Alarm set for : ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        mTextView.setText(timeText);
    }

    private void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);

        if(c.before((Calendar.getInstance()))){
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1*60*1000 ,  pendingIntent);

    }

    private void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        mTextView.setText("Alarm canceled");
    }
}