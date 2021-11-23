package com.example.mamason.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private RecyclerView mRecyclerView1;
    private RecyclerView mRecyclerView2;

    private emAdapter mEmapdater;

    //private HomeViewModel homeViewModel;
    //private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView2 = (RecyclerView) v.findViewById(R.id.recyclerView2);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView2.setLayoutManager(mLinearLayoutManager);
        //mRecyclerView1.setHasFixedSize(true);

        emAlarmData = new ArrayList<emalarm_data>();

        for(int i=0; i<10; i++){
            emAlarmData.add(new emalarm_data(i,String.valueOf(i),i, i, i,i,String.valueOf(i)));
        }

        mEmapdater = new emAdapter(emAlarmData);
        mRecyclerView2.setAdapter(mEmapdater);

        mEmapdater.notifyDataSetChanged();


        //binding = FragmentHomeBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();
        /*final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return v;
    }

    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }*/
}