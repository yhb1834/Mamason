package com.example.mamason.ui.notifications;

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

import com.example.mamason.R;
import com.example.mamason.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    //private NotificationsViewModel notificationsViewModel;
    //private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);
        //binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();

        /*final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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