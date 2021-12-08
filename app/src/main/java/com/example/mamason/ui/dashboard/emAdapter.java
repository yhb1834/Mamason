package com.example.mamason.ui.dashboard;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mamason.R;
import com.example.mamason.ui.home.AlertReceiver;
import com.example.mamason.ui.home.HomeFragment;
import com.example.mamason.ui.home.Phone;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class emAdapter extends RecyclerView.Adapter<emAdapter.ItemViewHolder>
{
    private ArrayList<emalarm_data> mEmalarm;
    private int position;

    public emAdapter(ArrayList<emalarm_data> list){
        this.mEmalarm = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.emrecyclerviewitem, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(itemView);
        return viewHolder;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        protected TextView name;
        protected TextView date;
        protected TextView hour;
        protected TextView min;
        protected TextView content;
        protected TextView ampm;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.content = itemView.findViewById(R.id.name);
            this.hour = itemView.findViewById(R.id.hour);
            this.min = itemView.findViewById(R.id.min);
            this.ampm = itemView.findViewById(R.id.ampm);
        }
        public void onBind(emalarm_data emalarmData){
            //name.setText(emalarmData.getName());
            //date.setText(String.valueOf(emalarmData.getDate()));
            ampm.setText(emalarmData.getAmpm());
            hour.setText(String.valueOf(emalarmData.getHour()));
            min.setText(String.valueOf(emalarmData.getMin()));
            content.setText(emalarmData.getContent());
        }
    }


    @Override
    public void onBindViewHolder(@NonNull emAdapter.ItemViewHolder holder, int position) {
        holder.onBind(mEmalarm.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //getPosition();
                showalert(v);
            }
        });
    }

    public void showalert(View v){
        android.app.AlertDialog.Builder msgBuilder = new android.app.AlertDialog.Builder(v.getContext())
                .setTitle("삭제")
                .setMessage("삭제하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<emalarm_data> ph = getListData();
                        emalarm_data simple = ph.get(position);
                        /*String name = simple.getPname();
                        String num = simple.getPnum();

                        HomeFragment.myDBHelper myHelper = new HomeFragment.myDBHelper(v.getContext());
                        SQLiteDatabase sqldb = myHelper.getWritableDatabase();
                        sqldb.execSQL("delete from pAlarm where mNumber='"+num +"';");

                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getActivity(), AlertReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);
                        alarmManager.cancel(pendingIntent);*/

                        mEmalarm.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mEmalarm.size());
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(, "안 끔", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }


    public ArrayList<emalarm_data> getListData(){
        return mEmalarm;
    }

    public  void addItem(emalarm_data data){
        mEmalarm.add(data);
    }

    public int getPosition(){
        return position;
    }

    public void setPosition(int position){
        this.position = position;
    }

    /*public void removeItem(int postion){
        itemList.remove(position);
        notifyItemRemoved(postion);
        notifyDataSetChanged();
    }*/

    @Override
    public int getItemCount() {
        return (null!=mEmalarm ? mEmalarm.size() : 0);
    }

    public ArrayList<emalarm_data> getmEmalarm(){
        return mEmalarm;
    }

    public void setmEmalarm(ArrayList<emalarm_data> listData){
        this.mEmalarm = listData;
    }
}
