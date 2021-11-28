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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mamason.MainActivity;
import com.example.mamason.R;
import com.example.mamason.databinding.FragmentHomeBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    MainActivity mainActivity;

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


        Button message = v.findViewById(R.id.message_setting);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                alert.setTitle("내용 및 예상 병명");
                alert.setMessage("내용을 입력하세요");


                final EditText name = new EditText(getContext());

                alert.setView(name);

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

        return v;
    }



    private void sendSMS(String phoneNumber, String message){
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},1);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(getActivity(), "긴급 문자가 전송되었습니다", Toast.LENGTH_LONG).show();
    }

}