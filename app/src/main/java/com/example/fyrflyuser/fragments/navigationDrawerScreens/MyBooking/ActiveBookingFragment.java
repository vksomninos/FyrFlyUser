package com.example.fyrflyuser.fragments.navigationDrawerScreens.MyBooking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyrflyuser.R;


public class ActiveBookingFragment extends Fragment {

    AppCompatButton track;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_booking, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        track=view.findViewById(R.id.track);
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFramlayout,new Screen223Fragment()).addToBackStack(null).commit();
            }
        });
    }
}