package com.example.mamason.ui.dashboard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mamason.R;
import com.example.mamason.ui.home.AlertReceiver;
import com.example.mamason.ui.home.HomeFragment;
import com.example.mamason.ui.home.Phone;
import com.example.mamason.ui.home.PhoneAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DashboardFragment extends Fragment {

    private ArrayList<emalarm_data> emAlarmData;
    private RecyclerView mRecyclerView2;
    private emAdapter mEmapdater;

    EditText mEditText;

    private TextView mTextView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //알람 추가
        mRecyclerView2 = (RecyclerView) v.findViewById(R.id.recyclerview2);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView2.setLayoutManager(mLinearLayoutManager);
        emAlarmData = new ArrayList<emalarm_data>();

        mRecyclerView2.setAdapter(mEmapdater);
        //알람 불러오기
        pAlarmsearch(v);

        //시간 정보 가져오기
        TimePicker timePicker = v.findViewById(R.id.tp_timepicker);

        //내용 가져오기
        mEditText = v.findViewById(R.id.content);

        //저장 클릭
        Button medicine = v.findViewById(R.id.medicine_add);
        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Hour, minute;
                String am_pm;
                Hour = timePicker.getHour();
                minute = timePicker.getMinute();
                if (Build.VERSION.SDK_INT >= 23 ){
                    Hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                    onTimeSet(timePicker, Hour, minute);
                }
                else{
                    Toast.makeText(getContext(), "your Build.VERSION.SDK_INT < 23", Toast.LENGTH_SHORT).show();
                    Hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }
                if(Hour > 12) {
                    am_pm = "PM";
                    Hour = Hour - 12;
                }
                else
                {
                    am_pm="AM";
                }
                String contents = mEditText.getText().toString();

                Toast.makeText(getContext()," 알람이 설정되었습니다", Toast.LENGTH_LONG);

                HomeFragment.myDBHelper myHelper = new HomeFragment.myDBHelper(getActivity());
                SQLiteDatabase emAlarm = myHelper.getWritableDatabase();
                emAlarm.execSQL("insert into pAlarm values('"+am_pm+"', '"+Hour+"','"+minute+"','"+contents+"')");
                emAlarm.close();

                emAlarmData.add(new emalarm_data(am_pm, Hour, minute, contents));
                mEmapdater = new emAdapter(emAlarmData);
                mRecyclerView2.setAdapter(mEmapdater);
                mEmapdater.notifyDataSetChanged();
            }
        });
        return v;
    }

    public void pAlarmsearch(View view){
        HomeFragment.myDBHelper myHelper = new HomeFragment.myDBHelper(getActivity());
        SQLiteDatabase sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("select * from pAlarm;", null);
        while(cursor.moveToNext()){
            String string1 = cursor.getString(0);
            String string2 = cursor.getString(1);
            String string3 = cursor.getString(2);
            String string4 = cursor.getString(3);
            emAlarmData.add(new emalarm_data(string1,Integer.parseInt(string2),Integer.parseInt(string3),string4));
            mEmapdater= new emAdapter(emAlarmData);
            mRecyclerView2.setAdapter(mEmapdater);
            mEmapdater.notifyDataSetChanged();
        }
        cursor.close();
        sqlDB.close();
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        //updateTimeText(c);
        startAlarm(c);
    }

    private void updateTimeText(Calendar c){
        String timeText = "Alarm set for : ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        mTextView.setText(timeText);
    }

    private void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver2.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);

        if(c.before((Calendar.getInstance()))){
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        //매일 같은 시간에 반복되는 알람
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY ,  pendingIntent);
    }

    private void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver2.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        mTextView.setText("Alarm canceled");
    }

}