package com.example.mamason.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mamason.R;

import java.util.ArrayList;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ItemViewHolder> implements SimpleItemTouchHelperCallback.OnItemMoveListener{

    private ArrayList<Phone> mList;
    private int position;
    public PhoneAdapter(ArrayList<Phone> list){
        this.mList = list;
    }

    public PhoneAdapter() {

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        protected TextView index;
        protected TextView phname;
        protected TextView phnum;
        protected Button sendm;

        public ItemViewHolder(@NonNull final View itemView){
            super(itemView);
            this.index = itemView.findViewById(R.id.phone_index);
            this.phname = itemView.findViewById(R.id.phone_name);
            this.phnum = itemView.findViewById(R.id.phone_num);
            this.sendm = itemView.findViewById(R.id.sendm1);
        }

        public void onBind(Phone ph){
            index.setText(ph.getIndex());
            phname.setText(ph.getPname());
            phnum.setText(ph.getPnum());

        }
    }
    @NonNull
    @Override
    public PhoneAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phonelayout, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneAdapter.ItemViewHolder holder, int position) {
        holder.onBind(mList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), position + "번째 리스트 클릭", Toast.LENGTH_LONG).show();
                setPosition(position);
                showalert(v);
            }
        });

        holder.sendm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPosition(position);
                showalert2(v);
            }
        });
    }

    public void showalert(View v){
        android.app.AlertDialog.Builder msgBuilder = new android.app.AlertDialog.Builder(v.getContext())
                .setTitle("삭제")
                .setMessage("삭제하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<Phone> ph = getListData();
                        Phone simple = ph.get(position);
                        String name = simple.getPname();
                        String num = simple.getPnum();

                        HomeFragment.myDBHelper myHelper = new HomeFragment.myDBHelper(v.getContext());
                        SQLiteDatabase sqldb = myHelper.getWritableDatabase();
                        sqldb.execSQL("delete from numbers where mNumber='"+num +"';");


                        mList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mList.size());

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

    public void showalert2(View v){
        android.app.AlertDialog.Builder msgBuilder = new AlertDialog.Builder(v.getContext())
                .setTitle("확인문자")
                .setMessage("확인 문자를 보내시겠습까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<Phone> ph = getListData();
                        Phone simple = ph.get(position);
                        String num = simple.getPnum();
                        sendSMS(num);
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
//@NonNull ViewGroup parent,
    private void sendSMS( String phoneNumber){
        //ActivityCompat.requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, "응급 알람 확인 문자입니다.", null, null);
        //Toast.makeText(, "긴급 문자가 전송되었습니다", Toast.LENGTH_LONG).show();
        System.out.println("sendmsg: 문자가 전송되었습니다.");
    }

    public void addItem(Phone ph){
        mList.add(ph);
    }

    public int getPosition(){
        return position;
    }

    public void setPosition(int position){
        this.position = position;
    }

    /*public void removeItem(int position){
        itemList.remove(position);
        notifyDataSetChanged(position);
        notifyDataSetChanged();
    }*/

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public ArrayList<Phone> getListData(){
        return mList;
    }

    public void setListData(ArrayList<Phone> listData){
        this.mList = listData;
        notifyDataSetChanged();
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        //Collections.swap(mList, fromPosition, toPosition);
        /*if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mList, i, i - 1);
            }
        }*/
        Phone number = mList.get(fromPosition);
        mList.remove(fromPosition);
        mList.add(toPosition,number);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemSwipe(int adapterPosition) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());
    }

    public interface ItemTouchHelperListener{
        boolean onItemMove(int from_position, int to_position);
        void onItemSwipe(int position);
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            mList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mList.size());
        }
    };
}


