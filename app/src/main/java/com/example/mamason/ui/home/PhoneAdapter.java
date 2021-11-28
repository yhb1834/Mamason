package com.example.mamason.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mamason.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

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

        public ItemViewHolder(@NonNull final View itemView){
            super(itemView);
            this.index = itemView.findViewById(R.id.phone_index);
            this.phname = itemView.findViewById(R.id.phone_name);
            this.phnum = itemView.findViewById(R.id.phone_num);
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
            }
        });
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
        this.notifyItemRemoved(position);
        //notifyItemRangeChanged(position, mList.size());
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
        }
    };
}


