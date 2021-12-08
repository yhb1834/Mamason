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
import android.widget.EditText;
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

    TextView mTextView;
    EditText message1;
    TextView msgView;

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

        //예정 알람 불러오기
        myDBHelper myHelper = new myDBHelper(getActivity());
        SQLiteDatabase sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("select * from emAlarm order by rowid desc limit 1;", null);
        while(cursor.moveToNext()){
            String string1 = cursor.getString(0);
            String string2 = cursor.getString(1);
            String string3 = cursor.getString(2);
            if (!string1.equals("")) mTextView.setText("예약된 응급 알람 \n"+string1+"  " +string2+" : "+string3);
        }

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
                        mTextView.setText("예약된 응급 알람\n"+am_pm+"  " +hour +":"+ minute);
                        Toast.makeText(getContext(),"응급 알람이 설정되었습니다", Toast.LENGTH_LONG);

                        myDBHelper myHelper = new myDBHelper(getActivity());
                        SQLiteDatabase emAlarm = myHelper.getWritableDatabase();
                        emAlarm.execSQL("insert into emAlarm values('"+am_pm+"', '"+hour+"','"+minute+"')");
                        emAlarm.close();
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
                initializeeAlarmDB(v);
            }
        });

        msgView = v.findViewById(R.id.msgView);
        cursor = sqlDB.rawQuery("select * from msg order by rowid desc limit 1;", null);
        while(cursor.moveToNext()){
            String string1 = cursor.getString(0) + System.lineSeparator();
            if (!string1.equals("")) msgView.setText(string1);
        }
        cursor.close();
        sqlDB.close();

        Button msgadd = v.findViewById(R.id.msgadd);
        message1 = v.findViewById(R.id.message1);
        msgadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgView.setText(message1.getText());
                myDBHelper myHelper = new myDBHelper(getActivity());
                SQLiteDatabase msg = myHelper.getWritableDatabase();
                msg.execSQL("insert into msg values('" + message1.getText() +"')");
                msg.close();
                Toast.makeText(getActivity(), "메세지가 저장되었습니다", Toast.LENGTH_LONG).show();
            }
        });

        Button send = v.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showalert2(v);
            }
        });


        return v;
    }


    public void showalert2(View v){
        android.app.AlertDialog.Builder msgBuilder = new android.app.AlertDialog.Builder(v.getContext())
                .setTitle("응급 문자")
                .setMessage("응급 문자를 보내시겠습까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        //ArrayList<Phone> ph = getListData();
                        //Phone simple = ph.get(position);
                        String string1 = "";
                        String string2 = "";
                        myDBHelper myHelper = new myDBHelper(getActivity());
                        SQLiteDatabase sqlDB = myHelper.getReadableDatabase();
                        Cursor cursor;
                        cursor = sqlDB.rawQuery("select * from numbers order by rowid limit 1;", null);
                        while(cursor.moveToNext()){
                            string1 = cursor.getString(1);
                        }

                        cursor = sqlDB.rawQuery("select * from msg order by rowid desc limit 1;", null);
                        while(cursor.moveToNext()){
                            string2 = cursor.getString(0) + System.lineSeparator();
                            if (string1.equals("")) string2 = "현재 응급 상황입니다. 구조 요청 바랍니다.";
                        }
                        cursor.close();
                        sqlDB.close();

                        String num = string1;
                        String msg = string2;
                        sendSMS(num,msg);
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(, "안 끔", Toast.LENGTH_SHORT).show();
                    }
                });
        android.app.AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    private void sendSMS(String phoneNumber, String message){
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},1);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(getActivity(), "긴급 문자가 전송되었습니다", Toast.LENGTH_LONG).show();
    }

    public static class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context){
            super(context, "myDB4", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table numbers (mName char(20), mNumber char(20));");
            db.execSQL("create table msg (content text);");
            db.execSQL("create table emAlarm (ampam char(4), Hour char(10), Minute char(10));");
            db.execSQL("create table pAlarm (ampam char(4), Hour char(10), Minute char(10), content text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists numbers;");
            db.execSQL("drop table if exists msg;");
            db.execSQL("drop table if exists emAlarm;");
            db.execSQL("drop table if exists pAlarm;");

            onCreate(db);
        }

    }

    public void initializeDB(View view){
        myDBHelper myHelper = new myDBHelper(getActivity());
        SQLiteDatabase numbers = myHelper.getWritableDatabase();
        myHelper.onUpgrade(numbers,1,2);
        numbers.close();
        Toast.makeText(getActivity(), "연락망 초기화", Toast.LENGTH_LONG).show();
    }
    public void initializeeAlarmDB(View view){
        myDBHelper myHelper = new myDBHelper(getActivity());
        SQLiteDatabase emAlarm = myHelper.getWritableDatabase();
        myHelper.onUpgrade(emAlarm,1,2);
        emAlarm.close();
        Toast.makeText(getActivity(), "응급 알람 삭제", Toast.LENGTH_LONG).show();
    }

    /*public void insertMSG(View view){
        myDBHelper myHelper = new myDBHelper(getActivity());
        SQLiteDatabase msg = myHelper.getWritableDatabase();
        msg.execSQL("insert into groupTBL values('" +  + "','" + binding.editText2.getText().toString()+"');");
        msg.close();
        Toast.makeText(getActivity(), "Inserted", Toast.LENGTH_LONG).show();
    }*/


    public void emAlarmsearchDB(View view){
        myDBHelper myHelper = new myDBHelper(getActivity());
        SQLiteDatabase sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("select * from emAlarm;", null);

        while(cursor.moveToNext()){
            String string1 = cursor.getString(0) + System.lineSeparator();
            String string2 = cursor.getString(1) + System.lineSeparator();
        }
        cursor.close();
        sqlDB.close();
    }

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
        PendingIntent mpendingIntent = PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
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