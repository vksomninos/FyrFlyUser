package com.example.fyrflyuser.fragments.driverPart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyrflyuser.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class RideRequestBottomFragment extends BottomSheetDialogFragment {


    public RideRequestBottomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_request_bottom, container, false);
    }
}