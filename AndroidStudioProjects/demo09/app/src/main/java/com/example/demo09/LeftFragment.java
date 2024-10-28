package com.example.demo09;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class LeftFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_fragment, container, false);

        //  3. 碎片与碎片之间的通信
        Button button1 = view.findViewById(R.id.button1);
        button1.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                RightFragment rightFragment = (RightFragment) activity.getSupportFragmentManager().findFragmentById(R.id.right_layout);
                if (rightFragment != null) {
                    rightFragment.updateMessage("Hello!");
                }
            }
        });

        return view;
    }
}