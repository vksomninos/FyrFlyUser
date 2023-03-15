package com.example.fyrflyuser.fragments.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.databinding.FragmentReceiptBinding;

public class ReceiptFragment extends Fragment {

   FragmentReceiptBinding binding  ;
    public ReceiptFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =binding.inflate(inflater, container, false);

        return  binding.getRoot();
    }
}