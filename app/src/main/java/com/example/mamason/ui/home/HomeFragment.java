package com.example.mamason.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mamason.R;
import com.example.mamason.databinding.FragmentHomeBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<emalarm_data> emAlarmData;

    private RecyclerView mRecyclerView2;

    private emAdapter mEmapdater;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);



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

        return v;
    }


    private void sendSMS(String phoneNumber, String message){
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},1);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(getActivity(), "긴급 문자가 전송되었습니다", Toast.LENGTH_LONG).show();
    }

}