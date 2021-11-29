package com.example.mamason.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mamason.R;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private ArrayList<emalarm_data> emAlarmData;
    private RecyclerView mRecyclerView2;
    private emAdapter mEmapdater;

    EditText mEditText;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //알람 추가
        mRecyclerView2 = (RecyclerView) v.findViewById(R.id.recyclerview2);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView2.setLayoutManager(mLinearLayoutManager);
        emAlarmData = new ArrayList<emalarm_data>();

        /*for(int i=0; i<5; i++){
            emAlarmData.add(new emalarm_data(i,String.valueOf(i),i, i, i,i,String.valueOf(i)));
        }

        mEmapdater = new emAdapter(emAlarmData);
        mRecyclerView2.setAdapter(mEmapdater);
        mEmapdater.notifyDataSetChanged();
*/

        //시간 정보 가져오기
        TimePicker timePicker = v.findViewById(R.id.tp_timepicker);
        /*int Hour, minute;
        String am_pm;
        Hour = timePicker.getHour();
        minute = timePicker.getMinute();*/
        //onTimeSet(timePicker, hour, minute);
        /*if (Build.VERSION.SDK_INT >= 23 ){
            Hour = timePicker.getHour();
            minute = timePicker.getMinute();
            //onTimeSet(timePicker, hour, minute);
        }
        else{
            Toast.makeText(getContext(), "your Build.VERSION.SDK_INT < 23", Toast.LENGTH_SHORT).show();
            Hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }*/
        /*if(Hour > 12) {
            am_pm = "PM";
            Hour = Hour - 12;
        }
        else
        {
            am_pm="AM";
        }*/
        //mTextView.setText("Selected Date: "+am_pm+" " +hour +":"+ minute);


        //내용 가져오기
        mEditText = v.findViewById(R.id.content);
        //String contents = mEditText.getText().toString();
        //message1.setText(contents);

        //저장 클릭
        Button medicine = v.findViewById(R.id.medicine_add);
        //int finalHour = Hour;
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
                    //onTimeSet(timePicker, hour, minute);
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
                emAlarmData.add(new emalarm_data(am_pm, Hour,minute, contents));
                mEmapdater = new emAdapter(emAlarmData);
                mRecyclerView2.setAdapter(mEmapdater);
                mEmapdater.notifyDataSetChanged();
            }
        });
        return v;
    }

}