package com.example.mamason.ui.home;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mamason.R;
import com.example.mamason.databinding.FragmentHomeBinding;

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

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.date = itemView.findViewById(R.id.date);
            this.hour = itemView.findViewById(R.id.time);
        }
        public void onBind(emalarm_data emalarmData){
            name.setText(emalarmData.getName());
            date.setText(String.valueOf(emalarmData.getDate()));
            hour.setText(String.valueOf(emalarmData.getHour()));
            //min.setText(alrm.getMin());
            //content.setText(alrm.getContent());
        }
    }


    @Override
    public void onBindViewHolder(@NonNull emAdapter.ItemViewHolder holder, int position) {
        holder.onBind(mEmalarm.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getPosition();


            }
        });
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
