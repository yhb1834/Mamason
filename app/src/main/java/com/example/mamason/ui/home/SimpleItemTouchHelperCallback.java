package com.example.mamason.ui.home;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {


    //private final PhoneAdapter mAdapter;

    public SimpleItemTouchHelperCallback(PhoneAdapter adapter, OnItemMoveListener mItemMoveListener) {
        //mAdapter = adapter;
        this.mItemMoveListener = mItemMoveListener;
    }
    private PhoneAdapter.ItemTouchHelperListener listener;
    public void ItemtouchHelperCallback(PhoneAdapter.ItemTouchHelperListener listener){ this.listener = listener;}

    private final OnItemMoveListener mItemMoveListener;
    public SimpleItemTouchHelperCallback(OnItemMoveListener listener) {
        this.mItemMoveListener = listener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        //listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    public interface OnItemMoveListener {
        boolean onItemMove(int fromPosition, int toPosition);

        void onItemSwipe(int adapterPosition);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        //listener.onItemSwipe(viewHolder.getAdapterPosition());
    }
}